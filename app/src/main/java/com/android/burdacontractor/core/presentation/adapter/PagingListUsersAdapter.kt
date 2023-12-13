package com.android.burdacontractor.core.presentation.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.burdacontractor.R
import com.android.burdacontractor.core.utils.enumValueToNormal
import com.android.burdacontractor.core.utils.getTimeDifference
import com.android.burdacontractor.core.utils.setImageFromUrl
import com.android.burdacontractor.databinding.ItemUsersBinding
import com.android.burdacontractor.feature.profile.domain.model.User

class PagingListUsersAdapter(val listener: (User) -> Unit) :
    PagingDataAdapter<User, PagingListUsersAdapter.ListViewHolder>(
        DIFF_CALLBACK
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding =
            ItemUsersBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ListViewHolder(private val binding: ItemUsersBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                tvNama.text = user.nama
                tvTanggal.text = itemView.context.getTimeDifference(user.createdAt)
                tvRole.text = enumValueToNormal(user.role)
                tvEmail.text = user.email
                tvNoHp.text = user.noHp
                if (user.foto != null) {
                    ivUser.setImageFromUrl(user.foto, itemView.context)
                } else {
                    ivUser.setImageDrawable(
                        ContextCompat.getDrawable(
                            itemView.context,
                            R.drawable.ic_profile
                        )
                    )
                }
                cvUser.setOnClickListener {
                    listener(user)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<User>() {
            override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: User,
                newItem: User
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}