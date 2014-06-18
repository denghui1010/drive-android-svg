package com.goodow.drive.android.svg;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by liudenghui on 14-6-18.
 */
public class LeftMenuLayout extends LinearLayout {
  public LeftMenuLayout(Context context) {
    super(context);
    init(context);
  }

  public LeftMenuLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public LeftMenuLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private Scroller mScroller;
  private boolean isLeftMenuShown;
  private float dx;

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      float abs =  Math.abs((mScroller.getCurrX() - mScroller.getStartX()) / dx);
      if (!isLeftMenuShown) {
        abs = 1 - abs;
      }
      setBackgroundColor(Color.argb((int) (100 * abs), 0, 0, 0));
      invalidate();
    }
    super.computeScroll();
  }

  private void init(Context context) {
    mScroller = new Scroller(context);
  }

  public void showLeftMenu(int dx) {
    this.dx = dx;
    mScroller.startScroll(getScrollX(), 0, -dx, 0, 350);
    isLeftMenuShown = true;
    invalidate();
  }

  public void hideLeftMenu(int dx) {
    this.dx = dx;
    mScroller.startScroll(getScrollX(), 0, dx, 0, 350);
    isLeftMenuShown = false;
    invalidate();
  }

  public boolean isLeftMenuShown() {
    return isLeftMenuShown;
  }
}
