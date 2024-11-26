package com.accessorqueuing.common;

import java.util.UUID;

public class IdempotencyCreator {

    public static String create(Object data) {
        return UUID.nameUUIDFromBytes(data.toString().getBytes()).toString();
    }
}
