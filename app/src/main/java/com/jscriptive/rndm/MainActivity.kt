package com.jscriptive.rndm

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.firestore.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var selectedCategory: String = POPULAR
    lateinit var thoughtsAdapter: ThoughtsAdapter
    val thoughts = arrayListOf<Thought>()
    val thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)
    lateinit var thoughtsListener: ListenerRegistration


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

    override fun onResume() {
        super.onResume()
        setListener()
    }

    fun setListener() {
        if (selectedCategory == POPULAR) {
            thoughtsListener = thoughtsCollectionRef
                    .orderBy(NUM_LIKES, Query.Direction.DESCENDING)
                    .addSnapshotListener(this) { snapshot, exception ->
                        updateViewOrLogException(snapshot, exception)
                    }
        } else {
            thoughtsListener = thoughtsCollectionRef
                    .orderBy(TIMESTAMP, Query.Direction.DESCENDING)
                    .whereEqualTo(CATEGORY, selectedCategory)
                    .addSnapshotListener(this) { snapshot, exception ->
                        updateViewOrLogException(snapshot, exception)
                    }
        }
    }

    private fun updateViewOrLogException(snapshot: QuerySnapshot?, exception: FirebaseFirestoreException?) {
        if (snapshot != null) {
            val thoughtObjects = snapshot.documents.map { document ->
                Thought(documentId = document.id,
                        username = document.data[USERNAME] as String,
                        timestamp = document.data[TIMESTAMP] as Date,
                        thoughtTxt = document.data[THOUGHT_TXT] as String,
                        numLikes = (document.data[NUM_LIKES] as Long).toInt(),
                        numComments = (document.data[NUM_COMMENTS] as Long).toInt())
            }
            thoughts.clear()
            thoughts.addAll(thoughtObjects)
            thoughtsAdapter.notifyDataSetChanged()
        }
        if (exception != null) {
            Log.e("Exception", "Could not retrieve documents: $exception")
        }
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
            thoughtsListener.remove()
            setListener()
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
            thoughtsListener.remove()
            setListener()
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
            thoughtsListener.remove()
            setListener()
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
            thoughtsListener.remove()
            setListener()
        }
    }

}
