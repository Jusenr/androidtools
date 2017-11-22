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
