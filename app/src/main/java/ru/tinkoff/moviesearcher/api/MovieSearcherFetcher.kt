package ru.tinkoff.moviesearcher.api

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.tinkoff.moviesearcher.model.Movie
import ru.tinkoff.moviesearcher.model.MovieDetail

private const val TAG = "MovieSearcherFetcher"

class MovieSearcherFetcher {

    private val movieSearcherApi: MovieSearcherApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://kinopoiskapiunofficial.tech/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        movieSearcherApi = retrofit.create(MovieSearcherApi::class.java)
    }

    fun getMoviesTop(page: Int = 1): LiveData<List<Movie>> {
        val responseLiveData: MutableLiveData<List<Movie>> = MutableLiveData()
        val movieSearcherRequest: Call<MoviesTopResponse> = movieSearcherApi.getMoviesTop(page=page)

        movieSearcherRequest.enqueue(object : Callback<MoviesTopResponse> {

            override fun onFailure(call: Call<MoviesTopResponse>, t: Throwable) {
                Log.e(TAG, "Failure", t)
            }

            override fun onResponse(call: Call<MoviesTopResponse>,
                                    response: Response<MoviesTopResponse>) {
                Log.e(TAG, "Response: ${response.body()}")

                val moviesTopResponse: MoviesTopResponse? = response.body()
                val movies: List<Movie> = moviesTopResponse?.movies?: mutableListOf()

                responseLiveData.value = movies
            }

        })

        return responseLiveData
    }

    fun getMovie(id: Int): LiveData<MovieDetail> {
        val responseLiveData: MutableLiveData<MovieDetail> = MutableLiveData()
        val movieSearcherRequest: Call<MovieDetail> = movieSearcherApi.getMovie(id=id)

        movieSearcherRequest.enqueue(object : Callback<MovieDetail>{

            override fun onFailure(call: Call<MovieDetail>, t: Throwable) {
                Log.e(TAG, "Failure", t)
            }

            override fun onResponse(call: Call<MovieDetail>, response: Response<MovieDetail>) {
                Log.e(TAG, "Response: ${response.body()}")

                val movieResponse: MovieDetail? = response.body()

                responseLiveData.value = movieResponse
            }

        })

        return responseLiveData
    }

    @WorkerThread
    fun getPhoto(url: String): Bitmap? {
        val response: Response<ResponseBody> = movieSearcherApi.getUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }

}