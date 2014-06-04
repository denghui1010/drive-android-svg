package com.goodow.drive.android.svg;

import com.goodow.drive.android.svg.graphics.BaseGraphic;
import com.goodow.drive.android.svg.graphics.Ellipse;

import java.util.List;

/**
 * Created by liudenghui on 14-5-27.
 */
public class GraphicSwitcher {
    private List<BaseGraphic> list;

    public GraphicSwitcher(List<BaseGraphic> list) {
        this.list = list;
    }

    public void switchByPoint(int x, int y) {
        for (BaseGraphic info : list) {
            if (info instanceof Ellipse) {
            }
        }
    }

}
