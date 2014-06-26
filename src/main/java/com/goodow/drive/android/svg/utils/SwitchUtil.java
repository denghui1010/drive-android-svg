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

  public void switchShape(List<MyBaseShape> shapeList, int left, int top, int right, int bottom) {
    for (MyBaseShape shape : shapeList) {
      boolean isContains;
      if(left==0 && top==0 && right==0 && bottom==0){
        isContains = false;
      } else {
        isContains = shape.containsShape(setRectF(left, top, right, bottom));
      }
      if(!shape.isSelected() && isContains){
        shape.setSelected(true);
      } else if(shape.isSelected() && !isContains){
        shape.setSelected(false);
      }
    }
  }

  private RectF setRectF(int left, int top, int right, int bottom) {
    RectF rectF = new RectF();
    rectF.set(left, top, right, bottom);
    return rectF;
  }

}
