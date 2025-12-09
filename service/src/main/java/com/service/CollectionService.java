package com.service;

import com.dataaccess.GlobalConf;
import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.model.CollectionEntity;
import com.domain.Collection;
import com.service.mapper.CollectionMapper;
import com.service.session.UserSession;
import com.service.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

public class CollectionService {

    private final ICollectionDao collectionDao;

    public CollectionService() {
        this.collectionDao = GlobalConf.getConnector().createCollectionDao();
    }

    public List<Collection> getUserCollections() {
        Long loggedUserId = UserSession.getLoggedUser().getId();

        return collectionDao.getAll().stream()
                .map(CollectionMapper::toDomain)
                .filter(c -> c.getOwner() != null && c.getOwner().getId().equals(loggedUserId))
                .collect(Collectors.toList());
    }

    public Collection getCollectionById(Long id) {
        CollectionEntity entity = collectionDao.getById(id);
        return entity != null ? CollectionMapper.toDomain(entity) : null;
    }

    public void saveCollection(Collection collection) {
        collection.setOwner(UserSession.getLoggedUser());
        CollectionEntity entity = CollectionMapper.toEntity(collection);
        collectionDao.save(entity);
    }

    public void updateCollection(Collection collection) {
        CollectionEntity entity = CollectionMapper.toEntity(collection);
        collectionDao.update(entity);
    }

}
