package com.jusenr.tools.base.api;

import com.jusenr.tools.BuildConfig;
import com.jusenr.tools.utils.LocaleUtils;

/**
 * Created by Jusenr on 2017/08/25.
 */

public class BaseApi {
    public static final int HOST_FORMAL = 1;//正式环境
    public static final int HOST_TEST = 2;//测试环境
    public static final int HOST_DEV = 3;//开发环境
    public static int HOST_NOW = BuildConfig.HOST_NOW;//当前环境

    public static String API_LOCALE;

    /**
     * 通行证
     */
    public static String ACCOUNT_BASE_URL = "";
    /**
     * 文件服务器
     */
    public static String FILE_BASE_URL = "";


    /**
     * environment: 1，外网 2，测试内网 3，开发内网
     */
    public static void init() {
        initLanguage();
        switch (HOST_NOW) {
            case 1:
                ACCOUNT_BASE_URL = "https://www.baidu.com/";
                FILE_BASE_URL = "http://www.sohu.com/";

                break;
            case 2:
                ACCOUNT_BASE_URL = "https://www.baidu.com/";
                FILE_BASE_URL = "http://www.sohu.com/";

                break;
            case 3:
                ACCOUNT_BASE_URL = "https://www.baidu.com/";
                FILE_BASE_URL = "http://www.sohu.com/";

                break;
        }
    }

    public static void initLanguage() {
        API_LOCALE = LocaleUtils.getLocaleLanguage();
    }

    public static boolean isInnerEnvironment() {
        return HOST_NOW == HOST_DEV || HOST_NOW == HOST_TEST;
    }

    /*所有的相对URL*/
    public static class Url {
        //===================== BindManageApi =====================//
        public static final String URL_REGISTER = "api/register";//注册
        public static final String URL_SEND_MAIL = "api/sendMail";//发送邮箱验证码(国际版)

        public static final String URL_CHILD_LIST = "child/list";//孩子列表

    }
}
