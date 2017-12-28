/*
 * Copyright 2017 androidtools Jusenr
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jusenr.tools.utils;


import com.jusenr.tools.TotalApplication;
import com.jusenr.tools.base.api.BaseApi;
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