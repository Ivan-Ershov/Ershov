package ru.tinkoff.moviesearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.tinkoff.moviesearcher.api.MovieSearcherFetcher
import ru.tinkoff.moviesearcher.model.Movie

class MovieListViewModel : ViewModel() {

    val moviesLiveData: LiveData<List<Movie>> = MovieSearcherFetcher().getMoviesTop()

}