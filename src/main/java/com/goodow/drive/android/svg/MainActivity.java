package com.goodow.drive.android.svg;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Bundle;
import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.channel.State;
import com.goodow.realtime.core.Registration;
import com.goodow.realtime.json.JsonObject;

public class MainActivity extends Activity {
//    protected final Bus bus = BusProvider.get();
    private Canvas mCanvas;
    private Registration drawController;
    private Paint paint = new Paint();
    private int screenWidth;
    private int screenHeight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
      getScreenSize();
      initCanvas();
      drawRect(100,100,100,100,Color.GRAY,Color.BLUE,2,0);
      drawEllipse(300,300,100,50,Color.YELLOW,Color.RED,5,0);
      drawEllipse(500,500,100,100,0,Color.CYAN,5,0);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (bus.getReadyState() == State.CLOSED || bus.getReadyState() == State.CLOSING) {
//            Log.w("EventBus Status", bus.getReadyState().name());
//            BusProvider.reconnect();
//        }
//        drawController = bus.registerLocalHandler("",new MessageHandler<JsonObject>(){
//            @Override
//            public void handle(Message<JsonObject> jsonObjectMessage) {
//
//            }
//        });
//    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        drawController.unregister();
//    }

    private void initCanvas() {
        Bitmap bmp = Bitmap.createBitmap(screenWidth, screenHeight, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bmp);
        ImageView view = (ImageView) findViewById(R.id.view);
        view.setImageBitmap(bmp);
    }

    private void drawRect(int x,int y,int width,int height,int fill,int stroke,int stroke_width,int transform){
        paint.setAntiAlias(true);
        if(stroke_width>0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawRect(x, y, x + width, y + height, paint);
        }
        if(fill !=0) {
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(fill);
            mCanvas.drawRect(x, y, x + width, y + height, paint);
        }
//      mCanvas.drawRect(x+150,y,x+150+width,y+height,paint);
    }

    private void drawEllipse(int cx,int cy,int rx,int ry,int fill,int stroke,int stroke_width,int transform){
        paint.setAntiAlias(true);
        RectF rect = new RectF();
        rect.left = cx-rx;
        rect.right = cx+rx;
        rect.top = cy-ry;
        rect.bottom = cy+ry;
        if(stroke_width>0){
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawOval(rect,paint);
        }
        if(fill!=0){
            paint.setColor(fill);
            paint.setStyle(Paint.Style.FILL);
            mCanvas.drawOval(rect,paint);
        }
    }

    private void drawPath(int x,int y,int sx,int sy,int fill, int stroke,int stroke_width,int transform){
        paint.setAntiAlias(true);
        Path path = new Path();
        path.moveTo(x,y);
        path.lineTo(sx,sy);
        if(stroke_width>0) {
            paint.setColor(stroke);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(stroke_width);
            mCanvas.drawPath(path,paint);
        }

    }

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
