package movie.ticket.movie_ticket_booking.service;

import java.util.List;
import movie.ticket.movie_ticket_booking.entity.Movie;
import movie.ticket.movie_ticket_booking.entity.Showtime;
import movie.ticket.movie_ticket_booking.modelDTO.MovieDTO;
import movie.ticket.movie_ticket_booking.repository.MovieRepository;
import movie.ticket.movie_ticket_booking.repository.ShowtimeRepository;
import movie.ticket.movie_ticket_booking.util.NotFoundException;
import movie.ticket.movie_ticket_booking.util.ReferencedWarning;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class MovieService {

    private final MovieRepository movieRepository;
    private final ShowtimeRepository showtimeRepository;

    public MovieService(final MovieRepository movieRepository,
            final ShowtimeRepository showtimeRepository) {
        this.movieRepository = movieRepository;
        this.showtimeRepository = showtimeRepository;
    }

    public List<MovieDTO> findAll() {
        final List<Movie> movies = movieRepository.findAll(Sort.by("movieId"));
        return movies.stream()
                .map(movie -> mapToDTO(movie, new MovieDTO()))
                .toList();
    }

    public MovieDTO get(final Integer movieId) {
        return movieRepository.findById(movieId)
                .map(movie -> mapToDTO(movie, new MovieDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Integer create(final MovieDTO movieDTO) {
        final Movie movie = new Movie();
        mapToEntity(movieDTO, movie);
        return movieRepository.save(movie).getMovieId();
    }

    public void update(final Integer movieId, final MovieDTO movieDTO) {
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow(NotFoundException::new);
        mapToEntity(movieDTO, movie);
        movieRepository.save(movie);
    }

    public void delete(final Integer movieId) {
        movieRepository.deleteById(movieId);
    }

    private MovieDTO mapToDTO(final Movie movie, final MovieDTO movieDTO) {
        movieDTO.setMovieId(movie.getMovieId());
        movieDTO.setTitle(movie.getTitle());
        movieDTO.setDescription(movie.getDescription());
        movieDTO.setPoster(movie.getPoster());
        movieDTO.setDuration(movie.getDuration());
        movieDTO.setGenre(movie.getGenre());
        movieDTO.setReleaseDate(movie.getReleaseDate());
        movieDTO.setRating(movie.getRating());
        return movieDTO;
    }

    private Movie mapToEntity(final MovieDTO movieDTO, final Movie movie) {
        movie.setTitle(movieDTO.getTitle());
        movie.setDescription(movieDTO.getDescription());
        movie.setPoster(movieDTO.getPoster());
        movie.setDuration(movieDTO.getDuration());
        movie.setGenre(movieDTO.getGenre());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setRating(movieDTO.getRating());
        return movie;
    }

    public ReferencedWarning getReferencedWarning(final Integer movieId) {
        final ReferencedWarning referencedWarning = new ReferencedWarning();
        final Movie movie = movieRepository.findById(movieId)
                .orElseThrow(NotFoundException::new);
        final Showtime movieShowtime = showtimeRepository.findFirstByMovie(movie);
        if (movieShowtime != null) {
            referencedWarning.setKey("movie.showtime.movie.referenced");
            referencedWarning.addParam(movieShowtime.getShowtimeId());
            return referencedWarning;
        }
        return null;
    }

}
