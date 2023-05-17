package com.kadun.kadchat.data.network.data

data class DataDto(
    val after: String?,
    val dist: Int?,
    val modhash: String?,
    val children: List<SubredditsDto>
)
