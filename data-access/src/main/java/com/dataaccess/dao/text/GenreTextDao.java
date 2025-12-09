package com.dataaccess.dao.text;

import com.dataaccess.dao.IGenreDao;
import com.dataaccess.model.GenreEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class GenreTextDao implements IGenreDao {

    private static final String filePath = "data/genres.txt";

    @Override
    public void save(GenreEntity genre) {

        if (genre.getId() == null) {
            long newId = findAll().size() + 1;
            genre.setId(newId);
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(
                    genre.getId() + ";" +
                            genre.getName()
            );
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public GenreEntity findById(Long id) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                long genreId = Long.parseLong(parts[0]);
                if (genreId == id) {
                    GenreEntity g = new GenreEntity();
                    g.setId(genreId);
                    g.setName(parts[1]);
                    return g;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("genres.txt neexistuje.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public GenreEntity findByName(String name) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                if (parts[1].equalsIgnoreCase(name)) {
                    GenreEntity g = new GenreEntity();
                    g.setId(Long.parseLong(parts[0]));
                    g.setName(parts[1]);
                    return g;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println("genres.txt neexistuje.");
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private void initDefaultGenres() {
        File file = new File(filePath);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }

        if (file.exists() && file.length() > 0) {
            return;
        }

        List<String> defaults = List.of(
                "Action",
                "Comedy",
                "Drama",
                "Horror",
                "Sci-Fi",
                "Thriller",
                "Romance",
                "Documentary"
        );

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            long id = 1;
            for (String genre : defaults) {
                writer.write(id + ";" + genre);
                writer.newLine();
                id++;
            }
            System.out.println("TXT genres initialized with default values.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<GenreEntity> findAll() {

        initDefaultGenres();

        List<GenreEntity> genres = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                if (line.trim().isEmpty()) continue;

                String[] parts = line.split(";");
                if (parts.length < 2) continue;

                GenreEntity g = new GenreEntity();
                g.setId(Long.parseLong(parts[0]));
                g.setName(parts[1]);

                genres.add(g);
            }

        } catch (FileNotFoundException e) {
            return genres;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return genres;
    }

    @Override
    public void delete(Long id) {
        List<GenreEntity> genres = findAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {

            for (GenreEntity g : genres) {
                if (!g.getId().equals(id)) {
                    writer.write(
                            g.getId() + ";" + g.getName()
                    );
                    writer.newLine();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
