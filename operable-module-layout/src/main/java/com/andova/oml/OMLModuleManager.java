package com.andova.oml;

import com.andova.oml.dao.ModuleManager;
import com.andova.oml.db.OMLSqlHelper;

/**
 * Created by Zsago on 2017/7/16.
 * <p>implement in application</p>
 *
 * @author Zsago
 */
@SuppressWarnings("WeakerAccess")
public interface OMLModuleManager {
    OMLSqlHelper getSQLHelper();

    ModuleManager getModuleManager();

    OMLModuleOptions getModuleOptions();
}
