package com.health.collect;

import java.util.Collection;

public class MoreCollections {
    public static boolean isNullOrEmpty(Collection collection) {
        return null == collection || collection.isEmpty();
    }
}
