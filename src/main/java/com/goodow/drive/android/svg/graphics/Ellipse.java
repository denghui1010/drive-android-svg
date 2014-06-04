package com.goodow.drive.android.svg.graphics;

import com.goodow.drive.android.svg.graphics.BaseGraphic;

/**
 * Created by liudenghui on 14-5-27.
 */
public class Ellipse extends BaseGraphic {
    private int cx;
    private int cy;
    private int rx;
    private int ry;

    public int getCx() {
        return cx;
    }

    public void setCx(int cx) {
        this.cx = cx;
    }

    public int getRy() {
        return ry;
    }

    public void setRy(int ry) {
        this.ry = ry;
    }

    public int getRx() {

        return rx;
    }

    public void setRx(int rx) {
        this.rx = rx;
    }

    public int getCy() {

        return cy;
    }

    public void setCy(int cy) {
        this.cy = cy;
    }
}
