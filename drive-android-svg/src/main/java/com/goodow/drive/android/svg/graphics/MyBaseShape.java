package com.goodow.drive.android.svg.graphics;

import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

/**
 * Created by liudenghui on 14-5-27.
 */
public class MyBaseShape {
  protected String type;
  protected int fill;
  protected int stroke;
  protected int stroke_width;
  protected int rotate;
  protected boolean isSelected;
  protected RectF bounds = new RectF();
  protected View popupMenuBtn;
  protected Path path = new Path();
  protected Matrix matrix = new Matrix();

  public Path getPath() {
    return path;
  }

  public MyBaseShape() {
  }

  public View getPopupMenuBtn() {
    return popupMenuBtn;
  }

  public void setPopupMenuBtn(View popupMenuBtn) {
    this.popupMenuBtn = popupMenuBtn;
  }

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public int getStroke() {
    return stroke;
  }

  public void setStroke(int stroke) {
    this.stroke = stroke;
  }

  public int getRotate() {
    return rotate;
  }

  public void setRotate(int rotate) {
    this.rotate = rotate;
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

  public boolean isInRect(RectF rectF) {
    if (rectF.contains(bounds)) {
      return true;
    }
    return false;
  }

  public RectF getBounds() {
    return bounds;
  }

  public void generatePath() {
  }
}
