package com.jpdevzone.knowyourdreams.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.jpdevzone.knowyourdreams.Constants
import com.jpdevzone.knowyourdreams.Dream
import com.jpdevzone.knowyourdreams.R
import es.dmoral.toasty.Toasty

class RecyclerViewAdapter(private val dreams: ArrayList<Dream>, private val listener: OnItemClickListener) : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(), Filterable {
    private var tempDreams = ArrayList<Dream>()
    init {
        tempDreams = dreams
    }

    inner class ViewHolder (itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var dream: TextView = itemView.findViewById(R.id.tv_item)
        val icon: ImageButton = itemView.findViewById(R.id.addToFavourites)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position: Int = absoluteAdapterPosition
            val currentItem = tempDreams[position]
            val item = tempDreams[position].dreamItem
            val definition = tempDreams[position].dreamDefinition
            if (position != RecyclerView.NO_POSITION) {
                listener.onItemClick(item,definition,currentItem)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_dream,viewGroup,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        val currentItem = tempDreams[position]
        viewHolder.dream.text = currentItem.dreamItem

        viewHolder.icon.setOnClickListener {
            if (!Constants.favourites.contains(currentItem)) {
                Constants.favourites.add(currentItem)
                viewHolder.icon.startAnimation(AnimationUtils.loadAnimation(viewHolder.icon.context, R.anim.shake))
                Toasty.custom(viewHolder.icon.context, R.string.addedToFavs,R.drawable.ic_star_full,R.color.blue_700,Toast.LENGTH_SHORT,true, true).show()
            }else{
                Toasty.custom(viewHolder.icon.context, R.string.alreadyAdded, R.drawable.ic_attention,R.color.blue_700,Toast.LENGTH_SHORT,true, true).show()
            }
        }
    }

    override fun getItemCount() = tempDreams.size

    interface OnItemClickListener {
        fun onItemClick(item: String, definition: String, currentItem: Dream)
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                tempDreams = if (constraint==null || constraint.isEmpty()) {
                    dreams
                } else {
                    val resultDreams = ArrayList<Dream>()
                    for (row in dreams) {
                        if (row.dreamItem.lowercase().startsWith(constraint.toString().lowercase().trim())) {
                            resultDreams.add(row)
                        }
                    }
                    resultDreams
                }
                val filterResults = FilterResults()
                filterResults.values = tempDreams
                return filterResults
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                @Suppress("UNCHECKED_CAST")
                tempDreams = results?.values as ArrayList<Dream>
                notifyDataSetChanged()
            }
        }
    }
}