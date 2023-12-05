package model;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Point> points;

    public Polygon() {
        points = new ArrayList<>();
    }

    public void addPoint(Point p) {
        points.add(p);
    }
    public int getSize() {
        return points.size();
    }

    public Point getPoint(int index) {
        return points.get(index);
    }

    public int getCount() {
        return points.size();
    }

    public void clear() {
        points.clear();
    }

    public List<Point> getPoints() {
        return points;
    }
}
