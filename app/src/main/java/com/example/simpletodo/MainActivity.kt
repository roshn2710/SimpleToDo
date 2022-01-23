package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.FileUtils
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileSystem

import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lateinit var adapter: TaskItemAdapter

        val onLongClickListener = object : TaskItemAdapter.OnLongClickListener {
            override fun onItemLongCliked(position: Int) {
                // remove the item from the list
                listOfTasks.removeAt(position)
                // notify the adapter that our data set has changed
                adapter.notifyDataSetChanged()
                saveItems()
            }

        }

        loadItems()

        //look up recyclerView in layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val inputTextField = findViewById<EditText>(R.id.addTaskField)

        //set up the button and input field, so that the user can enter a task and add it to the list
        //get a reference to the button
        //and then set an onclickListener
        findViewById<Button>(R.id.button).setOnClickListener {
            //grab the text the user has inputted into @+id/addTaskField
            val userInputtedTask = inputTextField.text.toString()

            //add the string to our list of tasks: lostOfTasks
            listOfTasks.add(userInputtedTask)

            //notify the adapoter that our data has been updated
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //reset text field
            inputTextField.setText("")
            saveItems()

        }
    }

    //save the data that the user has inputted
    //save data by writing and reading from a file
    //get the file we need
    fun getDataFile() : File {
        //every line is going to represent a specific task in our list of tasks
        return File(filesDir, "data.txt")
    }
    //load the items by reading every line in the data file
    fun loadItems() {
        try {
            listOfTasks =
                org.apache.commons.io.FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }

    }

    //save items by writing them into our data file
    fun saveItems() {
        try {
            org.apache.commons.io.FileUtils.writeLines(getDataFile(), listOfTasks)
        }catch (ioException: IOException) {
            ioException.printStackTrace()
        }
    }
}