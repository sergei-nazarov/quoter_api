package ru.sergeiproject.quoter.data;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Point<X, Y> {

    final X x;
    final Y y;

    public Point(X x, Y y) {
        this.x = x;
        this.y = y;
    }

}
