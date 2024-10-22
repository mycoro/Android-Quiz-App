package edu.tcu.mlcoronilla.quiz

import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.EditorInfo
import android.widget.Button
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

        //to go question if start button if clicked
        val startButton = findViewById<Button>(R.id.button)
        startButton.setOnClickListener{goToQuestion(nameEt)}
    }
    private fun goToQuestion(nameEt: TextInputEditText) {
        val name = nameEt.text.toString()

        //check if a name has not been entered
        if(name == ""){
            Toast.makeText(this, "Please Enter Your Name.",Toast.LENGTH_SHORT).show()
            return
        }

        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("username", name)
        startActivity(intent)
        finish()
    }
}
