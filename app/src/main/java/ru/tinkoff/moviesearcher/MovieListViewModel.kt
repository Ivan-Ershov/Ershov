package ru.tinkoff.moviesearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.tinkoff.moviesearcher.api.MovieSearcherFetcher
import ru.tinkoff.moviesearcher.model.Movie

class MovieListViewModel : ViewModel() {

    var moviesLiveData: LiveData<List<Movie>?> = MovieSearcherFetcher().getMoviesTop()

    fun repeatLoad() {
        moviesLiveData = MovieSearcherFetcher().getMoviesTop()
    }

}