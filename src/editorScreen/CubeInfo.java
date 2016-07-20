package editorScreen;

import guiTools.GuiComponent;
import util.MU;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

/**
 * Created by Blake on 7/15/2016.
 */
public class CubeInfo extends GuiComponent {


    protected CubeInfo(double x, double y, double width, Color bgColor) {
        super(x, y, width, width, bgColor, 14, true);
        setToolBarTitle("SELECTED CUBE INFO");
    }

    @Override
    protected void paintGuiComponent(Graphics2D g2d) {

    }

    @Override
    protected void update() {
        double y_;
        if (ComponentManager.getCanvasDataManger().getMinimized() < 0) {
            y_ = ComponentManager.getCanvasDataManger().getY() + 15 + 30;
        } else {
            y_ = ComponentManager.getCanvasDataManger().getY() + ComponentManager.getCanvasDataManger().getHeight() + 15 + 30;
        }
        setBounds((EditorScreen.s_maxWidth * (MU.getPercent(5 + 25, 1920)) - 16), y_, EditorScreen.s_maxWidth * MU.getPercent(310, 1920), EditorScreen.s_maxWidth * MU.getPercent(250, 1920) * 0.8);
    }

    @Override
    protected void hover(MouseEvent e) {

    }

    @Override
    protected void drag(MouseEvent e) {

    }

    @Override
    protected void scroll(MouseWheelEvent mwe) {

    }

    @Override
    protected void keyPress(KeyEvent e) {

    }

    @Override
    protected void mousePress(MouseEvent e) {

    }

    @Override
    protected void mouseRelease(MouseEvent e) {

    }
}