package com.dataaccess.dao.text;

import com.dataaccess.dao.IFilmDao;
import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.GenreEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FilmTextDao implements IFilmDao {

    private final String filePath = "data/films.txt";

    // =========================================================
    // SAVE (INSERT or UPDATE)
    // =========================================================
    @Override
    public void save(FilmEntity film) {

        if (film.getId() != null) {
            update(film);
            return;
        }

        long newId = getAll().size() + 1;
        film.setId(newId);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true))) {

            bw.write(
                    film.getId() + ";" +
                            safe(film.getName()) + ";" +
                            safe(film.getDescription()) + ";" +
                            (film.getGenre() != null && film.getGenre().getId() != null
                                    ? film.getGenre().getId()
                                    : "null") + ";" +
                            film.getReleaseYear() + ";" +
                            film.getRating()
            );

            bw.newLine();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // GET ALL
    // =========================================================
    @Override
    public List<FilmEntity> getAll() {

        List<FilmEntity> films = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(";", -1);
                if (p.length < 6) continue;

                FilmEntity f = new FilmEntity();
                f.setId(Long.parseLong(p[0]));
                f.setName(p[1]);
                f.setDescription(p[2]);

                // ✔ žáner len ak existuje a je platný
                GenreEntity g = new GenreEntity();
                if (p[3] != null && !p[3].equals("null") && !p[3].isEmpty()) {
                    g.setId(Long.parseLong(p[3]));
                }
                f.setGenre(g);

                f.setReleaseYear(Integer.parseInt(p[4]));
                f.setRating(Double.parseDouble(p[5]));

                films.add(f);
            }

        } catch (IOException e) {
            return films;
        }

        return films;
    }

    @Override
    public FilmEntity getById(Long id) {
        return getAll().stream()
                .filter(f -> f.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // =========================================================
    // UPDATE – rewrite whole file
    // =========================================================
    @Override
    public void update(FilmEntity film) {

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(";", -1);
                long id = Long.parseLong(p[0]);

                if (id == film.getId()) {

                    String newLine =
                            film.getId() + ";" +
                                    safe(film.getName()) + ";" +
                                    safe(film.getDescription()) + ";" +
                                    (film.getGenre() != null && film.getGenre().getId() != null
                                            ? film.getGenre().getId()
                                            : "null") + ";" +
                                    film.getReleaseYear() + ";" +
                                    film.getRating();

                    lines.add(newLine);

                } else {
                    lines.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // DELETE
    // =========================================================
    @Override
    public void delete(Long id) {

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] p = line.split(";", -1);
                long fid = Long.parseLong(p[0]);

                if (fid != id) {
                    lines.add(line);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            for (String l : lines) {
                bw.write(l);
                bw.newLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // =========================================================
    // HELPERS
    // =========================================================
    private String safe(String s) {
        return (s == null) ? "" : s.replace(";", ",");
    }
}
