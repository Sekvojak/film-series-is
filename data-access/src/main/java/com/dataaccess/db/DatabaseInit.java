package com.dataaccess.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseInit {

    public static void init() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // -----------------------------------------
            // 1. GENRES - musí byť prvé (kvôli FK)
            // -----------------------------------------
            String createGenres = """
                CREATE TABLE IF NOT EXISTS genres (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(100) UNIQUE NOT NULL
                )
            """;
            stmt.execute(createGenres);

            // -----------------------------------------
            // 2. FILMS - FK na genres
            // -----------------------------------------
            String createFilms = """
                CREATE TABLE IF NOT EXISTS films (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(100),
                    description VARCHAR(255),
                    genre_id BIGINT,
                    release_year INT,
                    rating DOUBLE,
                    trailer_url VARCHAR(500),
                    FOREIGN KEY (genre_id) REFERENCES genres(id)
                )
            """;
            stmt.execute(createFilms);

            // -----------------------------------------
            // 3. FILM_GENRES (ak budeš chcieť multi-žánre)
            // -----------------------------------------
            String createFilmGenres = """
                CREATE TABLE IF NOT EXISTS film_genres (
                    film_id BIGINT NOT NULL,
                    genre_id BIGINT NOT NULL,
                    PRIMARY KEY (film_id, genre_id),
                    FOREIGN KEY (film_id) REFERENCES films(id),
                    FOREIGN KEY (genre_id) REFERENCES genres(id)
                )
            """;
            stmt.execute(createFilmGenres);

            // -----------------------------------------
            // 4. USERS
            // -----------------------------------------
            String createUsers = """
                CREATE TABLE IF NOT EXISTS users (
                    id IDENTITY PRIMARY KEY,
                    fname VARCHAR(50),
                    lname VARCHAR(50),
                    phoneNumber VARCHAR(20),
                    email VARCHAR(100),
                    password VARCHAR(100)
                )
            """;
            stmt.execute(createUsers);

            // -----------------------------------------
            // 5. COLLECTIONS
            // -----------------------------------------
            String createCollections = """
                CREATE TABLE IF NOT EXISTS collections (
                    id IDENTITY PRIMARY KEY,
                    name VARCHAR(100),
                    user_id BIGINT,
                    FOREIGN KEY(user_id) REFERENCES users(id)
                )
            """;
            stmt.execute(createCollections);

            // -----------------------------------------
            // 6. COLLECTION_FILMS
            // -----------------------------------------
            String createCollectionFilms = """
                CREATE TABLE IF NOT EXISTS collection_films (
                    collection_id BIGINT NOT NULL,
                    film_id BIGINT NOT NULL,
                    PRIMARY KEY (collection_id, film_id),
                    FOREIGN KEY(collection_id) REFERENCES collections(id),
                    FOREIGN KEY(film_id) REFERENCES films(id)
                )
            """;
            stmt.execute(createCollectionFilms);

            // -----------------------------------------
            // 7. PRELOAD DEFAULT GENRES
            // -----------------------------------------
            ResultSet genreCount = stmt.executeQuery("SELECT COUNT(*) FROM genres");

            if (genreCount.next() && genreCount.getInt(1) == 0) {
                stmt.execute("INSERT INTO genres(name) VALUES ('Action')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Comedy')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Drama')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Horror')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Sci-Fi')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Thriller')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Romance')");
                stmt.execute("INSERT INTO genres(name) VALUES ('Documentary')");
                System.out.println("Preddefinované žánre boli vložené.");
            }

            // --- Vloženie filmov, ak tabuľka je prázdna ---
            ResultSet filmCount = stmt.executeQuery("SELECT COUNT(*) FROM films");
            if (filmCount.next() && filmCount.getInt(1) == 0) {

                // Gladiator (Action)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Gladiator',
                'Rímsky generál Maximus sa stáva otrok a gladiátor, aby sa pomstil cisárovi.',
                (SELECT id FROM genres WHERE name = 'Action'),
                2000,
                8.5)
    """);

                // Interstellar (Sci-Fi)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Interstellar',
                'Skupina astronautov cestuje cez červiu dieru za záchranou ľudstva.',
                (SELECT id FROM genres WHERE name = 'Sci-Fi'),
                2014,
                8.6)
    """);

                // Inception (Sci-Fi)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Inception',
                'Z zlodej špecializujúci sa na extrakciu snov sa pokúša vložiť myšlienku do cudzej mysle.',
                (SELECT id FROM genres WHERE name = 'Sci-Fi'),
                2010,
                8.5)
    """);

                // The Dark Knight (Action)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('The Dark Knight',
                'Batman čelí svojmu najväčšiemu protivníkovi – Jokerovi.',
                (SELECT id FROM genres WHERE name = 'Action'),
                2008,
                9.0)
    """);

                // The Shawshank Redemption (Drama)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('The Shawshank Redemption',
                'Príbeh o nádeji, priateľstve a prežití vo väzení Shawshank.',
                (SELECT id FROM genres WHERE name = 'Drama'),
                1994,
                9.3)
    """);

                // The Godfather (Drama)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('The Godfather',
                'Mocná talianska mafiánska rodina Corleonovcov bojuje o kontrolu.',
                (SELECT id FROM genres WHERE name = 'Drama'),
                1972,
                9.2)
    """);

                // Titanic (Romance)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Titanic',
                'Láska na palube slávnej lode Titanic.',
                (SELECT id FROM genres WHERE name = 'Romance'),
                1997,
                7.8)
    """);

                // The Conjuring (Horror)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('The Conjuring',
                'Manželia Warrenovci vyšetrujú paranormálny prípad v strašidelnom dome.',
                (SELECT id FROM genres WHERE name = 'Horror'),
                2013,
                7.5)
    """);

                // Joker (Thriller)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Joker',
                'Psychologický portrét muža meniacého sa na ikonického zločinca.',
                (SELECT id FROM genres WHERE name = 'Thriller'),
                2019,
                8.4)
    """);

                // Senna (Documentary)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Senna',
                'Príbeh legendárneho jazdca Formuly 1 Ayrtona Sennu.',
                (SELECT id FROM genres WHERE name = 'Documentary'),
                2010,
                8.5)
    """);

                // Forrest Gump (Comedy/Drama → dáme Drama)
                stmt.execute("""
        INSERT INTO films(name, description, genre_id, release_year, rating)
        VALUES ('Forrest Gump',
                'Jednoduchý muž zažíva úžasné momenty americkej histórie.',
                (SELECT id FROM genres WHERE name = 'Drama'),
                1994,
                8.8)
    """);

                System.out.println("Default filmy boli vložené.");
            }


            System.out.println("Databáza inicializovaná úspešne.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
