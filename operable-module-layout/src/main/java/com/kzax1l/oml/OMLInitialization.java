package com.kzax1l.oml;

import com.kzax1l.oml.dao.ChannelManager;
import com.kzax1l.oml.db.OMLSqlHelper;

/**
 * Created by Zsago on 2017/7/16.
 * <p>implement in application</p>
 *
 * @author Zsago
 */
public interface OMLInitialization {
    OMLSqlHelper getSQLHelper();

    void onTerminate(OMLSqlHelper sqlHelper);

    ChannelManager getChannelManage();
}
