package com.goodow.drive.android.svg;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.drive.android.svg.utils.DrawUtil;
import com.goodow.drive.android.svg.utils.ParseUtil;
import com.goodow.drive.android.svg.utils.SwitchUtil;
import com.goodow.drive.android.svg.view.LeftMenuLayout;
import com.goodow.drive.android.svg.view.MyDrawable;
import com.goodow.drive.android.svg.view.MySurfaceView;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.Store;
import com.google.inject.Inject;

import roboguice.activity.RoboActivity;
import roboguice.inject.ContentView;
import roboguice.inject.InjectView;

public class MainActivity extends RoboActivity {

  @Inject
  private Store store;
  private Document doc;
  private static final String ID = "ldh/test";
  //  @InjectView(R.id.view)
  private MySurfaceView mySurfaceView;
  //  @InjectView(R.id.ll_menu_root)
  private LeftMenuLayout ll_menu_root;
  //  @InjectView(R.id.lv_menu_list)
  private ListView listView;
  //  @InjectView(R.id.pb_progress)
  private ProgressBar pb_progress;
  //  @InjectView(R.id.iv_btn)
  private ImageView iv_btn;
  //  @InjectView(R.id.surfaceview_root)
  private FrameLayout surfaceview_root;
  private ActionBar actionBar;

