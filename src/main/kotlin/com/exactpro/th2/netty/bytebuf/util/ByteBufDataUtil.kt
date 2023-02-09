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

@file:JvmName("ByteBufDataUtil")

package com.exactpro.th2.netty.bytebuf.util

import io.netty.buffer.ByteBuf
import java.nio.charset.Charset
import java.util.*

const val DEFAULT_END_CHAR = '\u0000'

fun ByteBuf.readAsciiChar(): Char = (readByte().toInt() and 0xFF).toChar()

fun ByteBuf.writeAsciiChar(value: Char): ByteBuf {
    require(value.code <= Byte.MAX_VALUE) { "The $value char with ${value.code} code is out of ASCII table" }
    writeByte(value.code)
    return this
}

/**
 * Reads specified bytes, decodes them to string via specific charset and discards tail after the end character
 * @param length is size of read bytes
 */
@JvmOverloads
fun ByteBuf.readString(
    length: Int,
    endChar: Char = DEFAULT_END_CHAR,
    charset: Charset = Charsets.US_ASCII
): CharSequence {
    return readCharSequence(length, charset).run {
        when (val index = indexOf(endChar)) {
            -1 -> this
            else -> subSequence(0, index)
        }
    }
}

/**
 * Writes string value via specific charset and padded the end characters to specified length
 * @param length is size of write bytes
 */
@JvmOverloads
fun ByteBuf.writeString(
    value: String,
    length: Int = value.length,
    endChar: Char = DEFAULT_END_CHAR,
    charset: Charset = Charsets.US_ASCII
): ByteBuf {
    require(value.length <= length) {
        "The $value value with ${value.length} length is longer than the $length limit"
    }

    val writeIndex = writerIndex()
    writeBytes(value.toByteArray(charset))

    val actualLength = writerIndex() - writeIndex
    when {
        actualLength < length -> {
            val endBytes = endChar.toString().toByteArray(charset)
            val endLength = length - actualLength

            require(endLength % endBytes.size == 0) {
                "The '$value' ($actualLength bytes in $charset) string can't be encoded to $length bytes using the '$endChar' (${endBytes.size} bytes in $charset) end char"
            }

            repeat(endLength / endBytes.size) { writeBytes(endBytes) }
        }

        actualLength > length -> error("The '$value' ($actualLength bytes in $charset) string can't be encoded to $length bytes")
    }
    return this
}

fun ByteBuf.readUInt8(): UByte = readUnsignedByte().toUByte()

fun ByteBuf.writeUInt8(value: UByte): ByteBuf = writeByte(value.toInt())

fun ByteBuf.readInt8(): Byte = readByte()

fun ByteBuf.writeInt8(value: Byte): ByteBuf = writeByte(value.toInt())

fun ByteBuf.readUInt16LE(): UShort = readUnsignedShortLE().toUShort()

fun ByteBuf.writeUInt16LE(value: UShort): ByteBuf = writeShortLE(value.toInt())

fun ByteBuf.readInt16LE(): Short = readShortLE()

fun ByteBuf.writeInt16LE(value: Short): ByteBuf = writeShortLE(value.toInt())

fun ByteBuf.readUInt32LE(): UInt = readUnsignedIntLE().toUInt()

fun ByteBuf.writeUInt32LE(value: UInt): ByteBuf = writeIntLE(value.toInt())

fun ByteBuf.readInt32LE(): Int = readIntLE()

fun ByteBuf.writeInt32LE(value: Int): ByteBuf = writeIntLE(value)

fun ByteBuf.readUInt64LE(): ULong = readLongLE().toULong()

fun ByteBuf.writeUInt64LE(value: ULong): ByteBuf = writeLongLE(value.toLong())

fun ByteBuf.readInt64LE(): Long = readLongLE()

fun ByteBuf.writeInt64LE(value: Long): ByteBuf = writeLongLE(value)