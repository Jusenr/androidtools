package com.jusenr.tools.utils;


import com.jusenr.tools.TotalApplication;
import com.jusenr.tools.api.BaseApi;
import com.jusenr.tools.model.ChildInfo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jusenr on 2017/09/27.
 */

public class DiscCacheUtils {

    private static final String CACHE_CHILD_KEY = BaseApi.Url.URL_CHILD_LIST + "/";//绑定孩子的key

    public static <T extends Serializable> T getCachedData(String key) {
        T result = (T) TotalApplication.getDiskFileCacheHelper().getAsSerializable(key);
        return result;
    }

    public static List<ChildInfo> getCachedChildList() {
        return (ArrayList<ChildInfo>) TotalApplication.getDiskFileCacheHelper().getAsSerializable(
                CACHE_CHILD_KEY + "CurrentUid");
    }

    public static void cacheChildList(ArrayList<ChildInfo> childInfoList) {
        TotalApplication.getDiskFileCacheHelper().put(CACHE_CHILD_KEY + "CurrentUid", childInfoList);
    }
}
