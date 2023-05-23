package com.kadun.kadchat.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.R
import com.kadun.kadchat.common.PostClickListener
import com.kadun.kadchat.data.db.entity.DbPostsData
import com.kadun.kadchat.data.extentions.getCompatDrawable
import com.kadun.kadchat.data.extentions.loadImage
import com.kadun.kadchat.databinding.ItemPostBinding

class PostAdapter(private val clickListener: PostClickListener<DbPostsData>) :
    PagingDataAdapter<DbPostsData, PostAdapter.Holder>(
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

        fun bind(item: DbPostsData) {
            binding.apply {
                tvTitle.text = item.title
                tvPostBy.text = root.context.getString(R.string.author_is, item.author)
                tvPostText.isVisible = item.selftext.isNullOrEmpty().not()
                tvPostText.text = item.selftext
                tvCommentCount.text = item.num_comments?.toString() ?: "0"

                handlePostImage(item)

                ivFavoriteButton.setOnClickListener { clickListener.onFavoriteClicked(item) }
                ivComments.setOnClickListener { clickListener.onCommentClicked(item) }

                ivFavoriteButton.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        if (item.isFavorite) R.drawable.ic_favorite_fill_24 else R.drawable.ic_favorite_outline_24
                    )
                )

                tvEmptyData.isVisible = ivImage.isVisible.not() && tvPostText.isVisible.not()
            }
        }

        private fun ItemPostBinding.handlePostImage(item: DbPostsData) {
            item.preview?.images?.firstOrNull()?.resolutions
                ?.findLast { it?.url != null }?.let {
                    if (it.url == null || it.width == null || it.height == null) return@let
                    ivImage.loadImage(it.url)
                } ?: kotlin.run { ivImage.isVisible = false }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbPostsData>() {
        override fun areItemsTheSame(
            oldItem: DbPostsData,
            newItem: DbPostsData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbPostsData,
            newItem: DbPostsData
        ): Boolean {
            return oldItem == newItem
        }
    }
}