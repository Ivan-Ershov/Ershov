package ru.tinkoff.moviesearcher

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ru.tinkoff.moviesearcher.model.Movie

private const val TAG = "MovieListFragment"

class MovieListFragment : Fragment() {

    interface Callbacks {
        fun onMovieSelected(movieId: Int)
    }

    private var callbacks: Callbacks? = null
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var movieRecyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var movieLoadError: LinearLayoutCompat
    private lateinit var repeatButton: Button
    private lateinit var thumbnailDownloader: ThumbnailDownloader<MovieHolder>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callbacks = context as Callbacks?
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        retainInstance = true

        movieListViewModel =
            ViewModelProvider(this)[MovieListViewModel::class.java]

        val responseHandler = Handler()
        thumbnailDownloader = ThumbnailDownloader(responseHandler) {
            photoHolder, bitmap ->
            val drawable = BitmapDrawable(resources, bitmap)
            photoHolder.bindDrawable(drawable)
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

        val view = inflater.inflate(R.layout.fragment_movie_list, container, false)

        movieRecyclerView = view.findViewById(R.id.movie_recycler_view)
        movieRecyclerView.layoutManager = LinearLayoutManager(context)
        progressBar = view.findViewById(R.id.process)
        movieLoadError = view.findViewById(R.id.movie_load_error)
        repeatButton = view.findViewById(R.id.repeat)

        repeatButton.setOnClickListener {
            movieLoadError.visibility = View.INVISIBLE
            progressBar.visibility = View.VISIBLE
            movieListViewModel.repeatLoad()

            movieListViewModel.moviesLiveData.observe(
                viewLifecycleOwner
            ) { movieDetail ->
                movieListObserver(movieDetail)
            }

        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        movieListViewModel.moviesLiveData.observe(
            viewLifecycleOwner
        ) { movies ->
            movieListObserver(movies)
        }
    }

    private fun movieListObserver(movies: List<Movie>?) {
        progressBar.visibility = View.INVISIBLE

        if (movies == null) {
            movieLoadError.visibility = View.INVISIBLE
            movieLoadError.visibility = View.VISIBLE
        } else {
            movieLoadError.visibility = View.INVISIBLE
            movieRecyclerView.visibility = View.VISIBLE

            movieRecyclerView.adapter = MovieAdapter(movies)

        }
    }

    override fun onDetach() {
        super.onDetach()
        callbacks = null
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

    private inner class MovieHolder(view: View)
        : RecyclerView.ViewHolder(view), View.OnClickListener {

        private lateinit var movie: Movie
        private val nameTextView: TextView = itemView.findViewById(R.id.movie_name)
        private val descriptionTextView: TextView = itemView.findViewById(R.id.movie_description)
        private val previewImageView: ImageView = itemView.findViewById(R.id.preview)

        init {
            itemView.setOnClickListener(this)
        }

        val bindDrawable: (Drawable) -> Unit = previewImageView::setImageDrawable

        fun bind(movie: Movie) {
            this.movie = movie

            nameTextView.text = movie.nameRu

            val description: String = if (movie.genres.isEmpty()) {
                movie.year
            } else {
                "${movie.genres.first().name.substring(0, 1).uppercase()}${movie.genres.first().name.substring(1)} (${movie.year})"
            }

            descriptionTextView.text = description

        }

        override fun onClick(v: View?) {
            callbacks?.onMovieSelected(movie.id)
        }

    }

    private inner class MovieAdapter(private val movies: List<Movie>)
        : RecyclerView.Adapter<MovieHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup,
                                        viewType: Int
        ): MovieHolder {
           val view = layoutInflater.inflate(R.layout.list_item_movie, parent, false)
           return MovieHolder(view)
        }

        override fun getItemCount(): Int = movies.size

        override fun onBindViewHolder(holder: MovieHolder, position: Int) {
            val movie = movies[position]

            holder.bind(movie)

            thumbnailDownloader.queueThumbnail(holder, movie.posterUrlPreview)

        }

    }

    companion object {
        fun newInstance() = MovieListFragment()
    }

}