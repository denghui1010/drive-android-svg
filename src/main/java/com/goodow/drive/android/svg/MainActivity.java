package com.goodow.drive.android.svg;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.WindowManager;
import android.widget.RadioGroup;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.Store;
import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;

import java.util.List;

import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {
  private int screenWidth;
  private int screenHeight;

  private Store store;
  private Document doc;
  private static final String ID = "ldh/svg";
  private MySurfaceView mView;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);
    initView();
    store = StoreProvider.get();
    loadDoc();
    getScreenSize();
  }

  @SuppressLint("NewApi")
  private void getScreenSize() {// 获得屏幕尺寸大小
    if (android.os.Build.VERSION.SDK_INT < 17) {
      DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
      screenWidth = displayMetrics.widthPixels;
      screenHeight = displayMetrics.heightPixels;
    } else {// api>17用新方式
      WindowManager wm = this.getWindowManager();
      DisplayMetrics outMetrics = new DisplayMetrics();
      wm.getDefaultDisplay().getRealMetrics(outMetrics);
      screenWidth = outMetrics.widthPixels;
      screenHeight = outMetrics.heightPixels;
    }
  }

  private void loadDoc() {
    Handler<Document> onLoadHandler = new Handler<Document>() {
      @Override
      public void handle(Document document) {
        doc = document;
      }
    };
    Handler<Model> initHandler = new Handler<Model>() {
      @Override
      public void handle(Model model) {
      }
    };
    store.load(ID, onLoadHandler, null, null);
  }

  private void initView() {
    mView = (MySurfaceView) findViewById(R.id.view);
    mView.setZOrderOnTop(true);
    RadioGroup rg_btn = (RadioGroup) findViewById(R.id.rg_btn);
    rg_btn.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId) {
          case R.id.rb_switch:
            MySurfaceView.selectType = MySurfaceView.Select.SWITCH;
            break;
          case R.id.rb_rect:
            MySurfaceView.selectType = MySurfaceView.Select.RECT;
            break;
          case R.id.rb_oval:
            MySurfaceView.selectType = MySurfaceView.Select.OVAL;
            break;
          case R.id.rb_ellipse:
            MySurfaceView.selectType = MySurfaceView.Select.ELLIPSE;
            break;
          case R.id.rb_line:
            MySurfaceView.selectType = MySurfaceView.Select.LINE;
            break;
          case R.id.rb_path:
            MySurfaceView.selectType = MySurfaceView.Select.PATH;
            break;
        }
        cancelSelected();
      }
    });
  }

  private void cancelSelected(){
    List<MyBaseShape> list = mView.getShapeList();
    for (int i = 0; i < list.size(); i++) {
      MyBaseShape shape = list.get(i);
      shape.setSelected(false);
    }
  }
}