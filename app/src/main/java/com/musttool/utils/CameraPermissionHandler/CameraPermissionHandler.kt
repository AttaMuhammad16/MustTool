package com.musttool.utils.CameraPermissionHandler

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class CameraPermissionHandler @Inject constructor(val context: Activity):InterCameraPermissionHandler {
    override fun checkStorageAndCameraPermissions(): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        val cameraPermission = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
        return storagePermission && cameraPermission
    }
    override fun requestStorageAndCameraPermissions() {
        ActivityCompat.requestPermissions(context, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA), 0)
    }
}



