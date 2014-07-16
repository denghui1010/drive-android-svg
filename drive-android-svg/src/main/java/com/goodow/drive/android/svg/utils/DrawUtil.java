package com.goodow.drive.android.svg.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.RectF;

import com.goodow.drive.android.svg.OnShowPopupListener;
import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.realtime.store.CollaborativeMap;
import com.google.inject.Singleton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudenghui on 14-6-4.
 */

@Singleton
public class DrawUtil {
  private Paint mPaint = new Paint();
  private Paint shapeBoundsPaint = new Paint();
  private Paint switchBoundsPaint = new Paint();
  private Canvas mCanvas;
  private DashPathEffect shapeBoundsEffect;
  private DashPathEffect shapeBoundsEffect2;
  private DashPathEffect switchBoundsEffect;
  private List<MyBaseShape> shapeList = new ArrayList<MyBaseShape>();
  private List<CollaborativeMap> collList = new ArrayList<CollaborativeMap>();
  private OnShowPopupListener onShowPopupListener;


  public DrawUtil() {
    shapeBoundsEffect = new DashPathEffect(new float[]{3, 12, 3, 12}, 0);
    shapeBoundsEffect2 = new DashPathEffect(new float[]{0, 6, 6, 3}, 0);
    switchBoundsEffect = new DashPathEffect(new float[]{5, 5}, 1);
  }

  public void drawRect(MyRect myRect) {
    if (mCanvas == null) {
      return;
    }
    myRect.generatePath();
    if (myRect.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myRect.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myRect.getStroke_width());
      mCanvas.drawPath(myRect.getPath(), mPaint);
    }
    if (myRect.getFill() != -1) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setColor(myRect.getFill());
      mCanvas.drawPath(myRect.getPath(), mPaint);
    }
    if (myRect.isSelected()) {
      drawShapeBoundsRect(myRect.getBounds());
    }
  }

  public void drawSwitchBoundsRect(int x, int y, int sx, int sy) {
    switchBoundsPaint.setAntiAlias(true);
    switchBoundsPaint.setStyle(Paint.Style.STROKE);
    switchBoundsPaint.setColor(Color.BLUE);
    switchBoundsPaint.setStrokeWidth(2);
    switchBoundsPaint.setPathEffect(switchBoundsEffect);
    mCanvas.drawRect(x, y, sx, sy, switchBoundsPaint);
  }

  public void drawEllipse(MyEllipse myEllipse) {
    if (mCanvas == null) {
      return;
    }
    myEllipse.generatePath();
    if (myEllipse.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myEllipse.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myEllipse.getStroke_width());
      mCanvas.drawPath(myEllipse.getPath(), mPaint);
    }
    if (myEllipse.getFill() != -1) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myEllipse.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(myEllipse.getPath(), mPaint);
    }
    if (myEllipse.isSelected()) {
      drawShapeBoundsRect(myEllipse.getBounds());
    }
  }

  public void drawLine(MyLine myLine) {
    if (mCanvas == null) {
      return;
    }
    myLine.generatePath();
    if (myLine.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myLine.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myLine.getStroke_width());
      mCanvas.drawPath(myLine.getPath(), mPaint);
    }
    if (myLine.getFill() != -1) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myLine.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(myLine.getPath(), mPaint);
    }
    if (myLine.isSelected()) {
      drawShapeBoundsRect(myLine.getBounds());
    }
  }

  public void drawPath(MyPath myPath) {
    if (mCanvas == null || myPath.getPoints().size() == 0) {
      return;
    }
    myPath.generatePath();
    if (myPath.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
      mPaint.setStrokeJoin(Paint.Join.ROUND);
      mPaint.setColor(myPath.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myPath.getStroke_width());
      mCanvas.drawPath(myPath.getPath(), mPaint);
    }
    if (myPath.getFill() != -1) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
      mPaint.setStrokeJoin(Paint.Join.ROUND);
      mPaint.setColor(myPath.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(myPath.getPath(), mPaint);
    }
    if (myPath.isSelected()) {
      drawShapeBoundsRect(myPath.getBounds());
    }
  }

  public void drawAll() {
    for (MyBaseShape graphic : shapeList) {
      if (graphic instanceof MyRect) {
        drawRect((MyRect) graphic);
      } else if (graphic instanceof MyEllipse) {
        drawEllipse((MyEllipse) graphic);
      } else if (graphic instanceof MyPath) {
        drawPath((MyPath) graphic);
      } else if (graphic instanceof MyLine) {
        drawLine((MyLine) graphic);
      }
      onShowPopupListener.onShowPopup(graphic);
    }
  }

  private void drawShapeBoundsRect(RectF rectF) {
    shapeBoundsPaint.setAntiAlias(true);
    shapeBoundsPaint.setColor(Color.RED);
    shapeBoundsPaint.setStrokeWidth(3);
    shapeBoundsPaint.setStyle(Paint.Style.STROKE);
    shapeBoundsPaint.setPathEffect(shapeBoundsEffect);
    mCanvas.drawRect(rectF, shapeBoundsPaint);
    shapeBoundsPaint.setColor(Color.YELLOW);
    shapeBoundsPaint.setPathEffect(shapeBoundsEffect2);
    mCanvas.drawRect(rectF, shapeBoundsPaint);

  }

  public void setCanvas(Canvas mCanvas) {
    this.mCanvas = mCanvas;
  }

  public List<MyBaseShape> getShapeList() {
    return shapeList;
  }

  public List<CollaborativeMap> getCollList() {
    return collList;
  }

  public void setOnShowPopupListener(OnShowPopupListener onShowPopupListener) {
    this.onShowPopupListener = onShowPopupListener;
  }
}
