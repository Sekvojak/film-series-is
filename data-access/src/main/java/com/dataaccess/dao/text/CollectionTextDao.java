package com.dataaccess.dao.text;

import com.dataaccess.dao.ICollectionDao;
import com.dataaccess.model.CollectionEntity;
import com.dataaccess.model.FilmEntity;
import com.dataaccess.model.UserEntity;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CollectionTextDao implements ICollectionDao {

    private static final String filePath = "data/collections.txt";

    // =============================================================
    // SAVE – ak id existuje → UPDATE, ak nie → INSERT
    // =============================================================
    @Override
    public void save(CollectionEntity collection) {

        // ak má ID → je to update, nie insert
        if (collection.getId() != null) {
            update(collection);
            return;
        }

        // inak vytvor nové ID
        long newId = getAll().size() + 1;
        collection.setId(newId);

        writeAppend(collection);
    }

    // -------------------------------------------------------------
    // Pomocná metóda na zápis nového riadku (INSERT)
    // -------------------------------------------------------------
    private void writeAppend(CollectionEntity collection) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {

            String filmIds = generateFilmIds(collection);

            writer.write(
                    collection.getId() + ";" +
                            (collection.getOwner() != null ? collection.getOwner().getId() : "null") + ";" +
                            collection.getName() + ";" +
                            filmIds
            );
            writer.newLine();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // =============================================================
    // GET BY ID
    // =============================================================
    @Override
    public CollectionEntity getById(Long id) {
        return getAll().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    // =============================================================
    // GET ALL
    // =============================================================
    @Override
    public List<CollectionEntity> getAll() {

        List<CollectionEntity> collections = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;
            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");
                if (parts.length < 4) continue;

                CollectionEntity collection = new CollectionEntity();
                collection.setId(Long.parseLong(parts[0]));

                // owner
                UserEntity owner = new UserEntity();
                if (!parts[1].equals("null")) owner.setId(Long.parseLong(parts[1]));
                collection.setOwner(owner);

                // name
                collection.setName(parts[2]);

                // films
                String[] filmIds = parts[3].split(",");
                ArrayList<FilmEntity> films = new ArrayList<>();

                for (String fid : filmIds) {
                    if (fid.trim().isEmpty()) continue;
                    FilmEntity film = new FilmEntity();
                    film.setId(Long.parseLong(fid));
                    films.add(film);
                }

                collection.setFilmCollection(films);

                collections.add(collection);
            }

        } catch (FileNotFoundException e) {
            return collections;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return collections;
    }

    // =============================================================
    // UPDATE – kompletne prepíše súbor podľa ID
    // =============================================================
    @Override
    public void update(CollectionEntity collection) {

        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] parts = line.split(";");
                long existingId = Long.parseLong(parts[0]);

                if (existingId == collection.getId()) {

                    // prepíšeme tento riadok správnym formátom
                    String filmIds = generateFilmIds(collection);

                    String newLine =
                            collection.getId() + ";" +
                                    (collection.getOwner() != null ? collection.getOwner().getId() : "null") + ";" +
                                    collection.getName() + ";" +
                                    filmIds;

                    lines.add(newLine);

                } else {
                    lines.add(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // prepíšeme celý súbor
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (String ln : lines) {
                writer.write(ln);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -------------------------------------------------------------
    // Pomocná metóda na generovanie CSV id filmov
    // -------------------------------------------------------------
    private String generateFilmIds(CollectionEntity collection) {
        return collection.getFilmCollection().stream()
                .map(f -> f.getId().toString())
                .reduce((a, b) -> a + "," + b)
                .orElse("");
    }
}
