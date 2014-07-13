package com.goodow.drive.android.svg.utils;

import android.graphics.Point;

import com.goodow.drive.android.svg.OnRemoteChangeListener;
import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.graphics.MyEllipse;
import com.goodow.drive.android.svg.graphics.MyLine;
import com.goodow.drive.android.svg.graphics.MyPath;
import com.goodow.drive.android.svg.graphics.MyRect;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.json.Json;
import com.goodow.realtime.json.JsonArray;
import com.goodow.realtime.store.CollaborativeList;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.ObjectChangedEvent;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created by liudenghui on 14-6-16.
 */
@Singleton
public class ParseUtil {
  private OnRemoteChangeListener listener;
  @Inject
  private CoordinateUtil coordinateUtil;

  public CollaborativeMap parseShape2cmap(Document doc, MyBaseShape shape) {
    if (doc == null) {
      return null;
    }
    Model model = doc.getModel();
    CollaborativeList data = model.getRoot().get("data");
    final CollaborativeMap map = model.createMap(null);
    if (shape instanceof MyRect) {
      MyRect rect = (MyRect) shape;
      map.set("x", coordinateUtil.translateX2proportion(rect.getX()));
      map.set("y", coordinateUtil.translateY2proportion(rect.getY()));
      map.set("width", coordinateUtil.translateX2proportion(rect.getWidth()));
      map.set("height", coordinateUtil.translateY2proportion(rect.getHeight()));
    } else if (shape instanceof MyLine) {
      MyLine line = (MyLine) shape;
      JsonArray array = Json.createArray();
      array.push(Json.createArray().push(coordinateUtil.translateX2proportion(line.getX())).push(coordinateUtil.translateY2proportion(line.getY())));
      array.push(coordinateUtil.translateX2proportion(line.getSx())).push(coordinateUtil.translateY2proportion(line.getSy()));
      CollaborativeList list = model.createList(array);
      map.set("d", list);
    } else if (shape instanceof MyPath) {
      MyPath path = (MyPath) shape;
      JsonArray pathArray = Json.createArray();
      for (int i = 0; i < path.getPoints().size(); i++) {
        pathArray.push(Json.createArray().push(coordinateUtil.translateX2proportion(path.getPoints().get(i).x)).push(coordinateUtil.translateY2proportion(path.getPoints().get(i).y)));
      }
      CollaborativeList list = model.createList(pathArray);
      map.set("d", list);
    } else if (shape instanceof MyEllipse) {
      MyEllipse ellipse = (MyEllipse) shape;
      map.set("cx", coordinateUtil.translateX2proportion(ellipse.getCx()));
      map.set("cy", coordinateUtil.translateY2proportion(ellipse.getCy()));
      map.set("rx", coordinateUtil.translateX2proportion(ellipse.getRx()));
      map.set("ry", coordinateUtil.translateY2proportion(ellipse.getRy()));
    }
    map.set("fill", shape.getFill());
    map.set("stroke", shape.getStroke());
    map.set("stroke_width", shape.getStroke_width());
    map.set("rotate", shape.getRotate());
    map.set("type", shape.getType());
    map.onObjectChanged(new Handler<ObjectChangedEvent>() {
      @Override
      public void handle(ObjectChangedEvent objectChangedEvent) {
        if (!objectChangedEvent.isLocal()) {
          listener.onRemoteChange(map);
        }
      }
    });
    data.push(map);
    return map;
  }

  public void parseDoc2map(Document doc, List<CollaborativeMap> collList, List<MyBaseShape> shapeList) {
    CollaborativeList data = doc.getModel().getRoot().get("data");
    for (int i = 0; i < data.length(); i++) {
      final CollaborativeMap map = data.get(i);
      map.onObjectChanged(new Handler<ObjectChangedEvent>() {
        @Override
        public void handle(ObjectChangedEvent objectChangedEvent) {
          if (!objectChangedEvent.isLocal()) {
            listener.onRemoteChange(map);
          }
        }
      });
      shapeList.add(parseCmap2shape(map));
      collList.add(map);
    }
  }

  public MyBaseShape parseCmap2shape(CollaborativeMap map) {
    return parseCmap2shape(map, null);
  }

  public MyBaseShape parseCmap2shape(CollaborativeMap map, MyBaseShape shape) {
    String type = map.get("type");
    if (type.equals("rect")) {
      if (shape == null) {
        shape = new MyRect();
      }
      MyRect myRect = (MyRect) shape;
      myRect.setX(coordinateUtil.translateX2local((Double) map.get("x")));
      myRect.setY(coordinateUtil.translateY2local((Double) map.get("y")));
      myRect.setWidth(coordinateUtil.translateX2local((Double) map.get("width")));
      myRect.setHeight(coordinateUtil.translateY2local((Double) map.get("height")));
    } else if (type.equals("line")) {
      if (shape == null) {
        shape = new MyLine();
      }
      MyLine myLine = (MyLine) shape;
      CollaborativeList d = map.get("d");
      JsonArray start = d.get(0);
      JsonArray stop = d.get(1);
      myLine.setX(coordinateUtil.translateX2local((Double) start.get(0)));
      myLine.setY(coordinateUtil.translateY2local((Double) start.get(1)));
      myLine.setSx(coordinateUtil.translateX2local((Double) stop.get(0)));
      myLine.setSy(coordinateUtil.translateY2local((Double) stop.get(1)));
    } else if (type.equals("path")) {
      if (shape == null) {
        shape = new MyPath();
      }
      MyPath myPath = (MyPath) shape;
      CollaborativeList d = map.get("d");
      myPath.getPoints().clear();
      for (int j = 0; j < d.length(); j++) {
        JsonArray point = d.get(j);
        myPath.addPoint(new Point(coordinateUtil.translateX2local(point.getNumber(0)), coordinateUtil.translateY2local(point.getNumber(1))));
      }
    } else if (type.equals("ellipse")) {
      if (shape == null) {
        shape = new MyEllipse();
      }
      MyEllipse myEllipse = (MyEllipse) shape;
      myEllipse.setCx(coordinateUtil.translateX2local((Double) map.get("cx")));
      myEllipse.setCy(coordinateUtil.translateY2local((Double) map.get("cy")));
      myEllipse.setRx(coordinateUtil.translateX2local((Double) map.get("rx")));
      myEllipse.setRy(coordinateUtil.translateY2local((Double) map.get("ry")));
    } else {
      return null;
    }
    shape.setType(type);
    Double fill = map.get("fill");
    Double stroke = map.get("stroke");
    Double stroke_width = map.get("stroke_width");
    Double rotate = map.get("rotate");
    shape.setFill(fill.intValue());
    shape.setStroke(stroke.intValue());
    shape.setStroke_width(stroke_width.intValue());
    shape.setRotate(rotate.intValue());
    return shape;
  }

  public void setOnRemoteChangeListener(OnRemoteChangeListener listener) {
    this.listener = listener;
  }

}
