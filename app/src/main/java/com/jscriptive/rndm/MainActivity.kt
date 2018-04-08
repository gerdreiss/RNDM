package com.jscriptive.rndm

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    var selectedCategory: String = FUNNY
    lateinit var thoughtsAdapter: ThoughtsAdapter
    val thoughts = arrayListOf<Thought>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val addThoughtIntent = Intent(this, AddThoughtActivity::class.java)
            startActivity(addThoughtIntent)
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts)
        thoughtListView.adapter = thoughtsAdapter
        thoughtListView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun mainFunnyClicked(view: View) {
        if (selectedCategory == FUNNY) {
            mainFunnyBtn.isChecked = true
        } else {
            mainSeriousBtn.isChecked = false
            mainCrazyBtn.isChecked = false
            mainPopularBtn.isChecked = false
            selectedCategory = FUNNY
        }
    }

    fun mainSeriousClicked(view: View) {
        if (selectedCategory == SERIOUS) {
            mainSeriousBtn.isChecked = true
        } else {
            mainFunnyBtn.isChecked = false
            mainCrazyBtn.isChecked = false
            mainPopularBtn.isChecked = false
            selectedCategory = SERIOUS
        }
    }

    fun mainCrazyClicked(view: View) {
        if (selectedCategory == CRAZY) {
            mainCrazyBtn.isChecked = true
        } else {
            mainFunnyBtn.isChecked = false
            mainSeriousBtn.isChecked = false
            mainPopularBtn.isChecked = false
            selectedCategory = CRAZY
        }
    }

    fun mainPopularClicked(view: View) {
        if (selectedCategory == POPULAR) {
            mainPopularBtn.isChecked = true
        } else {
            mainFunnyBtn.isChecked = false
            mainSeriousBtn.isChecked = false
            mainCrazyBtn.isChecked = false
            selectedCategory = POPULAR
        }
    }

}
