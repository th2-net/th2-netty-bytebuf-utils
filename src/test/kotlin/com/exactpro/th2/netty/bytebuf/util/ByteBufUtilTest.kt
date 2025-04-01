package com.exactpro.th2.netty.bytebuf.util

import io.netty.buffer.Unpooled
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ByteBufUtilTest {
    @Test
    fun `writer index is updated via property`() {
        val buffer = Unpooled.buffer(10)
        assertEquals(0, buffer.writerIndex(), "initial write index must be 0")

        buffer.writerIndex = 4

        assertEquals(4, buffer.writerIndex(), "write index was not updated")
    }

    @Test
    fun `reader index is updated via property`() {
        val buffer = Unpooled.buffer(10)
            .writeLong(42L)

        assertEquals(0, buffer.readerIndex(), "initial reader index must be 0")

        buffer.readerIndex = 4

        assertEquals(4, buffer.readerIndex(), "reader index was not updated")
    }
}