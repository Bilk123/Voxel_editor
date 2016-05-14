package backend;

import util.MU;
import screen.Canvas;

import java.awt.*;
import java.awt.event.KeyEvent;

public class Cube {

    //int-------------------
    private int x;
    private int y;
    private int z;
    private int[][] a, b;

    //----------------------

    //Boolean---------------
    private boolean tb, bf, lr;
    private static boolean isHover;
    //----------------------

    //Polygon---------------
    private Polygon[] cube;
    private static Polygon hover;
    private static Polygon[] cubeHover;
    //----------------------

    //Color-----------------
    private Color color[];
    private Color top, bot, back, front, left, right;
    private static Color hoverC;
    //----------------------

    //Grid------------------
    private Grid grid;
    //----------------------

    //Canvas----------------
    private Canvas can;
    //----------------------

    //Constructor and init////////////////////////////////////////////////////////////////////
    public Cube(Canvas can, int x, int y, int z, int red, int green, int blue) {
        initCube(can, x, y, z, red, green, blue);
    }

    private void initCube(Canvas can, int x, int y, int z, int red, int green, int blue) {
        this.can = can;
        this.grid = can.getGrid();
        this.x = x;
        this.y = y;
        this.z = z;
        this.color = new Color[3];
        int colorHex = (red << 16) | (green << 8) | (blue);

        back = new Color((int) (red * 0.5), (int) (green * 0.5), (int) (blue * 0.5));
        bot = new Color((int) (red * 0.6), (int) (green * 0.6), (int) (blue * 0.6));
        left = new Color((int) (red * 0.7), (int) (green * 0.7), (int) (blue * 0.7));
        right = new Color((int) (red * 0.8), (int) (green * 0.8), (int) (blue * 0.8));
        top = new Color((int) (red * 0.9), (int) (green * 0.9), (int) (blue * 0.9));
        front = new Color(red, green, blue);
        hoverC = new Color(0.2f, 1f, 0.2f, 0.6f);
        this.color[0] = top;
        this.color[1] = front;
        this.color[2] = right;
        cube = new Polygon[3];
        a = new int[3][4];
        b = new int[3][4];
        {
            for (int i = 0; i < 4; i++) {
                a[0][i] = (int) grid.getPts()[z][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getX();
                b[0][i] = (int) grid.getPts()[z][x + MU.makeSquareI(false, i, 4)][y + MU.makeSquareI(true, i, 4)].getVecs().getY();
            }
        }
        cube[0] = new Polygon(a[0], b[0], 4);
        {
            for (int i = 0; i < 4; i++) {
                a[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y].getVecs().getX();
                b[1][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x + MU.makeSquareI(false, i, 4)][y].getVecs().getY();
            }
        }
        cube[1] = new Polygon(a[1], b[1], 4);
        {
            for (int i = 0; i < 4; i++) {
                a[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x][y + MU.makeSquareI(false, i, 4)].getVecs().getX();
                b[2][i] = (int) grid.getPts()[z + MU.makeSquareI(true, i, 4)][x][y + MU.makeSquareI(false, i, 4)].getVecs().getY();
            }
        }
        cube[2] = new Polygon(a[2], b[2], 4);
        hover = new Polygon();
        cubeHover = new Polygon[3];
        for (int i = 0; i < 3; i++) {
            cubeHover[i] = new Polygon();
        }

    }
    //////////////////////////////////////////////////////////////////////////////////////////

    //drawing and graphics update//////////////
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

    public void fillCube(Graphics2D g2d) {
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

        if (isHover) {
            drawCube(g2d);
        }
    }

    private void drawCube(Graphics2D g2d) {
        g2d.setColor(Color.black);
        for (int i = 0; i < 3; i++) {
            g2d.draw(cubeHover[i]);
        }
        g2d.setColor(hoverC);
        g2d.fill(hover);


    }
    ///////////////////////////////////////////

    ////////////////////////////////

    //misc//////////////////////////
    private boolean cubeAbove() {
        return z + 1 < grid.getHeight() - 1 && can.getCubes()[z + 1][x][y] != null;
    }

    private boolean cubeBelow() {
        return z - 1 >= 0 && can.getCubes()[z - 1][x][y] != null;
    }

    private boolean cubeLeft() {
        return x + 1 < grid.getSide() - 1 && can.getCubes()[z][x + 1][y] != null;
    }

    private boolean cubeRight() {
        return x - 1 >= 0 && can.getCubes()[z][x - 1][y] != null;
    }

    private boolean cubeFront() {
        return y + 1 < grid.getSide() - 1 && can.getCubes()[z][x][y + 1] != null;
    }

    private boolean cubeBack() {
        return y - 1 >= 0 && can.getCubes()[z][x][y - 1] != null;
    }

    @Override
    public String toString() {
        return x + " : " + y + " : " + z;
    }
    ////////////////////////////////

    //user input////////////////////
    public void hover(Rectangle pnt) {
        for (int i = 0; i < 3; i++) {
            if (pnt.intersects(cube[i].getBounds())) {
                cubeHover = cube;
                hover = cube[i];

            }
        }
    }

    public void click(Rectangle pnt) {

    }

    public static void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_1) {
            isHover = !isHover;
            System.out.println("hover: " + isHover);
        }
    }
    ////////////////////////////////


}
