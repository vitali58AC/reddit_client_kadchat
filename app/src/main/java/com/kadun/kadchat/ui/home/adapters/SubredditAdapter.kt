package com.kadun.kadchat.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.common.ItemClickListener
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.databinding.ItemSubredditBinding

class SubredditAdapter(private val clickListener: ItemClickListener<DbSubredditData>) :
    PagingDataAdapter<DbSubredditData, SubredditAdapter.SubredditHolder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = SubredditHolder(
        ItemSubredditBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: SubredditHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class SubredditHolder(private val binding: ItemSubredditBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: DbSubredditData) {
            binding.apply {
                tvTitle.text = item.title
                ibSubscribe.setOnClickListener {
                    clickListener.onItemClicked(item)
                }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbSubredditData>() {
        override fun areItemsTheSame(
            oldItem: DbSubredditData,
            newItem: DbSubredditData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbSubredditData,
            newItem: DbSubredditData
        ): Boolean {
            return oldItem == newItem
        }
    }
}