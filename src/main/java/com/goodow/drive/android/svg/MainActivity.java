package com.goodow.drive.android.svg;

import com.goodow.realtime.core.Handler;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.Store;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.WindowManager;
import roboguice.inject.ContentView;

@ContentView(R.layout.activity_main)
public class MainActivity extends Activity {
  // protected final Bus bus = BusProvider.get();
  private Registration drawController;
  private int screenWidth;
  private int screenHeight;

  private Store store;
  private Document doc;
  private static final String ID = "ldh/svg";

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    store = StoreProvider.get();
    getScreenSize();
    // initCanvas();
    // drawRect(100, 100, 100, 100, Color.GRAY, Color.BLUE, 2, 30);
    Point point1 = new Point();
    point1.x = 600;
    point1.y = 100;
    Point point2 = new Point();
    point2.x = 650;
    point2.y = 150;
    Point point3 = new Point();
    point3.x = 670;
    point3.y = 180;
    Point point4 = new Point();
    point4.x = 750;
    point4.y = 150;
    Point point5 = new Point();
    point5.x = 800;
    point5.y = 100;
    // drawPath(0, Color.RED, 3, 0, point1, point2, point3, point4, point5);

  }

  // @Override
  // protected void onResume() {
  // super.onResume();
  // if (bus.getReadyState() == State.CLOSED || bus.getReadyState() == State.CLOSING) {
  // Log.w("EventBus Status", bus.getReadyState().name());
  // BusProvider.reconnect();
  // }
  // drawController = bus.registerLocalHandler("",new MessageHandler<JsonObject>(){
  // @Override
  // public void handle(Message<JsonObject> jsonObjectMessage) {
  //
  // }
  // });
  // }

  // @Override
  // protected void onPause() {
  // super.onPause();
  // drawController.unregister();
  // }
  //
  // private void initCanvas() {
  // Bitmap bmp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
  // mCanvas = new Canvas(bmp);
  // ImageView view = (ImageView) findViewById(R.id.view);
  // view.setImageBitmap(bmp);
  // }

  @Override
  protected void onPause() {
    super.onResume();
    if (doc != null) {
      doc.close();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Handler<Document> onLoadHandler = new Handler<Document>() {
      @Override
      public void handle(Document document) {
        doc = document;
        // drawEllipse(300, 300, 100, 50, Color.YELLOW, Color.RED, 5, 50);
        // drawEllipse(300, 300, 100, 100, 0, Color.CYAN, 5, 0);
        // drawLine(400, 100, 500, 200, 0, Color.RED, 3, 0);

      }
    };
    Handler<Model> initHandler = new Handler<Model>() {
      @Override
      public void handle(Model model) {

      }
    };
    store.load(ID, onLoadHandler, null, null);
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
}
