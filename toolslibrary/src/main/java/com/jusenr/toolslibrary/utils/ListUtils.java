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

import android.support.v4.util.ArrayMap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * List tools class
 * Created by guchenkai on 2015/12/15.
 */
public final class ListUtils {

    private ListUtils() {
        throw new AssertionError();
    }

    /**
     * Is empty
     *
     * @param list list
     * @param <D>  bean
     * @return IS NULL [boolean]
     */
    public static <D> boolean isEmpty(List<D> list) {
        return list == null || list.isEmpty() || list.size() <= 0;
    }

    /**
     * SubString
     *
     * @param source Sourcrlist
     * @param start  Start
     * @param end    End
     * @return Completed list
     */
    public static <T extends Serializable> List<T> cutOutList(List<T> source, int start, int end) {
        List<T> result = new ArrayList<>();
        for (int i = start; i < end; i++) {
            result.add(source.get(i));
        }
        return result;
    }

    /**
     * Is Equals
     *
     * @param a   First List
     * @param b   Second List
     * @param <T> bean
     * @return Is Equals
     */
    public static <T extends Serializable> boolean equals(Collection<T> a, Collection<T> b) {
        if (a == null) return false;
        if (b == null) return false;
        if (a.isEmpty() && b.isEmpty()) return true;
        if (a.size() != b.size()) return false;
        List<T> alist = new ArrayList<>(a);
        List<T> blist = new ArrayList<>(b);
        Collections.sort(alist, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return o1.hashCode() - o2.hashCode();
            }

        });
        Collections.sort(blist, new Comparator<T>() {
            public int compare(T o1, T o2) {
                return o1.hashCode() - o2.hashCode();
            }

        });
        return alist.equals(blist);
    }

    /**
     * Order ArrayMap By Key
     *
     * @param map map
     * @param <K> key
     * @param <V> value
     * @return map
     */
    public static <K, V> ArrayMap<K, V> sortByKey(ArrayMap<K, V> map) {
        ArrayMap<K, V> arrayMap = new ArrayMap<>();
        K[] keys = (K[]) map.keySet().toArray();
        Arrays.sort(keys);
        for (K key : keys) {
            arrayMap.put(key, map.get(key));
        }
        return arrayMap;
    }

    /**
     * Sort out List
     *
     * @param source     source
     * @param groupIndex groupIndex
     * @param <T>        bean
     * @return Result list
     */
    public static <T extends Serializable> List<ArrayList<T>> group(List<T> source, int groupIndex) {
        List<ArrayList<T>> result = new ArrayList<>();
        ArrayList<T> group = new ArrayList<>();
        for (T t : source) {
            group.add(t);
            if (group.size() == groupIndex) {
                result.add(group);
                group.clear();
            }
        }
        return result;
    }
}