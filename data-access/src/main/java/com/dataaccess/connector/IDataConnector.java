package com.dataaccess.connector;

import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.dao.IFilmDao;
import com.dataaccess.dao.IGenreDao;
import com.dataaccess.dao.IUserDao;

public interface IDataConnector {
    IUserDao createUserDao();
    IFilmDao createFilmDao();
    ICollectionDao createCollectionDao();
    IGenreDao createGenreDao();
}