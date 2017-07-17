package com.kzax1l.oml;

import android.app.Application;

import com.kzax1l.oml.dao.ChannelItem;
import com.kzax1l.oml.dao.ChannelManager;
import com.kzax1l.oml.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class OMLApplication extends Application implements OMLInitialization, OMLDataProvider {
    private SQLHelper sqlHelper;

    @Override
    public void onCreate() {
        super.onCreate();
        OMLInitializer.initialize(this);
    }

    /**
     * 获取数据库Helper
     */
    @Override
    public SQLHelper getSQLHelper() {
        if (sqlHelper == null)
            sqlHelper = new SQLHelper(this);
        return sqlHelper;
    }

    @Override
    public void onTerminate(SQLHelper sqlHelper) {
        super.onTerminate();
        if (sqlHelper != null) {
            sqlHelper.close();
        }
        //整体摧毁的时候调用这个方法
    }

    @Override
    public ChannelManager getChannelManage() {
        return ChannelManager.getManager(getSQLHelper(), this);
    }

    @Override
    public List<ChannelItem> available() {
        List<ChannelItem> defaultUserChannels = new ArrayList<>();
        defaultUserChannels.add(new ChannelItem(1, "推荐", 1, 1));
        defaultUserChannels.add(new ChannelItem(2, "热点", 2, 1));
        defaultUserChannels.add(new ChannelItem(3, "杭州", 3, 1));
        defaultUserChannels.add(new ChannelItem(4, "时尚", 4, 1));
        defaultUserChannels.add(new ChannelItem(5, "科技", 5, 1));
        defaultUserChannels.add(new ChannelItem(6, "体育", 6, 1));
        defaultUserChannels.add(new ChannelItem(7, "军事", 7, 1));
        defaultUserChannels.add(new ChannelItem(19, "娱乐", 12, 0));
        return defaultUserChannels;
    }

    @Override
    public List<ChannelItem> unavailable() {
        List<ChannelItem> defaultOtherChannels = new ArrayList<>();
        defaultOtherChannels.add(new ChannelItem(8, "财经", 1, 0));
        defaultOtherChannels.add(new ChannelItem(9, "汽车", 2, 0));
        defaultOtherChannels.add(new ChannelItem(10, "房产", 3, 0));
        defaultOtherChannels.add(new ChannelItem(11, "社会", 4, 0));
        defaultOtherChannels.add(new ChannelItem(12, "情感", 5, 0));
        defaultOtherChannels.add(new ChannelItem(13, "女人", 6, 0));
        defaultOtherChannels.add(new ChannelItem(14, "旅游", 7, 0));
        defaultOtherChannels.add(new ChannelItem(15, "健康", 8, 0));
        defaultOtherChannels.add(new ChannelItem(16, "美女", 9, 0));
        defaultOtherChannels.add(new ChannelItem(17, "游戏", 10, 0));
        defaultOtherChannels.add(new ChannelItem(18, "数码", 11, 0));
        return defaultOtherChannels;
    }
}
