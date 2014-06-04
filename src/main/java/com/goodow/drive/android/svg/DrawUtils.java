package com.goodow.drive.android.svg;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;

import com.goodow.drive.android.svg.graphics.BaseGraphic;
import com.goodow.drive.android.svg.graphics.Ellipse;
import com.goodow.drive.android.svg.graphics.Rect;
import com.goodow.realtime.store.CollaborativeList;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.Model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liudenghui on 14-6-4.
 */
public class DrawUtils {
    private Paint paint = new Paint();
    private Canvas mCanvas;
    private List<BaseGraphic> list = new ArrayList<BaseGraphic>();;//要绘制的图形集合
    private enum GraphicType {RECT, LINE, ELLIPSE, PATH}

    private void drawRect(int x, int y, int width, int height, int fill, int stroke, int stroke_width, int transform) {
        paint.setAntiAlias(true);
        mCanvas.save();
        mCanvas.rotate(transform, x + width / 2, y + height + 2);
        if (stroke_width > 0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawRect(x, y, x + width, y + height, paint);
        }
        if (fill != 0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(fill);
            mCanvas.drawRect(x, y, x + width, y + height, paint);
        }
        mCanvas.restore();
        Rect rect = new Rect();
        rect.setX(x);
        rect.setY(y);
        rect.setWidth(width);
        rect.setHeight(height);
        list.add(rect);
    }

    private void drawEllipse(int cx, int cy, int rx, int ry, int fill, int stroke, int stroke_width, int transform) {
        paint.setAntiAlias(true);
        RectF rect = new RectF();
        rect.left = cx - rx;
        rect.right = cx + rx;
        rect.top = cy - ry;
        rect.bottom = cy + ry;
        mCanvas.save();
        mCanvas.rotate(transform, cx, cy);
        if (stroke_width > 0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawOval(rect, paint);
        }
        if (fill != 0) {
            paint.setColor(fill);
            paint.setStyle(Paint.Style.FILL);
            mCanvas.drawOval(rect, paint);
        }
        mCanvas.restore();
        Ellipse ellipse = new Ellipse();
        ellipse.setGraphicNum(0);
        ellipse.setCx(cx);
        ellipse.setCy(cy);
        ellipse.setRx(rx);
        ellipse.setRy(ry);
        ellipse.setFill(fill);
        ellipse.setStroke(stroke);
        ellipse.setStroke_width(stroke_width);
        ellipse.setTransform(transform);
        list.add(ellipse);
//        saveGraphics(GraphicType.ELLIPSE, null, cx, cy, rx, ry, fill, stroke, stroke_width, transform);
    }

    private void drawLine(int x, int y, int sx, int sy, int fill, int stroke, int stroke_width, int transform) {
        paint.setAntiAlias(true);
        mCanvas.save();
        mCanvas.rotate(transform, (x + sx) / 2, (y + sy) / 2);
        if (stroke_width > 0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawLine(x, y, sx, sy, paint);
        }
        if (fill != 0) {
            paint.setColor(fill);
            paint.setStyle(Paint.Style.FILL);
            mCanvas.drawLine(x, y, sx, sy, paint);
        }
        mCanvas.restore();
        com.goodow.drive.android.svg.graphics.Path path = new com.goodow.drive.android.svg.graphics.Path();
        Point point1 = new Point();
        point1.set(x,y);
        Point point2 = new Point();
        point2.set(sx,sy);
        path.setPoint(new Point[]{point1,point2});
        list.add(path);
//        saveGraphics(GraphicType.LINE, null, x, y, sx, sy, fill, stroke, stroke_width, transform);
    }

    private void drawPath(int fill, int stroke, int stroke_width, int transform, Point... point) {
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);// 形状
        paint.setStrokeJoin(Paint.Join.ROUND);
        Path path = new Path();
        path.moveTo(point[0].x, point[0].y);
        for (int i = 1; i < point.length; i++) {
            path.quadTo((point[i - 1].x + point[i].x) / 2, (point[i - 1].y + point[i].y) / 2, point[i].x, point[i].y);
        }
        if (stroke_width > 0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawPath(path, paint);
        }
        mCanvas.save();
        if (stroke_width > 0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawPath(path, paint);
        }
        if (fill != 0) {
            paint.setColor(fill);
            paint.setStyle(Paint.Style.FILL);
            mCanvas.drawPath(path, paint);
        }
    }

//    private void saveGraphics(GraphicType type, Point[] points, int... info) {
////        if (doc == null) {
////            return;
////        }
////        Model model = doc.getModel();
////        CollaborativeMap map = model.createMap(null);
//        switch (type) {
//            case RECT:
////                int x, int y, int width, int height, int fill, int stroke, int stroke_width, int transform
//                if (info.length != 8) {
//                    break;
//                }
//                map.set("x", info[0]);
//                map.set("y", info[1]);
//                map.set("width", info[2]);
//                map.set("height", info[3]);
//                map.set("fill", info[4]);
//                map.set("stroke", info[5]);
//                map.set("stroke_width", info[6]);
//                map.set("transform", info[7]);
//                model.getRoot().set("rect", map);
//                break;
//            case LINE:
////                int x, int y, int sx, int sy, int fill, int stroke, int stroke_width, int transform
//                if (info.length != 8) {
//                    break;
//                }
//                CollaborativeList startPoint = model.createList(null);
//                startPoint.push(info[0]);
//                startPoint.push(info[1]);
//                CollaborativeList endPoint = model.createList(null);
//                endPoint.push(info[2]);
//                endPoint.push(info[3]);
//                CollaborativeList line = model.createList(startPoint, endPoint);
//                map.set("d", line);
//                map.set("fill", info[4]);
//                map.set("stroke", info[5]);
//                map.set("stroke_width", info[6]);
//                map.set("transform", info[7]);
//                model.getRoot().set("path", map);
//                break;
//            case PATH:
////                int fill, int stroke, int stroke_width, int transform, Point... point
//                if (info.length != 4 || points.length == 0) {
//                    break;
//                }
//                CollaborativeList path = model.createList();
//                for (int i = 0; i < points.length; i++) {
//                    CollaborativeList point = model.createList(null);
//                    point.push(points[i].x);
//                    point.push(points[i].y);
//                    path.push(point);
//                }
//                map.set("d", path);
//                map.set("fill", info[0]);
//                map.set("stroke", info[1]);
//                map.set("stroke_width", info[2]);
//                map.set("transform", info[3]);
//                model.getRoot().set("path", map);
//                break;
//            case ELLIPSE:
////                int cx, int cy, int rx, int ry, int fill, int stroke, int stroke_width, int transform
//                if (info.length != 8) {
//                    break;
//                }
//                map.set("cx", info[0]);
//                map.set("cy", info[1]);
//                map.set("rx", info[2]);
//                map.set("ry", info[3]);
//                map.set("fill", info[4]);
//                map.set("stroke", info[5]);
//                map.set("stroke_width", info[6]);
//                map.set("transform", info[7]);
//                model.getRoot().set("ellipse", map);
//                break;
//        }
//    }
}
