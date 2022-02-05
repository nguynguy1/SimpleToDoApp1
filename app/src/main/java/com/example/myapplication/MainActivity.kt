package com.example.myapplication

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import org.apache.commons.io.FileUtils
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.view.ViewGroup
import android.widget.*
import org.w3c.dom.Text
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.get
import android.widget.Button ;
import android.widget.DatePicker ;
import java.text.SimpleDateFormat ;
import java.util.Calendar ;
import java.util.Date ;
import java.util.Locale ;

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var itemsList = mutableListOf<String>()
    val monthList = arrayOf("January", "February", "March","April","May","June","July",
        "August","September","November","December")
    lateinit var monthView : String
    val dateList = arrayOf("1st", "2nd", "3rd", "4th","5th", "6th", "7th", "8th","9th", "10th",
        "11th", "12th", "13th", "14th","15th", "16th", "17th", "18th","19th", "20th",
        "21st", "22nd", "23rd", "24th","25th", "26th", "27th", "28th","29th", "30th","31st")
    lateinit var dateView : String
    lateinit var adapter: ItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fun getDataFile() : File {
            return File(filesDir,"data.txt")
        }

        fun loadItems() {
            try {
                itemsList = FileUtils.readLines(getDataFile(), Charset.defaultCharset().toString()) as MutableList<String>
                Log.i("load","items")
            }
            catch(iOExceptions:IOException) {
                iOExceptions.printStackTrace()
            }
        }

        fun saveItems() {
            try {
                FileUtils.writeLines(getDataFile(),itemsList)
                Log.i("save","items")
            }
            catch(iOExceptions:IOException) {
                iOExceptions.printStackTrace()
            }
        }

        fun loadAlarm() {
            var context:Context = this
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
            val requestId = 101
            val pendingIntent =
                PendingIntent.getService(context, requestId, intent,
                    PendingIntent.FLAG_NO_CREATE)
            if (pendingIntent != null && alarmManager != null) {
                alarmManager.cancel(pendingIntent)
            }

        }

        val longClickListener = object: ItemAdapter.LongClickListener {
            override fun onItemLongClicked(adapterPosition: Int) {
                itemsList.removeAt(adapterPosition)
                adapter.notifyDataSetChanged()
                saveItems()
            }
        }

        loadItems()
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        adapter = ItemAdapter(itemsList, longClickListener)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        val addInput = findViewById<EditText>(R.id.addItem)
        val monthAdapter = ArrayAdapter.createFromResource(this,R.array.month,android.R.layout.simple_spinner_item)
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val monthSpinner = findViewById<Spinner>(R.id.month)
        monthSpinner.adapter = monthAdapter
        monthSpinner.onItemSelectedListener = this
        val dateAdapter = ArrayAdapter.createFromResource(this,R.array.date,android.R.layout.simple_spinner_item)
        dateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        val dateSpinner = findViewById<Spinner>(R.id.date)
        dateSpinner.adapter = dateAdapter
        dateSpinner.onItemSelectedListener = this

        findViewById<Button>(R.id.addButton).setOnClickListener {
            var addItem = addInput.text.toString()
            if (addItem != null && addItem.length > 1) {
                addItem = addItem.substring(0,addItem.length-1)
            }
            if (!addItem.equals("") && !monthView.equals("") && monthView != null) {
                val sepMonth = addItem.split(" ")
                if (sepMonth[sepMonth.size-1] !in monthList) {
                    addItem = "$addItem $monthView"
                }
            }
            if (!addItem.equals("") && !dateView.equals("") && dateView != null) {
                val sepDate = addItem.split(" ")
                if (sepDate[sepDate.size-1] !in dateList) {
                    addItem = "$addItem $dateView)"
                }
            }
            itemsList.add(addItem)
            adapter.notifyItemInserted(itemsList.size - 1)
            addInput.setText("")
            saveItems()
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        if (parent != null) {
            when (parent.getId()) {
                R.id.month->monthView = parent?.getItemAtPosition(position).toString()
                R.id.date->dateView = parent?.getItemAtPosition(position).toString()
                else->print("no month or date chosen")
            }
        }
        else {
            print("no month or date chosen")
        }
        //monthView = parent?.getItemAtPosition(position).toString()
        //dateView = parent?.getItemAtPosition(position).toString()
        //Toast.makeText(this,monthView,Toast.LENGTH_SHORT).show()
    }
    @SuppressLint("SetTextI18n")
    override fun onNothingSelected(p0: AdapterView<*>?) {
        monthView = "January"
        dateView = "1st"
    }
}