package backend;

import editorScreen.Canvas;
import editorScreen.ComponentManager;
import org.jetbrains.annotations.NotNull;
import util.MU;
import util.PU;

import java.awt.*;
import java.awt.event.MouseEvent;

public class Cube {

    private int x;
    private int y;
    private int z;
    private int colorHex;
    private int[][] a, b;
    private boolean tb, bf, lr;
    private Polygon[] cube;
    private Color color[];
    private Color top, bot, back, front, left, right;
    private Grid grid;
    private static Polygon selected, empty;
    private static int face;
    private Canvas can;

    static {
        selected = new Polygon();
        empty = new Polygon();
    }

    public Cube(@NotNull Canvas can, int x, int y, int z, int red, int green, int blue, int alpha) {
        initCube(can, x, y, z, red, green, blue, alpha);
    }

    public static Polygon getEmpty() {
        return empty;
    }

    public static void setSelected(Polygon selected) {
        Cube.selected = selected;
    }

    public static Polygon getSelected() {
        return selected;
    }

    private void initCube(@NotNull Canvas can, int x, int y, int z, int red, int green, int blue, int alpha) {
        this.can = can;
        this.grid = can.getGrid();
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = new Color[3];
        colorHex = (red << 16) | (green << 8) | (blue);
        back = PU.saturateColor(new Color(colorHex), 0.5);
        bot = PU.saturateColor(new Color(colorHex), 0.6);
        left = PU.saturateColor(new Color(colorHex), 0.7);
        right = PU.saturateColor(new Color(colorHex), 0.8);
        top = PU.saturateColor(new Color(colorHex), 0.9);
        front = new Color(colorHex);
        this.color[0] = top;
        this.color[1] = front;
        this.color[2] = right;
        cube = new Polygon[3];
        a = new int[3][4];
        b = new int[3][4];
        cube[0] = new Polygon();
        cube[1] = new Polygon();
        cube[2] = new Polygon();

    }

