package com.domain;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter
@Setter
public class Collection {
    private Long id;
    private User owner;
    private String name;
    private ArrayList<Film> filmCollection;

    @Override
    public String toString() {
        return name != null ? name : "(bez názvu)";
    }
}

