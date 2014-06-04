package com.goodow.drive.android.svg.graphics;

/**
 * Created by liudenghui on 14-5-27.
 */
public class BaseGraphic {
    protected int graphicNum;
    protected int fill;
    protected int stroke;
    protected int stroke_width;
    protected int transform;

    public int getGraphicNum() {
        return graphicNum;
    }

    public void setGraphicNum(int graphicNum) {
        this.graphicNum = graphicNum;
    }

    public int getStroke() {
        return stroke;
    }

    public void setStroke(int stroke) {
        this.stroke = stroke;
    }

    public int getTransform() {
        return transform;
    }

    public void setTransform(int transform) {
        this.transform = transform;
    }

    public int getStroke_width() {

        return stroke_width;
    }

    public void setStroke_width(int stroke_width) {
        this.stroke_width = stroke_width;
    }

    public int getFill() {

        return fill;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }
}
