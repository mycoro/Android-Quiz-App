package edu.tcu.mlcoronilla.quiz

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class QuestionActivity : AppCompatActivity(), View.OnClickListener {

    private val questions = Constants.getQuestions().shuffled()
    private var questionIdx = 0
    private var selectedOptionIdx = -1
    private val optionTvs = mutableListOf<TextView>()
    private var answerRevealed = false
    private var score = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_question)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        findViewById<ProgressBar>(R.id.progress_bar).max = questions.size
        findViewById<Button>(R.id.submitBtn).setOnClickListener(this)
        setQuestion()
    }

    private fun setQuestion() { //to set the question format on the screen
        //setting up flags here
        selectedOptionIdx = -1
        answerRevealed = false

        val question = questions[questionIdx]
        findViewById<TextView>(R.id.questionTv).text = question.question
        findViewById<ImageView>(R.id.image).setImageResource(question.image)
        val progressBar = findViewById<ProgressBar>(R.id.progress_bar)
        progressBar.max = questions.size
        progressBar.progress = questionIdx + 1
        val progressTv = findViewById<TextView>(R.id.progress)
        progressTv.text = buildString {
        append(progressBar.progress)
        append("/")
        append(progressBar.max)
    }
        //findViewById<TextView>(R.id.progress).text = progressBar.max
        setOptionTvs(question)
        //here change the submit questions
        val subBtn = findViewById<Button>(R.id.submitBtn)
        subBtn.text = getString(R.string.submit_btn)
    }

    //responsible for conversion from dp to px to be able to set up layout
    private fun dpToPx(dp: Float, context: Context): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

    private fun setOptionTvs(question: Question) { //phase 2
        //remove all the options from the previous question from Linear layout
        val optionLl = findViewById<LinearLayout>(R.id.option_ll)

        optionLl.removeAllViews()
        optionTvs.clear()
        val subBtn = findViewById<Button>(R.id.submitBtn)
        subBtn.text = getString(R.string.submit_btn)
        //val optionLl = findViewById<LinearLayout>(R.id.option_ll) original line placement here
        for(option in question.options.shuffled()) {
            val optionTv = TextView(this)
            optionTv.setBackgroundResource(R.drawable.default_option_bg)
            optionTv.setOnClickListener(this)//if option is clicked
            optionTvs.add(optionTv)
            val paddingInDp = 10F   //setting up the Linear Layout for options
            val paddingInPx = dpToPx(paddingInDp, this)
            val layoutParams = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT)
            val marginInDp = 10F
            val marginInDpZero = 0F
            val zeroMarginInPx = dpToPx(marginInDpZero, this)
            val marginInPx = dpToPx(marginInDp, this)
            layoutParams.setMargins(zeroMarginInPx, zeroMarginInPx,zeroMarginInPx, marginInPx)
            optionTv.layoutParams = layoutParams
            optionTv.setPadding(paddingInPx,paddingInPx,paddingInPx,paddingInPx)
            optionTv.setTextColor(ContextCompat.getColor(this, R.color.black))
            optionTv.gravity = Gravity.CENTER_HORIZONTAL
            optionTv.textSize = 16F
            optionTv.text = option
            optionLl.addView(optionTv)
        }
    }

    private fun selectedOptionView(selectedOptionTv: TextView) {
        //resets options to default look
        for(optionTv in optionTvs) {
            optionTv.setBackgroundResource(R.drawable.default_option_bg)
        }
        selectedOptionTv.setBackgroundResource(R.drawable.selected_option_bg) //highlights the selected option
        selectedOptionIdx = optionTvs.indexOf(selectedOptionTv) //to set up that an option has been selected
    }

    private fun answerView(correctOptionTv: TextView) {
        for(optionTv in optionTvs) {
            if(optionTv == correctOptionTv) { //check if current option we are at is the correct option
                optionTv.setBackgroundResource(R.drawable.correct_option_bg)
            }else if(optionTvs.indexOf(optionTv) == selectedOptionIdx) { //check if selected option was wrong
                optionTv.setBackgroundResource(R.drawable.wrong_option_bg)
            }else { //if else, then it is default
                optionTv.setBackgroundResource(R.drawable.default_option_bg)
            }
            optionTv.isEnabled = false
        }
        //change the text of the submit button when viewing answers
        val subBtn = findViewById<Button>(R.id.submitBtn)
        subBtn.text = getString(R.string.go_to)
    }

    private fun goToResult() {
        val subBtn = findViewById<Button>(R.id.submitBtn)
        subBtn.text = getString(R.string.finish)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        intent.putExtra("totalQuestions", questions.size)
        val username = this.intent.getStringExtra("username")
        intent.putExtra("name", username)
        startActivity(intent)
        finish()
    }

    override fun onClick(view: View?) { // to trigger once something is clicked
        if(view == findViewById<Button>(R.id.submitBtn)) { // if submit is clicked
            if(!answerRevealed) {
                if(selectedOptionIdx == -1) { //to check if the user did not make a selection, produce a toast
                    Toast.makeText(this, "Please make a Selection.",Toast.LENGTH_SHORT).show()
                } else {
                    // go to phase two and call answerView()
                    val question = questions[questionIdx]
                    val selectedOption = optionTvs[selectedOptionIdx].text.toString()

                    if (selectedOption == question.correctAnswer) {
                        score++ //increment score if the answer is correct
                    }

                    //go to answer view to show the correct answer
                    val correctOptionTv = optionTvs.find { it.text == question.correctAnswer }
                    if (correctOptionTv != null) {
                        answerView(correctOptionTv)
                    }
                    answerRevealed = true //change flag
                }
            } else { //if answer is revealed, move on to the next question
                questionIdx++
                if(questionIdx < questions.size){// to go back to phase 1
                    setQuestion()
                }else{ //check if at last question then go to result
                    goToResult()
                }
            }
        } else { // if an option is clicked
            //checking which option is clicked from the choices
            for(optionTv in optionTvs){
                if(view == optionTv) { // view is the triggered component, and checking if its an option
                    selectedOptionView(optionTv) // to highlight the selected option
                }
            }
        }
    }
}