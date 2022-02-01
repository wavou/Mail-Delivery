package com.picus.maildelivery.constants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum LinkStatus {

    ALREADY_CLICKED("already_clicked"),
    SUCCESS("success");

    private static final Map<String, LinkStatus> ENUM_MAP;
    private String key;

    LinkStatus(String id) {
        this.key = id;
    }

    public static LinkStatus get(String key) {
        return ENUM_MAP.get(key);
    }

    public String getKey() {
        return this.key;
    }

    static {
        Map<String, LinkStatus> map = new HashMap<>();
        LinkStatus[] notificationTypeEnumItems = values();

        for (LinkStatus type : notificationTypeEnumItems) {
            map.put(type.getKey(), type);
        }

        ENUM_MAP = Collections.unmodifiableMap(map);
    }
}
