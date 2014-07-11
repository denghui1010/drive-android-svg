package com.goodow.drive.android.svg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.goodow.drive.android.svg.OnRemoteChangeListener;
import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.drive.android.svg.utils.DrawUtil;
import com.goodow.drive.android.svg.utils.ParseUtil;
import com.goodow.drive.android.svg.utils.SwitchUtil;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.store.CollaborativeList;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.ObjectChangedEvent;
import com.goodow.realtime.store.ValuesAddedEvent;
import com.goodow.realtime.store.ValuesRemovedEvent;

import java.util.List;

/**
 * 用于绘图的surfaceview
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

  public static enum Select {RECT, ELLIPSE, OVAL, LINE, PATH, SWITCH, MOVE, ROTATE}

  public static Select selectType;
  private int startX;
  private int startY;
  private int startRotate;
  private int currentX;
  private int currentY;
  private MyBaseShape graphic;
  private List<MyBaseShape> shapeList;
  private List<CollaborativeMap> collList;
  private boolean isCanDraw;
  private SwitchUtil mSwitchUtil;
  private DrawUtil mDrawUtil;
  private ParseUtil mParseUtil;
  private Document doc;
  private MyBaseShape currShape;

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
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (!isCanDraw) {
      return true;
    }
    if (selectType == Select.MOVE) {
      moveShape(event);
      return true;
    }
    if (selectType == Select.ROTATE) {
      rotateShape(event);
      return true;
    }
    if (selectType == Select.SWITCH) {
      switchShape(event);
      return true;
    }
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        if (selectType == Select.RECT) {
          graphic = new MyRect();
          graphic.setType("rect");
        } else if (selectType == Select.OVAL || selectType == Select.ELLIPSE) {
          graphic = new MyEllipse();
          graphic.setType("ellipse");
        } else if (selectType == Select.LINE) {
          graphic = new MyLine();
          graphic.setType("line");
        } else if (selectType == Select.PATH) {
          graphic = new MyPath();
          graphic.setType("path");
        } else {
          graphic = null;
        }
        if (graphic != null) {
          shapeList.add(graphic);
        }
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        int dX = currentX - startX;
        int dY = currentY - startY;
        if (selectType == Select.RECT) {
          MyRect myRect = (MyRect) graphic;
          myRect.setX(startX);
          myRect.setY(startY);
          myRect.setWidth(dX);
          myRect.setHeight(dY);
        } else if (selectType == Select.OVAL) {
          MyEllipse oval = (MyEllipse) graphic;
          int cenX = startX + dX / 2;
          int cenY = startY + dY / 2;
          int r = (int) Math.sqrt((cenX - startX) * (cenX - startX) + (cenY - startY) * (cenY - startY));
          oval.setCx(cenX);
          oval.setCy(cenY);
          oval.setRx(r);
          oval.setRy(r);
          oval.getRectF().set(cenX - r, cenY - r, cenX + r, cenY + r);
        } else if (selectType == Select.ELLIPSE) {
          MyEllipse myEllipse = (MyEllipse) graphic;
          myEllipse.setCx(startX + dX / 2);
          myEllipse.setCy(startY + dY / 2);
          myEllipse.setRx(Math.abs(dX / 2));
          myEllipse.setRy(Math.abs(dY / 2));
          myEllipse.getRectF().set(startX, startY, currentX, currentY);
        } else if (selectType == Select.LINE) {
          MyLine myLine = (MyLine) graphic;
          myLine.setX(startX);
          myLine.setY(startY);
          myLine.setSx(startX + dX);
          myLine.setSy(startY + dY);
        } else if (selectType == Select.PATH) {
          MyPath myPath = (MyPath) graphic;
          Point point = new Point();
          point.set((int) event.getX(), (int) event.getY());
          myPath.addPoint(point);
        }
        if (graphic != null) {
          graphic.setFill(0);
          graphic.setStroke(Color.RED);
          graphic.setStroke_width(3);
          graphic.setRotate(0);
          updateShapes();
        }
        break;
      case MotionEvent.ACTION_UP:
        if (graphic != null) {
          collList.add(mParseUtil.parseShape2cmap(doc, graphic));
        }
        break;
    }
    return true;
  }

  public void setCanDraw(boolean isCanDraw) {
    this.isCanDraw = isCanDraw;
  }

  public void setUtils(DrawUtil drawUtil, SwitchUtil switchUtil, ParseUtil parseUtil) {
    mDrawUtil = drawUtil;
    mSwitchUtil = switchUtil;
    mParseUtil = parseUtil;
    shapeList = drawUtil.getShapeList();
    collList = drawUtil.getCollList();
    parseUtil.setOnRemoteChangeListener(new OnRemoteChangeListener() {
      @Override
      public void onRemoteChange(CollaborativeMap map) {
        mParseUtil.parseCmap2shape(map, shapeList.get(collList.indexOf(map)));
        updateShapes();
      }
    });
  }

  public void setDocument(final Document doc) {
    this.doc = doc;
    mParseUtil.parseDoc2map(doc, collList, shapeList);
    updateShapes();
    final CollaborativeList data = doc.getModel().getRoot().get("data");
    data.onValuesAdded(new Handler<ValuesAddedEvent>() {
      @Override
      public void handle(ValuesAddedEvent valuesAddedEvent) {
        if (!valuesAddedEvent.isLocal()) {
          JsonArray values = valuesAddedEvent.values();
          for (int i = 0; i < values.length(); i++) {
            final CollaborativeMap map = values.get(i);
            map.onObjectChanged(new Handler<ObjectChangedEvent>() {
              @Override
              public void handle(ObjectChangedEvent objectChangedEvent) {
                if (!objectChangedEvent.isLocal()) {
                  mParseUtil.parseCmap2shape(map, shapeList.get(collList.indexOf(map)));
                  updateShapes();
                }
              }
            });
            MyBaseShape shape = mParseUtil.parseCmap2shape(map);
            shapeList.add(shape);
            collList.add(map);
            updateShapes();
          }
        }
      }
    });
    data.onValuesRemoved(new Handler<ValuesRemovedEvent>() {
      @Override
      public void handle(ValuesRemovedEvent valuesRemovedEvent) {
        if (!valuesRemovedEvent.isLocal()) {
          JsonArray values = valuesRemovedEvent.values();
          for (int i = 0; i < values.length(); i++) {
            CollaborativeMap map = values.get(i);
            int j = collList.indexOf(map);
            MyBaseShape shape = shapeList.get(j);
            ((ViewGroup) MySurfaceView.this.getParent()).removeView(shape.getPopupMenuBtn());
            shape.setPopupMenuBtn(null);
            collList.remove(j);
            shapeList.remove(j);
            updateShapes();
          }
        }
      }
    });
  }

  public void updateShapes() {
    Canvas canvas = mHolder.lockCanvas();
    mDrawUtil.setCanvas(canvas);
    canvas.drawColor(Color.WHITE);
    mDrawUtil.drawAll();
    mHolder.unlockCanvasAndPost(canvas);
  }

  public void setCurrentShape(MyBaseShape shape) {
    currShape = shape;
  }

  private void switchShape(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        Canvas canvas = mHolder.lockCanvas();
        mDrawUtil.setCanvas(canvas);
        canvas.drawColor(Color.WHITE);
        mDrawUtil.drawAll();
        mDrawUtil.drawSwitchBoundsRect(startX, startY, currentX, currentY);
        mHolder.unlockCanvasAndPost(canvas);
        break;
      case MotionEvent.ACTION_UP:
        mSwitchUtil.switchShape(shapeList, startX, startY, currentX, currentY);
        updateShapes();
        break;
    }
  }

  private void moveShape(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        int dX = currentX - startX;
        int dY = currentY - startY;
        translateMyBaseShape(dX, dY);
        View popupMenuBtn = currShape.getPopupMenuBtn();
        RectF bounds = currShape.getBounds();
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) popupMenuBtn.getLayoutParams();
        layoutParams.bottomMargin = getHeight() - (int) bounds.top + dY;
        layoutParams.rightMargin = getWidth() - (int) bounds.right + dX;
        popupMenuBtn.setLayoutParams(layoutParams);
        updateShapes();
        startX = currentX;
        startY = currentY;
        break;
      case MotionEvent.ACTION_UP:
        translateCollMap();
        break;
    }
  }

  private void rotateShape(MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        startRotate = currShape.getRotate();
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        RectF bounds = currShape.getBounds();
        float centerX = bounds.centerX();
        float centerY = bounds.centerY();
        double d = Math.atan((double) (currentY - centerY) / (currentX - centerX));
        double s = Math.atan((double) (startY - centerY) / (startX - centerX));
        int degrees = (int) Math.toDegrees(d - s);
        currShape.setRotate((startRotate + degrees) % 360);
        updateShapes();
        break;
      case MotionEvent.ACTION_UP:
        int i = shapeList.indexOf(currShape);
        CollaborativeMap collaborativeMap = collList.get(i);
        collaborativeMap.set("rotate", currShape.getRotate());
        break;
    }
  }

  private void translateMyBaseShape(int dX, int dY) {
    if (currShape instanceof MyEllipse) {
      MyEllipse myEllipse = (MyEllipse) currShape;
      myEllipse.setCx(myEllipse.getCx() + dX);
      myEllipse.setCy(myEllipse.getCy() + dY);
    } else if (currShape instanceof MyRect) {
      MyRect myRect = (MyRect) currShape;
      myRect.setX(myRect.getX() + dX);
      myRect.setY(myRect.getY() + dY);
    } else if (currShape instanceof MyLine) {
      MyLine myLine = (MyLine) currShape;
      myLine.setX(myLine.getX() + dX);
      myLine.setY(myLine.getY() + dY);
      myLine.setSx(myLine.getSx() + dX);
      myLine.setSy(myLine.getSy() + dY);
    } else if (currShape instanceof MyPath) {
      MyPath myPath = (MyPath) currShape;
      List<Point> points = myPath.getPoints();
      for (Point point : points) {
        point.set(point.x + dX, point.y + dY);
      }
    }
  }

  private void translateCollMap() {
    int i = shapeList.indexOf(currShape);
    CollaborativeMap collaborativeMap = collList.get(i);
    if (currShape instanceof MyEllipse) {
      MyEllipse myEllipse = (MyEllipse) currShape;
      collaborativeMap.set("cx", myEllipse.getCx());
      collaborativeMap.set("cy", myEllipse.getCy());
    } else if (currShape instanceof MyRect) {
      MyRect myRect = (MyRect) currShape;
      collaborativeMap.set("x", myRect.getX());
      collaborativeMap.set("y", myRect.getY());
    } else if (currShape instanceof MyLine) {
      MyLine myLine = (MyLine) currShape;
      CollaborativeList d = collaborativeMap.get("d");
      d.clear();
      d.push(Json.createArray().push(myLine.getX()).push(myLine.getY()));
      d.push(Json.createArray().push(myLine.getSx()).push(myLine.getSy()));
    } else if (currShape instanceof MyPath) {
      MyPath myPath = (MyPath) currShape;
      CollaborativeList d = collaborativeMap.get("d");
      d.clear();
      for (int j = 0; j < myPath.getPoints().size(); j++) {
        d.push(Json.createArray().push(myPath.getPoints().get(j).x).push(myPath.getPoints().get(j).y));
      }
    }
  }

  public void deleteShape(MyBaseShape shape) {
    int i = shapeList.indexOf(shape);
    shapeList.remove(i);
    collList.remove(i);
    CollaborativeList data = doc.getModel().getRoot().get("data");
    data.remove(i);
    updateShapes();
  }

}
