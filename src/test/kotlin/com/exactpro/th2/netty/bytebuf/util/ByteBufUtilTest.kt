/*
 * Copyright 2025 Exactpro (Exactpro Systems Limited)
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