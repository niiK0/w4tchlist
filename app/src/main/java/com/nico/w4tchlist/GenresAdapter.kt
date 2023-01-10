package com.nico.w4tchlist

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.nico.w4tchlist.models.Genre
import kotlinx.android.synthetic.main.genre_item.view.*
import kotlinx.android.synthetic.main.movie_item.view.*

class GenresAdapter(
    private val genres : List<Genre>
) : RecyclerView.Adapter<GenresAdapter.GenreViewHolder>() {

    private lateinit var mListener : onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class GenreViewHolder(view : View, listener: onItemClickListener) : RecyclerView.ViewHolder(view){
        fun bindGenre(genre : Genre){
            itemView.tvGenreName.text = genre.name
        }

        init{
            itemView.setOnClickListener {
                itemView.clLayout.setBackgroundColor(Color.parseColor("#777777"))
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenreViewHolder {
        return GenreViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.genre_item, parent, false),
            mListener
        )
    }

    override fun onBindViewHolder(holder: GenreViewHolder, position: Int) {
        holder.bindGenre(genres[position])
    }

    override fun getItemCount(): Int = genres.size
}