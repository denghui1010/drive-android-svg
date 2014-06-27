package com.goodow.drive.android.svg.utils;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
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
  private Canvas mCanvas;
  private DashPathEffect rectBounds;
  private Path mPath;
  private List<MyBaseShape> shapeList = new ArrayList<MyBaseShape>();
  private List<CollaborativeMap> collList = new ArrayList<CollaborativeMap>();
  private OnShowPopupListener onShowPopupListener;


  public DrawUtil() {
    rectBounds = new DashPathEffect(new float[]{3, 12, 12, 24}, 0);
    mPath = new Path();
  }

  public void drawRect(MyRect myRect) {
    if (mCanvas == null) {
      return;
    }
    mCanvas.save();
    mCanvas.rotate(myRect.getRotate(), myRect.getX() + myRect.getWidth() / 2, myRect.getY() + myRect.getHeight() + 2);
    mPath.reset();
    mPath.addRect(myRect.getX(), myRect.getY(), myRect.getX() + myRect.getWidth(), myRect.getY() + myRect.getHeight(), Path.Direction.CW);
    if (myRect.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myRect.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myRect.getStroke_width());
      mCanvas.drawPath(mPath, mPaint);
    }
    if (myRect.getFill() != 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStyle(Paint.Style.FILL);
      mPaint.setColor(myRect.getFill());
      mCanvas.drawPath(mPath, mPaint);
    }
    RectF rectF = computeBounds(mPath);
    myRect.setBounds(rectF);
    if (myRect.isSelected()) {
      drawRectBounds(rectF);
    }
    mCanvas.restore();

  }

  public void drawDashRect(int x, int y, int sx, int sy) {
    mPaint.reset();
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setColor(Color.BLUE);
    mPaint.setStrokeWidth(2);
    DashPathEffect dashPathEffect = new DashPathEffect(new float[]{5, 5}, 1);
    mPaint.setPathEffect(dashPathEffect);
    mCanvas.drawRect(x, y, sx, sy, mPaint);
  }

  public void drawEllipse(MyEllipse myEllipse) {
    if (mCanvas == null) {
      return;
    }
    mCanvas.save();
    mCanvas.rotate(myEllipse.getRotate(), myEllipse.getCx(), myEllipse.getCy());
    mPath.reset();
    mPath.addOval(new RectF(myEllipse.getCx() - myEllipse.getRx(), myEllipse.getCy() - myEllipse.getRy(), myEllipse.getCx() + myEllipse.getRx(), myEllipse.getCy() + myEllipse.getRy()), Path.Direction.CW);
    if (myEllipse.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myEllipse.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myEllipse.getStroke_width());
      mCanvas.drawPath(mPath, mPaint);
    }
    if (myEllipse.getFill() != 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myEllipse.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(mPath, mPaint);
    }
    RectF rectF = computeBounds(mPath);
    myEllipse.setBounds(rectF);
    if (myEllipse.isSelected()) {
      drawRectBounds(rectF);
    }
    mCanvas.restore();
  }

  public void drawLine(MyLine myLine) {
    if (mCanvas == null) {
      return;
    }
    mCanvas.save();
    mCanvas.rotate(myLine.getRotate(), (myLine.getX() + myLine.getSx()) / 2, (myLine.getY() + myLine.getSy()) / 2);
    mPath.reset();
    mPath.moveTo(myLine.getX(), myLine.getY());
    mPath.lineTo(myLine.getSx(), myLine.getSy());
    if (myLine.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myLine.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myLine.getStroke_width());
      mCanvas.drawPath(mPath, mPaint);
    }
    if (myLine.getFill() != 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setColor(myLine.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(mPath, mPaint);
    }
    RectF rectF = computeBounds(mPath);
    myLine.setBounds(rectF);
    if (myLine.isSelected()) {
      drawRectBounds(rectF);
    }
    mCanvas.restore();
  }

  public void drawPath(MyPath myPath) {
    if (mCanvas == null || myPath.getPoints().size() == 0) {
      return;
    }
    mPath.reset();
    mPath.moveTo(myPath.getPoints().get(0).x, myPath.getPoints().get(0).y);
    for (int i = 1; i < myPath.getPoints().size(); i++) {
      mPath.quadTo((myPath.getPoints().get(i - 1).x + myPath.getPoints().get(i).x) / 2, (myPath.getPoints().get(i - 1).y + myPath.getPoints().get(i).y) / 2, myPath.getPoints().get(i).x, myPath.getPoints().get(i).y);
    }
    RectF rectF = computeBounds(mPath);
    myPath.setBounds(rectF);
    mCanvas.save();
    mCanvas.rotate(myPath.getRotate(), rectF.centerX(), rectF.centerY());
    if (myPath.getStroke_width() > 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
      mPaint.setStrokeJoin(Paint.Join.ROUND);
      mPaint.setColor(myPath.getStroke());
      mPaint.setStyle(Paint.Style.STROKE);
      mPaint.setStrokeWidth(myPath.getStroke_width());
      mCanvas.drawPath(mPath, mPaint);
    }
    if (myPath.getFill() != 0) {
      mPaint.reset();
      mPaint.setAntiAlias(true);
      mPaint.setStrokeCap(Paint.Cap.ROUND);// 形状
      mPaint.setStrokeJoin(Paint.Join.ROUND);
      mPaint.setColor(myPath.getFill());
      mPaint.setStyle(Paint.Style.FILL);
      mCanvas.drawPath(mPath, mPaint);
    }
    if (myPath.isSelected()) {
      drawRectBounds(rectF);
    }
    mCanvas.restore();

  }

  public void drawAll() {
    for(MyBaseShape graphic : shapeList){
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

  private void drawRectBounds(RectF rectF) {
    mPaint.reset();
    mPaint.setAntiAlias(true);
    mPaint.setColor(Color.RED);
    mPaint.setStrokeWidth(3);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setPathEffect(rectBounds);
    mCanvas.drawRect(rectF, mPaint);
  }

  private RectF computeBounds(Path path) {
    RectF rectF = new RectF();
    path.computeBounds(rectF, true);
    return rectF;
  }

  public void setCanvas(Canvas mCanvas) {
    this.mCanvas = mCanvas;
  }

  public List<MyBaseShape> getShapeList(){
    return shapeList;
  }

  public List<CollaborativeMap> getCollList(){
    return collList;
  }

  public void setOnShowPopupListener(OnShowPopupListener onShowPopupListener){
    this.onShowPopupListener = onShowPopupListener;
  }
}
