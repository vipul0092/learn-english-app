package io.vgaur.vidya.models.serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Created by vgaur created on 22/10/20
 */
public class LocalDateTimeStringSerializer extends StdSerializer<LocalDateTime> {
    private LocalDateTimeStringSerializer() {
        super(LocalDateTime.class);
    }

    @Override
    public void serialize(LocalDateTime date, JsonGenerator generator, SerializerProvider provider) throws IOException {
        generator.writeString(date.toString());
    }
}
