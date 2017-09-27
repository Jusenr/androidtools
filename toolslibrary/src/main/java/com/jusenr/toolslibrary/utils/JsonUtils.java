package com.jusenr.toolslibrary.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;

/**
 * JSON tool class
 * Created by guchenkai on 2015/10/27.
 */
public final class JsonUtils {
    private static final String TAG = JsonUtils.class.getSimpleName();

    private JsonUtils() {
        throw new AssertionError();
    }

    /**
     * Determine the type of JSON
     *
     * @param json json
     * @return type of JSON
     */
    public static JsonType getJSONType(String json) {
        if (TextUtils.isEmpty(json)) {
            return JsonType.JSON_TYPE_ERROR;
        }
        final char[] strChar = json.substring(0, 1).toCharArray();
        final char firstChar = strChar[0];
        switch (firstChar) {
            case '{':
                return JsonType.JSON_TYPE_OBJECT;
            case '[':
                return JsonType.JSON_TYPE_ARRAY;
            default:
                return JsonType.JSON_TYPE_ERROR;
        }
    }

    /**
     * JSON type enumeration
     */
    public enum JsonType {
        JSON_TYPE_OBJECT,//JSONObject
        JSON_TYPE_ARRAY,//JSONArray
        JSON_TYPE_ERROR//The string is not in JSON format
    }

    /**
     * JSON format
     *
     * @param json json
     * @return Formatted JSON
     */
    public static String jsonFormatter(String json) throws NullPointerException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        JsonParser jp = new JsonParser();
        JsonElement je = jp.parse(json);
        String formatJson1 = gson.toJson(je);
        return formatJson1;
    }

    /**
     * JSON format
     *
     * @param jsonString jsonString
     * @param paramClass paramClass
     * @param <T>        paramClass
     * @return Formatted JSON
     */
    public static <T> T parseData(String jsonString, Class<T> paramClass) {
        return new Gson().fromJson(jsonString, paramClass);
    }

    public static boolean isJson(String string) {
        try {
            new JsonParser().parse(string);
            return true;
        } catch (JsonParseException e) {
            e.printStackTrace();
        }
        return false;
    }
}
