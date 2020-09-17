package com.example.crashtest

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.doOnPreDraw
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    data class Data(
        val int: Int,
        val boolean: Boolean
    )

    val diffUtil = object : DiffUtil.ItemCallback<Data>() {
        override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem.int == newItem.int
        }

        override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
            return oldItem == newItem
        }
    }

    val listAdapter = object : androidx.recyclerview.widget.ListAdapter<Data, ViewH>(diffUtil) {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewH {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.list_item, parent, false)
            return ViewH(view)
        }

        override fun onBindViewHolder(holder: ViewH, position: Int) {}
    }

    inner class ViewH(itemView: View) : RecyclerView.ViewHolder(itemView) {

        init {
            itemView.setOnClickListener {
                listAdapter.submitList(
                    emptyList()
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recycler.apply {
            layoutManager = LinearLayoutManager(context)
            itemAnimator = MyAnimator()
            adapter = listAdapter

            doOnPreDraw {
                listAdapter.submitList(
                    (0..20).map { Data(it, false) }.toList()
                )
            }
        }


    }

}

object Logger {
    fun d(m: String) {
        Log.d("123", m)
    }
}
