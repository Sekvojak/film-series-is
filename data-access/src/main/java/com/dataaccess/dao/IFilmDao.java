package com.dataaccess.dao;


import com.dataaccess.model.FilmEntity;
import com.domain.Film;

import java.util.List;

public interface IFilmDao {
    void save(FilmEntity film);
    FilmEntity getById(Long id);
    List<FilmEntity> getAll();

    void update(FilmEntity entity);
    void delete(Long id);
}