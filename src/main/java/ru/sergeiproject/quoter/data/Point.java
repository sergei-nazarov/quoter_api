package ru.sergeiproject.quoter.data;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Point<X, Y> {

    X x;
    Y y;

    public Point(X x, Y y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Point<?, ?> point = (Point<?, ?>) o;

        if (!Objects.equals(x, point.x)) return false;
        return Objects.equals(y, point.y);
    }

    @Override
    public int hashCode() {
        int result = x != null ? x.hashCode() : 0;
        result = 31 * result + (y != null ? y.hashCode() : 0);
        return result;
    }
}
