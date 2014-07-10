package com.goodow.drive.android.svg.graphics;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudenghui on 14-6-3.
 */
public class MyPath extends MyBaseShape {
  private List<Point> points = new ArrayList<Point>();

  public void addPoint(Point point) {
    points.add(point);
  }

  public List<Point> getPoints() {
    return points;
  }
}
