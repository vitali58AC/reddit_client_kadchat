package com.kadun.kadchat.ui.favorite.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.R
import com.kadun.kadchat.common.PostClickListener
import com.kadun.kadchat.data.db.entity.DbFavoritesPosts
import com.kadun.kadchat.data.extentions.loadImage
import com.kadun.kadchat.databinding.ItemPostBinding

class PostsListAdapter(private val clickListener: PostClickListener<DbFavoritesPosts>) :
    ListAdapter<DbFavoritesPosts, PostsListAdapter.Holder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = Holder(
        ItemPostBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class Holder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DbFavoritesPosts) {
            binding.apply {
                tvTitle.text = item.title
                tvPostBy.text = root.context.getString(R.string.author_is, item.author)
                tvPostText.isVisible = item.selftext.isNullOrEmpty().not()
                tvPostText.text = item.selftext
                tvCommentCount.text = item.num_comments?.toString() ?: "0"

                handlePostImage(item)

                ivFavoriteButton.setOnClickListener { clickListener.onFavoriteClicked(item) }
                ivComments.setOnClickListener { clickListener.onCommentClicked(item) }

                tvEmptyData.isVisible = ivImage.isVisible.not() && tvPostText.isVisible.not()
            }
        }

        private fun ItemPostBinding.handlePostImage(item: DbFavoritesPosts) {
            item.preview?.images?.firstOrNull()?.resolutions
                ?.findLast { it?.url != null }?.let {
                    if (it.url == null || it.width == null || it.height == null) return@let
                    ivImage.loadImage(it.url)
                } ?: kotlin.run { ivImage.isVisible = false }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbFavoritesPosts>() {
        override fun areItemsTheSame(
            oldItem: DbFavoritesPosts,
            newItem: DbFavoritesPosts
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbFavoritesPosts,
            newItem: DbFavoritesPosts
        ): Boolean {
            return oldItem == newItem
        }
    }
}