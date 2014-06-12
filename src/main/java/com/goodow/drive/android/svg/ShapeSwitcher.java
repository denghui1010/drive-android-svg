package com.goodow.drive.android.svg;

import android.graphics.RectF;

import com.goodow.drive.android.svg.graphics.MyBaseShape;

import java.util.List;

/**
 * Created by liudenghui on 14-5-27.
 */
public class ShapeSwitcher {
  private List<MyBaseShape> list;

  public ShapeSwitcher(List<MyBaseShape> list) {
    this.list = list;
  }

  public void switchShape(int left, int top, int right, int bottom) {
    for (MyBaseShape shape : list) {
      shape.setSelected(shape.containsShape(setRectF(left, top, right, bottom)));
    }
  }

  private RectF setRectF(int left, int top, int right, int bottom) {
    RectF rectF = new RectF();
    rectF.set(left, top, right, bottom);
    return rectF;
  }

}
