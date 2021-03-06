package com.example.a2in1app

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class GuessThePhrase : AppCompatActivity() {

    private lateinit var main_constraintLayout: ConstraintLayout
    private lateinit var phrase_editText : EditText
    private lateinit var main_recycleView: RecyclerView
    private lateinit var phrase_message: ArrayList<String>
    private lateinit var guess_button: Button
    private lateinit var phrase_textView: TextView
    private lateinit var letter_textView: TextView
    private lateinit var highScore_textView: TextView

    private lateinit var sharedPreferences: SharedPreferences

    private var goal_phrase = "welcome to coding dojo"
    private var guessed_letter = ""
    private var guessed_phrase = ""

    private var selection = 1 //for whole phrase
    private var count = 0
    private var highScore = 0
    private var currentScore = 0

    private val myAnswerDictionary = mutableMapOf<Int, Char>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.guess_the_phrase)

        sharedPreferences = this.getSharedPreferences(
            getString(R.string.preference_file_key), Context.MODE_PRIVATE)
        highScore = sharedPreferences.getInt("myHighScore", 0)

        var i = 0
        while(i < goal_phrase.length) {
            if(goal_phrase[i] == ' '){
                myAnswerDictionary[i] = ' '
                guessed_phrase += ' '
            }else{
                myAnswerDictionary[i] = '*'
                guessed_phrase += '*'
            }
            i++
        }

        if(savedInstanceState != null){
            goal_phrase = savedInstanceState.getString("goal_phrase").toString()
            guessed_letter = savedInstanceState.getString("guessed_letter").toString()
            guessed_phrase = savedInstanceState.getString("guessed_phrase").toString()
            selection = savedInstanceState.getInt("selection")
            count = savedInstanceState.getInt("count")
            highScore = savedInstanceState.getInt("highScore")
            currentScore = savedInstanceState.getInt("currentScore")
            phrase_message = savedInstanceState.getStringArrayList("phrase_message") as ArrayList<String>
        } else {
            phrase_message = ArrayList()
        }

        main_constraintLayout = findViewById(R.id.main_constraintLayout)

        main_recycleView = findViewById(R.id.main_recycleView)
        main_recycleView.adapter = PhraseAdapter(this,phrase_message)
        main_recycleView.layoutManager = LinearLayoutManager(this)

        phrase_editText = findViewById(R.id.phrase_editText)
        phrase_textView = findViewById(R.id.phrase_textView)
        letter_textView = findViewById(R.id.letter_textView)
        highScore_textView = findViewById(R.id.highScore_textView)
        highScore_textView.text = "Your High Score: $highScore"

        guess_button = findViewById<Button>(R.id.guss_button)
        guess_button.setOnClickListener { Guss_the_Phrase() }

        update()
    }

    private fun Guss_the_Phrase(){
        val input = phrase_editText.text.toString()
        if(selection == 1){
            if(input == goal_phrase){
                val msg = "Great Job, You Win!!"
                phrase_message.add(msg)
                setHigherScore()
                playAgainAlert(msg+",\nPlay again?")
                endOfTheGAme(false)
                update()
            } else {
                phrase_message.add("Incorrect Guess :(")
                selection = 2
                update()
            }
        } else{
            if(input != ""){
                if(input.length == 1){
                    selection = 1
                    guessed_phrase = ""
                    checkLetters(input[0])
                } else{
                    Snackbar.make(main_constraintLayout, "Invalid input, you should enter only one letter", Snackbar.LENGTH_LONG).show()
                }
            }
        }
        phrase_editText.text.clear()
        phrase_editText.clearFocus()
        main_recycleView.adapter?.notifyDataSetChanged()
    }

    private fun update(){
        phrase_textView.text = "Phrase:  " + guessed_phrase.toUpperCase()
        letter_textView.text = "Guessed Letters:  " + guessed_letter
        if(selection == 1){
            phrase_editText.hint = "Guess the full phrase"
        }else{
            phrase_editText.hint = "Guess a letter"
        }
    }

    private fun checkLetters(guessedLetter: Char){
        var flag = true
        for(i in guessed_letter.indices){
            if ( guessed_letter[i] == guessedLetter) {
                Snackbar.make(
                    main_constraintLayout,
                    "You already guess this letter, guess another one",
                    Snackbar.LENGTH_LONG
                ).show()
                selection = 2
                flag = false
                break
            }
        }

        if (flag == true){
            var found = 0
            for(i in goal_phrase.indices){
                if(goal_phrase[i] == guessedLetter){
                    myAnswerDictionary[i] = guessedLetter
                    found++
                }
            }
            for(i in myAnswerDictionary) {
                guessed_phrase += myAnswerDictionary[i.key]
            }
            if(guessed_phrase==goal_phrase){
                val msg = "Great Job, You Win!!"
                phrase_message.add(msg)
                setHigherScore()
                playAgainAlert(msg+",\nPlay again?")
                endOfTheGAme(false)
                update()
            }
            if(guessed_letter.isEmpty()){
                guessed_letter+=guessedLetter
            } else{
                guessed_letter+=", "+guessedLetter
            }
            if(found>0){
                phrase_message.add("Found $found ${guessedLetter.toUpperCase()}(s)")
            }else{
                phrase_message.add("No ${guessedLetter.toUpperCase()}s found")
            }

            count++
            val guessesLeft = 10 - count
            if (guessesLeft == 0){
                val msg = "You loose"
                phrase_message.add(msg)
                setHigherScore()
                playAgainAlert(msg+",\nPlay again?")
                endOfTheGAme(false)
            }

            if(count<10){
                phrase_message.add("$guessesLeft guesses remaining")
            }
            update()
            main_recycleView.scrollToPosition(phrase_message.size - 1)
        }
    }

    private fun setHigherScore(){
        currentScore = 10 - count

        if(currentScore > highScore){
            highScore = currentScore
            highScore_textView.text = "Your High Score: $highScore"
            with(sharedPreferences.edit()) {
                putInt("myHighScore",highScore)
                apply()
            }
        }
    }

    private fun endOfTheGAme(bool: Boolean){
        phrase_editText.isEnabled = bool
        guess_button.isEnabled = bool
        phrase_editText.isClickable = bool
        guess_button.isClickable = bool
    }

    private fun playAgainSetup(){
        this.recreate()
        endOfTheGAme(true)

        goal_phrase = "welcome to coding dojo"
        guessed_letter = ""
        guessed_phrase = ""
        selection = 1
        count = 0
        phrase_message = ArrayList()
    }

    private fun playAgainAlert(message: String){

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setMessage(message)
            // positive button
            .setPositiveButton("play", DialogInterface.OnClickListener {
                    dialog, id -> playAgainSetup()
            })
            // negative button
            .setNegativeButton("Cancel", DialogInterface.OnClickListener {
                    dialog, id -> dialog.cancel()
            })

        val alert = dialogBuilder.create()
        alert.setTitle("Game Over!")
        alert.show()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("goal_phrase",goal_phrase)
        outState.putString("guessed_letter",guessed_letter)
        outState.putString("guessed_phrase",guessed_phrase)
        outState.putInt("selection", selection)
        outState.putInt("count", count)
        outState.putInt("highScore", highScore)
        outState.putInt("currentScore", currentScore)
        outState.putStringArrayList("phrase_message", phrase_message)
    }

    private fun moveToNextActivity(activity_number: Int){
        var intent: Intent
        if (activity_number == 1){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else if(activity_number == 2){
            intent = Intent(this, NumbersGame::class.java)
            startActivity(intent)
        }else {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.phrase_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGame_item -> {
                endOfTheGAme(false)
                playAgainAlert("Play again?")
                return true
            }
            R.id.numbersGame_item -> {
                moveToNextActivity(2)
                return true
            }
            R.id.mainMenu_item -> {
                moveToNextActivity(3)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}