package com.service.mapper;

import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.GenreEntity;
import com.domain.Film;
import com.domain.Genre;
import com.service.FilmService;
import com.service.GenreService;

public class FilmMapper {

    private static final GenreService genreService = new GenreService();

    public static Film toDomain(FilmEntity entity) {
        if (entity == null) return null;

        if (entity.getName() == null) {
            FilmService filmService = new FilmService();
            Film full = filmService.getFilmById(entity.getId());
            if (full != null) return full;
        }

        Film film = new Film();
        film.setId(entity.getId());
        film.setName(entity.getName());
        film.setDescription(entity.getDescription());
        film.setRating(entity.getRating());
        film.setReleaseYear(entity.getReleaseYear());

        if (entity.getGenre() != null && entity.getGenre().getId() != null) {
            Genre g = genreService.getGenreById(entity.getGenre().getId());
            film.setGenre(g);
        }

        return film;
    }


    public static FilmEntity toEntity(Film film) {
        if (film == null) return null;

        FilmEntity entity = new FilmEntity();
        entity.setId(film.getId());
        entity.setName(film.getName());
        entity.setDescription(film.getDescription());
        entity.setRating(film.getRating());
        entity.setReleaseYear(film.getReleaseYear());

        if (film.getGenre() != null) {
            GenreEntity ge = new GenreEntity();
            ge.setId(film.getGenre().getId());
            entity.setGenre(ge);
        }else {
            entity.setGenre(null);
        }

        return entity;
    }
}
