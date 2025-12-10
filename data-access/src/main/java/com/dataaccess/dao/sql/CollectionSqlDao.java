package com.dataaccess.dao.sql;

import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.db.DatabaseConnection;
import com.dataaccess.model.CollectionEntity;
import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.GenreEntity;
import com.dataaccess.model.UserEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionSqlDao implements ICollectionDao {

    @Override
    public void save(CollectionEntity collection) {
        String insertCollection = "INSERT INTO collections (name, user_id) VALUES (?, ?)";
        String insertCollectionFilm = "INSERT INTO collection_films (collection_id, film_id) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);

            PreparedStatement ps = conn.prepareStatement(insertCollection, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, collection.getName());

            if (collection.getOwner() != null && collection.getOwner().getId() != null)
                ps.setLong(2, collection.getOwner().getId());
            else
                ps.setNull(2, Types.BIGINT);

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                collection.setId(rs.getLong(1));
            }

            if (collection.getFilmCollection() != null && !collection.getFilmCollection().isEmpty()) {
                PreparedStatement psFilm = conn.prepareStatement(insertCollectionFilm);
                for (FilmEntity film : collection.getFilmCollection()) {
                    if (film.getId() != null) {
                        psFilm.setLong(1, collection.getId());
                        psFilm.setLong(2, film.getId());
                        psFilm.addBatch();
                    }
                }
                psFilm.executeBatch();
            }

            conn.commit();
            System.out.println("Kolekcia uložená: " + collection.getName());

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public CollectionEntity getById(Long id) {
        String sql = """
                SELECT c.id, c.name,
                       u.id AS user_id, u.fname, u.lname
                FROM collections c
                LEFT JOIN users u ON c.user_id = u.id
                WHERE c.id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CollectionEntity collection = new CollectionEntity();
                collection.setId(rs.getLong("id"));
                collection.setName(rs.getString("name"));

                UserEntity owner = new UserEntity();
                owner.setId(rs.getLong("user_id"));
                owner.setFname(rs.getString("fname"));
                owner.setLname(rs.getString("lname"));
                collection.setOwner(owner);

                collection.setFilmCollection(loadFilmsForCollection(conn, id));
                return collection;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<CollectionEntity> getAll() {
        List<CollectionEntity> collections = new ArrayList<>();

        String sql = """
                SELECT c.id, c.name,
                       u.id AS user_id, u.fname, u.lname
                FROM collections c
                LEFT JOIN users u ON c.user_id = u.id
                """;

        try (Connection conn = DatabaseConnection.getConnection()) {

            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                CollectionEntity collection = new CollectionEntity();
                collection.setId(rs.getLong("id"));
                collection.setName(rs.getString("name"));

                UserEntity owner = new UserEntity();
                owner.setId(rs.getLong("user_id"));
                owner.setFname(rs.getString("fname"));
                owner.setLname(rs.getString("lname"));
                collection.setOwner(owner);

                collection.setFilmCollection(loadFilmsForCollection(conn, collection.getId()));
                collections.add(collection);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return collections;
    }

    private ArrayList<FilmEntity> loadFilmsForCollection(Connection conn, Long collectionId) throws SQLException {

        String sql = """
                SELECT f.id, f.name, f.description, f.release_year, f.rating,
                       g.id AS genre_id, g.name AS genre_name
                FROM films f
                JOIN collection_films cf ON f.id = cf.film_id
                LEFT JOIN genres g ON f.genre_id = g.id
                WHERE cf.collection_id = ?
                """;

        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setLong(1, collectionId);
        ResultSet rs = ps.executeQuery();

        ArrayList<FilmEntity> films = new ArrayList<>();

        while (rs.next()) {
            FilmEntity film = new FilmEntity();
            film.setId(rs.getLong("id"));
            film.setName(rs.getString("name"));
            film.setDescription(rs.getString("description"));
            film.setReleaseYear(rs.getInt("release_year"));
            film.setRating(rs.getDouble("rating"));

            // načítanie žánru
            GenreEntity genre = null;
            Long gid = rs.getLong("genre_id");
            if (!rs.wasNull()) {
                genre = new GenreEntity();
                genre.setId(gid);
                genre.setName(rs.getString("genre_name"));
            }
            film.setGenre(genre);

            films.add(film);
        }

        return films;
    }

    @Override
    public void update(CollectionEntity collection) {
        try (Connection conn = DatabaseConnection.getConnection()) {

            String updateSql = "UPDATE collections SET name = ? WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(updateSql);
            stmt.setString(1, collection.getName());
            stmt.setLong(2, collection.getId());
            stmt.executeUpdate();

            String deleteFilms = "DELETE FROM collection_films WHERE collection_id = ?";
            PreparedStatement del = conn.prepareStatement(deleteFilms);
            del.setLong(1, collection.getId());
            del.executeUpdate();

            String insert = "INSERT INTO collection_films (collection_id, film_id) VALUES (?, ?)";
            PreparedStatement ins = conn.prepareStatement(insert);

            for (FilmEntity f : collection.getFilmCollection()) {
                ins.setLong(1, collection.getId());
                ins.setLong(2, f.getId());
                ins.executeUpdate();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
