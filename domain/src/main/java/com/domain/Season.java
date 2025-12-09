package com.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class Season {
    private Long id;
    private int seasonNumber;
    private List<Episode> episodes = new ArrayList<>();
}