  @Inject
  private DrawUtil drawUtil;
  @Inject
  private ParseUtil parseUtil;
  @Inject
  private SwitchUtil switchUtil;

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case android.R.id.home:
        if (!ll_menu_root.isLeftMenuShown()) {
          ll_menu_root.showLeftMenu();
        } else {
          ll_menu_root.hideLeftMenu();
        }
        break;
    }
    return super.onOptionsItemSelected(item);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_MENU) {
      if (!ll_menu_root.isLeftMenuShown()) {
        ll_menu_root.showLeftMenu();
      } else {
        ll_menu_root.hideLeftMenu();
      }
      return true;
    } else if (keyCode == KeyEvent.KEYCODE_BACK) {
      if (ll_menu_root.isLeftMenuShown()) {
        ll_menu_root.hideLeftMenu();
        return true;
      }
      if (MySurfaceView.selectType == MySurfaceView.Select.MOVE
          || MySurfaceView.selectType == MySurfaceView.Select.ROTATE) {
        cancelSelected();
        MySurfaceView.selectType = MySurfaceView.Select.SWITCH;
        actionBar.setTitle("选择");
        return true;
      }
    }
    return super.onKeyDown(keyCode, event);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    this.setContentView(R.layout.activity_main);
    initView();
    initUtils();
    loadDoc();
  }

  private void loadDoc() {
    pb_progress.setVisibility(View.VISIBLE);
    Handler<Document> onLoadHandler = new Handler<Document>() {
      @Override
      public void handle(Document document) {
        doc = document;
        pb_progress.setVisibility(View.INVISIBLE);
        mySurfaceView.setCanDraw(true);
        mySurfaceView.setDocument(doc);
        actionBar.setTitle("画图工具箱");
      }
    };
    Handler<Model> initHandler = new Handler<Model>() {
      @Override
      public void handle(Model model) {
        model.getRoot().set("data", model.createList(null));
      }
    };
    store.load(ID, onLoadHandler, initHandler, null);
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
  private void initView() {
    mySurfaceView = (MySurfaceView) findViewById(R.id.view);
    ll_menu_root = (LeftMenuLayout) findViewById(R.id.ll_menu_root);
    listView = (ListView) findViewById(R.id.lv_menu_list);
    pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
    iv_btn = (ImageView) findViewById(R.id.iv_btn);
    surfaceview_root = (FrameLayout) findViewById(R.id.surfaceview_root);
    ll_menu_root.setControlButton(iv_btn);
    actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(false);
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
      MyDrawable myDrawable = new MyDrawable(getResources().getDrawable(R.drawable.menu));
      actionBar.setHomeAsUpIndicator(myDrawable);
      myDrawable.setmOffset(0.5f);
      ll_menu_root.setActionBarDrawable(myDrawable);
    }
    actionBar.setTitle("初始化中...");
    listView.setAdapter(new MyAdapter());
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setOnItemClickListener(new ListViewOnItemClickListener());
  }

  private void initUtils() {
    mySurfaceView.setUtils(drawUtil, switchUtil, parseUtil);
    drawUtil.setOnShowPopupListener(new OnShowPopupListener() {
      @Override
      public void onShowPopup(MyBaseShape shape) {
        View popupMenuBtn = shape.getPopupMenuBtn();
        if (shape.isSelected()) {
          if (popupMenuBtn == null) {
            showPopup(shape);
          } else {
            RectF bounds = shape.getBounds();
            FrameLayout.LayoutParams layoutParams =
                (FrameLayout.LayoutParams) popupMenuBtn.getLayoutParams();
            layoutParams.bottomMargin = mySurfaceView.getHeight() - (int) bounds.top;
            layoutParams.rightMargin = mySurfaceView.getWidth() - (int) bounds.right;
            popupMenuBtn.setLayoutParams(layoutParams);
          }
        } else if (!shape.isSelected() && shape.getPopupMenuBtn() != null) {
          hidePopup(shape);
        }
      }
    });
  }

  private void cancelSelected() {
    switchUtil.switchShape(drawUtil.getShapeList(), 0, 0, 0, 0);
    mySurfaceView.updateShapes();
  }

  private void showPopup(final MyBaseShape shape) {
    RectF bounds = shape.getBounds();
    final TextView textView = new TextView(this);
    textView.setBackgroundColor(Color.LTGRAY);
    textView.setTextSize(14);
    textView.setText("编辑");
    textView.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

        mySurfaceView.setCurrentShape(shape);
        switchUtil.switchShape(drawUtil.getShapeList(), (int) shape.getBounds().left,
            (int) shape.getBounds().top, (int) shape.getBounds().right,
            (int) shape.getBounds().bottom);
        mySurfaceView.updateShapes();
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, textView);
        Menu menu = popupMenu.getMenu();
        popupMenu.getMenuInflater().inflate(R.menu.popup_menu, menu);
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
          @Override
          public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.popup_move) {
              textView.setText("移动");
              MySurfaceView.selectType = MySurfaceView.Select.MOVE;
              actionBar.setTitle("移动");
            } else if (item.getItemId() == R.id.popup_rotate) {
              textView.setText("旋转");
              MySurfaceView.selectType = MySurfaceView.Select.ROTATE;
              actionBar.setTitle("旋转");
            } else if (item.getItemId() == R.id.popup_delete) {
              hidePopup(shape);
              mySurfaceView.deleteShape(shape);
            } else if (item.getItemId()
                == R.id.popup_cancel) {//                textView.setText("编辑");
              MySurfaceView.selectType = MySurfaceView.Select.SWITCH;
              actionBar.setTitle("选择");

            }
            return false;
          }
        });
      }
    });
    FrameLayout.LayoutParams layoutParams =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.BOTTOM | Gravity.RIGHT;
    layoutParams.bottomMargin = mySurfaceView.getHeight() - (int) bounds.top;
    layoutParams.rightMargin = mySurfaceView.getWidth() - (int) bounds.right;
    surfaceview_root.addView(textView, layoutParams);
    shape.setPopupMenuBtn(textView);
  }

  private void hidePopup(MyBaseShape shape) {
    surfaceview_root.removeView(shape.getPopupMenuBtn());
    shape.setPopupMenuBtn(null);
  }

  class MyAdapter extends BaseAdapter {
    private String[] opration = new String[] {"选择", "矩形", "圆", "椭圆", "线", "路径"};

    @Override
    public int getCount() {
      return opration.length + 5;
    }

    @Override
    public Object getItem(int position) {
      return null;
    }

    @Override
    public long getItemId(int position) {
      return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
      View view = null;
      if (position == 0) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText("操作");
        textView.setTextSize(20);
        textView.setPadding(20, 10, 0, 10);
        textView.setBackgroundColor(Color.LTGRAY);
        view = textView;
      } else if (position < opration.length + 1) {
        RadioButton radioButton = new RadioButton(MainActivity.this);
        radioButton.setText(opration[position - 1]);
        radioButton.setFocusable(false);
        radioButton.setClickable(false);
        view = radioButton;
      } else if (position == opration.length + 1) {
        TextView textView = new TextView(MainActivity.this);
        textView.setTextSize(20);
        textView.setPadding(20, 10, 0, 10);
        textView.setBackgroundColor(Color.LTGRAY);
        textView.setText("画笔");
        view = textView;
      } else if (position == opration.length + 2) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText("画笔颜色");
        textView.setTextSize(16);
        textView.setPadding(20, 10, 0, 10);
        view = textView;
      } else if (position == opration.length + 3) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText("画笔粗细");
        textView.setTextSize(16);
        textView.setPadding(20, 10, 0, 10);
        view = textView;
      } else if (position == opration.length + 4) {
        TextView textView = new TextView(MainActivity.this);
        textView.setText("填充颜色");
        textView.setTextSize(16);
        textView.setPadding(20, 10, 0, 10);
        view = textView;
      }
      return view;
    }
  }

  class ListViewOnItemClickListener implements AdapterView.OnItemClickListener {

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      switch (position) {
        case 1:
          MySurfaceView.selectType = MySurfaceView.Select.SWITCH;
          actionBar.setTitle("选择");
          break;
        case 2:
          MySurfaceView.selectType = MySurfaceView.Select.RECT;
          actionBar.setTitle("矩形");
          break;
        case 3:
          MySurfaceView.selectType = MySurfaceView.Select.OVAL;
          actionBar.setTitle("圆");
          break;
        case 4:
          MySurfaceView.selectType = MySurfaceView.Select.ELLIPSE;
          actionBar.setTitle("椭圆");
          break;
        case 5:
          MySurfaceView.selectType = MySurfaceView.Select.LINE;
          actionBar.setTitle("直线");
          break;
        case 6:
          MySurfaceView.selectType = MySurfaceView.Select.PATH;
          actionBar.setTitle("曲线");
          break;
      }
      cancelSelected();
      ll_menu_root.hideLeftMenu();
    }
  }

  @Override
  protected void onDestroy() {
    if (doc != null) {
      doc.close();
    }
    super.onDestroy();
    android.os.Process.killProcess(android.os.Process.myPid());
  }
}