package com.kzax1l.oml;

import com.kzax1l.oml.dao.ModuleManager;
import com.kzax1l.oml.db.OMLSqlHelper;

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
