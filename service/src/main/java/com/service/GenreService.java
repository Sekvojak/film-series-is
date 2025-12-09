package com.service;

import com.dataaccess.GlobalConf;
import com.dataaccess.dao.IGenreDao;
import com.dataaccess.model.GenreEntity;
import com.domain.Genre;
import com.service.mapper.GenreMapper;

import java.util.List;
import java.util.stream.Collectors;

public class GenreService {

    private final IGenreDao genreDao;

    public GenreService() {
        this.genreDao = GlobalConf.getConnector().createGenreDao();
    }

    public List<Genre> getAllGenres() {
        List<GenreEntity> entities = genreDao.findAll();
        return entities.stream()
                .map(GenreMapper::toDomain)
                .collect(Collectors.toList());
    }

    public Genre getGenreById(Long id) {
        GenreEntity entity = genreDao.findById(id);
        return entity != null ? GenreMapper.toDomain(entity) : null;
    }

    public Genre getGenreByName(String name) {
        GenreEntity entity = genreDao.findByName(name);
        return entity != null ? GenreMapper.toDomain(entity) : null;
    }

    public void saveGenre(Genre genre) {
        GenreEntity entity = GenreMapper.toEntity(genre);
        genreDao.save(entity);
    }

    public void deleteGenre(Long id) {
        genreDao.delete(id);
    }
}
