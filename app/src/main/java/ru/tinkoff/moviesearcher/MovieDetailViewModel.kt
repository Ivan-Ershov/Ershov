package ru.tinkoff.moviesearcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ru.tinkoff.moviesearcher.api.MovieSearcherFetcher
import ru.tinkoff.moviesearcher.model.MovieDetail

class MovieDetailViewModel : ViewModel() {

    val movieDetailLiveData: LiveData<MovieDetail>
    = MovieSearcherFetcher().getMovie(CurrentMovie.movieId)

}