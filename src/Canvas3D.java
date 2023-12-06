import rasterize.*;
import solids.*;
import transforms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import renderer.WiredRenderer;


public class Canvas3D {
    private final JFrame frame;
    private final JPanel panel;
    private final Raster raster;
    private final LineRasterizer lineRasterizer;
    private final WiredRenderer wiredRenderer;

    // SCENE
    private ArrayList<Solid> scene;
    private Solid cube;
    private Solid pentagonalPrism;
    private Solid donut;
    private Solid pyramid;
    private Axis xAxis,yAxis,zAxis;
    private Camera camera;
    private Mat4 perspectiveProjection;
    private Mat4 orthogonalProjection;
    private Mat4 currentProjection;



    // help vars & colors
    private int startClickX, startClickY = 0;
    int outlineColor = 0xf0f0f0;
    int editColor = 0xff0000;
    private double translX, translY = 0;
    private int selectedIndex = -1;
    private String objectId = "";



    public Canvas3D(int width, int height)
    {
        frame = new JFrame();

        frame.setLayout(new BorderLayout());
        frame.setTitle("PGRF1");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        raster = new RasterBufferedImage(width, height);
        lineRasterizer = new LineRasterizerGraphics(raster, outlineColor);
        wiredRenderer = new WiredRenderer(lineRasterizer, raster);

        panel = new JPanel(){
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(((RasterBufferedImage)raster).getImg(), 0,0, null);
            }
        };

        panel.setPreferredSize(new Dimension(width, height));
        panel.requestFocus();
        panel.requestFocusInWindow();

