package io.github.froger.instamaterial.ui.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import io.github.froger.instamaterial.R;

public class RestaurantActivity extends AppCompatActivity implements AbsListView.OnScrollListener {

    private ListView listView;
    private Toolbar toolbar;
    private TextView floatTitle;
    private ImageView headerBg;

    private float headerHeight;
    private float minHeaderHeight;
    private float floatTitleLeftMargin;
    private float floatTitleSize;
    private float floatTitleSizeLarge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant);


        initMeasure();
        initView();
        initListViewHeader();
        initListView();
        initEvent();
    }

    private void initMeasure() {
        headerHeight = getResources().getDimension(R.dimen.header_height);
        minHeaderHeight = getResources().getDimension(R.dimen.abc_action_bar_default_height_material);
        floatTitleLeftMargin = getResources().getDimension(R.dimen.float_title_left_margin);
        floatTitleSize = getResources().getDimension(R.dimen.float_title_size);
        floatTitleSizeLarge = getResources().getDimension(R.dimen.float_title_size_large);
    }

    private void initView() {
        listView = (ListView) findViewById(R.id.lv_main);
        floatTitle = (TextView) findViewById(R.id.tv_main_title);
        toolbar = (Toolbar) findViewById(R.id.tb_main);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initListView() {
        List<String> data = new ArrayList<>();
        data.add("Samosa      Rs10");
        data.add("Sandwich    Rs40");
        data.add("Momos       Rs30");
        data.add("Burger       Rs40");
        data.add("Thali(Veg)  Rs60");
        data.add("Half Fry     Rs30");
        data.add("Soft Drink  Rs35");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.activity_list_item, android.R.id.text1, data);
        listView.setAdapter(adapter);
    }

    private void initListViewHeader() {
        View headerContainer = LayoutInflater.from(this).inflate(R.layout.header, listView, false);
        headerBg = (ImageView) headerContainer.findViewById(R.id.img_header_bg);

        listView.addHeaderView(headerContainer);
    }

    private void initEvent() {
        listView.setOnScrollListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private void onBackPressed(Menu menu){

    }

    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }


    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        //Y轴偏移量
        float scrollY = getScrollY(view);

        //变化率
        float headerBarOffsetY = headerHeight - minHeaderHeight;//Toolbar与header高度的差值
        float offset = 1 - Math.max((headerBarOffsetY - scrollY) / headerBarOffsetY, 0f);

        //Toolbar背景色透明度
        toolbar.setBackgroundColor(Color.argb((int) (offset * 255), 0, 0, 0));
        //header背景图Y轴偏移
        headerBg.setTranslationY(scrollY / 2);

        /*** 标题文字处理 ***/
        //标题文字缩放圆心（X轴）
        floatTitle.setPivotX(floatTitle.getLeft() + floatTitle.getPaddingLeft());
        //标题文字缩放比例
        float titleScale = floatTitleSize / floatTitleSizeLarge;
        //标题文字X轴偏移
        floatTitle.setTranslationX(floatTitleLeftMargin * offset);
        //标题文字Y轴偏移：（-缩放高度差 + 大文字与小文字高度差）/ 2 * 变化率 + Y轴滑动偏移
        floatTitle.setTranslationY(
                (-(floatTitle.getHeight() - minHeaderHeight) +//-缩放高度差
                        floatTitle.getHeight() * (1 - titleScale))//大文字与小文字高度差
                        / 2 * offset +
                        (headerHeight - floatTitle.getHeight()) * (1 - offset));//Y轴滑动偏移
        //标题文字X轴缩放
        floatTitle.setScaleX(1 - offset * (1 - titleScale));
        //标题文字Y轴缩放
        floatTitle.setScaleY(1 - offset * (1 - titleScale));

        //判断标题文字的显示
        if (scrollY > headerBarOffsetY) {
            toolbar.setTitle(getResources().getString(R.string.toolbar_title));
            floatTitle.setVisibility(View.GONE);
        } else {
            toolbar.setTitle("");
            floatTitle.setVisibility(View.VISIBLE);
        }
    }

    public float getScrollY(AbsListView view) {
        View c = view.getChildAt(0);

        if (c == null)
            return 0;

        int firstVisiblePosition = view.getFirstVisiblePosition();
        int top = c.getTop();

        float headerHeight = 0;
        if (firstVisiblePosition >= 1)
            headerHeight = this.headerHeight;

        return -top + firstVisiblePosition * c.getHeight() + headerHeight;
    }
}





