package ca.concordia.encs.distributed.service.serialization;

import com.google.gson.internal.LinkedTreeMap;

import java.lang.reflect.Field;

public class ContentSerializer {
    public static Object deserialize(LinkedTreeMap map, Object object) {
        for (Field field : object.getClass().getFields()) {
            try {
                field.set(object, map.get(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return object;
    }
}