    public void updateCube() {
        {
            int drawTop;
            if (grid.getRotateY() < 0) {
                drawTop = 0;
                color[0] = bot;
                if (cubeBelow()) {
                    tb = false;
                    cube[0].reset();
                } else {
                    tb = true;

                    for (int i = 0; i < 4; i++) {
                        a[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getX();
                        b[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getY();
                    }
                    cube[0].xpoints = a[0];
                    cube[0].ypoints = b[0];
                    cube[0].reset();
                    cube[0].npoints = 4;
                }
            } else {
                drawTop = 1;
                color[0] = top;
                if (cubeAbove()) {
                    tb = false;
                    cube[0].reset();
                } else {
                    tb = true;
                    for (int i = 0; i < 4; i++) {
                        a[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getX();
                        b[0][i] = (int) grid.getPts()[z + drawTop][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getY();
                    }
                    cube[0].xpoints = a[0];
                    cube[0].ypoints = b[0];
                    cube[0].reset();
                    cube[0].npoints = 4;
                }
            }
        }
        {
            int shift;
            if ((grid.getRotate() >= 0 && grid.getRotate() <= 90) || (grid.getRotate() >= 270 && grid.getRotate() <= 360)) {
                shift = 1;
                color[1] = back;
                if (cubeFront()) {
                    bf = false;
                    cube[1].reset();
                } else {
                    bf = true;
                    for (int i = 0; i < 4; i++) {
                        a[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVecs().getX();
                        b[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVecs().getY();
                    }
                    cube[1].xpoints = a[1];
                    cube[1].ypoints = b[1];
                    cube[1].reset();
                    cube[1].npoints = 4;
                }
            } else {
                shift = 0;
                color[1] = front;
                if (cubeBack()) {
                    bf = false;
                    cube[1].reset();
                } else {
                    bf = true;
                    for (int i = 0; i < 4; i++) {
                        a[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVecs().getX();
                        b[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y + shift].getVecs().getY();
                    }
                    cube[1].xpoints = a[1];
                    cube[1].ypoints = b[1];
                    cube[1].reset();
                    cube[1].npoints = 4;
                }
            }
        }
        {
            int shift;
            if (grid.getRotate() >= 0 && grid.getRotate() <= 180) {
                shift = 1;
                color[2] = left;
                if (cubeLeft()) {
                    lr = false;
                    cube[2].reset();
                } else {
                    lr = true;
                    for (int i = 0; i < 4; i++) {
                        a[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVecs().getX();
                        b[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVecs().getY();
                    }
                    cube[2].xpoints = a[2];
                    cube[2].ypoints = b[2];
                    cube[2].reset();
                    cube[2].npoints = 4;
                }
            } else {
                shift = 0;
                color[2] = right;
                if (cubeRight()) {
                    lr = false;
                    cube[2].reset();
                } else {
                    lr = true;
                    for (int i = 0; i < 4; i++) {
                        a[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVecs().getX();
                        b[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + shift][y + MU.makeSquareI(false, i, 4)].getVecs().getY();
                    }
                    cube[2].xpoints = a[2];
                    cube[2].ypoints = b[2];
                    cube[2].reset();
                    cube[2].npoints = 4;
                }
            }
        }
    }

    public void fillCube(@NotNull Graphics2D g2d) {
        if (tb) {
            g2d.setColor(color[0]);
            g2d.fill(cube[0]);
        }
        if (bf) {
            g2d.setColor(color[1]);
            g2d.fill(cube[1]);
        }
        if (lr) {
            g2d.setColor(color[2]);
            g2d.fill(cube[2]);
        }
        g2d.setColor(Color.green);
        g2d.fill(selected);


    }

    private boolean cubeAbove() {// checks for a cube above its self
        return z + 1 < grid.getHeight() - 1 && can.getCubes()[z + 1][x][y] != null;
    }

    private boolean cubeBelow() {// checks for a cube below its self
        return z - 1 >= 0 && can.getCubes()[z - 1][x][y] != null;
    }

    private boolean cubeLeft() {// checks for a cube to the left of its self
        return x + 1 < grid.getSide() - 1 && can.getCubes()[z][x + 1][y] != null;
    }

    private boolean cubeRight() {// checks for a cube to the right of its self
        return x - 1 >= 0 && can.getCubes()[z][x - 1][y] != null;
    }

    private boolean cubeFront() {// checks for a cube in front of its self
        return y + 1 < grid.getSide() - 1 && can.getCubes()[z][x][y + 1] != null;
    }

    private boolean cubeBack() {// checks for a cube behind its self
        return y - 1 >= 0 && can.getCubes()[z][x][y - 1] != null;
    }

    @NotNull
    @Override
    public String toString() {
        return x + " : " + y + " : " + z;
    }

    public boolean contains(int mx, int my) {
        boolean temp = false;
        for (int i = 0; i < 3; i++) {
            if (cube[i].contains(mx, my)) {
                face = i;
                temp = true;
            }
        }
        return temp;
    }

    public int getColorHex() {
        return colorHex;
    }

    public static int getFace() {
        return face;
    }

    public void paint(MouseEvent e) {
        int red;
        int green;
        int blue;
        if (e.isShiftDown()) {
            red = ComponentManager.getColorWheel().getC2Red();
            green = ComponentManager.getColorWheel().getC2Green();
            blue = ComponentManager.getColorWheel().getC2Blue();
        } else {
            red = ComponentManager.getColorWheel().getC1Red();
            green = ComponentManager.getColorWheel().getC1Green();
            blue = ComponentManager.getColorWheel().getC1Blue();
        }
        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color((red), (green), (blue));
        colorHex = (red << 16) | (green << 8) | blue;
        this.color[0] = top;
        this.color[1] = front;
        this.color[2] = right;
    }

    public Polygon[] getFaces() {
        return cube;
    }

    public void setColor(int red, int green, int blue) {

        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color((red), (green), (blue));
        colorHex = (red << 16) | (green << 8) | blue;
        this.color[0] = top;
        this.color[1] = front;
        this.color[2] = right;
    }
}
