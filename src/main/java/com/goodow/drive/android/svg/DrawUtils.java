package com.goodow.drive.android.svg;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.store.CollaborativeList;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudenghui on 14-6-4.
 */
public class DrawUtils {
  private Paint mPaint = new Paint();
  private Canvas mCanvas;
  private Document doc;
  private DashPathEffect rectBounds;
  private Path mPath;

  private enum GraphicType {RECT, LINE, ELLIPSE, PATH}

  private List<MyBaseShape> list = new ArrayList<MyBaseShape>(); //要绘制的图形集合

  public DrawUtils() {
    rectBounds = new DashPathEffect(new float[]{3, 12, 12, 24}, 0);
    mPath = new Path();
  }

  public void drawRect(MyRect myRect) {
    if (mCanvas == null) {
      return;
    }
    mCanvas.save();
    mCanvas.rotate(myRect.getTransform(), myRect.getX() + myRect.getWidth() / 2, myRect.getY() + myRect.getHeight() + 2);
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
    mCanvas.rotate(myEllipse.getTransform(), myEllipse.getCx(), myEllipse.getCy());
    mPath.reset();
    mPath.addOval(myEllipse.getRectF(), Path.Direction.CW);
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
//  saveGraphics(GraphicType.ELLIPSE, null, cx, cy, rx, ry, fill, stroke, stroke_width, transform);
  }

  public void drawLine(MyLine myLine) {
    if (mCanvas == null) {
      return;
    }
    mCanvas.save();
    mCanvas.rotate(myLine.getTransform(), (myLine.getX() + myLine.getSx()) / 2, (myLine.getY() + myLine.getSy()) / 2);
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
//  saveGraphics(GraphicType.LINE, null, x, y, sx, sy, fill, stroke, stroke_width, transform);
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
    mCanvas.save();
//    mCanvas.rotate();
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
    RectF rectF = computeBounds(mPath);
    myPath.setBounds(rectF);
    if (myPath.isSelected()) {
      drawRectBounds(rectF);
    }
    mCanvas.restore();

  }

  public void drawAll() {
    for (int i = 0; i < list.size(); i++) {
      MyBaseShape graphic = list.get(i);
      if (graphic instanceof MyRect) {
        drawRect((MyRect) graphic);
      } else if (graphic instanceof MyEllipse) {
        drawEllipse((MyEllipse) graphic);
      } else if (graphic instanceof MyPath) {
        drawPath((MyPath) graphic);
      } else if (graphic instanceof MyLine) {
        drawLine((MyLine) graphic);
      }
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

  private void saveGraphics(GraphicType type, Point[] points, int... info) {
    if (doc == null) {
      return;
    }
    Model model = doc.getModel();
    CollaborativeMap map = model.createMap(null);
    switch (type) {
      case RECT:
//      int x, int y, int width, int height, int fill, int stroke, int stroke_width, int transform
        if (info.length != 8) {
          break;
        }
        map.set("x", info[0]);
        map.set("y", info[1]);
        map.set("width", info[2]);
        map.set("height", info[3]);
        map.set("fill", info[4]);
        map.set("stroke", info[5]);
        map.set("stroke_width", info[6]);
        map.set("transform", info[7]);
        model.getRoot().set("rect", map);
        break;
      case LINE:
//      int x, int y, int sx, int sy, int fill, int stroke, int stroke_width, int transform
        if (info.length != 8) {
          break;
        }
        JsonArray array = Json.createArray();
        JsonArray startPoint = Json.createArray();
        JsonArray endPoint = Json.createArray();
        startPoint.push(info[0]);
        startPoint.push(info[1]);
        endPoint.push(info[2]);
        endPoint.push(info[3]);
        array.push(Json.createArray().push(info[0]).push(info[1]));
        array.push(Json.createArray().push(info[2]).push(info[3]));
        CollaborativeList line = model.createList(array);
        map.set("d", line);
        map.set("fill", info[4]);
        map.set("stroke", info[5]);
        map.set("stroke_width", info[6]);
        map.set("transform", info[7]);
        model.getRoot().set("path", map);
        break;
      case PATH:
//      int fill, int stroke, int stroke_width, int transform, Point... point
        if (info.length != 4 || points.length == 0) {
          break;
        }
        JsonArray pathArray = Json.createArray();
        CollaborativeList path = model.createList(pathArray);
        for (int i = 0; i < points.length; i++) {
          pathArray.push(Json.createArray().push(points[i].x).push(points[i].y));
        }
        map.set("d", path);
        map.set("fill", info[0]);
        map.set("stroke", info[1]);
        map.set("stroke_width", info[2]);
        map.set("transform", info[3]);
        model.getRoot().set("path", map);
        break;
      case ELLIPSE:
//      int cx, int cy, int rx, int ry, int fill, int stroke, int stroke_width, int transform
        if (info.length != 8) {
          break;
        }
        map.set("cx", info[0]);
        map.set("cy", info[1]);
        map.set("rx", info[2]);
        map.set("ry", info[3]);
        map.set("fill", info[4]);
        map.set("stroke", info[5]);
        map.set("stroke_width", info[6]);
        map.set("transform", info[7]);
        model.getRoot().set("ellipse", map);
        break;
    }
  }

  public void setCanvas(Canvas mCanvas) {
    this.mCanvas = mCanvas;
  }

  public List<MyBaseShape> getShapeList() {
    return list;
  }
}
