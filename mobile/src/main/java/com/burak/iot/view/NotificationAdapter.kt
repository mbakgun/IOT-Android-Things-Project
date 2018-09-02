package com.burak.iot.view

import android.databinding.DataBindingUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.burak.iot.R
import com.burak.iot.databinding.SavedNotificationListItemBinding
import com.burak.iot.model.notification.Notification

class NotificationAdapter(val onItemSelected: (item: Notification) -> Unit)
    : RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {

    private val notificationSummaries = mutableListOf<Notification>()

    fun updateList(updates: List<Notification>) {
        notificationSummaries.clear()
        notificationSummaries.addAll(updates)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = DataBindingUtil.inflate<SavedNotificationListItemBinding>(
                inflater, R.layout.saved_notification_list_item, parent, false)

        return NotificationViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return notificationSummaries.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(notificationSummaries[position])
    }

    inner class NotificationViewHolder(val binding: SavedNotificationListItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Notification) {
            binding.item = item
            binding.imageViewImage.setOnClickListener { onItemSelected(item) }
            binding.executePendingBindings()
        }
    }
}