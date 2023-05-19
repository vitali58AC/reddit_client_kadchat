package com.kadun.kadchat.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.R
import com.kadun.kadchat.common.SubredditClickListener
import com.kadun.kadchat.data.db.entity.DbSubredditData
import com.kadun.kadchat.data.extentions.loadImage
import com.kadun.kadchat.databinding.ItemSubredditBinding

class SubredditAdapter(private val clickListener: SubredditClickListener<DbSubredditData>) :
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
                ibSubscribe.setImageDrawable(getSubscribeDrawable(item.user_is_subscriber))
                ibSubscribe.setOnClickListener {
                    clickListener.onSubscribeClicked(item)
                }
                clExpandedRoot.isVisible = item.isExpanded

                ivCommunityIcon.isVisible = item.community_icon.isNullOrEmpty().not()
                item.community_icon?.let { ivCommunityIcon.loadImage(it) }

                ivImage.isVisible = item.header_img.isNullOrEmpty().not()
                        || item.banner_background_image.isNullOrEmpty().not()
                item.header_img?.let { ivImage.loadImage(it) }
                    ?: item.banner_background_image?.let { ivImage.loadImage(it) }

                tvDescription.isVisible = item.public_description.isNullOrEmpty().not()
                tvDescription.text = item.public_description

                tvEmptyData.isVisible = tvDescription.isVisible.not() && ivImage.isVisible.not()

                ivFavoriteButton.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        if (item.isFavorite) R.drawable.ic_favorite_fill_24 else R.drawable.ic_favorite_outline_24
                    )
                )
                ivFavoriteButton.setOnClickListener { clickListener.onFavoriteClicked(item) }
                llTitleRoot.setOnClickListener { clickListener.onRootClicked(item) }
                flToPosts.setOnClickListener { clickListener.onOpenSubredditPosts(item) }
            }
        }

        private fun ItemSubredditBinding.getSubscribeDrawable(isSubscribed: Boolean?) =
            if (isSubscribed == true) {
                AppCompatResources.getDrawable(root.context, R.drawable.ic_unsubscribe)
            } else {
                AppCompatResources.getDrawable(root.context, R.drawable.ic_subscribe)
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