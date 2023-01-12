package com.nico.w4tchlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nico.w4tchlist.models.Genre
import com.nico.w4tchlist.models.MovieList
import kotlinx.android.synthetic.main.genre_item.view.*
import kotlinx.android.synthetic.main.genre_item.view.clLayout
import kotlinx.android.synthetic.main.lists_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class ListsAdapter(
    private val movielists : List<MovieList>
) : RecyclerView.Adapter<ListsAdapter.ListsViewHolder>() {

    private lateinit var mListener : onItemClickListener
    private lateinit var mListenerLong : onItemLongClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    interface onItemLongClickListener {
        fun onItemLongClick(position: Int) : Boolean
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    fun setOnItemClickListener(listenerLong: onItemLongClickListener){
        mListenerLong = listenerLong
    }

    class ListsViewHolder(view : View, listener: onItemClickListener, listenerLong: onItemLongClickListener) : RecyclerView.ViewHolder(view){
        fun bindList(movielist : MovieList){
            itemView.tvListName.text = movielist.name
            itemView.tvMoviesCount.text = "${movielist.movie_count}"
        }

        init{
            itemView.setOnClickListener {
                itemView.clLayout.setBackgroundColor(Color.parseColor("#777777"))
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener{
                itemView.clLayout.setBackgroundColor(Color.parseColor("#777777"))
                listenerLong.onItemLongClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListsViewHolder {
        return ListsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.lists_item, parent, false),
            mListener, mListenerLong
        )
    }

    override fun onBindViewHolder(holder: ListsViewHolder, position: Int) {
        holder.bindList(movielists[position])
    }

    override fun getItemCount(): Int = movielists.size
}