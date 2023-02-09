/*
 * Copyright 2023 Exactpro (Exactpro Systems Limited)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.exactpro.th2.netty.bytebuf.util

import io.netty.buffer.ByteBufUtil
import io.netty.buffer.Unpooled
import org.apache.commons.lang3.RandomStringUtils
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class ByteBufDataUtilTest {

    @Test
    fun `serialize deserialize int test`() {
        val buf = Unpooled.buffer()
        buf.writeInt8(Byte.MIN_VALUE)
        assertEquals(Byte.MIN_VALUE, buf.readInt8())
        buf.writeInt8(Byte.MAX_VALUE)
        assertEquals(Byte.MAX_VALUE, buf.readInt8())

        buf.writeUInt8(UByte.MIN_VALUE)
        assertEquals("00", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UByte.MIN_VALUE, buf.readUInt8())
        buf.writeUInt8(UByte.MAX_VALUE)
        assertEquals("ff", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UByte.MAX_VALUE, buf.readUInt8())

        buf.writeInt16LE(Short.MIN_VALUE)
        assertEquals(Short.MIN_VALUE, buf.readInt16LE())
        buf.writeInt16LE(Short.MAX_VALUE)
        assertEquals(Short.MAX_VALUE, buf.readInt16LE())

        buf.writeUInt16LE(UShort.MIN_VALUE)
        assertEquals("0000", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UShort.MIN_VALUE, buf.readUInt16LE())
        buf.writeUInt16LE(UShort.MAX_VALUE)
        assertEquals("ffff", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UShort.MAX_VALUE, buf.readUInt16LE())

        buf.writeInt32LE(Int.MIN_VALUE)
        assertEquals(Int.MIN_VALUE, buf.readInt32LE())
        buf.writeInt32LE(Int.MAX_VALUE)
        assertEquals(Int.MAX_VALUE, buf.readInt32LE())

        buf.writeUInt32LE(UInt.MIN_VALUE)
        assertEquals("00000000", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UInt.MIN_VALUE, buf.readUInt32LE())
        buf.writeUInt32LE(UInt.MAX_VALUE)
        assertEquals("ffffffff", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(UInt.MAX_VALUE, buf.readUInt32LE())

        buf.writeInt64LE(Long.MIN_VALUE)
        assertEquals(Long.MIN_VALUE, buf.readInt64LE())
        buf.writeInt64LE(Long.MAX_VALUE)
        assertEquals(Long.MAX_VALUE, buf.readInt64LE())

        buf.writeUInt64LE(ULong.MIN_VALUE)
        assertEquals("0000000000000000", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(ULong.MIN_VALUE, buf.readUInt64LE())
        buf.writeUInt64LE(ULong.MAX_VALUE)
        assertEquals("ffffffffffffffff", ByteBufUtil.hexDump(buf, buf.readerIndex(), buf.readableBytes()))
        assertEquals(ULong.MAX_VALUE, buf.readUInt64LE())
    }

    @Test
    fun `serialize deserialize string test`() {
        val buf = Unpooled.buffer()
        val value = RandomStringUtils.randomAscii(10)

        var writerIndex = buf.writerIndex()
        (value.length + 1).also { length ->
            buf.writeString(value, length)
            assertEquals(length, buf.writerIndex() - writerIndex)
            assertEquals(value, buf.readString(length).toString())
        }

        writerIndex = buf.writerIndex()
        value.length.also { length ->
            buf.writeString(value, length)
            assertEquals(length, buf.writerIndex() - writerIndex)
            assertEquals(value, buf.readString(length).toString())
        }

        (value.length - 1).also { length ->
            assertFailsWith<RuntimeException> {
                buf.writeString(value, length)
            }.also {
                assertEquals("The $value value with ${value.length} length is longer than the $length limit", it.message)
            }
        }

        writerIndex = buf.writerIndex()
        "$value$DEFAULT_END_CHAR".also { extended ->
            buf.writeString(extended, extended.length)
            assertEquals(extended.length, buf.writerIndex() - writerIndex)
            assertEquals(value, buf.readString(extended.length).toString())
        }

        writerIndex = buf.writerIndex()
        "$value$DEFAULT_END_CHAR$value$DEFAULT_END_CHAR".also { extended ->
            buf.writeString(extended, extended.length)
            assertEquals(extended.length, buf.writerIndex() - writerIndex)
            assertEquals(value, buf.readString(extended.length).toString())
        }

        "\u0100".also { string ->
            val charset = Charsets.UTF_8
            var length = 1

            assertFailsWith<RuntimeException> {
                buf.writeString(string, length, charset = charset)
            }.also {
                assertEquals("The '$string' (2 bytes in $charset) string can't be encoded to $length bytes", it.message)
            }

            length = 3
            val endChar = '\u0101'
            assertFailsWith<RuntimeException> {
                buf.writeString(string, length, endChar, charset)
            }.also {
                assertEquals("The '$string' (2 bytes in $charset) string can't be encoded to $length bytes using the '$endChar' (2 bytes in $charset) end char", it.message)
            }
        }
    }

    @Test
    fun `serialize deserialize char test`() {
        val buf = Unpooled.buffer()
        for (num in 0 .. Byte.MAX_VALUE) {
            num.toChar().run {
                val writerIndex = buf.writerIndex()
                val readerIndex = buf.readerIndex()
                buf.writeAsciiChar(this)
                assertEquals(writerIndex + 1, buf.writerIndex(), "write index for the $this($num) char")
                assertEquals(this, buf.readAsciiChar(), "check value for the $this($num) char")
                assertEquals(readerIndex + 1, buf.readerIndex(), "read index for the $this($num) char")
            }
        }

        with((Byte.MAX_VALUE.toInt() + 1).toChar()) {
            assertFailsWith<IllegalArgumentException> {
                buf.writeAsciiChar(this)
            }.also {
                assertEquals(
                    "The $this char with $code code is out of ASCII table",
                    it.message
                )
            }
        }
        with((-1).toChar()) {
            assertFailsWith<IllegalArgumentException> {
                buf.writeAsciiChar(this)
            }.also {
                assertEquals("The $this char with $code code is out of ASCII table", it.message)
            }
        }
    }
}