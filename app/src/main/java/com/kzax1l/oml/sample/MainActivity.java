package com.kzax1l.oml.sample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kzax1l.oml.OMLInitializer;
import com.kzax1l.oml.OMLModuleProvider;
import com.kzax1l.oml.Utils;
import com.kzax1l.oml.dao.ModuleItem;
import com.kzax1l.oml.edit.SampleModuleActivity;
import com.kzax1l.oml.sample.fragment.NewsFragment;
import com.kzax1l.oml.sample.fragment.NewsFragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.kzax1l.oml.edit.ModuleActivityAgent.OML_CODE_REQUEST;
import static com.kzax1l.oml.edit.ModuleActivityAgent.OML_CODE_RESULT;

/**
 * Description：仿今日头条首页tab动态添加和删除
 * <p>
 * Created by Mjj on 2016/11/18.
 */
public class MainActivity extends AppCompatActivity implements OMLModuleProvider {

    private ColumnHorizontalScrollView mColumnHorizontalScrollView; // 自定义HorizontalScrollView
    private LinearLayout mRadioGroup_content; // 每个标题

    private LinearLayout ll_more_columns; // 右边+号的父布局
    private ImageView button_more_columns; // 标题右边的+号

    private RelativeLayout rl_column; // +号左边的布局：包括HorizontalScrollView和左右阴影部分
    public ImageView shade_left; // 左阴影部分
    public ImageView shade_right; // 右阴影部分

    private int columnSelectIndex = 0; // 当前选中的栏目索引

    private int mScreenWidth = 0; // 屏幕宽度

    // tab集合：HorizontalScrollView的数据源
    private ArrayList<ModuleItem> userChannelList = new ArrayList<ModuleItem>();

    private ViewPager mViewPager;
    private ArrayList<Fragment> fragments = new ArrayList<Fragment>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mScreenWidth = Utils.getWindowsWidth(this);
        initView();
        OMLInitializer.getModuleManager().setModuleProvider(this);
    }

    private void initView() {
        setContentView(R.layout.activity_main);
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);

        mViewPager = (ViewPager) findViewById(R.id.mViewPager);

        // + 号监听
        button_more_columns.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent_channel = new Intent(getApplicationContext(), SampleModuleActivity.class);
                startActivityForResult(intent_channel, OML_CODE_REQUEST);
            }
        });

        setChangelView();
    }

    /**
     * 当栏目项发生变化时候调用
     */
    private void setChangelView() {
        initColumnData();
        initTabColumn();
        initFragment();
    }

    /**
     * 获取Column栏目 数据
     */
    private void initColumnData() {
        userChannelList = ((ArrayList<ModuleItem>) OMLInitializer.available());
    }

    /**
     * 初始化Column栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();
        int count = userChannelList.size();
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = 5;
            params.rightMargin = 5;
            TextView columnTextView = new TextView(this);
            columnTextView.setGravity(Gravity.CENTER);
            columnTextView.setPadding(5, 5, 5, 5);
            columnTextView.setId(i);
            columnTextView.setText(userChannelList.get(i).name);
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }

            // 单击监听
            columnTextView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        View localView = mRadioGroup_content.getChildAt(i);
                        if (localView != v) {
                            localView.setSelected(false);
                        } else {
                            localView.setSelected(true);
                            mViewPager.setCurrentItem(i);
                        }
                    }
                    Toast.makeText(getApplicationContext(), userChannelList.get(v.getId()).name, Toast.LENGTH_SHORT).show();
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
        }
    }

    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空
        int count = userChannelList.size();
        for (int i = 0; i < count; i++) {
            NewsFragment newfragment = new NewsFragment();
            fragments.add(newfragment);
        }
        NewsFragmentPagerAdapter mAdapetr = new NewsFragmentPagerAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(mAdapetr);
        mViewPager.addOnPageChangeListener(pageListener);
    }

    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
        }
    };

    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
        }
        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            View checkView = mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
            } else {
                ischeck = false;
            }
            checkView.setSelected(ischeck);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case OML_CODE_REQUEST:
                if (resultCode == OML_CODE_RESULT) {
                    setChangelView();
                }
                break;
            default:
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public List<ModuleItem> available() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("场所档案", "module_place"));
        defaultModules.add(new ModuleItem("身份验证", "module_identification"));
        defaultModules.add(new ModuleItem("场所检查", "module_enterprise"));
        defaultModules.add(new ModuleItem("现场勘查", "module_spot"));
        defaultModules.add(new ModuleItem("特行年检", "module_annual_survey"));
        defaultModules.add(new ModuleItem("企业备案", "module_filings"));
        defaultModules.add(new ModuleItem("快递抽查", "module_deliver"));
        defaultModules.add(new ModuleItem("报警/预警处理", "module_ring"));
        defaultModules.add(new ModuleItem("法律法规", "module_law"));
        return defaultModules;
    }

    @Override
    public List<ModuleItem> unavailable() {
        List<ModuleItem> defaultModules = new ArrayList<>();
        defaultModules.add(new ModuleItem("危化盘库异常处理", "module_dangerous"));
        defaultModules.add(new ModuleItem("旅馆验收", "module_hotel"));
        return defaultModules;
    }
}
