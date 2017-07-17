package com.kzax1l.oml;

import com.kzax1l.oml.dao.ChannelItem;

import java.util.List;

/**
 * Created by Zsago on 2017/7/17.
 *
 * @author Zsago
 */
public interface OMLDataProvider {
    List<ChannelItem> available();

    List<ChannelItem> unavailable();
}
