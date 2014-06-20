package com.goodow.drive.android.svg.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Scroller;

/**
 * Created by liudenghui on 14-6-13.
 */
public class MyViewGroup extends ViewGroup {
  public MyViewGroup(Context context) {
    super(context);
    init(context);
  }

  public MyViewGroup(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public MyViewGroup(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private Scroller mScroller;
  private final int FLING_DURATION = 500;
  private boolean isMenuShow;

  public static enum MenuType {LEFT, RIGHT, NONE}

  private MenuType menuType = MenuType.NONE;
  private View mMenu;
  private int screenWidth;
  private int screenHeight;
  private Context mContext;

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    if (changed) {
      int childLeft = 0;
      final int count = getChildCount();
      for (int i = 0; i < count; i++) {
        final View child = getChildAt(i);
        MenuType tag = (MenuType) child.getTag();
        if (tag != MenuType.LEFT && tag != MenuType.RIGHT && child.getVisibility() != View.GONE) {
          final int childWidth = child.getMeasuredWidth();
          child.layout(childLeft, 0, childLeft + childWidth, child.getMeasuredHeight());
          childLeft += childWidth;
        }
      }
    }
  }

  @Override
  public void addView(View child) {
    menuType = MenuType.NONE;
    super.addView(child);
  }

  private void init(Context context) {
    mContext = context;
    mScroller = new Scroller(context);
    getScreenSize();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);
    int measuredHeight = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(measuredWidth, measuredHeight);
    for (int i = 0; i < getChildCount(); i++) {
      View v = getChildAt(i);
      measureView(v, measuredWidth, measuredHeight);
    }
  }

  public void showMenu() {
    if (menuType == MenuType.LEFT) {
      mScroller.startScroll(getScrollX(), 0, -mMenu.getWidth(), 0, FLING_DURATION);
    } else if (menuType == MenuType.RIGHT) {
      mScroller.startScroll(getScrollX(), 0, mMenu.getWidth(), 0, FLING_DURATION);
    }
    invalidate();
    isMenuShow = true;
  }

  public void hideMenu() {
    if (menuType == MenuType.LEFT) {
      mScroller.startScroll(getScrollX(), 0, mMenu.getWidth(), 0, FLING_DURATION);
    } else if (menuType == MenuType.RIGHT) {
      mScroller.startScroll(getScrollX(), 0, -mMenu.getWidth(), 0, FLING_DURATION);
    }
    invalidate();
    isMenuShow = false;
  }

  @Override
  public void computeScroll() {
    if (mScroller.computeScrollOffset()) {
      scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
      postInvalidate();
    }
  }

  public boolean getIsMenuShow() {
    return isMenuShow;
  }

  public void addMenu(View view, MenuType menuType) {
    mMenu = view;
    view.setTag(menuType);
    measureView(view, screenWidth, screenHeight);
    addView(view);
    this.menuType = menuType;
    if (menuType == MenuType.LEFT) {
      view.layout(-view.getMeasuredWidth(), 0, 0, view.getMeasuredHeight());
    }
  }

  @SuppressLint("NewApi")
  private void getScreenSize() {// 获得屏幕尺寸大小
    if (android.os.Build.VERSION.SDK_INT < 17) {
      DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      screenWidth = displayMetrics.widthPixels;
      screenHeight = displayMetrics.heightPixels;
    } else {// api>17用新方式
      WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
      DisplayMetrics outMetrics = new DisplayMetrics();
      wm.getDefaultDisplay().getRealMetrics(outMetrics);
      screenWidth = outMetrics.widthPixels;
      screenHeight = outMetrics.heightPixels;
    }
  }

  private void measureView(View v, int width, int height) {
    int widthSpec = 0;
    int heightSpec = 0;
    LayoutParams params = v.getLayoutParams();
    if (params.width > 0) {
      widthSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
    } else if (params.width == -1) {
      widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
    } else if (params.width == -2) {
      widthSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.AT_MOST);
    }
    if (params.height > 0) {
      heightSpec = MeasureSpec.makeMeasureSpec(params.height, MeasureSpec.EXACTLY);
    } else if (params.height == -1) {
      heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
    } else if (params.height == -2) {
      heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
    }
    v.measure(widthSpec, heightSpec);
  }

}
