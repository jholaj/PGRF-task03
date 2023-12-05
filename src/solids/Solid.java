package solids;

import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {
    protected ArrayList<Point3D> vb = new ArrayList<>();
    protected ArrayList<Integer> ib = new ArrayList<>();
    protected Mat4 model = new Mat4Identity();

    public ArrayList<Point3D> getVb() {
        return vb;
    }

    public ArrayList<Integer> getIb() {
        return ib;
    }

    public Mat4 getModel() {
        return model;
    }

    protected void addIndices(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }


}