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

  @Override
  public void generatePath() {
    path.reset();
    path.moveTo(points.get(0).x, points.get(0).y);
    for (int i = 1; i < points.size(); i++) {
      path.quadTo((points.get(i - 1).x + points.get(i).x) / 2, (points.get(i - 1).y + points.get(i).y) / 2, points.get(i).x, points.get(i).y);
    }
    path.computeBounds(bounds, true);
    matrix.setRotate(rotate, bounds.centerX(), bounds.centerY());
    path.transform(matrix);
    path.computeBounds(bounds, true);
  }
}
