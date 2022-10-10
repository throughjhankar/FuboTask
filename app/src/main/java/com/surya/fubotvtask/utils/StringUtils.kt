package com.surya.fubotvtask.utils

import android.content.Context
import android.widget.Toast

fun String.showOnToast(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}
