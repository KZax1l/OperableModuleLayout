package com.andova.oml;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.andova.oml.dao.ModuleItem;

/**
 * Created by Administrator on 2018-01-16.
 *
 * @author kzaxil
 * @since 1.0.1
 */
@SuppressWarnings("WeakerAccess")
public interface OMLModuleOptions {
    /**
     * 自定义模块布局的接口
     */
    View moduleLayout(Context context, ViewGroup parent);

    /**
     * 自定义模块适配器
     *
     * @return 若自定义适配器，则返回true，否则，返回false
     */
    boolean moduleAdapter(View parent, ModuleItem item);

    /**
     * 数据库版本号，需大于0才有效
     */
    int version();
}