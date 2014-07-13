package com.goodow.drive.android.svg.view;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.View;
import android.widget.ImageView;
import android.widget.NumberPicker;

import com.goodow.drive.android.svg.R;
import com.goodow.drive.android.svg.SvgMainActivity;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Created by liudenghui on 14-7-12.
 */
@Singleton
public class StrokeWidthDialog {
  private AlertDialog alertDialog;
  private NumberPicker numberPicker;
  private int oldValue;
  private ImageView sample;
  private Paint mPaint;
  private Canvas canvas;
  private Bitmap bitmap;
  private Path path;

  @Inject
  public StrokeWidthDialog(Context context) {
    init(context);
    initPaint();
    addListener();
  }

  private void init(Context context) {
    AlertDialog.Builder builder = new AlertDialog.Builder(context);
    View view = View.inflate(context, R.layout.dialog_stroke_width, null);
    numberPicker = (NumberPicker) view.findViewById(R.id.bp_numpicker);
    sample = (ImageView) view.findViewById(R.id.iv_sample);
    numberPicker.setMaxValue(20);
    numberPicker.setMinValue(1);
    builder.setTitle("画笔粗细");
    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        SvgMainActivity.defaultStrokeWidth = numberPicker.getValue();
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
    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
      @Override
      public void onShow(DialogInterface dialog) {
        if (bitmap == null) {
          bitmap = Bitmap.createBitmap(sample.getWidth(), sample.getHeight(), Bitmap.Config.ARGB_8888);
          canvas = new Canvas(bitmap);
          path.addCircle(sample.getWidth() / 2, sample.getHeight() / 2, sample.getWidth() > sample.getHeight() ? sample.getHeight() / 2 - 30 : sample.getWidth() / 2 - 30, Path.Direction.CW);
        }
        mPaint.setColor(SvgMainActivity.defaultStrokeColor);
        mPaint.setStrokeWidth(SvgMainActivity.defaultStrokeWidth);
        bitmap.eraseColor(Color.TRANSPARENT);
        canvas.drawPath(path, mPaint);
        sample.setImageBitmap(bitmap);
      }
    });
    alertDialog.show();
    oldValue = numberPicker.getValue();
    numberPicker.setValue(SvgMainActivity.defaultStrokeWidth);
  }

  private void initPaint() {
    mPaint = new Paint();
    mPaint.setAntiAlias(true);
    mPaint.setStyle(Paint.Style.STROKE);
    mPaint.setStrokeJoin(Paint.Join.ROUND);// 设置外边缘
    mPaint.setStrokeCap(Paint.Cap.SQUARE);// 形状
    mPaint.setStrokeWidth(numberPicker.getValue());// 画笔宽度
    mPaint.setColor(Color.RED);
    path = new Path();
  }

  private void addListener() {
    numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
      @Override
      public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
        mPaint.setStrokeWidth(newVal);
        bitmap.eraseColor(Color.TRANSPARENT);
        canvas.drawPath(path, mPaint);
        sample.setImageBitmap(bitmap);
      }
    });
  }


}
