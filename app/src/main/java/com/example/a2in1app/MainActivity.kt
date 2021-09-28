package com.example.a2in1app

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var main_constraintLayout: ConstraintLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        main_constraintLayout = findViewById(R.id.main_constraintLayout)

        val numberGame_Button = findViewById<Button>(R.id.numberGame_button)
        numberGame_Button.setOnClickListener{
            moveToNextActivity(1)

        }

        val phrase_button = findViewById<Button>(R.id.phrase_button)
        phrase_button.setOnClickListener{
            moveToNextActivity(2)
        }
    }

    private fun moveToNextActivity(activity_number: Int){
        var intent: Intent
        if (activity_number == 1){
            intent = Intent(this, NumbersGame::class.java)
            startActivity(intent)
        } else {
            intent = Intent(this, GuessThePhrase::class.java)
            startActivity(intent)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.gussPhrase_item -> {
                moveToNextActivity(2)
                return true
            }
            R.id.numbersGame_item -> {
                moveToNextActivity(1)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}