package com.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class CollectionEntity {
    private Long id;
    private UserEntity owner;
    private String name;
    private ArrayList<FilmEntity> filmCollection;
}
