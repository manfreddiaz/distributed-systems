package ca.concordia.encs.distributed.serialization.json;


import com.google.gson.Gson;

import java.lang.reflect.Type;

public class JsonSerializer {
    private static final Gson serializer = new Gson();

    public static byte[] toByteArray(Object object, Class clazz) {
        return toJSON(object, clazz).getBytes();
    }
    public static String toJSON(Object object, Class clazz) {
        return serializer.toJson(object, clazz);
    }
    public static Object fromJSON(String json, Class clazz) {
        return serializer.fromJson(json, clazz);
    }
    public static Object fromJSON(String json, Type type) {
        return serializer.fromJson(json, type);
    }
    public static Object fromByteArray(byte[] serialized, int offset, int length, Class clazz) {
        String json = new String(serialized, offset, length);
        return fromJSON(json, clazz);
    }
}
