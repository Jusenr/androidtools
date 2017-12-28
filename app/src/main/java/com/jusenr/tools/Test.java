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

package com.jusenr.tools;

import com.jusenr.toolslibrary.utils.DateUtils;
import com.jusenr.toolslibrary.utils.MD5Utils;

/**
 * Description:
 * Author     : Jusenr
 * Email      : jusenr@163.com
 * Date       : 2017/09/18  22:30.
 */

public class Test {

    public static void main(String[] args) {
        String md5String = MD5Utils.getMD5String("Hello world!");
        System.err.println(md5String);
        String date = DateUtils.getCurrentDate();
        System.out.println(date);


//        System.out.println(isFirst);
    }
}