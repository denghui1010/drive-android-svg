package com.goodow.drive.android.svg;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodow.drive.android.svg.graphics.MyBaseShape;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.store.CollaborativeList;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.Store;

import java.util.List;

public class MainActivity extends Activity {

  private Store store;
  private Document doc;
  private static final String ID = "ldh/svg_test";
  private MySurfaceView mView;
  private LeftMenuLayout ll_menu_root;
  private ListView listView;
  private ProgressBar pb_progress;
  private ActionBar actionBar;
  private ImageView iv_btn;

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
  protected void onCreate(Bundle savedInstanceState) {
    setContentView(R.layout.activity_main);
    super.onCreate(savedInstanceState);
    store = StoreProvider.get();
    initView();
    loadDoc();
  }

  private void loadDoc() {
    pb_progress.setVisibility(View.VISIBLE);
    Handler<Document> onLoadHandler = new Handler<Document>() {
      @Override
      public void handle(Document document) {
        doc = document;
        pb_progress.setVisibility(View.INVISIBLE);
        mView.setCanDraw(true);
        mView.setDocument(doc);
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

  private void initView() {
    mView = (MySurfaceView) findViewById(R.id.view);
    pb_progress = (ProgressBar) findViewById(R.id.pb_progress);
    ll_menu_root = (LeftMenuLayout) findViewById(R.id.ll_menu_root);
    iv_btn = (ImageView) findViewById(R.id.iv_btn);
    ll_menu_root.setControlButton(iv_btn);
    actionBar = getActionBar();
    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setDisplayShowHomeEnabled(false);
    MyDrawable myDrawable = new MyDrawable(getResources().getDrawable(R.drawable.menu));
    actionBar.setHomeAsUpIndicator(myDrawable);
    myDrawable.setmOffset(0.5f);
    actionBar.setTitle("初始化中...");
    ll_menu_root.setActionBarDrawable(myDrawable);
    listView = (ListView) findViewById(R.id.lv_menu_list);
    listView.setAdapter(new MyAdapter());
    listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    listView.setOnItemClickListener(new ListViewOnItemClickListener());
  }

  private void cancelSelected() {
    List<MyBaseShape> list = mView.getShapeList();
    for(MyBaseShape shape : list){
      shape.setSelected(false);
    }
  }

  class MyAdapter extends BaseAdapter {
    private String[] opration = new String[]{"选择", "矩形", "圆", "椭圆", "线", "路径"};

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
      CollaborativeList data = doc.getModel().getRoot().get("data");
      data.clear();
      doc.close();
    }
    super.onDestroy();
  }
}