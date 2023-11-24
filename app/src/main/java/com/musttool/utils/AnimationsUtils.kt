package com.musttool.utils

import android.content.Context
import com.blogspot.atifsoftwares.animatoolib.Animatoo

class AnimationUtils(private val context: Context) {
    fun getRandomAnimation(randomValue: Int) {
        when (randomValue) {
            0 -> Animatoo.animateZoom(context)
            1 -> Animatoo.animateSlideDown(context)
            2 -> Animatoo.animateSlideUp(context)
            3 -> Animatoo.animateFade(context)
            4 -> Animatoo.animateSpin(context)
            5 -> Animatoo.animateWindmill(context)
            6 -> Animatoo.animateShrink(context)
            7 -> Animatoo.animateInAndOut(context)
            8 -> Animatoo.animateCard(context)
            9 -> Animatoo.animateDiagonal(context)
            10 -> Animatoo.animateSplit(context)
        }
    }
}




