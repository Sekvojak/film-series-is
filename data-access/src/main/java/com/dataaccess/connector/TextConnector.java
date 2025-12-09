package com.dataaccess.connector;

import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.dao.IFilmDao;
import com.dataaccess.dao.IGenreDao;
import com.dataaccess.dao.IUserDao;
import com.dataaccess.dao.text.CollectionTextDao;
import com.dataaccess.dao.text.FilmTextDao;
import com.dataaccess.dao.text.GenreTextDao;
import com.dataaccess.dao.text.UserTextDao;

public class TextConnector implements IDataConnector{

    @Override
    public IUserDao createUserDao() {
        return new UserTextDao();
    }

    @Override
    public IFilmDao createFilmDao() {
        return new FilmTextDao();
    }

    @Override
    public ICollectionDao createCollectionDao() {
        return new CollectionTextDao();
    }

    @Override
    public IGenreDao createGenreDao() { return new GenreTextDao();}
}
