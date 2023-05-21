package com.example.sens

import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


class bot : AppCompatActivity() {
    var recyclerView: RecyclerView? = null
    var welcomeTextView: TextView? = null
    var messageEditText: EditText? = null
    var sendButton: ImageButton? = null
    var messageList: MutableList<Message>? = null
    var messageAdapter: MessageAdapter? = null
    var client = OkHttpClient()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        messageList = ArrayList()
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        welcomeTextView = findViewById<TextView>(R.id.welcome_text)
        messageEditText = findViewById<EditText>(R.id.message_edit_text)
        sendButton = findViewById<ImageButton>(R.id.send_btn)

        //setup recycler view
        messageAdapter = MessageAdapter(messageList)
        recyclerView.setAdapter(messageAdapter)
        val llm = LinearLayoutManager(this)
        llm.stackFromEnd = true
        recyclerView.setLayoutManager(llm)
        sendButton.setOnClickListener(View.OnClickListener { v: View? ->
            val question = messageEditText.getText().toString().trim { it <= ' ' }
            addToChat(question, Message.SENT_BY_ME)
            messageEditText.setText("")
            callAPI(question)
            welcomeTextView.setVisibility(View.GONE)
        })
    }

    fun addToChat(message: String?, sentBy: String?) {
        runOnUiThread {
            messageList!!.add(Message(message!!, sentBy!!))
            messageAdapter!!.notifyDataSetChanged()
            recyclerView!!.smoothScrollToPosition(messageAdapter!!.itemCount)
        }
    }

    fun addResponse(response: String?) {
        messageList!!.removeAt(messageList!!.size - 1)
        addToChat(response, Message.SENT_BY_BOT)
    }

    fun callAPI(question: String?) {
        //okhttp
        messageList!!.add(Message("Typing... ", Message.SENT_BY_BOT))
        val jsonBody = JSONObject()
        try {
            jsonBody.put("model", "text-davinci-003")
            jsonBody.put("prompt", question)
            jsonBody.put("max_tokens", 4000)
            jsonBody.put("temperature", 0)
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        val body: RequestBody = RequestBody.create(jsonBody.toString(), MainActivity.Companion.JSON)
        val request: Request = Builder()
            .url("https://api.openai.com/v1/completions")
            .header("Authorization", "Bearer YOUR_API_KEY")
            .post(body)
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                addResponse("Failed to load response due to " + e.message)
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.body!!.string())
                        val jsonArray = jsonObject.getJSONArray("choices")
                        val result = jsonArray.getJSONObject(0).getString("text")
                        addResponse(result.trim { it <= ' ' })
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                } else {
                    addResponse("Failed to load response due to " + response.body.toString())
                }
            }
        })
    }

    companion object {
        val JSON = MediaType.get("application/json; charset=utf-8")
    }
}