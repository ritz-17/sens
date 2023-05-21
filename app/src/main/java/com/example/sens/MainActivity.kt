package com.example.sens

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
class MainActivity : AppCompatActivity() {

    private lateinit var logoutButton: Button
    private lateinit var button4: Button
    private lateinit var button5: Button
    private lateinit var button6: Button
    private lateinit var bott: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logoutButton = findViewById(R.id.logoutButton)
        button4 = findViewById(R.id.button4)
        button5 = findViewById(R.id.button5)
        button6 = findViewById(R.id.button6)
        bott = findViewById(R.id.bott)
        logoutButton.setOnClickListener{
            Firebase.auth.signOut()
            val intent = Intent(this,login::class.java)
            startActivity(intent)
            finish()
        }

        button4.setOnClickListener{
            val intent =Intent(this,pre_teen::class.java)
            startActivity(intent);
        }
        button5.setOnClickListener{
            val intent =Intent(this,teen::class.java)
            startActivity(intent);
        }
        button6.setOnClickListener{
            val intent =Intent(this,adult::class.java)
            startActivity(intent);
        }
        bott.setOnClickListener{
            val intent =Intent(this,bot::class.java)
            startActivity(intent);
        }
    }
}