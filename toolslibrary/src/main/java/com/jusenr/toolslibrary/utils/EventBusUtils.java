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

package com.jusenr.toolslibrary.utils;


import org.simple.eventbus.EventBus;

/**
 * EventBus tools class
 * Created by riven_chris on 2017/4/13.
 */

public final class EventBusUtils {

    private EventBusUtils() {
        throw new AssertionError();
    }

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