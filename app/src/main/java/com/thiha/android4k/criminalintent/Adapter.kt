package com.thiha.android4k.criminalintent

import android.text.format.DateFormat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class Adapter(private val callbacks: Callbacks) :
    ListAdapter<Crime, RecyclerView.ViewHolder>(object : DiffUtil.ItemCallback<Crime>() {
        override fun areItemsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: Crime, newItem: Crime): Boolean =
            oldItem.id == newItem.id

    }) {

    companion object {
        private const val NORMAL_CASE = 1
        private const val SERIOUS_CASE = 2

        fun formatDate(date: Date): String {
            return DateFormat.format("EEEE, MMM dd,yyyy", date).toString()
        }

        fun formatTime(date: Date): String {
            return DateFormat.format("h:mm a", date).toString()
        }

    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Crime) {
            crime = item
            itemView.findViewById<TextView>(R.id.crime_title).text = item.title
            itemView.findViewById<TextView>(R.id.crime_date).text = formatDate(item.date)
            itemView.findViewById<ImageView>(R.id.crimeSolved).visibility = when (item.isSolved) {
                true -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }

        override fun onClick(v: View?) {
            callbacks.onCrimeSelected(crime.id)
        }
    }

    inner class SeriousHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {
        private lateinit var crime: Crime

        init {
            itemView.setOnClickListener(this)
        }

        fun bind(item: Crime) {
            crime = item
            itemView.findViewById<TextView>(R.id.crime_title).text = item.title
            itemView.findViewById<TextView>(R.id.crime_date).text = formatDate(item.date)
            itemView.findViewById<Button>(R.id.btnCallPolice).isEnabled = true
            itemView.findViewById<ImageView>(R.id.crimeSolved).visibility = when (item.isSolved) {
                true -> View.VISIBLE
                else -> View.INVISIBLE
            }
        }

        override fun onClick(v: View?) {
            callbacks.onCrimeSelected(crime.id)
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentItem = currentList[position]
        return if (!currentItem.requirePolice) {
            NORMAL_CASE
        } else {
            SERIOUS_CASE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == NORMAL_CASE) {
            MyViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
            )
        } else {
            SeriousHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.list_item_crime, parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (currentList[position].requirePolice) {
            (holder as SeriousHolder).bind(currentList[position])
        } else {
            (holder as MyViewHolder).bind(currentList[position])
        }
    }

    interface Callbacks {
        fun onCrimeSelected(crimeId: UUID)
    }


}