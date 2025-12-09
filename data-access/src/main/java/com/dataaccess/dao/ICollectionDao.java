package com.dataaccess.dao;

import com.dataaccess.model.CollectionEntity;

import java.util.List;

public interface ICollectionDao {
    void save(CollectionEntity comment);
    CollectionEntity getById(Long id);
    List<CollectionEntity> getAll();

    void update(CollectionEntity entity);
}
