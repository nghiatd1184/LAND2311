package com.nghiatd.test2103

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.nghiatd.test2103.adapter.MessageAdapter
import com.nghiatd.test2103.databinding.ActivityMainBinding
import com.nghiatd.test2103.model.Message
import com.nghiatd.test2103.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var _binding: ActivityMainBinding
    private val binding: ActivityMainBinding by lazy { _binding }

    private val messages = arrayListOf<Message>()
    private val adapter :MessageAdapter by lazy { MessageAdapter(messages) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user1Messages = listOf(
            Message(1710990060000L, "Hello 1", false),
            Message(1711069260000L, "Hello 2", false),
            Message(1711069440000L, "Hello 3", false),
            Message(1711069500000L, "Hello 4", false),
            Message(1711069510000L, "Hello 5", false)
        )

        val user2Messages = listOf(
            Message(1710993660000L, "Hello 6", true),
            Message(1711069380000L, "Hello 7", true),
            Message(1711069390000L, "Hello 8", true),
            Message(1711076400000L, "Hello 9", true),
            Message(1711076460000L, "Hello 10", true)
        )

        val user1 = User("Martina Wolna", R.drawable.user1_avt, user1Messages)
        val user2 = User("Maciej Kowalski", R.drawable.user2_avt, user2Messages)


        messages.addAll(user1.messages)
        messages.addAll(user2.messages)

        binding.img1.setImageResource(user1.avatar)
        binding.img2.setImageResource(user2.avatar)
        binding.tvUser1Name.text = user1.name
        binding.tvUser2Name.text = user2.name

        binding.recyclerView.apply {
            adapter = this@MainActivity.adapter
        }

        binding.btnSend.setOnClickListener{
            val messageInput = binding.etMessage.text.toString()
            if (messageInput.isNotEmpty()) {
                messages.add(0, Message(System.currentTimeMillis(),messageInput,true))
                binding.etMessage.text = null
                adapter.notifyDataSetChanged()
            }
        }

    }
}