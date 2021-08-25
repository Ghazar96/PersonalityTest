package com.example.personalitytest.network

import com.example.personalitytest.DownloadEventResponse
import com.example.personalitytest.UploadEventResponse
import com.example.personalitytest.downloadFile
import com.example.personalitytest.uploadBytes
import com.google.firebase.storage.FirebaseStorage
import org.json.JSONObject
import java.io.File

class QuestionsFirebaseNetworkService(private val storage: FirebaseStorage) :
    QuestionsNetworkService {
    override suspend fun getQuestions(): File? {
        val result = storage.getReference("questions/personality_test.json")
            .downloadFile(File.createTempFile("questions", "json"))

        return when (result) {
            is DownloadEventResponse.Success -> {
                result.file
            }
            is DownloadEventResponse.Cancelled -> {
                null
            }
        }
    }

    override suspend fun uploadResult(json: JSONObject): UploadEventResponse {
        val objAsBytes: ByteArray = json.toString().toByteArray()

        return storage.getReference("questions/m").uploadBytes(objAsBytes)
    }
}
