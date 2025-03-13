/*
 * Copyright (c) 2014-2025 Stream.io Inc. All rights reserved.
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

package io.getstream.chat.android.ui.common.permissions

import androidx.activity.result.contract.ActivityResultContracts
import org.amshove.kluent.`should be equal to`
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

internal class VisualMediaTypeTest {

    @ParameterizedTest
    @MethodSource("visualMediaMappingInput")
    fun testVisualMediaTypeImageMapping(
        visualMediaType: VisualMediaType,
        contract: ActivityResultContracts.PickVisualMedia.VisualMediaType,
    ) {
        visualMediaType.toContractVisualMediaType() `should be equal to` contract
    }

    companion object {

        @JvmStatic
        fun visualMediaMappingInput() = listOf(
            Arguments.of(VisualMediaType.IMAGE, ActivityResultContracts.PickVisualMedia.ImageOnly),
            Arguments.of(VisualMediaType.VIDEO, ActivityResultContracts.PickVisualMedia.VideoOnly),
            Arguments.of(VisualMediaType.IMAGE_AND_VIDEO, ActivityResultContracts.PickVisualMedia.ImageAndVideo),
        )
    }
}
