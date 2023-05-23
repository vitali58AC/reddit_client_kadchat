package com.kadun.kadchat.data.extentions

import java.sql.Date
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


fun Double.toDateTime(): String {
    val date = Date(this.toLong())
    val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy в HH:mm", Locale.getDefault())
    return simpleDateFormat.format(date)
}
