package com.kadun.kadchat.ui.home.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kadun.kadchat.R
import com.kadun.kadchat.common.AnswerClickListener
import com.kadun.kadchat.data.extentions.setRandomBackgroundColor
import com.kadun.kadchat.data.extentions.toDateTime
import com.kadun.kadchat.data.network.data.comments.CommentAnswers
import com.kadun.kadchat.databinding.ItemCommentAnswerBinding

class CommentAnswersAdapter(
    private val clickListener: AnswerClickListener<CommentAnswers>,
) :
    ListAdapter<CommentAnswers, CommentAnswersAdapter.Holder>(
        DiffCallback
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ) = Holder(
        ItemCommentAnswerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: Holder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class Holder(private val binding: ItemCommentAnswerBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CommentAnswers) {
            binding.apply {
                ivAvatar.setRandomBackgroundColor()
                tvFirstUsernameChar.text = item.author?.firstOrNull()?.toString() ?: "K"
                tvUserName.text = item.author
                tvBodyText.text = item.body
                val date = item.created?.toDateTime() ?: "Unknown"
                tvCreateDate.text = root.context.getString(R.string.create_at, date)

                llUsernameRoot.setOnClickListener { clickListener.onUsernameClick(item) }
            }
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<CommentAnswers>() {
        override fun areItemsTheSame(
            oldItem: CommentAnswers,
            newItem: CommentAnswers
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CommentAnswers,
            newItem: CommentAnswers
        ): Boolean {
            return oldItem == newItem
        }
    }
}