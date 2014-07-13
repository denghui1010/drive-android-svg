package com.goodow.drive.android.svg.graphics;

/**
 * Created by liudenghui on 14-6-11.
 */
public class MyLine extends MyBaseShape {
  private int x;
  private int y;
  private int sx;
  private int sy;

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public void setX(int x) {
    this.x = x;
  }

  public void setY(int y) {
    this.y = y;
  }

  public void setSx(int sx) {
    this.sx = sx;
  }

  public void setSy(int sy) {
    this.sy = sy;
  }

  public int getSx() {
    return sx;
  }

  public int getSy() {
    return sy;
  }

  @Override
  public void generatePath() {
    generatePath(x, y, sx, sy, rotate);
  }

  public void generatePath(int x, int y, int sx, int sy, int rotate) {
    path.reset();
    path.moveTo(x, y);
    path.lineTo(sx, sy);
    matrix.setRotate(rotate, (x + sx) / 2, (y + sy) / 2);
    path.transform(matrix);
    path.computeBounds(bounds, true);
  }
}
