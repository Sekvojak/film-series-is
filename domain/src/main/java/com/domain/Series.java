package com.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Series {
    private Long id;
    private String title;
    private String description;
    private Genre genre;
    private double rating;
    private List<Season> seasons = new ArrayList<>();
}