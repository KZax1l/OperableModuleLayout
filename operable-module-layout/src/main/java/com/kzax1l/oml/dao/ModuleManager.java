package com.kzax1l.oml.dao;

import android.database.SQLException;

import com.kzax1l.oml.OMLDataProvider;
import com.kzax1l.oml.db.OMLSqlHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ModuleManager {

    private static ModuleManager channelManager;
    /**
     * 默认的用户选择频道列表
     */
    private final List<ModuleItem> defaultUserChannels;
    /**
     * 默认的其他频道列表
     */
    private final List<ModuleItem> defaultOtherChannels;

    private ModuleDao channelDao;

    /**
     * 判断数据库中是否存在用户数据
     */
    private boolean userExist = false;

    private ModuleManager(OMLSqlHelper paramDBHelper, OMLDataProvider provider) throws SQLException {
        if (provider == null)
            throw new NullPointerException("OMLDataProvider can not be null!");
        if (channelDao == null)
            channelDao = new ModuleDao(paramDBHelper.getContext());
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
    public static ModuleManager getManager(OMLSqlHelper dbHelper, OMLDataProvider provider) throws SQLException {
        if (channelManager == null)
            channelManager = new ModuleManager(dbHelper, provider);
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
    public List<ModuleItem> getUserChannel() {
        Object cacheList = channelDao.listCache(OMLSqlHelper.OML_MODULE_CHECK_STATE + "= ?", new String[]{"1"});
        List<ModuleItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            userExist = true;
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ModuleItem navigate = new ModuleItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_ID)));
                navigate.setName(maplist.get(i).get(OMLSqlHelper.OML_MODULE_NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_ORDER_ID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_CHECK_STATE)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setDeletable(maplist.get(i).get(OMLSqlHelper.OML_MODULE_OPERABLE).equals("1"));
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
    public List<ModuleItem> getOtherChannel() {
        Object cacheList = channelDao.listCache(OMLSqlHelper.OML_MODULE_CHECK_STATE + "= ?", new String[]{"0"});
        List<ModuleItem> list = new ArrayList<>();
        if (cacheList != null && !((List) cacheList).isEmpty()) {
            List<Map<String, String>> maplist = (List) cacheList;
            int count = maplist.size();
            for (int i = 0; i < count; i++) {
                ModuleItem navigate = new ModuleItem();
                navigate.setId(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_ID)));
                navigate.setName(maplist.get(i).get(OMLSqlHelper.OML_MODULE_NAME));
                navigate.setOrderId(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_ORDER_ID)));
                navigate.setSelected(Integer.valueOf(maplist.get(i).get(OMLSqlHelper.OML_MODULE_CHECK_STATE)));
                // 由于SQLite中获取到的Boolean对象是一个数字，1表示true
                navigate.setDeletable(maplist.get(i).get(OMLSqlHelper.OML_MODULE_OPERABLE).equals("1"));
                list.add(navigate);
            }
            return list;
        }
        if (userExist) {
            return list;
        }
        cacheList = defaultOtherChannels;
        return (List<ModuleItem>) cacheList;
    }

    /**
     * 保存用户频道到数据库
     */
    public void saveUserChannel(List<ModuleItem> userList) {
        for (int i = 0; i < userList.size(); i++) {
            ModuleItem channelItem = userList.get(i);
            channelItem.setOrderId(i);
            channelItem.setSelected(1);
            channelDao.addCache(channelItem);
        }
    }

    /**
     * 保存其他频道到数据库
     */
    public void saveOtherChannel(List<ModuleItem> otherList) {
        for (int i = 0; i < otherList.size(); i++) {
            ModuleItem channelItem = otherList.get(i);
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
