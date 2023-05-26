package com.kadun.kadchat.ui.favorite.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.kadun.kadchat.R
import com.kadun.kadchat.common.CommentClickListener
import com.kadun.kadchat.data.db.entity.DbFavoritesComments
import com.kadun.kadchat.data.extentions.setRandomBackgroundColor
import com.kadun.kadchat.data.extentions.toDateTime
import com.kadun.kadchat.databinding.ItemCommentBinding
import com.kadun.kadchat.ui.home.adapters.CommentAnswersAdapter

class CommentListAdapter(
    private val clickListener: CommentClickListener<DbFavoritesComments>,
    private val answerAdapter: CommentAnswersAdapter
) :
    ListAdapter<DbFavoritesComments, CommentListAdapter.Holder>(
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

        fun bind(item: DbFavoritesComments) {
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

                ivFavoriteButton.isInvisible = true
                answerAdapter.submitList(item.replies)
                llUsernameRoot.setOnClickListener { clickListener.onUsernameClick(item) }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<DbFavoritesComments>() {
        override fun areItemsTheSame(
            oldItem: DbFavoritesComments,
            newItem: DbFavoritesComments
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: DbFavoritesComments,
            newItem: DbFavoritesComments
        ): Boolean {
            return oldItem == newItem
        }
    }
}