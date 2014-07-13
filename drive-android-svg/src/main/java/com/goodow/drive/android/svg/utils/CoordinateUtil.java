package com.goodow.drive.android.svg.utils;

import android.view.ViewGroup;

import com.goodow.realtime.store.Document;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by liudenghui on 14-7-11.
 */
@Singleton
public class CoordinateUtil {
  private int width;
  private int height;
  private ViewGroup view;
  @Inject
  private DrawUtil drawUtil;

  public void setView(ViewGroup view) {
    this.view = view;
  }

  public double translateX2proportion(int x) {
//    System.out.println((double) x / width);
    return (double) x / width;
  }

  public float translateY2proportion(int y) {
//    System.out.println((double) y / height);
    return (float) y / height;
  }

  public int translateX2local(double x) {
//    System.out.println((int) (x * width));
    return (int) (x * width);
  }

  public int translateY2local(double y) {
//    System.out.println((int) (y * height));
    return (int) (y * height);
  }

  public void setRatio(Document doc) {
    int viewWidth = view.getWidth();
    int viewHeight = view.getHeight();
//    System.out.println(viewWidth + "," + viewHeight);
    Double ratio = doc.getModel().getRoot().get("ratio");
    double currentRatio = (double) viewHeight / viewWidth;
    if (ratio > currentRatio) {
      width = (int) (viewHeight / ratio);
      height = viewHeight;
    } else {
      height = (int) (viewWidth * ratio);
      width = viewWidth;
    }
//    System.out.println("width=" + width + ",height=" + height);
    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
    layoutParams.height = height;
    layoutParams.width = width;
    view.setLayoutParams(layoutParams);
//    System.out.println("surface"+view.getWidth());
  }

}
