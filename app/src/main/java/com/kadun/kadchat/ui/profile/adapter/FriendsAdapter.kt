package com.kadun.kadchat.ui.profile.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.data.db.entity.DbFriendsData
import com.kadun.kadchat.databinding.ItemFriendBinding

class FriendsAdapter : PagingDataAdapter<DbFriendsData, FriendsAdapter.FriendsHolder>(
    DiffCallback
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = FriendsHolder(
        ItemFriendBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: FriendsHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class FriendsHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DbFriendsData) {
            binding.apply {
                tvName.text = item.id
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbFriendsData>() {
        override fun areItemsTheSame(
            oldItem: DbFriendsData,
            newItem: DbFriendsData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbFriendsData,
            newItem: DbFriendsData
        ): Boolean {
            return oldItem == newItem
        }
    }
}