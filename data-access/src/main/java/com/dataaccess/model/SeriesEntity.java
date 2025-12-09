package com.dataaccess.model;

import com.domain.Genre;
import com.domain.Season;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeriesEntity {
    private Long id;
    private String title;
    private String description;
    private GenreEntity genre;
    private double rating;
    private List<SeasonEntity> seasons = new ArrayList<>();
}