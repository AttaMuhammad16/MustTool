package com.musttool.utils

import android.content.Context
import android.widget.Toast

object Toaster {
    fun myToast(context: Context,data:String){
        Toast.makeText(context, data, Toast.LENGTH_SHORT).show()
    }
}