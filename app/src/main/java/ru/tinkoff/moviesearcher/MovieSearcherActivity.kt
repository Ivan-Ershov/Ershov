package ru.tinkoff.moviesearcher

import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity

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

        if (findViewById<FrameLayout>(R.id.detail_fragment_container) == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .addToBackStack(null)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .replace(R.id.detail_fragment_container, fragment)
                .commit()
        }

    }
}