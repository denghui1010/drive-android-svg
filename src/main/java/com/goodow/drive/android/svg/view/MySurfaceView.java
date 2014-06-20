package com.goodow.drive.android.svg.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.goodow.drive.android.svg.OnRemoteChangeListener;
import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.drive.android.svg.utils.DrawUtil;
import com.goodow.drive.android.svg.utils.ParseUtil;
import com.goodow.drive.android.svg.utils.SwitchUtil;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.Document;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    if(selectType == Select.MOVE){
      moveShape(event);
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
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        int dX = currentX - startX;
        int dY = currentY - startY;
        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.WHITE);
        mDrawUtil.setCanvas(canvas);
        mDrawUtil.drawAll();
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
          mDrawUtil.drawRect(myRect);
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
          oval.setFill(0);
          oval.setStroke(Color.CYAN);
          oval.setStroke_width(5);
          oval.setTransform(0);
          mDrawUtil.drawEllipse(oval);
        } else if (selectType == Select.ELLIPSE) {
          MyEllipse myEllipse = (MyEllipse) graphic;
          myEllipse.setCx(startX + dX / 2);
          myEllipse.setCy(startY + dY / 2);
          myEllipse.setRx(Math.abs(dX / 2));
          myEllipse.setRy(Math.abs(dY / 2));
          myEllipse.getRectF().set(startX, startY, currentX, currentY);
          myEllipse.setFill(Color.YELLOW);
          myEllipse.setStroke(Color.RED);
          myEllipse.setStroke_width(5);
          myEllipse.setTransform(0);
          mDrawUtil.drawEllipse(myEllipse);
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
          mDrawUtil.drawLine(myLine);
        } else if (selectType == Select.PATH) {
          MyPath myPath = (MyPath) graphic;
          Point point = new Point();
          point.set((int) event.getX(), (int) event.getY());
          myPath.addPoint(point);
          myPath.setFill(0);
          myPath.setStroke(Color.RED);
          myPath.setStroke_width(3);
          myPath.setTransform(0);
          mDrawUtil.drawPath(myPath);
        } else if (selectType == Select.SWITCH) {
          mDrawUtil.drawDashRect(startX, startY, currentX, currentY);
        }
        mHolder.unlockCanvasAndPost(canvas);
        break;
      case MotionEvent.ACTION_UP:
        if (graphic != null) {
          shapeList.add(graphic);
          collList.add(mParseUtil.parseShape2cmap(doc, graphic));
        }
        if (selectType == Select.SWITCH) {
          mSwitchUtil.switchShape(shapeList, startX, startY, currentX, currentY);
          updateShapes();
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
        shapeList.set(collList.indexOf(map), mParseUtil.parseCmap2shape(map));
        updateShapes();
      }
    });
  }

  public void setDocument(final Document doc) {
    this.doc = doc;
    mParseUtil.parseDoc2map(doc, collList, shapeList);
    updateShapes();
  }

  public void updateShapes() {
    Canvas canvas = mHolder.lockCanvas();
    mDrawUtil.setCanvas(canvas);
    canvas.drawColor(Color.WHITE);
    mDrawUtil.drawAll();
    mHolder.unlockCanvasAndPost(canvas);
  }

  public void setShape2mvoe(MyBaseShape shape){
    currShape = shape;
  }

  private void moveShape(MotionEvent event){
    switch (event.getAction()){
      case MotionEvent.ACTION_DOWN:
        startX = (int) event.getX();
        startY = (int) event.getY();
        break;
      case MotionEvent.ACTION_MOVE:
        currentX = (int) event.getX();
        currentY = (int) event.getY();
        int dX = currentX - startX;
        int dY = currentY - startY;
        translatePoint(dX, dY);
        View popupMenuBtn = currShape.getPopupMenuBtn();
        updateShapes();
        startX = currentX;
        startY = currentY;
        break;
      case MotionEvent.ACTION_UP:
        break;
    }
  }

  private void translatePoint(int dX, int dY){
    if(currShape instanceof MyEllipse){
      MyEllipse myEllipse = (MyEllipse) currShape;
      myEllipse.setCx(myEllipse.getCx() + dX);
      myEllipse.setCy(myEllipse.getCy() + dY);
    }
  }

}
