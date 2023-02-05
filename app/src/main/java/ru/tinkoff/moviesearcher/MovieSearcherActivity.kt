package ru.tinkoff.moviesearcher

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MovieSearcherActivity : AppCompatActivity(), MovieListFragment.Callbacks {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_searcher)

        val isFragmentContainerEmpty = (savedInstanceState == null)
        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MovieListFragment.newInstance())
                .commit()
        }

    }

    override fun onMovieSelected(movieId: Int) {
        val fragment = MovieFragment.newInstance(movieId)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}