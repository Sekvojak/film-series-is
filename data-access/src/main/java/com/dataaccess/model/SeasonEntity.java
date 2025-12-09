package com.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class SeasonEntity {
    private Long id;
    private int seasonNumber;
    private List<EpisodeEntity> episodes = new ArrayList<>();
}