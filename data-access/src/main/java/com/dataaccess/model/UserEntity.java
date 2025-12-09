package com.dataaccess.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserEntity {
    private Long id;
    private String fname;
    private String lname;
    private String phoneNumber;
    private String email;
    private String password;

    private List<FilmEntity> watchedFilms = new ArrayList<>();
    private List<CollectionEntity> collections = new ArrayList<>();

    private Long watchlistId;
}
