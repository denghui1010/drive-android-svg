package com.goodow.drive.android.svg;

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

/**
 * Created by liudenghui on 14-6-16.
 */
public class SaveUtils {
  public static void saveGraphics(Document doc, MyBaseShape shape) {
    if (doc == null) {
      return;
    }
    Model model = doc.getModel();
    CollaborativeList data = model.getRoot().get("data");
    CollaborativeMap map = model.createMap(null);
    if (shape instanceof MyRect) {
      MyRect rect = (MyRect) shape;
      map.set("x", rect.getX());
      map.set("y", rect.getY());
      map.set("width", rect.getWidth());
      map.set("height", rect.getHeight());
      map.set("fill", rect.getFill());
      map.set("stroke", rect.getStroke());
      map.set("stroke_width", rect.getStroke_width());
      map.set("transform", rect.getTransform());
    } else if (shape instanceof MyLine) {
      MyLine line = (MyLine) shape;
      JsonArray array = Json.createArray();
      array.push(Json.createArray().push(line.getX()).push(line.getY()));
      array.push(Json.createArray().push(line.getSx()).push(line.getSy()));
      CollaborativeList list = model.createList(array);
      map.set("d", list);
      map.set("fill", line.getFill());
      map.set("stroke", line.getStroke());
      map.set("stroke_width", line.getStroke_width());
      map.set("transform", line.getStroke_width());
    } else if (shape instanceof MyPath) {
      MyPath path = (MyPath) shape;
      JsonArray pathArray = Json.createArray();
      CollaborativeList list = model.createList(pathArray);
      for (int i = 0; i < path.getPoints().size(); i++) {
        pathArray.push(Json.createArray().push(path.getPoints().get(i).x).push(path.getPoints().get(i).y));
      }
      map.set("d", list);
      map.set("fill", path.getFill());
      map.set("stroke", path.getStroke());
      map.set("stroke_width", path.getStroke_width());
      map.set("transform", path.getTransform());
    } else if (shape instanceof MyEllipse) {
      MyEllipse ellipse = (MyEllipse) shape;
      map.set("cx", ellipse.getCx());
      map.set("cy", ellipse.getCy());
      map.set("rx", ellipse.getRx());
      map.set("ry", ellipse.getRy());
      map.set("fill", ellipse.getFill());
      map.set("stroke", ellipse.getStroke());
      map.set("stroke_width", ellipse.getStroke_width());
      map.set("transform", ellipse.getTransform());
    }
    data.push(map);
  }
}
