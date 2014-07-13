package com.goodow.drive.android.svg.graphics;

import android.graphics.Path;

/**
 * Created by liudenghui on 14-6-3.
 */
public class MyRect extends MyBaseShape {
  private int x;
  private int y;
  private int width;
  private int height;

  public int getX() {
    return x;
  }

  public void setX(int x) {
    this.x = x;
  }

  public int getY() {
    return y;
  }

  public void setY(int y) {
    this.y = y;
  }

  public int getWidth() {
    return width;
  }

  public void setWidth(int width) {
    this.width = width;
  }

  public int getHeight() {
    return height;
  }

  public void setHeight(int height) {
    this.height = height;
  }

  @Override
  public void generatePath() {
    generatePath(x, y, width, height, rotate);
  }

  public void generatePath(int x, int y, int width, int height, int rotate) {
    path.reset();
    path.addRect(x, y, x + width, y + height, Path.Direction.CW);
    matrix.setRotate(rotate, x + width / 2, y + height / 2);
    path.transform(matrix);
    path.computeBounds(bounds, true);
  }
}
