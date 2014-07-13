package com.goodow.drive.android.svg.graphics;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * Created by liudenghui on 14-5-27.
 */
public class MyEllipse extends MyBaseShape {
  private int cx;
  private int cy;
  private int rx;
  private int ry;

  public int getRx() {
    return rx;
  }

  public void setRx(int rx) {
    this.rx = rx;
  }

  public int getRy() {
    return ry;
  }

  public void setRy(int ry) {
    this.ry = ry;
  }

  private RectF rectF = new RectF();

  public int getCx() {
    return cx;
  }

  public void setCx(int cx) {
    this.cx = cx;
  }

  public int getCy() {
    return cy;
  }

  public void setCy(int cy) {
    this.cy = cy;
  }

  public RectF getRectF() {
    return rectF;
  }

  public void setRectF(RectF rectF) {
    this.rectF = rectF;
  }

  public void generatePath(int cx, int cy, int rx, int ry, int rotate) {
    path.reset();
    path.addOval(new RectF(cx - rx, cy - ry, cx + rx, cy + ry), Path.Direction.CW);
    matrix.setRotate(rotate, cx, cy);
    path.transform(matrix);
    path.computeBounds(bounds, true);
  }

  @Override
  public void generatePath() {
    generatePath(cx, cy, rx, ry, rotate);
  }

}
