package com.andova.oml;

import com.andova.oml.dao.ModuleItem;

import java.util.List;

/**
 * Created by Zsago on 2017/7/17.
 *
 * @author Zsago
 */
public interface OMLModuleProvider {
    List<ModuleItem> available();

    List<ModuleItem> unavailable();
}