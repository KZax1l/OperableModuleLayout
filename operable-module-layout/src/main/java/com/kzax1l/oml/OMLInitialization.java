package com.kzax1l.oml;

import com.kzax1l.oml.db.SQLHelper;

/**
 * Created by Zsago on 2017/7/16.
 * <p>implement in application</p>
 *
 * @author Zsago
 */
public interface OMLInitialization {
    SQLHelper getSQLHelper();

    void onTerminate(SQLHelper sqlHelper);
}
