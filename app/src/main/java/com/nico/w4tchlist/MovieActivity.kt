package com.nico.w4tchlist

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.nico.w4tchlist.models.Movie
import kotlinx.android.synthetic.main.movie_item.view.*
import kotlin.math.floor

class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        val extras = intent.extras
        if(extras != null){
            val movie = extras.getParcelable<Movie>("movie") as Movie

            val IMAGE_BASE = "https://image.tmdb.org/t/p/w500/"

            //LOAD IMAGES
            if(movie.backdrop != null){
                Glide.with(this).load(IMAGE_BASE + movie.backdrop).into(findViewById(R.id.ivBackdrop))
            }

            if(movie.poster != null){
                Glide.with(this).load(IMAGE_BASE + movie.poster).into(findViewById(R.id.ivPoster))
            }

            findViewById<TextView>(R.id.tvTitle).text = movie.title
            findViewById<TextView>(R.id.tvDescription).text = movie.description
            findViewById<TextView>(R.id.tvDate).text = movie.release?.take(4)
            val rate_string = "${Math.round(movie.rate!!.toFloat() * 10.0) / 10.0}/10"
            findViewById<TextView>(R.id.tvRate).text = rate_string

            if(movie.adult == true){
                findViewById<ImageView>(R.id.ivAdult).isVisible = true
            }

            //GENRE
            var genre_string = ""
            var first = true
            for(genre in movie.genres!!){
                if(first){
                    genre_string += genre.name
                    first = false
                }else{
                    genre_string += ", " + genre.name
                }
            }
            findViewById<TextView>(R.id.tvGenres).text = genre_string

            //COMPANIES
            var companies_string = ""
            first = true
            for(company in movie.companies!!){
                if(first){
                    companies_string += company.name
                    first = false
                }else{
                    companies_string += ", " + company.name
                }
            }
            findViewById<TextView>(R.id.tvCompanies).text = companies_string

            //LANGUAGES
            var languages_string = ""
            first = true
            for(language in movie.languages!!){
                if(first){
                    languages_string += language.name
                    first = false
                }else{
                    languages_string += ", " + language.name
                }
            }
            findViewById<TextView>(R.id.tvLanguage).text = languages_string

        }
    }
}