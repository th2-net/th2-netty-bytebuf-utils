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
import io.netty.buffer.ByteBufUtil
import java.nio.charset.Charset
import java.util.*

const val DEFAULT_PAD_CHAR = '\u0000'

fun ByteBuf.readAsciiChar(): Char = this.readCharSequence(1, Charsets.US_ASCII)[0]

fun ByteBuf.writeAsciiChar(value: Char): ByteBuf = this.apply {
    require(value.code < 256) {
        "The $value char with ${value.code} code is out of ASCII table"
    }
    ByteBufUtil.writeAscii(this, value.toString())
}

@JvmOverloads
fun ByteBuf.readStringPadEnd(
    length: Int,
    padChar: Char = DEFAULT_PAD_CHAR,
    charset: Charset = Charsets.US_ASCII
): String = readCharSequence(length, charset).toString().substringBefore(padChar)

@JvmOverloads
fun ByteBuf.writeStringPadEnd(
    value: String,
    length: Int = value.length,
    padChar: Char = DEFAULT_PAD_CHAR,
    charset: Charset = Charsets.US_ASCII
): ByteBuf = this.apply {
    require(value.length <= length) {
        "The $value value with ${value.length} length is longer than the $length limit"
    }
    writeBytes(value.padEnd(length, padChar).toByteArray(charset))
}

fun ByteBuf.readUInt8(): UByte = this.readUnsignedByte().toUByte()

fun ByteBuf.writeUInt8(value: UByte): ByteBuf = this.writeByte(value.toInt())

fun ByteBuf.readInt8(): Byte = this.readByte()

fun ByteBuf.writeInt8(value: Byte): ByteBuf = this.writeByte(value.toInt())

fun ByteBuf.readUInt16LE(): UShort = this.readUnsignedShortLE().toUShort()

fun ByteBuf.writeUInt16LE(value: UShort): ByteBuf = this.writeShortLE(value.toInt())

fun ByteBuf.readInt16LE(): Short = this.readShortLE()

fun ByteBuf.writeInt16LE(value: Short): ByteBuf = this.writeShortLE(value.toInt())

fun ByteBuf.readUInt32LE(): UInt = this.readUnsignedIntLE().toUInt()

fun ByteBuf.writeUInt32LE(value: UInt): ByteBuf = this.writeIntLE(value.toInt())

fun ByteBuf.readInt32LE(): Int = this.readIntLE()

fun ByteBuf.writeInt32LE(value: Int): ByteBuf = this.writeIntLE(value)

fun ByteBuf.readUInt64LE(): ULong = this.readLongLE().toULong()

fun ByteBuf.writeUInt64LE(value: ULong): ByteBuf = this.writeLongLE(value.toLong())

fun ByteBuf.readInt64LE(): Long = this.readLongLE()

fun ByteBuf.writeInt64LE(value: Long): ByteBuf = this.writeLongLE(value)