        panel.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startClickX = e.getX();
                startClickY = e.getY();
            }

        });

        panel.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                // movement of mouse
                // horizontal
                camera = camera.addAzimuth((double) Math.PI * (e.getX() - startClickX) / (double) panel.getWidth());
                // vertical
                camera = camera.addZenith((double) Math.PI * (e.getY() - startClickY) / (double) panel.getHeight());

                // preventing camera from rolling over
                if(camera.getZenith() > 90)
                    camera = camera.withZenith(90);
                if(camera.getZenith() < -90)
                    camera = camera.withZenith(-90);

                startClickX = e.getX();
                startClickY = e.getY();
                renderScene();
            }
        });

        frame.add(panel, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.requestFocusInWindow();


        frame.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent keyEvent) {

            }

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                // wasd
                if(keyEvent.getKeyCode() == KeyEvent.VK_W){
                    System.out.println("W pressed... Camera goes forward");
                    camera = camera.forward(0.1);
                    renderScene();

                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_S){
                    System.out.println("S pressed... Camera goes backward");
                    camera = camera.backward(0.1);
                    renderScene();
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_A){
                    System.out.println("A pressed... Camera goes left");
                    camera = camera.left(0.1);
                    renderScene();
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_D){
                    System.out.println("D pressed... Camera goes right");
                    camera = camera.right(0.1);
                    renderScene();
                }

                // up & down
                if(keyEvent.getKeyCode() == KeyEvent.VK_Q){
                    camera = camera.up(0.1);
                    renderScene();
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_E){
                    camera = camera.down(0.1);
                    renderScene();
                }

                // selecting solids
                if(keyEvent.getKeyCode() == KeyEvent.VK_ENTER){
                    if (selectedIndex != -1) {
                        scene.get(selectedIndex).setColor(outlineColor);
                    }

                    selectedIndex = (selectedIndex + 1) % scene.size();
                    scene.get(selectedIndex).setColor(editColor);
                    // ROTATING / TRANSLATING SOLID
                    objectId = scene.get(selectedIndex).getIdentifier();

                    System.out.println("EDIT: " + objectId);

                    renderScene();
                }

                if(objectId == "CUBE"){
                    processKeyEvent(cube, keyEvent);
                }
                if(objectId == "DONUT"){
                    processKeyEvent(donut, keyEvent);
                }
                if(objectId == "PENTAGON"){
                    processKeyEvent(pentagonalPrism, keyEvent);
                }
                if(objectId == "PYRAMID"){
                    processKeyEvent(pyramid, keyEvent);
                }

                // projection switch
                if(keyEvent.getKeyCode() == KeyEvent.VK_P){
                    System.out.println("switched projection");
                    if(currentProjection == perspectiveProjection){
                        currentProjection = orthogonalProjection;
                    } else {
                        currentProjection = perspectiveProjection;
                    }
                }
                renderScene();
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        initScene();
    }

    public void initScene(){
        camera = new Camera(
                new Vec3D(0.5,-5,2.3),
                Math.toRadians(90), //azimuth
                Math.toRadians(-15), //zenith
                10,
                true
        );

        perspectiveProjection = new Mat4PerspRH(
                Math.PI / 4,
                raster.getHeight() / (float)raster.getWidth(), // FLOAT!
                0.1,
                20.
        );
        orthogonalProjection = new Mat4OrthoRH(
                raster.getWidth() / 100,
                raster.getHeight() / 100,
                0.1,
                20.
        );

        xAxis = new Axis('x');
        xAxis.setColor(0xFF0000); // RED

        yAxis = new Axis('y');
        yAxis.setColor(0x00FF00); // GREEN

        zAxis = new Axis('z');
        zAxis.setColor(0x0000FF); // BLUE

        cube = new Cube();
        pentagonalPrism = new PentagonalPrism();
        donut = new Donut();
        pyramid = new Pyramid();

        currentProjection = perspectiveProjection;
        scene = new ArrayList<>();
        scene.add(cube);
        scene.add(pentagonalPrism);
        scene.add(donut);
        scene.add(pyramid);
        for (Solid obj : scene) {
            obj.setColor(outlineColor);
        }
    }

    public void renderScene(){
        clear(0x000000);
        wiredRenderer.setView(camera.getViewMatrix());
        wiredRenderer.setProj(currentProjection);
        wiredRenderer.renderAxis(xAxis,yAxis,zAxis);
        wiredRenderer.renderScene(scene);
        panel.repaint();
    }

    public void clear(int color) {
        raster.setClearColor(color);
        raster.clear();
    }

    public void start() {
        raster.clear();
        renderScene();
        panel.repaint();
    }

    public void processKeyEvent(Solid solid, KeyEvent keyEvent) {
        switch (keyEvent.getKeyCode()) {
            // MOVEMENT
            case KeyEvent.VK_LEFT:
                System.out.println("LEFT");
                solid.setModel(solid.decreaseX());
                break;
            case KeyEvent.VK_RIGHT:
                System.out.println("RIGHT");
                solid.setModel(solid.increaseX());
                break;
            case KeyEvent.VK_UP:
                System.out.println("FORWARD");
                solid.setModel(solid.increaseY());
                break;
            case KeyEvent.VK_DOWN:
                System.out.println("BACKWARD");
                solid.setModel(solid.decreaseY());
                break;
            case KeyEvent.VK_SHIFT:
                System.out.println("UP");
                solid.setModel(solid.increaseZ());
                break;
            case KeyEvent.VK_CONTROL:
                System.out.println("DOWN");
                solid.setModel(solid.decreaseZ());
                break;
            // ROTATING
            case KeyEvent.VK_X:
                System.out.println("ROTATE X");
                solid.setModel(solid.rotateX());
                break;
            case KeyEvent.VK_Y:
                System.out.println("ROTATE Y");
                solid.setModel(solid.rotateY());
                break;
            case KeyEvent.VK_Z:
                System.out.println("ROTATE Z");
                solid.setModel(solid.rotateZ());
                break;
            // ZOOM
            //numeric
            case KeyEvent.VK_ADD:
                System.out.println("ZOOM UP");
                solid.setModel(solid.zoomUp());
                break;
            case KeyEvent.VK_SUBTRACT:
                System.out.println("ZOOM DOWN");
                solid.setModel(solid.zoomDown());
                break;
        }
        renderScene();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas3D(800, 600).start());
    }

}
