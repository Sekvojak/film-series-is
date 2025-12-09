package com.dataaccess.dao;

import com.dataaccess.model.GenreEntity;
import java.util.List;

public interface IGenreDao {

    void save(GenreEntity genre);

    GenreEntity findById(Long id);

    GenreEntity findByName(String name);

    List<GenreEntity> findAll();

    void delete(Long id);
}
