package solids;

import transforms.Point3D;

public class Axis extends Solid {
    public Axis(char axis) {
        switch (axis) {
            case 'x':
                vb.add(new Point3D(0, 0, 0));
                vb.add(new Point3D(2, 0, 0));
                break;
            case 'y':
                vb.add(new Point3D(0, 0, 0));
                vb.add(new Point3D(0, 2, 0));
                break;
            case 'z':
                vb.add(new Point3D(0, 0, 0));
                vb.add(new Point3D(0, 0, 2));
                break;
        }
        addIndices(0, 1);
    }
}