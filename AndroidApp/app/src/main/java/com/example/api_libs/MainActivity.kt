package com.example.api_libs

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.example.api_libs.brokers.VolleyBroker
import com.google.android.material.textfield.TextInputEditText
import org.json.JSONObject


class MainActivity : AppCompatActivity() {
    lateinit var volleyBroker: VolleyBroker

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)


        volleyBroker = VolleyBroker(this.applicationContext)

        val getButton: Button = findViewById(R.id.fetch_button)
        val getResultTextView : TextView = findViewById(R.id.get_result_text)
        getButton.setOnClickListener {
            volleyBroker.instance.add(VolleyBroker.getRequest("collectors",
                { response ->
                    // Display the first 500 characters of the response string.
                    getResultTextView.text = buildString {
                                                            append("Response is: ")
                                                            append(response)
                                                        }
                },
                {
                    Log.d("TAG", it.toString())
                    getResultTextView.text = "That didn't work!"
                }
            ))
        }

        val postButton: Button = findViewById(R.id.post_button)
        val postResultTextView : TextView = findViewById(R.id.post_result_text)
        postButton.setOnClickListener {
            val mailTxt : TextInputEditText = findViewById(R.id.txt_post_mail)
            val nameTxt : TextInputEditText = findViewById(R.id.txt_post_name)
            val phoneTxt : TextInputEditText = findViewById(R.id.txt_post_phone)
            val postParams = mapOf<String, Any>(
                "name" to nameTxt.text.toString(),
                "telephone" to phoneTxt.text.toString(),
                "email" to mailTxt.text.toString()
            )
            volleyBroker.instance.add(VolleyBroker.postRequest("collectors", JSONObject(postParams),
                { response ->
                    // Display the first 500 characters of the response string.
                    postResultTextView.text = buildString {
                                                            append("Response is: ")
                                                            append(response.toString())
                                                        }
                },
                {
                    Log.d("TAG", it.toString())
                    postResultTextView.text = "That didn't work!"
                }
            ))
        }


    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.layout_menu, menu)
        supportActionBar!!.title = "Volley"
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_switch_layout -> {
                // Create an intent with a destination of the other Activity
                val intent = Intent(this, RetrofitActivity::class.java)
                startActivity(intent)
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
