package ru.yandex.practicum.filmorate.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;

public class ValueSerializer extends StdSerializer<String> {
        public ValueSerializer() {
            this(null);
        }

        public ValueSerializer(Class<String> t) {
            super(t);
        }

        @Override
        public void serialize(String name, JsonGenerator gen, SerializerProvider provider) throws IOException {
            if(name != null && name != ""){
                gen.writeString(name.toString());
            } else {
                gen.writeString("");
            }

        }
}

