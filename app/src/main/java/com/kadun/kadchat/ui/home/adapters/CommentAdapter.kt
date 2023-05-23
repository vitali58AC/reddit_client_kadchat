package com.kadun.kadchat.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.CommentClickListener
import com.kadun.kadchat.data.db.entity.DbCommentsData
import com.kadun.kadchat.data.extentions.setRandomBackgroundColor
import com.kadun.kadchat.data.extentions.toDateTime
import com.kadun.kadchat.databinding.ItemCommentBinding

class CommentAdapter(
    private val clickListener: CommentClickListener<DbCommentsData>,
    private val answerAdapter: CommentAnswersAdapter
) :
    PagingDataAdapter<DbCommentsData, CommentAdapter.Holder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = Holder(
        ItemCommentBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class Holder(private val binding: ItemCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: DbCommentsData) {
            binding.apply {
                ivAvatar.setRandomBackgroundColor()
                tvFirstUsernameChar.text = item.author?.firstOrNull()?.toString() ?: "K"
                tvUserName.text = item.author
                tvBodyText.text = item.body
                val date = item.created_utc?.toDateTime() ?: "Unknown"
                tvCreateDate.text = root.context.getString(R.string.create_at, date)
                rvAnswers.apply {
                    adapter = answerAdapter
                    setHasFixedSize(true)
                    (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
                }

                ivFavoriteButton.setImageDrawable(
                    AppCompatResources.getDrawable(
                        root.context,
                        if (item.isFavorite) R.drawable.ic_favorite_fill_24 else R.drawable.ic_favorite_outline_24
                    )
                )
                answerAdapter.submitList(item.replies)
                ivFavoriteButton.setOnClickListener { clickListener.onFavoriteClick(item) }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbCommentsData>() {
        override fun areItemsTheSame(
            oldItem: DbCommentsData,
            newItem: DbCommentsData
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbCommentsData,
            newItem: DbCommentsData
        ): Boolean {
            return oldItem == newItem
        }
    }
}