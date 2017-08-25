package com.jusenr.toolslibrary;

import com.jusenr.toolslibrary.utils.JsonUtils;

/**
 * Description: 工具测试专用类
 * Copyright  : Copyright (c) 2017
 * Email      : jusenr@163.com
 * Author     : Jusenr
 * Date       : 2017/08/25
 * Time       : 14:58
 * Project    ：Tools.
 */
public class ToolsTest {

    public static void main(String[] args) {
        String s = "{\n" +
                "    \"data\": true,\n" +
                "    \"elapsed\": 25,\n" +
                "    \"http_code\": 200\n" +
                "}";
        boolean json = JsonUtils.isJson(s);
        System.out.println(json);
    }
}
