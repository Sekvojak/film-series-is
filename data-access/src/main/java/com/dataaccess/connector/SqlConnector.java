package com.dataaccess.connector;

import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.dao.IFilmDao;
import com.dataaccess.dao.IGenreDao;
import com.dataaccess.dao.IUserDao;
import com.dataaccess.dao.sql.CollectionSqlDao;
import com.dataaccess.dao.sql.FilmSqlDao;
import com.dataaccess.dao.sql.GenreSqlDao;
import com.dataaccess.dao.sql.UserSqlDao;

public class SqlConnector implements IDataConnector {

    @Override
    public IUserDao createUserDao() {
        return new UserSqlDao();
    }

    @Override
    public IFilmDao createFilmDao() {
        return new FilmSqlDao();
    }

    @Override
    public ICollectionDao createCollectionDao() {
        return new CollectionSqlDao();
    }

    @Override
    public IGenreDao createGenreDao() { return new GenreSqlDao(); }
}
