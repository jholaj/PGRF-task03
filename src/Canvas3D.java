import rasterize.*;
import solids.Cube;
import solids.Solid;
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
    private Camera camera;
    private Mat4 projection;

    // help vars & colors
    private int startClickX, startClickY = 0;
    int outlineColor = 0xf0f0f0;

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
                    System.out.println("W stisknuto... Posun VPRED");
                    camera = camera.forward(0.1);
                    renderScene();

                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_S){
                    System.out.println("S stisknuto... Posun VZAD");
                    camera = camera.backward(0.1);
                    renderScene();
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_A){
                    System.out.println("A stisknuto... Posun VLEVO");
                    camera = camera.left(0.1);
                    renderScene();
                }
                if(keyEvent.getKeyCode() == KeyEvent.VK_D){
                    System.out.println("D stisknuto... Posun VPRAVO");
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

            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {

            }
        });

        initScene();
    }

    public void initScene(){
        camera = new Camera(
                new Vec3D(0.5,-1,0.3),
                Math.toRadians(90), //azimuth
                Math.toRadians(-15), //zenith
                10,
                true
        );

        // switch for ortho?
        projection = new Mat4PerspRH(
                Math.PI / 4,
                raster.getHeight() / (float)raster.getWidth(), // FLOAT!
                0.1,
                20.
        );

        cube = new Cube();
        scene = new ArrayList<>();
        scene.add(cube);

    }

    public void renderScene(){
        clear(0x000000);
        wiredRenderer.setView(camera.getViewMatrix());
        wiredRenderer.setProj(projection);
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

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Canvas3D(800, 600).start());
    }

}