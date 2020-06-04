package com.sssproductions.rockpapersscissors

import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var rules_btn:ImageView
    private lateinit var logo:ImageView
    private lateinit var userImg:ImageView
    private lateinit var droidImg:ImageView
    private lateinit var tv_result:TextView
    private var radioGroup: RadioGroup? = null
    private lateinit var radioButton: RadioButton
    private lateinit var fab: FloatingActionButton

    //default value assigned to user_selection
    private var user_selection = Selection.SCISSOR
    private var cpu_selection = Selection.ROCK

    private var user_winner = true
    private var match_draw = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        logo = findViewById(R.id.logo)
        userImg = findViewById(R.id.userImage)
        droidImg = findViewById(R.id.droidImage)
        tv_result = findViewById(R.id.tv_result)
        radioGroup = findViewById(R.id.selection_rg)
        fab = findViewById(R.id.fab_play)
        rules_btn = findViewById(R.id.rules_btn)

        loadAnim()

        addListener()
        rulesBtnClicked()
    }

    private fun loadAnim() {
        val shake:Animation = AnimationUtils.loadAnimation(this,R.anim.anim1)
        userImg.startAnimation(shake)
        droidImg.startAnimation(shake)
        logo.startAnimation(shake)
    }

    private fun rulesBtnClicked() {
        val message:String = "Just Select your Option and click on Play Button \n That's All"
        rules_btn.setOnClickListener(View.OnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Rules")
                .setMessage(message)
                .show()
        })
    }

    private fun addListener() {


        fab.setOnClickListener(View.OnClickListener {

            val selected: Int = radioGroup!!.checkedRadioButtonId

            when (selected) {
                R.id.rb_rock -> user_selection = Selection.ROCK
                R.id.rb_paper -> user_selection = Selection.PAPER
                R.id.rb_scissor -> user_selection = Selection.SCISSOR
            }

            radioButton = findViewById(selected)
            Toast.makeText(this, "" + radioButton.text, Toast.LENGTH_SHORT).show()

            proceed()
        })


    }

    private fun proceed() {
        //get random cpu selection
        cpu_selection = getRandomCPUSelection()

        //get winner based on cpu_selection and user_selection
        checkWinner()

        //print result based on result
        showWinner()
    }

    private fun showWinner() {
        val line1 = "USER selection : $user_selection"
        val line2 = "CPU selection : $cpu_selection"
        val result: String = getResultString()

        val message = "$line1\n$line2\nResult: $result"
        tv_result.text = result
        setImage()

        resetValues()
    }

    private fun setImage() {
        when(user_selection){
            Selection.SCISSOR -> userImg.setImageDrawable(getDrawable(R.drawable.scissor_user))
            Selection.ROCK -> userImg.setImageDrawable(getDrawable(R.drawable.rock_user))
            Selection.PAPER -> userImg.setImageDrawable(getDrawable(R.drawable.paper_user))
        }
        when(cpu_selection){
            Selection.SCISSOR -> droidImg.setImageDrawable(getDrawable(R.drawable.scissor_droid))
            Selection.ROCK -> droidImg.setImageDrawable(getDrawable(R.drawable.rock_droid))
            Selection.PAPER -> droidImg.setImageDrawable(getDrawable(R.drawable.paper_droid))
        }
    }

    private fun getResultString(): String {
        val result: String = if (match_draw == true) {
            "Match draw! Let's go again"
        } else {
            if (user_winner) "You win!" else "You loose..."
        }

        return result
    }

    private fun resetValues() {
        user_selection = Selection.SCISSOR;
        cpu_selection = Selection.ROCK;

        user_winner = true;
        match_draw = false;
        Log.e("RESET:::", "values reset successful");

    }

    private fun getRandomCPUSelection(): Selection {

        when (Random().nextInt(3)) {
            0 -> return Selection.ROCK
            1 -> return Selection.PAPER
            2 -> return Selection.SCISSOR
        }
        return Selection.ROCK
    }

    private fun checkWinner() {
        if (user_selection == cpu_selection) {
            match_draw = true;
            return;
        }

        //rock wins from scissor, loose from paper
        if (user_selection == Selection.ROCK) {
            if (cpu_selection == Selection.PAPER) {
                user_winner = false;
                return;
            } else if (cpu_selection == Selection.SCISSOR) {
                user_winner = true;
                return;
            }
        }

        //paper wins from rock, loose from scissor
        if (user_selection == Selection.PAPER) {
            if (cpu_selection == Selection.SCISSOR) {
                user_winner = false;
                return;
            } else if (cpu_selection == Selection.ROCK) {
                user_winner = true;
                return;
            }
        }

        //scissor wins from paper, loose from rock
        if (user_selection == Selection.SCISSOR) {
            if (cpu_selection == Selection.ROCK) {
                user_winner = false;
                return;
            } else if (cpu_selection == Selection.PAPER) {
                user_winner = true;
                return;
            }
        }

        Log.e("cpu selected", cpu_selection.toString());
        Log.e("user selected", user_selection.toString());
    }

}