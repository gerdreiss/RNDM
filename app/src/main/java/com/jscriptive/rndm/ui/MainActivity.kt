package com.jscriptive.rndm.ui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.jscriptive.rndm.*
import com.jscriptive.rndm.domain.Thought
import com.jscriptive.rndm.ui.adapter.ThoughtsAdapter
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var selectedCategory: String = POPULAR
    val thoughts = arrayListOf<Thought>()
    val thoughtsCollectionRef = FirebaseFirestore.getInstance().collection(THOUGHTS_REF)

    lateinit var thoughtsAdapter: ThoughtsAdapter
    lateinit var thoughtsListener: ListenerRegistration
    lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener {
            val intent = Intent(this, AddThoughtActivity::class.java)
            intent.putExtra(SELECTED_CATEGORY, if (selectedCategory == POPULAR) FUNNY else selectedCategory)
            startActivity(intent)
        }

        thoughtsAdapter = ThoughtsAdapter(thoughts) { thought ->
            val intent = Intent(this, CommentsActivity::class.java)
            intent.putExtra(DOCUMENT_KEY, thought.documentId)
            startActivity(intent)
        }
        thoughtListView.adapter = thoughtsAdapter
        thoughtListView.layoutManager = LinearLayoutManager(this)

        auth = FirebaseAuth.getInstance()
    }

    override fun onResume() {
        super.onResume()
        updateUI()
    }

    fun updateUI() {
        if (auth.currentUser == null) {
            enableButtons(false)
            thoughts.clear()
            thoughtsAdapter.notifyDataSetChanged()
        } else {
            enableButtons(true)
            setListener()
        }
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

    override fun onPrepareOptionsMenu(menu: Menu): Boolean {
        val menuItem = menu.getItem(0)
        if (auth.currentUser == null)
            menuItem.title = "Login"
        else
            menuItem.title = "Logout"
        return super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_login -> {
                if (auth.currentUser == null)
                    startActivity(Intent(this, LoginActivity::class.java))
                else auth.signOut()
                updateUI()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun enableButtons(enable: Boolean) {
        mainFunnyBtn.isEnabled = enable
        mainSeriousBtn.isEnabled = enable
        mainCrazyBtn.isEnabled = enable
        mainPopularBtn.isEnabled = enable
        fab.isEnabled = enable
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
