package com.nghiatd.test2103.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nghiatd.test2103.databinding.ItemMessageBinding
import com.nghiatd.test2103.model.Message
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale

class MessageAdapter(
    private val messages: ArrayList<Message>
) : RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val binding: ItemMessageBinding =
            ItemMessageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding)
    }

    override fun getItemCount(): Int = messages.size

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
//        sortMessagesByTime(messages)
        val message = messages[position]
        holder.bind(message,position)
    }

    inner class MessageViewHolder(private val binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message, position: Int) {
            binding.tvTime.visibility = View.GONE
            if (message.isSender) {
                binding.tvReceive.visibility = View.GONE
                binding.tvSend.text = message.message
            } else {
                binding.tvSend.visibility = View.GONE
                binding.tvReceive.text = message.message
            }

            val dateTime = convertTime(message.time, "dd MMMM HH:mm")
            val hourTime = convertTime(message.time, "HH:mm")
            if (position == messages.size - 1) {
                binding.tvTime.text = dateTime
            } else {
                val timeDistance = messages[position].time - messages[position+1].time
                val isNextDay = convertTime(messages[position].time, "dd") != convertTime(messages[position+1].time, "dd")
                if (timeDistance < 900000L) {
                    binding.tvTime.visibility = View.GONE
                } else if(timeDistance > 86400000L || isNextDay) {
                    binding.tvTime.text = dateTime
                }else
                    binding.tvTime.text = hourTime
            }
        }
    }

    fun convertTime(timestampInMillis: Long, pattern: String): String {
        val instant = Instant.ofEpochMilli(timestampInMillis)
        val localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        val formatter = DateTimeFormatter.ofPattern(pattern, Locale.ENGLISH)
        return formatter.format(localDateTime)
    }

    fun sortMessagesByTime(list:ArrayList<Message>) {
        val n = list.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                if (list[j].time < list[j + 1].time) {
                    val temp = list[j]
                    list[j] = list[j + 1]
                    list[j + 1] = temp
                }
            }
        }
    }
}
