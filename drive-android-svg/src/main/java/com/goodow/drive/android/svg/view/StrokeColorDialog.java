package com.goodow.drive.android.svg.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;

import com.goodow.drive.android.svg.R;
import com.goodow.drive.android.svg.SvgMainActivity;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.SVBar;

/**
 * Created by liudenghui on 14-7-12.
 */
@Singleton
public class StrokeColorDialog {

  private AlertDialog alertDialog;
  private ColorPicker picker;

  @Inject
  public StrokeColorDialog(Context context) {
    init(context);
  }

  private void init(Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    builder.setTitle("画笔颜色");
    View view = View.inflate(context, R.layout.dialog_stroke_color, null);
    picker = (ColorPicker) view.findViewById(R.id.picker);
    SVBar svBar = (SVBar) view.findViewById(R.id.svbar);
    picker.addSVBar(svBar);
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SvgMainActivity.defaultStrokeColor = picker.getColor();
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
    picker.setNewCenterColor(SvgMainActivity.defaultStrokeColor);
    picker.setOldCenterColor(SvgMainActivity.defaultStrokeColor);
    picker.setColor(SvgMainActivity.defaultStrokeColor);
  }

}
