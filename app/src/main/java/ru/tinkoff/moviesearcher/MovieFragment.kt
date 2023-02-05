package ru.tinkoff.moviesearcher

import android.annotation.SuppressLint
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import ru.tinkoff.moviesearcher.model.MovieDetail

private const val TAG = "MovieFragment"
private const val ARG_MOVIE_ID = "movie_id"

class MovieFragment : Fragment() {

    private lateinit var movieDetailViewModel: MovieDetailViewModel
    private lateinit var posterImageView: ImageView
    private lateinit var nameTextView: TextView
    private lateinit var descriptionTextView: TextView
    private lateinit var genresTextView: TextView
    private lateinit var countriesTextView: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieLoadError: LinearLayoutCompat
    private lateinit var repeatButton: Button
    private lateinit var movieScrollView: ScrollView
    private lateinit var thumbnailDownloader: ThumbnailDownloader<ImageView>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val movieId: Int = arguments?.getSerializable(ARG_MOVIE_ID) as Int
        CurrentMovie.movieId = movieId

        movieDetailViewModel =
            ViewModelProvider(this)[MovieDetailViewModel::class.java]

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler) {
                imageView, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            imageView.setImageDrawable(drawable)
        }
        lifecycle.addObserver(thumbnailDownloader.fragmentLifecycleObserver)

    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        viewLifecycleOwner.lifecycle.addObserver(
            thumbnailDownloader.viewLifecycleObserver
        )

        val view = inflater.inflate(R.layout.fragment_movie, container, false)

        posterImageView = view.findViewById(R.id.poster)
        nameTextView = view.findViewById(R.id.name)
        descriptionTextView = view.findViewById(R.id.description)
        genresTextView = view.findViewById(R.id.genres)
        countriesTextView = view.findViewById(R.id.countries)
        progressBar = view.findViewById(R.id.process)
        movieLoadError = view.findViewById(R.id.movie_load_error)
        repeatButton = view.findViewById(R.id.repeat)
        movieScrollView = view.findViewById(R.id.movie)

        repeatButton.setOnClickListener {
            movieLoadError.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            movieDetailViewModel.repeatLoad()

            movieDetailViewModel.movieDetailLiveData.observe(
                viewLifecycleOwner
            ) { movieDetail ->
                movieDetailObserver(movieDetail)
            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieDetailViewModel.movieDetailLiveData.observe(
            viewLifecycleOwner
        ) { movieDetail ->
            movieDetailObserver(movieDetail)
        }
    }

    private fun movieDetailObserver(movieDetail: MovieDetail?) {
        progressBar.visibility = View.INVISIBLE

        if (movieDetail == null) {
            movieLoadError.visibility = View.INVISIBLE
            movieLoadError.visibility = View.VISIBLE
        } else {

            movieLoadError.visibility = View.INVISIBLE
            movieScrollView.visibility = View.VISIBLE

            nameTextView.text = movieDetail.nameRu
            descriptionTextView.text = movieDetail.description

            var genresString = "Жанры:"
            var isStart = true

            movieDetail.genres.forEach { genre ->
                run {
                    if (!isStart) {
                        genresString += ","
                    } else {
                        isStart = false
                    }
                    genresString += " "
                    genresString += genre.name
                }
            }

            genresTextView.text = genresString

            var countiesString = "Страны:"
            isStart = true

            movieDetail.countries.forEach { country ->
                run {
                    if (!isStart) {
                        countiesString += ","
                    } else {
                        isStart = false
                    }
                    countiesString += " "
                    countiesString += country.name
                }
            }

            countriesTextView.text = countiesString

            thumbnailDownloader
                .queueThumbnail(posterImageView, movieDetail.posterUrl)

        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewLifecycleOwner.lifecycle.addObserver(
            thumbnailDownloader.viewLifecycleObserver
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(
            thumbnailDownloader.fragmentLifecycleObserver
        )
    }

    companion object {

        fun newInstance(movieId: Int): MovieFragment {
            val args = Bundle().apply {
                putSerializable(ARG_MOVIE_ID, movieId)
            }
            return MovieFragment().apply {
                arguments = args
            }
        }

    }

}