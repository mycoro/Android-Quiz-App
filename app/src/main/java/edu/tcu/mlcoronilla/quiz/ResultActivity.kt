package edu.tcu.mlcoronilla.quiz

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_result)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        findViewById<Button>(R.id.restart_btn).setOnClickListener{goToMain()}
        setResult()
    }
    private fun setResult() {
        //get the information passed from other kt files
        val username = intent.getStringExtra("name")
        val score = intent.getIntExtra("score", 0)
        val totalQuestions = intent.getIntExtra("totalQuestions", 10)

        findViewById<TextView>(R.id.result_score_tv).text = buildString {
        append("Your score is ")
        append(score)
        append("/")
        append(totalQuestions)
        append(".")
    }
        //to set up the emoji image based on the score received
        if(score < 6){
            findViewById<ImageView>(R.id.result_iv).setImageResource(R.drawable.ic_sweat_face)
            "Good luck next time, $username!".also { findViewById<TextView>(R.id.result_message_tv).text = it }
        } else {
            findViewById<ImageView>(R.id.result_iv).setImageResource(R.drawable.ic_trophy)
            "Congratulations, $username!".also { findViewById<TextView>(R.id.result_message_tv).text = it }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}