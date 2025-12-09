package com.service;

import com.dataaccess.GlobalConf;
import com.dataaccess.dao.IFilmDao;
import com.dataaccess.model.FilmEntity;
import com.domain.Film;
import com.domain.Genre;
import com.service.mapper.FilmMapper;

import java.util.List;
import java.util.stream.Collectors;

public class FilmService {

    private final IFilmDao filmDao;

    public FilmService() {
        this.filmDao = GlobalConf.getConnector().createFilmDao();
    }

    public List<Film> getAllFilms() {
        List<FilmEntity> entities = filmDao.getAll();
        return entities.stream()
                .map(FilmMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Film getFilmById(Long id) {
        FilmEntity entity = filmDao.getById(id);
        return entity != null ? FilmMapper.toDomain(entity) : null;
    }

    public void saveFilm(Film film) {
        FilmEntity entity = FilmMapper.toEntity(film);
        filmDao.save(entity);
    }

    public void updateFilm(Film film) {
        FilmEntity entity = FilmMapper.toEntity(film);
        filmDao.update(entity);
    }

    public void deleteFilm(Long id) {
        filmDao.delete(id);
    }

    public List<Film> filterFilms(String name, Genre genre, Integer yearFrom, Integer yearTo, Double minRating) {
        List<Film> all = getAllFilms();

        return all.stream()
                .filter(f -> name == null || name.isEmpty() || f.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(f -> genre == null || (f.getGenre() != null && f.getGenre().getId().equals(genre.getId())))
                .filter(f -> yearFrom == null || f.getReleaseYear() >= yearFrom)
                .filter(f -> yearTo == null || f.getReleaseYear() <= yearTo)
                .filter(f -> minRating == null || f.getRating() >= minRating)
                .toList();
    }


}
