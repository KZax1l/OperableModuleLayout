package com.kzax1l.oml.dao;

import android.database.SQLException;

import com.kzax1l.oml.OMLDataProvider;
import com.kzax1l.oml.db.SQLHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Description：频道管理
 * <p>
 * Created by Mjj on 2016/11/18.
 */
public class ChannelManager {

    private static ChannelManager channelManager;
    /**
     * 默认的用户选择频道列表
     */
    private final List<ChannelItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    private final List<ChannelItem> defaultOtherChannels;

    private ChannelDao channelDao;

    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    private ChannelManager(SQLHelper paramDBHelper, OMLDataProvider provider) throws SQLException {
        if (provider == null)
            throw new NullPointerException("OMLDataProvider can not be null!");
        if (channelDao == null)
            channelDao = new ChannelDao(paramDBHelper.getContext());
        if (provider.available() != null) {
            defaultUserChannels = provider.available();
        } else {
            defaultUserChannels = new ArrayList<>();
        }
        if (provider.unavailable() != null) {
            defaultOtherChannels = provider.unavailable();
        } else {
            defaultOtherChannels = new ArrayList<>();
        }
    }

    /**
     * 初始化频道管理类
     */
    public static ChannelManager getManager(SQLHelper dbHelper, OMLDataProvider provider) throws SQLException {
        if (channelManager == null)
            channelManager = new ChannelManager(dbHelper, provider);
        return channelManager;
    }

    /**
     * 清除所有的频道
     */
    public void deleteAllChannel() {
        channelDao.clearFeedTable();
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的用户选择频道 : 默认用户选择频道 ;
     */
    public List<ChannelItem> getUserChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"1"});
        List<ChannelItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setDeletable(maplist.get(i).get(SQLHelper.DELETABLE).equals("1"));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        initDefaultChannel();
        return defaultUserChannels;
    }

    /**
     * 获取其他的频道
     *
     * @return 数据库存在用户配置 ? 数据库内的其它频道 : 默认其它频道 ;
     */
    public List<ChannelItem> getOtherChannel() {
        Object cacheList = channelDao.listCache(SQLHelper.SELECTED + "= ?", new String[]{"0"});
        List<ChannelItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ChannelItem navigate = new ChannelItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(SQLHelper.ID)));
                navigate.setName(maplist.get(i).get(SQLHelper.NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(SQLHelper.ORDERID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(SQLHelper.SELECTED)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setDeletable(maplist.get(i).get(SQLHelper.DELETABLE).equals("1"));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultOtherChannels;
        return (List<ChannelItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     */
    public void saveUserChannel(List<ChannelItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ChannelItem channelItem = userList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(1);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存其他频道到数据库
     */
    public void saveOtherChannel(List<ChannelItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ChannelItem channelItem = otherList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(0);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 初始化数据库内的频道数据
     */
    private void initDefaultChannel() {
        deleteAllChannel();
        saveUserChannel(defaultUserChannels);
        saveOtherChannel(defaultOtherChannels);
    }
}
