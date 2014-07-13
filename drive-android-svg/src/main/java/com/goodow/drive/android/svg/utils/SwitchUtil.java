package com.goodow.drive.android.svg.utils;

import android.graphics.RectF;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created by liudenghui on 14-5-27.
 */
@Singleton
public class SwitchUtil {

  public void switchShape(List<MyBaseShape> shapeList, float left, float top, float right, float bottom) {
    for (MyBaseShape shape : shapeList) {
      boolean isContains;
      isContains = !(left == 0 && top == 0 && right == 0 && bottom == 0) && shape.isInRect(setRectF(left, top, right, bottom));
      if (!shape.isSelected() && isContains) {
        shape.setSelected(true);
      } else if (shape.isSelected() && !isContains) {
        shape.setSelected(false);
      }
    }
  }

  private RectF setRectF(float left, float top, float right, float bottom) {
    RectF rectF = new RectF();
    rectF.set(left <= right ? left : right, top <= bottom ? top : bottom, left <= right ? right : left, top <= bottom ? bottom : top);
    return rectF;
  }

}
