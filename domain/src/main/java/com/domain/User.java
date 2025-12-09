package com.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class User {
    private Long id;
    private String fname;
    private String lname;
    private String phoneNumber;
    private String email;
    private List<Film> watchedFilms = new ArrayList<>();
    private List<Collection> collections = new ArrayList<>();
    private String password;

    @Override
    public String toString() {
        return fname + " " + lname;
    }

}
