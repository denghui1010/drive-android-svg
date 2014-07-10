package com.goodow.drive.android.svg.view;

import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;

/**
 * Created by liudenghui on 14-6-19.
 */
public class MyDrawable extends InsetDrawable {
  public MyDrawable(Drawable drawable) {
    super(drawable, 0);
  }

  private float mOffset;
  private float mPosition;

  public void setmOffset(float offset){
    mOffset = offset;
  }

  public void setPosition(float position){
    mPosition = position;
    invalidateSelf();
  }

  @Override
  public void draw(Canvas canvas) {
    canvas.save();
    canvas.translate(-mOffset * getBounds().width() * mPosition, 0);
    super.draw(canvas);
    canvas.restore();
  }
}
