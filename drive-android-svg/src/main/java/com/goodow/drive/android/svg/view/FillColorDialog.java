package com.goodow.drive.android.svg.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;

import com.goodow.drive.android.svg.R;
import com.goodow.drive.android.svg.SvgMainActivity;
import com.google.inject.Inject;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * Created by liudenghui on 14-7-13.
 */
public class FillColorDialog {
  private AlertDialog alertDialog;
  private ColorPicker picker;

  @Inject
  public FillColorDialog(Context context) {
    init(context);
  }

  private void init(Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("填充颜色");
    View view = View.inflate(context, R.layout.dialog_stroke_color, null);
    picker = (ColorPicker) view.findViewById(R.id.picker);
    SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
    picker.addSVBar(svBar);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SvgMainActivity.defaultFillColor = picker.getColor();
      }
    });
    builder.setNeutralButton("不填充", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SvgMainActivity.defaultFillColor = 0;
        picker.setColor(Color.RED);
        picker.setOldCenterColor(Color.TRANSPARENT);
        picker.setNewCenterColor(Color.TRANSPARENT);
        alertDialog.dismiss();
      }
    });
    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        alertDialog.dismiss();
      }
    });
    builder.setView(view);
    alertDialog = builder.create();

  }

  public void show() {
    alertDialog.show();
    picker.setColor(SvgMainActivity.defaultFillColor == 0 ? Color.RED : SvgMainActivity.defaultFillColor);
    picker.setNewCenterColor(SvgMainActivity.defaultFillColor == 0 ? Color.TRANSPARENT : SvgMainActivity.defaultFillColor);
    picker.setOldCenterColor(SvgMainActivity.defaultFillColor == 0 ? Color.TRANSPARENT : SvgMainActivity.defaultFillColor);
  }
}
