/*
 * Copyright (c) 2014-2022 Stream.io Inc. All rights reserved.
 *
 * Licensed under the Stream License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    https://github.com/GetStream/stream-chat-android/blob/main/LICENSE
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.getstream.chat.android.client.parser2

import io.getstream.chat.android.client.api2.model.dto.UpstreamChannelDto
import io.getstream.chat.android.client.parser2.testdata.ChannelDtoTestData
import io.kotest.assertions.json.shouldEqualJson
import org.amshove.kluent.invoking
import org.amshove.kluent.shouldThrow
import org.junit.jupiter.api.Test

internal class UpstreamChannelDtoAdapterTest {

    private val parser = ParserFactory.createMoshiChatParser()

    @Test
    fun `Serialize JSON channel with custom fields`() {
        val jsonString = parser.toJson(ChannelDtoTestData.upstreamChannel)
        jsonString.shouldEqualJson(ChannelDtoTestData.upstreamJson)
    }

    @Test
    fun `Serialize JSON channel without custom fields`() {
        val jsonString = parser.toJson(ChannelDtoTestData.upstreamChannelWithoutExtraData)
        jsonString.shouldEqualJson(ChannelDtoTestData.upstreamJsonWithoutExtraData)
    }

    @Test
    fun `Can't parse upstream channel`() {
        invoking {
            parser.fromJson(ChannelDtoTestData.upstreamJson, UpstreamChannelDto::class.java)
        }.shouldThrow(RuntimeException::class)
    }
}
