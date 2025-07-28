package com.oneplus.redcableclub.data.model

import androidx.compose.runtime.saveable.Saver


data class Achievement(val id: Int,val name: String, val description: String,val iconUrl: String?)
{
    companion object {
        val Saver: Saver<Achievement?, *> = Saver(
            save = { achievement ->
                listOf(achievement?.id, achievement?.name, achievement?.description, achievement?.iconUrl)
            },
            restore = { list ->
                Achievement(list[0] as Int, list[1] as String, list[2] as String, list[3] as String?)
            }
        )
    }
}


