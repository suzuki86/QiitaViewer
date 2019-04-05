package com.timersinc.trainning.qiitaviewer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.list_item_item.view.*

class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(MainViewModel::class.java)
    }

    private val itemAdapter = ItemAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.items.observe(this, Observer { items ->
            items?.let { itemAdapter.set(it) }
        })

        itemList.layoutManager = LinearLayoutManager(this)
        itemList.adapter = itemAdapter

        viewModel.load()
    }

    class ItemAdapter : RecyclerView.Adapter<ItemViewHolder>() {
        private val items: ArrayList<Item> = ArrayList()
        fun set(newItems: List<Item>) {
            items.clear()
            items.addAll(newItems)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ItemViewHolder {
            return ItemViewHolder(parent)
        }

        override fun getItemCount(): Int {
            return items.count()
        }

        override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
            holder.bindData(items[position])
        }
    }

    class ItemViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_item, parent, false)
    ) {
        fun bindData(item: Item) {
            Glide.with(itemView.context)
                .load(item.user.imageUrl)
                .into(itemView.userThumb)
            itemView.title.text = item.title
            itemView.userId.text = item.user.id
        }
    }
}
