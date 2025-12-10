package com.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FilmEntity {
    private Long id;
    private String name;
    private String description;
    private GenreEntity genre;
    private int releaseYear;
    private double rating;

    private String trailerUrl;
}
