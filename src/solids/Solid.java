package solids;

import transforms.*;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Solid {
    private int color;

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

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getIdentifier() {
        return "DEFAULT";
    }

    protected void addIndices(Integer... indices) {
        ib.addAll(Arrays.asList(indices));
    }

    // translating each axis... x y z
    public Mat4 increaseX(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(1,0,0));
        return getModel().mul(mat);
    }

    public Mat4 decreaseX(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(-1,0,0));
        return getModel().mul(mat);
    }

    public Mat4 increaseY(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(0,1,0));
        return getModel().mul(mat);
    }

    public Mat4 decreaseY(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(0,-1,0));
        return getModel().mul(mat);
    }

    public Mat4 increaseZ(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(0,0,1));
        return getModel().mul(mat);
    }

    public Mat4 decreaseZ(){
        Mat4Transl mat = new Mat4Transl(new Vec3D(0,0,-1));
        return getModel().mul(mat);
    }

    // rotating // radians
    public Mat4 rotateX(){
        Mat4RotX mat = new Mat4RotX(Math.toRadians(1));
        return getModel().mul(mat);
    }

    public Mat4 rotateY(){
        Mat4RotY mat = new Mat4RotY(Math.toRadians(1));
        return getModel().mul(mat);
    }

    public Mat4 rotateZ(){
        Mat4RotZ mat = new Mat4RotZ(Math.toRadians(1));
        return getModel().mul(mat);
    }

    // zoom
    public Mat4 zoomUp(){
        Mat4Scale mat = new Mat4Scale(1.1);
        return getModel().mul(mat);
    }

    public Mat4 zoomDown(){
        Mat4Scale mat = new Mat4Scale(0.9);
        return getModel().mul(mat);
    }

    public void setModel(Mat4 model) {
        this.model = model;
    }

}
