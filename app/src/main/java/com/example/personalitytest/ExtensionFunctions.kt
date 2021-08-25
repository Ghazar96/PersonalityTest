package com.example.personalitytest

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.storage.StorageReference
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun FragmentManager.openFragment(
    fragment: Fragment,
    tag: String,
    @IdRes containerIdRes: Int
) {

    val fragmentTransaction = beginTransaction()

    fragmentTransaction.add(containerIdRes, fragment, tag)
    fragmentTransaction.addToBackStack(tag)
    if (!isStateSaved) {
        fragmentTransaction.commit()
    } else {
        fragmentTransaction.commitAllowingStateLoss()
    }
}

suspend fun StorageReference.downloadFile(destinationFile: File): DownloadEventResponse =
    suspendCoroutine { continuation ->
        val task = getFile(destinationFile)
        task.addOnSuccessListener {
            continuation.resume(DownloadEventResponse.Success(destinationFile))
        }
        task.addOnCanceledListener {
            continuation.resume(DownloadEventResponse.Cancelled(""))
        }
        task.addOnFailureListener {
            continuation.resume(DownloadEventResponse.Cancelled(it.message ?: ""))
        }
    }

suspend fun StorageReference.uploadBytes(byteArray: ByteArray): UploadEventResponse =
    suspendCoroutine { continuation ->
        val task = putBytes(byteArray)
        task.addOnSuccessListener {
            continuation.resume(UploadEventResponse.Success)
        }
        task.addOnCanceledListener {
            continuation.resume(UploadEventResponse.Cancelled("Task Canceled"))
        }

        task.addOnFailureListener {
            continuation.resume(UploadEventResponse.Cancelled("Task Failure " + it.message))
        }
    }

sealed class UploadEventResponse {
    object Success : UploadEventResponse()
    data class Cancelled(val error: String) : UploadEventResponse()
}

sealed class DownloadEventResponse {
    data class Success(val file: File) : DownloadEventResponse()
    data class Cancelled(val error: String) : DownloadEventResponse()
}
