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

package io.getstream.chat.android.client.models

import io.getstream.chat.android.core.internal.InternalStreamChatApi

/**
 * Represents constants used across all SDKs.
 */
@InternalStreamChatApi
public object Constants {

    /**
     * Number of bytes in a megabyte.
     */
    public const val MB_IN_BYTES: Long = 1024 * 1024

    /**
     * Maximum request body length in bytes.
     */
    internal const val MAX_REQUEST_BODY_LENGTH = 1 * MB_IN_BYTES
}
