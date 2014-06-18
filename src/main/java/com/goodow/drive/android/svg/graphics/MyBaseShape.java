package com.goodow.drive.android.svg.graphics;

import android.graphics.RectF;

/**
 * Created by liudenghui on 14-5-27.
 */
public class MyBaseShape {
  protected int graphicNum;
  protected int fill;
  protected int stroke;
  protected int stroke_width;
  protected int transform;
  protected boolean isSelected;
  protected RectF bounds;

  public boolean isSelected() {
    return isSelected;
  }

  public void setSelected(boolean isSelected) {
    this.isSelected = isSelected;
  }

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

  public boolean containsShape(RectF rectF) {
    if (rectF.contains(bounds)) {
      return true;
    }
    return false;
  }

  public void setBounds(RectF bounds) {
    this.bounds = bounds;
  }
}
