package io.getstream.chat.android.livedata.usecase

import android.webkit.MimeTypeMap
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import io.getstream.chat.android.client.errors.ChatError
import io.getstream.chat.android.client.models.Attachment
import io.getstream.chat.android.client.utils.Result
import io.getstream.chat.android.livedata.BaseConnectedMockedTest
import io.getstream.chat.android.livedata.TestResultCall
import io.getstream.chat.android.livedata.randomAttachmentsWithFile
import io.getstream.chat.android.livedata.randomMessage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import org.amshove.kluent.When
import org.amshove.kluent.`should throw`
import org.amshove.kluent.`with message`
import org.amshove.kluent.calling
import org.amshove.kluent.invoking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.Shadows.shadowOf
import java.io.File
import java.security.InvalidParameterException

@RunWith(AndroidJUnit4::class)
class SendMessageWithFilesTest : BaseConnectedMockedTest() {

    val sendMessageWithFile: SendMessage by lazy { chatDomain.useCases.sendMessage }

    @Before
    override fun setup() {
        super.setup()
        shadowOf(MimeTypeMap.getSingleton()).addExtensionMimeTypMapping("jpg", "image/jpeg")
    }

    private fun mockFileUploads(files: List<File>) {
        for (file in files) {
            val result = Result(file.absolutePath)
            When calling client.sendFile(channelControllerImpl.channelType, channelControllerImpl.channelId, file) doReturn TestResultCall(result)
            When calling client.sendImage(channelControllerImpl.channelType, channelControllerImpl.channelId, file) doReturn TestResultCall(result)
        }
    }

    private fun mockFileUploadsFailure(files: List<File>) {
        for (file in files) {
            val path: String? = null
            val result = Result(path, file.toChatError())
            When calling client.sendFile(channelControllerImpl.channelType, channelControllerImpl.channelId, file) doReturn TestResultCall(result)
            When calling client.sendImage(channelControllerImpl.channelType, channelControllerImpl.channelId, file) doReturn TestResultCall(result)
        }
    }

    @Test
    fun `Should return message sending files`() {
        runBlocking(Dispatchers.IO) {
            val message = randomMessage()
            message.cid = channelControllerImpl.cid
            message.attachments = randomAttachmentsWithFile().toMutableList()

            val expectedAttachments = message.attachments.map { it.copy(assetUrl = it.upload!!.absolutePath, upload = null, type = "file") }

            val expectedResult = Result(
                message.copy(
                    attachments = expectedAttachments.toMutableList()
                )
            )
            val files: List<File> = message.attachments.map { it.upload!! }

            When calling channelMock.sendMessage(any()) doReturn TestResultCall(expectedResult)

            mockFileUploads(files)

            val result = sendMessageWithFile(message).execute()

            Truth.assertThat(result).isEqualTo(expectedResult)
        }
    }

    /*
    // TODO: mocks for files don't seem to be specific to that file, but instead match any()
    @Test
    fun `Errors should still return the attachments`() {
        runBlocking(Dispatchers.IO) {
            val message = randomMessage()
            message.cid = channelControllerImpl.cid
            message.attachments = randomAttachmentsWithFile().toMutableList()

            val expectedResult = Result(
                    message
            )
            val files: List<File> = message.attachments.map { it.upload!! }

            When calling channelMock.sendMessage(any()) doReturn TestResultCall(expectedResult)

            mockFileUploadsFailure(files)

            val result = sendMessageWithFile(message).execute()

            result `should be equal to` expectedResult
        }
    }*/

    @Test
    fun `Should return apply the right transformation to attachments`() {
        runBlocking(Dispatchers.IO) {
            val message = randomMessage()
            message.cid = channelControllerImpl.cid
            message.attachments = randomAttachmentsWithFile().toMutableList()
            val extra = mutableMapOf<String, Any>("the answer" to 42)

            val files: List<File> = message.attachments.map { it.upload!! }

            mockFileUploads(files)

            val result = channelControllerImpl.uploadAttachment(message.attachments.first()) {
                attachment, _, _ ->
                attachment.copy(extraData = extra)
            }
            val uploadedAttachment = result.data()

            Truth.assertThat(uploadedAttachment.extraData).isEqualTo(extra)
        }
    }

    @Test
    fun `Should throw an exception if the channel cid is empty`() {
        val message = randomMessage()
        message.attachments = randomAttachmentsWithFile().toMutableList()
        message.cid = ""

        invoking {
            sendMessageWithFile(message)
        } `should throw` InvalidParameterException::class `with message` "cid cant be empty"
    }

    @Test
    fun `Should throw an exception if the channel cid doesn't contain a colon`() {
        val message = randomMessage()
        message.attachments = randomAttachmentsWithFile().toMutableList()
        message.cid = "abc"

        invoking {
            sendMessageWithFile(message)
        } `should throw` InvalidParameterException::class`with message` "cid needs to be in the format channelType:channelId. For example messaging:123"
    }
}

private fun File.toChatError(): ChatError = ChatError(absolutePath)
private fun File.toAttachment(mimetype: String?) = Attachment(
    name = name,
    fileSize = length().toInt(),
    mimeType = mimetype,
    url = absolutePath
)
