package net.kyrptonaught.kyrptconfig.config;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.api.DeserializationException;
import blue.endless.jankson.impl.MarshallerImpl;
import blue.endless.jankson.magic.TypeMagic;

public class CustomMarshaller extends MarshallerImpl {

    public JsonElement serialize(Object obj) {
        if (obj instanceof CustomSerializable customSerializable) {
            return customSerializable.toJson(this);
        }
        return super.serialize(obj);
    }

    public JsonElement serializeNonCustom(Object obj) {
        return super.serialize(obj);
    }

    public <T> T marshall(Class<T> clazz, JsonElement elem, boolean failFast) throws DeserializationException {
        if (CustomSerializable.class.isAssignableFrom(clazz)) {
            CustomSerializable custom = (CustomSerializable) TypeMagic.createAndCast(clazz, failFast);
            return (T) custom.fromJson(this, elem, (Class<CustomSerializable>) clazz);
        }
        return super.marshall(clazz, elem, failFast);
    }

    public <T> T marshallNonCustom(Class<T> clazz, JsonElement elem, boolean failFast) throws DeserializationException {
        return super.marshall(clazz, elem, failFast);
    }
}
