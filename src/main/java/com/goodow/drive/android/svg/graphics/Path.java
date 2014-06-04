package com.goodow.drive.android.svg.graphics;

import android.graphics.Point;

import com.goodow.drive.android.svg.graphics.BaseGraphic;

/**
 * Created by liudenghui on 14-6-3.
 */
public class Path extends BaseGraphic {
    private Point[] point;

    public Point[] getPoint() {
        return point;
    }

    public void setPoint(Point[] point) {
        this.point = point;
    }
}
