package solids;

import transforms.Point3D;

public class PentagonalPrism extends Solid {

    public PentagonalPrism() {

        // vertex buffer
        // +3x
        vb.add(new Point3D(2, 0, 0)); // 0
        vb.add(new Point3D(3, 0, 0)); // 1
        vb.add(new Point3D(3.5, 1, 0)); // 2
        vb.add(new Point3D(2.5, 1.5, 0)); // 3
        vb.add(new Point3D(1.5, 1, 0)); // 4
        vb.add(new Point3D(2, 0, 1)); // 5
        vb.add(new Point3D(3, 0, 1)); // 6
        vb.add(new Point3D(3.5, 1, 1)); // 7
        vb.add(new Point3D(2.5, 1.5, 1)); // 8
        vb.add(new Point3D(1.5, 1, 1)); // 9

        // index buffer
        addIndices(0, 1, 1, 2, 2, 3, 3, 4, 4, 0); // front face
        addIndices(5, 6, 6, 7, 7, 8, 8, 9, 9, 5); // back face
        addIndices(0, 5, 1, 6, 2, 7, 3, 8, 4, 9); // five side faces
    }

    @Override
    public String getIdentifier() {
        return "PENTAGON";
    }
}