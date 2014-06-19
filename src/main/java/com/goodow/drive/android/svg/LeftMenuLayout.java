package com.goodow.drive.android.svg;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
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
  private MyDrawable mDrawable;
  private View mButton;
  private View mListView;
  private int menuState;
  public static final int FLING2RIGHT = 3;
  public static final int FLING2LEFT = 2;
  public static final int SHOW = 1;
  public static final int HIDE = 0;

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mListView = getChildAt(0);
  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      synAction();
      invalidate();
    } else {
      if(menuState == FLING2LEFT){
        menuState = HIDE;
      } else if(menuState == FLING2RIGHT) {
        menuState = SHOW;
      }
    }
    super.computeScroll();
  }

  private void init(Context context) {
    mScroller = new Scroller(context);
    this.setAlwaysDrawnWithCacheEnabled(true);
  }



  private void synAction(){
    float position = -getScrollX() / (float) mListView.getWidth();
    mDrawable.setPosition(position);
    setBackgroundColor(Color.argb((int) (100 * position), 0, 0, 0));
  }

  public void showLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, -mListView.getWidth(), 0, 350);
    menuState = FLING2RIGHT;
    invalidate();
  }

  public void hideLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, mListView.getWidth(), 0, 350);
    menuState = FLING2LEFT;
    invalidate();
  }

  public boolean isLeftMenuShown() {
    return menuState == SHOW;
  }

  public void setActionBarDrawable(MyDrawable myDrawable) {
    mDrawable = myDrawable;
  }

  public void setControlButton(View view){
    mButton = view;
    view.setOnTouchListener(new ButtonOntoucListener());
  }

  class ButtonOntoucListener implements OnTouchListener{

    private float startX;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

      switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
          startX = event.getRawX();
          break;
        case MotionEvent.ACTION_MOVE:
          float currX = event.getRawX();
          float dX = currX - startX;
          if(-getScrollX()+dX<0 || -getScrollX()+dX > mListView.getWidth()){
            dX = 0;
          }
          scrollBy(-(int)dX, 0);
          synAction();
          startX = currX;
          break;
        case MotionEvent.ACTION_UP:
          if(menuState == HIDE) {
            menuState = FLING2RIGHT;
            mScroller.startScroll(getScrollX(), 0, -mListView.getWidth()-getScrollX(), 0, 500);
            invalidate();
          } else if(menuState == SHOW) {
            menuState = FLING2LEFT;
            mScroller.startScroll(getScrollX(), 0, -getScrollX(), 0, 500);
            invalidate();
          }
          break;
      }
      return true;
    }
  }

}
