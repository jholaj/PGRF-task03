package renderer;

import rasterize.LineRasterizer;
import solids.Solid;
import transforms.Mat4;
import transforms.Mat4Identity;
import transforms.Point3D;
import transforms.Vec3D;
import rasterize.Raster;

import java.util.ArrayList;

public class WiredRenderer {
    private LineRasterizer lineRasterizer;
    private Raster raster;
    private Mat4 view;
    private Mat4 proj;

    public WiredRenderer(LineRasterizer lineRasterizer, Raster raster){
        this.lineRasterizer = lineRasterizer;
        this.raster = raster;
        this.view = new Mat4Identity();
        this.proj = new Mat4Identity();
    }

    public void render(Solid solid) {
        // solid má index buffer, projdu ho v cyklu
        // pro každé dva prvky si načtu odpovídající vertex
        // spojím vertexy linou
        for (int i = 0; i < solid.getIb().size() - 1; i += 2) {
            int indexA = solid.getIb().get(i);
            int indexB = solid.getIb().get(i + 1);

            Point3D vertexA = solid.getVb().get(indexA);
            Point3D vertexB = solid.getVb().get(indexB);

            // pronasobeni matici
            vertexA = vertexA.mul(solid.getModel()).mul(view).mul(proj);
            vertexB = vertexB.mul(solid.getModel()).mul(view).mul(proj);

            // TODO: ořezání
            if (!checkVertex(vertexA)) {
                continue;
            }

            if (!checkVertex(vertexB)) {
                continue;
            }

            // TODO: dehomogenizace
            Point3D dehomogA = vertexA.mul(1/ vertexA.getW());
            Point3D dehomogB = vertexB.mul(1/ vertexB.getW());

            Vec3D v1 = transformToWindows(new Vec3D(dehomogA));
            Vec3D v2 = transformToWindows(new Vec3D(dehomogB));


            lineRasterizer.rasterize((int) Math.round(v1.getX()), (int) Math.round(v1.getY()), (int) Math.round(v2.getX()), (int) Math.round(v2.getY()), solid.getColor());
        }
    }

    public Vec3D transformToWindows(Vec3D p){
        return  p.mul(new Vec3D(1,-1,1))
                .add(new Vec3D(1, 1, 0))
                .mul(new Vec3D((raster.getWidth()-1)/2., (raster.getHeight() - 1) / 2., 1));
    }

    public void setProj(Mat4 proj) {
        this.proj = proj;
    }

    public void setView(Mat4 view) {
        this.view = view;
    }

    public void renderScene(ArrayList<Solid> scene){
        for(Solid solid : scene){
            render(solid);
        }
    }

    private boolean checkVertex(Point3D vertex) {
        double x = vertex.getX();
        double y = vertex.getY();
        double z = vertex.getZ();
        double w = vertex.getW();

        return -w <= x && x <= w && -w <= y && y <= w && 0 <= z && z <= w;
    }
}
