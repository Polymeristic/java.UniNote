package main;

import javafx.geometry.Pos;

import javax.swing.plaf.PanelUI;
import javax.swing.text.Position;

/**
 * Simple rectangle for geometric representations
 */
public class Rect {
    public double PositionX;
    public double PositionY;
    public double Width;
    public double Height;

    /**
     * Copy a Rect using another Rect as a base
     * @param base Base Rect to copy
     */
    public Rect(Rect base) {
        PositionX = base.PositionX;
        PositionY = base.PositionY;
        Width = base.Width;
        Height = base.Height;
    }

    /**
     * Create a new Rect
     * @param positionX Position on the X axis
     * @param positionY Position on the Y axis
     * @param width     Width of the rectangle
     * @param height    Height of the rectangle
     */
    public Rect(double positionX, double positionY, double width, double height) {
        PositionX = positionX;
        PositionY = positionY;
        Width = width;
        Height = height;
    }


    public void TranslateWithinXBounds(double x, double y, double lineHeight) {

    }

    /**
     * Translates the rectangle by a given x and y value
     * @param x X to translate
     * @param y Y to translate
     */
    public void Translate(double x, double y) {
        PositionX += x;
        PositionY += y;
    }
}
