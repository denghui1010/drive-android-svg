package com.goodow.drive.android.svg;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.realtime.store.Document;

import java.util.List;

/**
 * Created by liudenghui on 14-6-4.
 */
public class MySurfaceView extends SurfaceView {
  public MySurfaceView(Context context) {
    super(context);
    init();
  }

  public MySurfaceView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public MySurfaceView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  private SurfaceHolder mHolder;

  public static enum Select {RECT, ELLIPSE, OVAL, LINE, PATH, SWITCH}

  public static Select selectType;
  private DrawUtils mDrawUtils;
  private int startX;
  private int startY;
  private int currentX;
  private int currentY;
  private MyBaseShape graphic;
  private List<MyBaseShape> list;
  private ShapeSwitcher mShapeSwitcher;
  private boolean isCanDraw;
  private Document doc;

  private SurfaceHolder.Callback mCallback = new SurfaceHolder.Callback() {

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
      Canvas canvas = holder.lockCanvas();
      canvas.drawColor(Color.WHITE);
      holder.unlockCanvasAndPost(canvas);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }
  };

  private void init() {
    mHolder = getHolder();
    mHolder.addCallback(mCallback);
    mDrawUtils = new DrawUtils();
    list = mDrawUtils.getShapeList();
    mShapeSwitcher = new ShapeSwitcher(list);
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isCanDraw) {
      return true;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        if (selectType == Select.RECT) {
          graphic = new MyRect();
        } else if (selectType == Select.OVAL || selectType == Select.ELLIPSE) {
          graphic = new MyEllipse();
        } else if (selectType == Select.LINE) {
          graphic = new MyLine();
        } else if (selectType == Select.PATH) {
          graphic = new MyPath();
        } else {
          return true;
        }
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        int dX = currentX - startX;
        int dY = currentY - startY;
        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mDrawUtils.setCanvas(canvas);
        mDrawUtils.drawAll();
        if (selectType == Select.RECT) {
          MyRect myRect = (MyRect) graphic;
          myRect.setX(startX);
          myRect.setY(startY);
          myRect.setWidth(dX);
          myRect.setHeight(dY);
          myRect.setFill(0);
          myRect.setStroke(Color.BLUE);
          myRect.setStroke_width(2);
          myRect.setTransform(0);
          mDrawUtils.drawRect(myRect);
        } else if (selectType == Select.OVAL) {
          MyEllipse oval = (MyEllipse) graphic;
          oval.setGraphicNum(0);
          int cenX = startX + dX / 2;
          int cenY = startY + dY / 2;
          int r = (int) Math.sqrt((cenX - startX) * (cenX - startX) + (cenY - startY) * (cenY - startY));
          oval.setCx(cenX);
          oval.setCy(cenY);
          oval.setRx(r);
          oval.setRy(r);
          oval.getRectF().set(cenX - r, cenY - r, cenX + r, cenY + r);
          oval.setFill(0);
          oval.setStroke(Color.CYAN);
          oval.setStroke_width(5);
          oval.setTransform(0);
          mDrawUtils.drawEllipse(oval);
        } else if (selectType == Select.ELLIPSE) {
          MyEllipse myEllipse = (MyEllipse) graphic;
          myEllipse.setGraphicNum(0);
          myEllipse.setCx(startX + dX / 2);
          myEllipse.setCy(startY + dY / 2);
          myEllipse.setRx(Math.abs(dX / 2));
          myEllipse.setRy(Math.abs(dY / 2));
          myEllipse.getRectF().set(startX, startY, currentX, currentY);
          myEllipse.setFill(Color.YELLOW);
          myEllipse.setStroke(Color.RED);
          myEllipse.setStroke_width(5);
          myEllipse.setTransform(0);
          mDrawUtils.drawEllipse(myEllipse);
        } else if (selectType == Select.LINE) {
          MyLine myLine = (MyLine) graphic;
          myLine.setX(startX);
          myLine.setY(startY);
          myLine.setSx(startX + dX);
          myLine.setSy(startY + dY);
          myLine.setFill(0);
          myLine.setStroke(Color.RED);
          myLine.setStroke_width(3);
          myLine.setTransform(0);
          mDrawUtils.drawLine(myLine);
        } else if (selectType == Select.PATH) {
          MyPath myPath = (MyPath) graphic;
          Point point = new Point();
          point.set((int) event.getX(), (int) event.getY());
          myPath.addPoint(point);
          myPath.setFill(0);
          myPath.setStroke(Color.RED);
          myPath.setStroke_width(3);
          myPath.setTransform(0);
          mDrawUtils.drawPath(myPath);
        } else if (selectType == Select.SWITCH) {
          mDrawUtils.drawDashRect(startX, startY, currentX, currentY);
        }
        mHolder.unlockCanvasAndPost(canvas);
        break;
      case MotionEvent.ACTION_UP:
        list.add(graphic);
        SaveUtils.saveGraphics(doc, graphic);
        if (selectType == Select.SWITCH) {
          mShapeSwitcher.switchShape(startX, startY, currentX, currentY);
          canvas = mHolder.lockCanvas();
          mDrawUtils.setCanvas(canvas);
          canvas.drawColor(Color.WHITE);
          mDrawUtils.drawAll();
          mHolder.unlockCanvasAndPost(canvas);
        }
        break;
    }
    return true;
  }

  public List<MyBaseShape> getShapeList() {
    return list;
  }

  public boolean isCanDraw() {
    return isCanDraw;
  }

  public void setCanDraw(boolean isCanDraw) {
    this.isCanDraw = isCanDraw;
  }

  public void setDocument(Document doc) {
    this.doc = doc;
  }

}
