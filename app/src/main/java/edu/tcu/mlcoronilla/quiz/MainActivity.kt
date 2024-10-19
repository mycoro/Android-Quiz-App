package edu.tcu.mlcoronilla.quiz

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val nameEt = findViewById<TextInputEditText>(R.id.name_et)
        nameEt.setOnEditorActionListener {_,actionID,_ ->
            if(actionID == EditorInfo.IME_ACTION_GO) {
                goToQuestion(nameEt)
                true
            } else false
        }
    }
    private fun goToQuestion(nameEt: TextInputEditText) {
        //have to not let it go to next phase with this toast
        Toast.makeText(this, "Please Enter Your Name.",Toast.LENGTH_SHORT).show()

        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("username", nameEt.text.toString())
        startActivity(intent)
        finish()
    }
}
//two ways to move forward into the quiz is to press the arrow button from the keyboard or the start button