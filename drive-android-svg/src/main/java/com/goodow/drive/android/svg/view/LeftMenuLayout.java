package com.goodow.drive.android.svg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Scroller;

/**
 * Created by liudenghui on 14-6-18.
 */
public class LeftMenuLayout extends LinearLayout {

  private Scroller mScroller;
  private MyDrawable mDrawable;
  private View mButton;
  private int menuState;
  public static final int FLING2RIGHT = 3;
  public static final int FLING2LEFT = 2;
  public static final int SHOW = 1;
  public static final int HIDE = 0;
  private int mWidth;

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

  @Override
  protected void onDraw(Canvas canvas) {
    float position = -getScrollX() / (float) mWidth;
    canvas.drawARGB((int) (100 * position), 0, 0, 0);
    super.onDraw(canvas);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    View mListView = getChildAt(0);
    mWidth = mListView.getWidth();
    mListView.setDrawingCacheEnabled(true);
    mListView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_LOW);

  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), 0);
      synAction();
      invalidate();
    } else {
      if (menuState == FLING2LEFT) {
        menuState = HIDE;
      } else if (menuState == FLING2RIGHT) {
        menuState = SHOW;
      }
    }
  }

  private void init(Context context) {
    DecelerateInterpolator decelerateInterpolator = new DecelerateInterpolator();
    mScroller = new Scroller(context, decelerateInterpolator);
  }


  private void synAction() {
    if (mDrawable == null) {
      return;
    }
    float position = -getScrollX() / (float) mWidth;
    mDrawable.setPosition(position);

  }

  public void showLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, -mWidth, 0);
    menuState = FLING2RIGHT;
    invalidate();
  }

  public void hideLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, mWidth, 0);
    menuState = FLING2LEFT;
    invalidate();
  }

  public boolean isLeftMenuShown() {
    return menuState == SHOW;
  }

  public void setActionBarDrawable(MyDrawable myDrawable) {
    mDrawable = myDrawable;
  }

  public void setControlButton(View view) {
    mButton = view;
    view.setOnTouchListener(new ButtonOntoucListener());
  }

  class ButtonOntoucListener implements OnTouchListener {

    private int startX;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
      switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
          startX = (int) event.getRawX();
          break;
        case MotionEvent.ACTION_MOVE:
          int currX = (int) event.getRawX();
          int dX = currX - startX;
          if (-getScrollX() + dX < 0) {
            scrollTo(0, 0);
          } else if (-getScrollX() + dX > mWidth) {
            scrollTo(-mWidth, 0);
          } else {
            scrollBy(-dX, 0);
          }
          synAction();
          startX = currX;
          break;
        case MotionEvent.ACTION_UP:
          if (menuState == HIDE) {
            menuState = FLING2RIGHT;
            mScroller.startScroll(getScrollX(), 0, -mWidth - getScrollX(), 0, 300);
          } else if (menuState == SHOW) {
            menuState = FLING2LEFT;
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 300);
          }
          invalidate();
          break;
      }
      return true;
    }
  }

}
