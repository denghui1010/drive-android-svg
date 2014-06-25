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
import com.google.inject.Singleton;

import java.util.List;

/**
 * Created by liudenghui on 14-6-16.
 */
@Singleton
public class ParseUtil {
  private OnRemoteChangeListener listener;

  public CollaborativeMap parseShape2cmap(Document doc, MyBaseShape shape) {
    if (doc == null) {
      return null;
    }
    Model model = doc.getModel();
    CollaborativeList data = model.getRoot().get("data");
    final CollaborativeMap map = model.createMap(null);
    if (shape instanceof MyRect) {
      MyRect rect = (MyRect) shape;
      map.set("x", rect.getX());
      map.set("y", rect.getY());
      map.set("width", rect.getWidth());
      map.set("height", rect.getHeight());
    } else if (shape instanceof MyLine) {
      MyLine line = (MyLine) shape;
      JsonArray array = Json.createArray();
      array.push(Json.createArray().push(line.getX()).push(line.getY()));
      array.push(Json.createArray().push(line.getSx()).push(line.getSy()));
      CollaborativeList list = model.createList(array);
      map.set("d", list);
    } else if (shape instanceof MyPath) {
      MyPath path = (MyPath) shape;
      JsonArray pathArray = Json.createArray();
      CollaborativeList list = model.createList(pathArray);
      for (int i = 0; i < path.getPoints().size(); i++) {
        pathArray.push(Json.createArray().push(path.getPoints().get(i).x).push(path.getPoints().get(i).y));
      }
      map.set("d", list);
    } else if (shape instanceof MyEllipse) {
      MyEllipse ellipse = (MyEllipse) shape;
      map.set("cx", ellipse.getCx());
      map.set("cy", ellipse.getCy());
      map.set("rx", ellipse.getRx());
      map.set("ry", ellipse.getRy());
    }
    map.set("fill", shape.getFill());
    map.set("stroke", shape.getStroke());
    map.set("stroke_width", shape.getStroke_width());
    map.set("transform", shape.getTransform());
    map.set("type",shape.getType());
    map.onObjectChanged(new Handler<ObjectChangedEvent>() {
      @Override
      public void handle(ObjectChangedEvent objectChangedEvent) {
        if(!objectChangedEvent.isLocal()) {
          listener.onRemoteChange(map);
        }
      }
    });
    data.push(map);
    return map;
  }

  public void parseDoc2map(Document doc, List<CollaborativeMap> collList, List<MyBaseShape> shapeList){
    CollaborativeList data = doc.getModel().getRoot().get("data");
    for(int i=0; i<data.length(); i++){
      final CollaborativeMap map = data.get(i);
      shapeList.add(parseCmap2shape(map));
      collList.add(map);
      map.onObjectChanged(new Handler<ObjectChangedEvent>() {
        @Override
        public void handle(ObjectChangedEvent objectChangedEvent) {
          if(!objectChangedEvent.isLocal()) {
            listener.onRemoteChange(map);
          }
        }
      });
    }
  }

  public MyBaseShape parseCmap2shape(CollaborativeMap map){
    MyBaseShape shape;
    String type = map.get("type");
    if (type.equals("rect")) {
      shape = new MyRect();
      MyRect myRect = (MyRect) shape;
      Double x = map.get("x");
      Double y = map.get("y");
      Double width = map.get("width");
      Double height = map.get("height");
      myRect.setX(x.intValue());
      myRect.setY(y.intValue());
      myRect.setWidth(width.intValue());
      myRect.setHeight(height.intValue());
    } else if (type.equals("line")) {
      shape = new MyLine();
      MyLine myLine = (MyLine) shape;
      CollaborativeList d = map.get("d");
      JsonArray start = d.get(0);
      JsonArray stop = d.get(1);
      Double startX = start.get(0);
      Double startY = start.get(1);
      Double stopX = stop.get(0);
      Double stopY = stop.get(1);
      myLine.setX(startX.intValue());
      myLine.setY(startY.intValue());
      myLine.setSx(stopX.intValue());
      myLine.setSy(stopY.intValue());
    } else if (type.equals("path")) {
      shape = new MyPath();
      MyPath myPath = (MyPath) shape;
      CollaborativeList d = map.get("d");
      for (int j = 0; j < d.length(); j++) {
        JsonArray point = d.get(j);
        myPath.addPoint(new Point((int) point.getNumber(0), (int) point.getNumber(1)));
      }
    } else if (type.equals("ellipse")) {
      shape = new MyEllipse();
      MyEllipse myEllipse = (MyEllipse) shape;
      Double cx = map.get("cx");
      Double cy = map.get("cy");
      Double rx = map.get("rx");
      Double ry = map.get("ry");
      myEllipse.setCx(cx.intValue());
      myEllipse.setCy(cy.intValue());
      myEllipse.setRx(rx.intValue());
      myEllipse.setRy(ry.intValue());
    } else {
      return null;
    }
    shape.setType(type);
    Double fill = map.get("fill");
    Double stroke = map.get("stroke");
    Double stroke_width = map.get("stroke_width");
    Double transform = map.get("transform");
    shape.setFill(fill.intValue());
    shape.setStroke(stroke.intValue());
    shape.setStroke_width(stroke_width.intValue());
    shape.setTransform(transform.intValue());
    return shape;
  }

  public void setOnRemoteChangeListener(OnRemoteChangeListener listener){
    this.listener = listener;
  }

}
