package solids;

import transforms.Point3D;

public class Pyramid extends Solid {

    public Pyramid() {

        // vertex buffer
        // -2x
        vb.add(new Point3D(-2, 0, 0)); // 0
        vb.add(new Point3D(-1, 0, 0)); // 1
        vb.add(new Point3D(-1.5, 0.5, Math.sqrt(2)/2)); // 2
        vb.add(new Point3D(-2, 1, 0)); // 3
        vb.add(new Point3D(-1, 1, 0)); // 4

        // index buffer
        addIndices(0, 1, 1, 2, 2, 0); // front face
        addIndices(3, 4, 4, 2, 2, 3); // back face
        addIndices(0, 3, 1, 4); // bottom face
    }

    @Override
    public String getIdentifier() {
        return "PYRAMID";
    }
}