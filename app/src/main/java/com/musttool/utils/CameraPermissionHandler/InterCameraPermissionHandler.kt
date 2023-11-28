package com.musttool.utils.CameraPermissionHandler

interface InterCameraPermissionHandler {
    fun checkStorageAndCameraPermissions():Boolean
    fun requestStorageAndCameraPermissions()
}