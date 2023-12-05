package solids;

import transforms.Point3D;

public class Cube extends Solid {

    public Cube() {

        // vertex buffer
        vb.add(new Point3D(0, 0, 0)); // 0
        vb.add(new Point3D(1, 0, 0)); // 1
        vb.add(new Point3D(1, 1, 0)); // 2
        vb.add(new Point3D(0, 1, 0)); // 3
        vb.add(new Point3D(0, 0, 1)); // 4
        vb.add(new Point3D(1, 0, 1)); // 5
        vb.add(new Point3D(1, 1, 1)); // 6
        vb.add(new Point3D(0, 1, 1)); // 7

        // index buffer
        addIndices(0, 1, 1, 2, 2, 3, 3, 0); // front face
        addIndices(4, 5, 5, 6, 6, 7, 7, 4); // back face
        addIndices(0, 4, 1, 5, 2, 6, 3, 7); // four side faces
    }
}
