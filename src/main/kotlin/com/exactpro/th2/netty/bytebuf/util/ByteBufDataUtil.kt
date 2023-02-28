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

const val DEFAULT_END_CHAR = ' '

fun ByteBuf.readAsciiChar(): Char = (readByte().toInt() and 0xFF).toChar()

fun ByteBuf.writeAsciiChar(value: Char): ByteBuf {
    require(value.code < 256) { "The $value char with ${value.code} code is out of ASCII table" }
    writeByte(value.code)
    return this
}

/**
 * Read string with the specified [length] in bytes via the specified [charset] and trim end pads using [padChar] if necessary
 * @param length is size of write bytes
 */
@JvmOverloads
fun ByteBuf.readPaddedString(
    length: Int,
    padChar: Char = DEFAULT_END_CHAR,
    charset: Charset = Charsets.UTF_8
): CharSequence = readCharSequence(length, charset).trimEnd(padChar)

/**
 * Writes string [value] and pads using [padChar] it to the specified [length] in bytes if necessary
 * @param length is size of write bytes
 */
@JvmOverloads
fun ByteBuf.writePaddedString(
    value: String,
    length: Int = value.length,
    padChar: Char = DEFAULT_END_CHAR,
    charset: Charset = Charsets.UTF_8
): ByteBuf {
    require(value.length <= length) {
        "The $value value with ${value.length} length is longer than the $length limit"
    }

    val array = value.toByteArray(charset)
    val actualLength = array.size

    when {
        actualLength < length -> {
            val padBytes = padChar.toString().toByteArray(charset)
            val tailLength = length - actualLength

            require(tailLength % padBytes.size == 0) {
                "The '$value' ($actualLength bytes in $charset) string can't be encoded to $length bytes using the '$padChar' (${padBytes.size} bytes in $charset) end char"
            }

            writeBytes(array)
            repeat(tailLength / padBytes.size) { writeBytes(padBytes) }
        }
        actualLength > length -> error("The '$value' ($actualLength bytes in $charset) string can't be encoded to $length bytes")
        else -> writeBytes(array)
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