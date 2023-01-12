package com.nico.w4tchlist

import android.graphics.Color
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.nico.w4tchlist.models.Movie
import kotlinx.android.synthetic.main.fragment_list_open.view.*
import kotlinx.android.synthetic.main.genre_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class MovieAdapterList(
    private val movies : List<Movie>
) : RecyclerView.Adapter<MovieAdapterList.MovieViewHolder>() {

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

    class MovieViewHolder(view : View, listener: onItemClickListener, listenerLong: onItemLongClickListener) : RecyclerView.ViewHolder(view){
        private val IMAGE_BASE = "https://image.tmdb.org/t/p/w342/"
        fun bindMovie(movie : Movie){
            Glide.with(itemView).load(IMAGE_BASE + movie.poster).into(itemView.movie_poster)
        }

        init{
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            itemView.setOnLongClickListener{
                listenerLong.onItemLongClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.movie_item_on_list, parent, false),
            mListener, mListenerLong
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bindMovie(movies[position])
    }

    override fun getItemCount(): Int = movies.size
}
