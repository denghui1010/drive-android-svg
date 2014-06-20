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
  private int mWidth;

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    mListView = getChildAt(0);
    mWidth = mListView.getWidth();
      mListView.setDrawingCacheEnabled(true);
      mListView.setDrawingCacheQuality(DRAWING_CACHE_QUALITY_LOW);
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
  }

  private void init(Context context) {
    mScroller = new Scroller(context);
    this.setAlwaysDrawnWithCacheEnabled(true);

  }



  private void synAction(){
    float position = -getScrollX() / (float) mWidth;
    mDrawable.setPosition(position);
//    setBackgroundColor(Color.argb((int) (100 * position), 0, 0, 0));
  }

  public void showLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, -mWidth, 0, 350);
    menuState = FLING2RIGHT;
    invalidate();
  }

  public void hideLeftMenu() {
    mScroller.startScroll(getScrollX(), 0, mWidth, 0, 350);
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

    private int startX;
    @Override
    public boolean onTouch(View v, MotionEvent event) {

      switch (event.getAction()){
        case MotionEvent.ACTION_DOWN:
          startX = (int) event.getRawX();
          break;
        case MotionEvent.ACTION_MOVE:
          int currX = (int) event.getRawX();
          int dX = currX - startX;
          if(-getScrollX()+dX<0){
            scrollTo(0,0);
          } else if( -getScrollX()+dX > mWidth){
            scrollTo(-mWidth,0);
          } else {
            scrollBy(-dX, 0);
          }
          synAction();
          startX = currX;
          break;
        case MotionEvent.ACTION_UP:
          if(menuState == HIDE) {
            menuState = FLING2RIGHT;
//            mScroller.startScroll(getScrollX(), 0, -mWidth-getScrollX(), 0, 500);
            mScroller.fling(getScrollX(), 0, 20, 0,-200,-400,0,0);
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
