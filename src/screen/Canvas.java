package screen;


import backend.Cube;
import backend.Grid;
import backend.Settings;
import guiTools.GuiComponent;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import util.MU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;


public class Canvas extends GuiComponent {

    private static int mx, my, mb, selectedTool;
    private int noCubes = 0;
    private int maxNoCubes = 0;
    @NotNull
    private final Grid grid;
    @NotNull
    private final Cube[][][] cubes;
    @NotNull
    private final Rectangle pnt;
    private boolean square, shiftSquare, half;
    @NotNull
    private final PaintEvent paintX;
    @NotNull
    private final PaintEvent paintY;
    @NotNull
    private Settings canvasSettings;


    @SuppressWarnings("SameParameterValue")
    public Canvas(int side, int height) {
        super(0, 0, EditorScreen.s_maxWidth, EditorScreen.s_maxHeight);
        grid = new Grid(side, height, EditorScreen.s_maxWidth / 2, EditorScreen.s_maxHeight / 2);
        cubes = new Cube[height - 1][side - 1][side - 1];
        pnt = new Rectangle(0, 0, 1, 1);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        maxNoCubes = (int) MU.square(side - 1) * (height - 1);
        int x_ = MU.makeSquareI(false, (int) grid.getRotate(), 360);
        int y_ = MU.makeSquareI(true, (int) grid.getRotate(), 360);
        paintY = (PaintEvent e, int z, int y, Graphics2D g2d) -> {
            if (!square) {
                for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                    e.event(null, z, yi, g2d);
                }
            } else {
                for (int yi = grid.getSide() - 2; yi >= 0; yi--) {
                    e.event(null, z, yi, g2d);
                }
            }
        };
        paintX = (PaintEvent e, int z, int y, Graphics2D g2d) -> {
            if (!shiftSquare) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (!(cubes[z][xi][y] == null)) {
                        cubes[z][xi][y].fillCube(g2d);
                    }
                }
            } else {
                for (int xi = grid.getSide() - 2; xi >= 0; xi--) {
                    if (!(cubes[z][xi][y] == null)) {
                        cubes[z][xi][y].fillCube(g2d);
                    }
                }
            }
        };
        //cuboid(0, 0, 0, side - 1, side - 1, height - 1);
        sphere((int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((side - 2) / 2.0), (int) ((height - 2) / 2.0));
        //cuboid(12,12,12,1,1,1);
    }

    @SuppressWarnings({"ConstantConditions", "SameParameterValue", "unused"})
    private void cuboid(int x, int y, int z, int width, int length, int height) {
        for (int xi = x; xi < x + width; xi += 1) {
            for (int yi = y; yi < y + length; yi += 1) {
                for (int zi = z; zi < z + height; zi += 1) {
                    if ((!(zi > grid.getHeight() - 2) && !(zi < 0)) && (!(xi > grid.getSide() - 1) && !(xi < 0)) && (!(yi > grid.getSide() - 1) && !(yi < 0))) {
                        //cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, (int) (xi * 255.0 / width), (int) (yi * 255.0 / length), (int) (zi * 255.0 / height));
                        cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);
                    }
                }
            }
        }
    }

    @SuppressWarnings("unused")
    private void sphere(int x, int y, int z, double width, double length, double height) {
        for (int theta = 0; theta < 2880; theta += 5) {
            for (int pi = -1440; pi < 1440; pi += 5) {
                int xi = (int) (Math.round(x + (width * MU.cos(theta / 8.0) * MU.cos(pi / 8.0))));
                int yi = (int) (Math.round(y + (length * MU.sin(theta / 8.0) * MU.cos(pi / 8.0))));
                int zi = (int) (Math.round(z + (height * MU.sin(pi / 8.0))));

                cubes[zi][xi][yi] = new Cube(this, xi, yi, zi, 255, 255, 255);

            }
        }
    }

    @SuppressWarnings("unused")
    public void setCube(int x, int y, int z, int red, int green, int blue) {
        if (checkIfInBounds(x, y, z))
            cubes[z][x][y] = new Cube(this, x, y, z, red, green, blue);
    }

    @SuppressWarnings("unused")
    public void clearCanvas() {
        for (int z = 0; z < grid.getHeight() - 1; z++) {
            for (int y = 0; y < grid.getSide() - 1; y++) {
                for (int x = 0; x < grid.getSide() - 1; x++) {
                    cubes[z][x][y] = null;
                }
            }
        }
    }

    public boolean checkIfInBounds(int x, int y, int z) {
        return (!(x < 0) && !(x >= grid.getSide() - 1) && !(y < 0) && !(y >= grid.getSide() - 1) && !(z < 0) && !(z >= grid.getHeight() - 1));
    }

    @Contract(pure = true)
    @SuppressWarnings("unused")
    public static boolean checkForCube(@NotNull Canvas c, int x, int y, int z) {
        return c.checkIfInBounds(x, y, z) && c.cubes[z][x][y] != null;
    }

    private void paintZ(@NotNull PaintEvent e, Graphics2D g2d) {
        if (grid.getRotateY() < 0) {
            for (int zi = grid.getHeight() - 2; zi > -1; zi--) {
                e.event(paintX, zi, 0, g2d);
            }
            if (ComponentManager.settings.isShowGrid())
            grid.paint(g2d);
        } else {
            if (ComponentManager.settings.isShowGrid())
            grid.paint(g2d);
            for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
                e.event(paintX, zi, 0, g2d);
            }
        }
    }

    @SuppressWarnings("unused")
    private void drawPolyhedron(int[] xp, int[] yp, int[] zp, int np) {
        // TODO: 5/22/2016 make polyhedrons.
    }

    @SuppressWarnings("unused")
    private void drawRect(int x, int y, int z, int width, int length, boolean rotate90) {
        // TODO: 5/22/2016 make rectangles.
    }

    @SuppressWarnings("unused")
    private void drawLine(int x1, int y1, int z1, int x2, int y2, int z2) {
        // TODO: 5/22/2016 make drawLine method.
    }

    private void showCoords(Graphics2D g2d) {
        int x_ = grid.getSide() - 1;
        int y_ = grid.getSide() - 1;
        int z_ = grid.getHeight() - 1;
        int side = grid.getSide() - 1;
        int height = grid.getHeight() - 2;
        g2d.setColor(Color.white);
        g2d.setFont(EditorScreen.font.deriveFont(11f));
        g2d.drawString("(z,x,y)", (int) grid.getPts()[0][0][0].getVecs().getX(), (int) grid.getPts()[0][0][0].getVecs().getY() - 14);
        g2d.drawString("(0,0,0)", (int) grid.getPts()[0][0][0].getVecs().getX(), (int) grid.getPts()[0][0][0].getVecs().getY());
        g2d.drawString("(0," + side + ",0)", (int) grid.getPts()[0][x_][0].getVecs().getX(), (int) grid.getPts()[0][x_][0].getVecs().getY());
        g2d.drawString("(0,0," + side + ")", (int) grid.getPts()[0][0][y_].getVecs().getX(), (int) grid.getPts()[0][0][y_].getVecs().getY());
        g2d.drawString("(0," + side + "," + side + ")", (int) grid.getPts()[0][x_][y_].getVecs().getX(), (int) grid.getPts()[0][x_][y_].getVecs().getY());
        g2d.drawString("(" + height + ",0,0)", (int) grid.getPts()[z_][0][0].getVecs().getX(), (int) grid.getPts()[z_][0][0].getVecs().getY());
        g2d.drawString("(" + height + "," + side + ",0)", (int) grid.getPts()[z_][x_][0].getVecs().getX(), (int) grid.getPts()[z_][x_][0].getVecs().getY());
        g2d.drawString("(" + height + ",0," + side + ")", (int) grid.getPts()[z_][0][y_].getVecs().getX(), (int) grid.getPts()[z_][0][y_].getVecs().getY());
        g2d.drawString("(" + height + "," + side + "," + side + ")", (int) grid.getPts()[z_][x_][y_].getVecs().getX(), (int) grid.getPts()[z_][x_][y_].getVecs().getY());
    }

    @Override
    protected void paintGuiComponent(@NotNull Graphics2D g2d) {
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
        g2d.setColor(Color.black);

        if (half) {
            paintZ(paintY, g2d);
            if (ComponentManager.settings.isShowAxis()) {
                grid.paintAxis(g2d);
            }
        } else {
            if (ComponentManager.settings.isShowAxis()) {
                grid.paintAxis(g2d);
            }
            paintZ(paintY, g2d);
        }

        g2d.setColor(Color.white);
        g2d.setColor(Color.white);
        g2d.setColor(new Color(1f, 1f, 1f, 0.4f));

        if (ComponentManager.settings.isShowCoords()) {
            showCoords(g2d);
        }
    }

    @Override
    protected void update() {
        grid.update();
        pnt.setLocation(mx, my);
        square = MU.makeSquareB(false, (int) grid.getRotate(), 360);
        shiftSquare = MU.makeSquareB(true, (int) grid.getRotate(), 360);
        half = MU.makeHalvesB(true, (int) grid.getRotate(), 360);
        noCubes = 0;
        for (int zi = 0; zi < grid.getHeight() - 1; zi++) {
            for (int yi = 0; yi < grid.getSide() - 1; yi++) {
                for (int xi = 0; xi < grid.getSide() - 1; xi++) {
                    if (cubes[zi][xi][yi] != null)
                        cubes[zi][xi][yi].updateCube();
                }
            }
        }


    }

    @Override
    protected void hover(@NotNull MouseEvent e) {
        mx = e.getX();
        my = e.getY();

    }

    @Override
    protected void drag(@NotNull MouseEvent e) {
        int dx = mx - e.getX();
        int dy = my - e.getY();
        if (mb == 2) {
            grid.update();
            grid.setLocation(mx, my);
        }
        mx = e.getX();
        my = e.getY();

        if (mb == 3) {
            grid.rotate(dx / 2);
            grid.rotatey(-dy / 2);
        }
    }

    @Override
    protected void mousePress(@NotNull MouseEvent e) {
        mb = e.getButton();
        mx = e.getX();
        my = e.getY();
        if (mb == 1) {

        }
    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }

    @Override
    protected void keyPress(@NotNull KeyEvent ke) {
        if (ke.getKeyCode() == KeyEvent.VK_W) {
            grid.rotatey(1);
        }
        if (ke.getKeyCode() == KeyEvent.VK_S) {
            grid.rotatey(-1);
        }
        if (ke.getKeyCode() == KeyEvent.VK_E) {
            grid.rotate(1);
        }
        if (ke.getKeyCode() == KeyEvent.VK_Q) {
            grid.rotate(-1);
        }
        if (ke.getKeyCode() == KeyEvent.VK_UP) {
            grid.zoom(1);
        }
        if (ke.getKeyCode() == KeyEvent.VK_DOWN) {
            grid.zoom(-1);
        }

        Cube.keyPressed(ke);
    }

    @Override
    protected void scroll(@NotNull MouseWheelEvent e) {
        int n = e.getWheelRotation();
        grid.zoom((int) (-2.5 * n));
    }

    @NotNull
    public Grid getGrid() {
        return grid;
    }

    @NotNull
    public Cube[][][] getCubes() {
        return cubes;
    }

    @SuppressWarnings("unused")
    public int getNoCubes() {
        return noCubes;
    }

    @SuppressWarnings("unused")
    public int getMaxNoCubes() {
        return maxNoCubes;
    }

    public void setRotate(int direction) {
        grid.setRotate(direction);
    }

    public void setRotatey(int direction) {
        grid.setRotatey(direction);
    }

    public void setZoom(int zoom) {
        grid.setZoom(zoom);
    }

    public void setSelectedTool(int tool) {
        selectedTool = tool;
    }

}
