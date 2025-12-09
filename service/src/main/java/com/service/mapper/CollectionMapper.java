package com.service.mapper;

import com.dataaccess.model.CollectionEntity;
import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.UserEntity;
import com.domain.Collection;
import com.domain.Film;
import com.domain.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionMapper {

    public static Collection toDomain(CollectionEntity entity) {
        if (entity == null) return null;

        Collection collection = new Collection();
        collection.setId(entity.getId());
        collection.setName(entity.getName());

        // OWNER — správne mapovanie cez UserMapper
        if (entity.getOwner() != null) {
            collection.setOwner(UserMapper.toDomain(entity.getOwner()));
        } else {
            collection.setOwner(null);
        }

        // FILMS — bezpečné mapovanie
        if (entity.getFilmCollection() != null) {
            collection.setFilmCollection(
                    entity.getFilmCollection().stream()
                            .map(FilmMapper::toDomain)
                            .collect(Collectors.toCollection(ArrayList::new))
            );
        } else {
            collection.setFilmCollection(new ArrayList<>());
        }

        return collection;
    }

    public static CollectionEntity toEntity(Collection collection) {
        CollectionEntity entity = new CollectionEntity();

        entity.setId(collection.getId());
        entity.setName(collection.getName());

        // OWNER
        UserEntity ue = new UserEntity();
        ue.setId(collection.getOwner().getId());  // MUSÍ byť ID, nie null
        entity.setOwner(ue);

        // FILMY
        ArrayList<FilmEntity> filmEntities = new ArrayList<>();
        for (Film f : collection.getFilmCollection()) {
            FilmEntity fe = new FilmEntity();
            fe.setId(f.getId());  // ak je null → problém
            filmEntities.add(fe);
        }

        entity.setFilmCollection(filmEntities);

        return entity;
    }

}
