package com.dataaccess.dao.sql;

import com.dataaccess.dao.IGenreDao;
import com.dataaccess.db.DatabaseConnection;
import com.dataaccess.model.GenreEntity;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GenreSqlDao implements IGenreDao {

    @Override
    public void save(GenreEntity genre) {
        String sql = "INSERT INTO genres (name) VALUES (?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, genre.getName());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GenreEntity findById(Long id) {
        String sql = "SELECT * FROM genres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                GenreEntity g = new GenreEntity();
                g.setId(rs.getLong("id"));
                g.setName(rs.getString("name"));
                return g;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public GenreEntity findByName(String name) {
        String sql = "SELECT * FROM genres WHERE LOWER(name) = LOWER(?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                GenreEntity g = new GenreEntity();
                g.setId(rs.getLong("id"));
                g.setName(rs.getString("name"));
                return g;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<GenreEntity> findAll() {
        List<GenreEntity> genres = new ArrayList<>();

        String sql = "SELECT * FROM genres ORDER BY name";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                GenreEntity g = new GenreEntity();
                g.setId(rs.getLong("id"));
                g.setName(rs.getString("name"));
                genres.add(g);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return genres;
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM genres WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
