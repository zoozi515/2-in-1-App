package com.example.a2in1app

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlin.random.Random

class NumbersGame : AppCompatActivity() {

    private lateinit var main_constraintLayout: ConstraintLayout
    private lateinit var number_editText : EditText
    private lateinit var main_recycleView: RecyclerView
    private lateinit var Gusses: ArrayList<String>
    private lateinit var guss_button: Button

    private var number_OfAttempts = 3
    private var random_number : Int = -1
    private  var guessed_nuumber = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.numbers_game)

        if(savedInstanceState != null){
            random_number = savedInstanceState.getInt("random_number")
            number_OfAttempts = savedInstanceState.getInt("number_OfAttempts")
            Gusses = savedInstanceState.getStringArrayList("guesses") as ArrayList<String>
        } else {
            random_number = Random.nextInt(0, 11)
            Gusses = ArrayList()
        }

        main_constraintLayout = findViewById(R.id.numbers_constraintLayout)
        main_recycleView = findViewById(R.id.main_recycleView)
        main_recycleView.adapter = GuessedNumberAdapter(this,Gusses)
        main_recycleView.layoutManager = LinearLayoutManager(this)

        number_editText = findViewById(R.id.number_editText)

        guss_button = findViewById<Button>(R.id.guss_button)
        guss_button.setOnClickListener { Guss_the_Number() }
    }

    fun Guss_the_Number(){
        if(number_editText.text.toString() == "")
            Snackbar.make(main_constraintLayout,"You should Enter data", Snackbar.LENGTH_LONG).show()
        else{
            guessed_nuumber = number_editText.text.toString().toInt()
            if(number_OfAttempts > 0){
                if(guessed_nuumber == random_number) {
                    val msg = "You Win!!"
                    Gusses.add(msg)
                    playAgainAlert(msg+",\nPlay again?")
                    endOfTheGAme(false)
                } else{
                    number_OfAttempts--
                    Gusses.add("You guessed $guessed_nuumber\nYou have $number_OfAttempts guesses left")
                }
                if(number_OfAttempts == 0){
                    val msg = "You lose :( \nThe correct number is : $random_number"
                    Gusses.add(msg)
                    endOfTheGAme(false)
                    playAgainAlert(msg + ",\nPlay again?")
                }
            }
            main_recycleView.adapter?.notifyDataSetChanged()
            number_editText.text.clear()
            number_editText.clearFocus()
        }
    }

    private fun endOfTheGAme(bool: Boolean){
        number_editText.isEnabled = bool
        guss_button.isEnabled = bool
        number_editText.isClickable = bool
        guss_button.isClickable = bool
    }

    private fun playAgainSetup(){
        this.recreate()
        endOfTheGAme(true)
        Gusses = ArrayList()
        random_number = Random.nextInt(0, 11)
        number_OfAttempts = 3
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
        outState.putInt("number_OfAttempts", number_OfAttempts)
        outState.putInt("random_number", random_number)
        outState.putStringArrayList("guesses", Gusses)
    }

    private fun moveToNextActivity(activity_number: Int){
        var intent: Intent
        if (activity_number == 1){
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }else if(activity_number == 2){
            intent = Intent(this, GuessThePhrase::class.java)
            startActivity(intent)
        }else {
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.numbers_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.newGame_item -> {
                endOfTheGAme(false)
                playAgainAlert("Play again?")
                return true
            }
            R.id.gussPhrase_item -> {
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