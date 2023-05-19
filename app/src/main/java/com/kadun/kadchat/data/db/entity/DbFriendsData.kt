package com.kadun.kadchat.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.kadun.kadchat.data.network.data.users.FriendDto

@Entity
data class DbFriendsData(
    @PrimaryKey(autoGenerate = true)
    val sortingPosition: Long = 0,
    val id: String?
) {
    companion object {
        fun fromDto(dto: FriendDto) = DbFriendsData(
            id = dto.id,
        )
    }
}
