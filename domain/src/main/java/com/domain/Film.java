package com.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Film {
    private Long id;
    private String name;
    private String description;
    private Genre genre;
    private int releaseYear;
    private double rating;



    @Override
    public String toString() {
        return name + " (" + releaseYear + ")";
    }

}

