package com.dataaccess.dao.sql;

import com.dataaccess.dao.IFilmDao;
import com.dataaccess.db.DatabaseConnection;
import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.GenreEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FilmSqlDao implements IFilmDao {

    @Override
    public void save(FilmEntity film) {

        String sql = """
                INSERT INTO films (name, description, genre_id, release_year, rating)
                VALUES (?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3,
                    film.getGenre() != null ? film.getGenre().getId() : null,
                    Types.BIGINT
            );
            stmt.setInt(4, film.getReleaseYear());
            stmt.setDouble(5, film.getRating());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public FilmEntity getById(Long id) {
        String sql = """
                SELECT f.id, f.name, f.description, f.release_year, f.rating,
                       g.id AS genre_id, g.name AS genre_name
                FROM films f
                LEFT JOIN genres g ON f.genre_id = g.id
                WHERE f.id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                FilmEntity film = new FilmEntity();
                film.setId(rs.getLong("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseYear(rs.getInt("release_year"));
                film.setRating(rs.getDouble("rating"));

                Long genreId = rs.getLong("genre_id");
                if (!rs.wasNull()) {
                    GenreEntity genre = new GenreEntity();
                    genre.setId(genreId);
                    genre.setName(rs.getString("genre_name"));
                    film.setGenre(genre);
                }

                return film;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<FilmEntity> getAll() {
        String sql = """
                SELECT f.id, f.name, f.description, f.release_year, f.rating,
                       g.id AS genre_id, g.name AS genre_name
                FROM films f
                LEFT JOIN genres g ON f.genre_id = g.id
                """;

        List<FilmEntity> films = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                FilmEntity film = new FilmEntity();
                film.setId(rs.getLong("id"));
                film.setName(rs.getString("name"));
                film.setDescription(rs.getString("description"));
                film.setReleaseYear(rs.getInt("release_year"));
                film.setRating(rs.getDouble("rating"));

                Long genreId = rs.getLong("genre_id");
                if (!rs.wasNull()) {
                    GenreEntity genre = new GenreEntity();
                    genre.setId(genreId);
                    genre.setName(rs.getString("genre_name"));
                    film.setGenre(genre);
                }

                films.add(film);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return films;
    }

    @Override
    public void update(FilmEntity film) {

        String sql = """
                UPDATE films
                SET name=?, description=?, genre_id=?, release_year=?, rating=?
                WHERE id=?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setObject(3,
                    film.getGenre() != null ? film.getGenre().getId() : null,
                    Types.BIGINT
            );
            stmt.setInt(4, film.getReleaseYear());
            stmt.setDouble(5, film.getRating());
            stmt.setLong(6, film.getId());

            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM films WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
