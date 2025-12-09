package com.service.mapper;

import com.dataaccess.model.GenreEntity;
import com.domain.Genre;

public class GenreMapper {

    public static Genre toDomain(GenreEntity entity) {
        if (entity == null) return null;

        Genre genre = new Genre();
        genre.setId(entity.getId());
        genre.setName(entity.getName()); // môže byť null pri textovej DB
        return genre;
    }

    public static GenreEntity toEntity(Genre genre) {
        if (genre == null) return null;

        GenreEntity entity = new GenreEntity();
        entity.setId(genre.getId());
        entity.setName(genre.getName());
        return entity;
    }
}
