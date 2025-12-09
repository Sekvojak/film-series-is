package com.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EpisodeEntity {
    private Long id;
    private String title;
    private int episodeNumber;
    private double rating;
}