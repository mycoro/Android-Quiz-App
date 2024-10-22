package edu.tcu.mlcoronilla.quiz

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class ResultActivity : AppCompatActivity(){
    private val score = intent.getIntExtra("score", 0)
    private val username = intent.getStringExtra("username")
    private val totalQuestions = intent.getIntExtra("totalQuestions", 10)

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
        findViewById<TextView>(R.id.result_score_tv).text = "Your score is $score/$totalQuestions"
        if(score < 6){
            findViewById<ImageView>(R.id.image).setImageResource(R.drawable.ic_sweat_face)
            findViewById<TextView>(R.id.result_message_tv).text = "Good luck next time, $username!"
        } else {
            findViewById<ImageView>(R.id.image).setImageResource(R.drawable.ic_trophy)
            findViewById<TextView>(R.id.result_message_tv).text = "Congratulations, $username!"
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}