package com.jusenr.toolslibrary.utils;


import org.simple.eventbus.EventBus;

/**
 * EventBus tools class
 * Created by riven_chris on 2017/4/13.
 */

public class EventBusUtils {
    private static EventBus eventBus = EventBus.getDefault();

    public static void register(Object subscriber) {
        eventBus.register(subscriber);
    }

    public static void unregister(Object subscriber) {
        eventBus.unregister(subscriber);
    }

    public static void post(Object target, String tag) {
        eventBus.post(target, tag);
    }

    public static void postSticky(Object target, String tag) {
        eventBus.postSticky(target, tag);
    }

    public static void removeSticky(Class<?> eventClass, String tag) {
        eventBus.removeStickyEvent(eventClass, tag);
    }
}
