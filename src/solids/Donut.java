package solids;

import transforms.Point3D;

public class Donut extends Solid {

    public Donut() {

        // source: https://en.wikipedia.org/wiki/Torus?useskin=vector

        int m = 20, n = 10;
        double R = 0.6, r = 0.25;

        // m = number of segments around the main axis
        // n = number of segments around the minor axis

        // R = distance of the center of the "tube" to the center of the donut
        // r = radius of the "tube"

        // u = angle... determines the position of a point on the donut relative to the main axis

        // vertex buffer
        // -1x
        // -2y
        for (int i = 0; i <= m; i++) {
            double u = i * 2 * Math.PI / m;
            for (int j = 0; j <= n; j++) {
                double v = j * 2 * Math.PI / n;
                // x(u,v) = (R * r * cos(v)) * cos(u)
                double x = (R + r * Math.cos(v)) * Math.cos(u);
                // y(u,v) = (R * r * cos(v)) * sin(u)
                double y = (R + r * Math.cos(v)) * Math.sin(u);
                // z(u,v) = r * sin(v)
                double z = r * Math.sin(v);
                vb.add(new Point3D(x-1, y-2, z));
            }
        }

        // index buffer
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int base = i * (n + 1) + j;
                int baseNextRow = (i + 1) * (n + 1) + j;

                // first triangle
                addIndices(base, base + 1, baseNextRow);

                // second triangle
                addIndices(base + 1, baseNextRow + 1, baseNextRow);
            }
        }
    }

    @Override
    public String getIdentifier() {
        return "DONUT";
    }
}