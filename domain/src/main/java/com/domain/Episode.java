package com.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Episode {
    private Long id;
    private String title;
    private int episodeNumber;
    private double rating;
}