package io.vgaur.vidya;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.vgaur.vidya.models.auth.ImmutableStudentToken;
import io.vgaur.vidya.models.auth.StudentToken;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Created by vgaur created on 22/10/20
 */
public class StudentTokenSerializationDeserializationTest {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
    }

    @Test
    public void testSerDeser() throws JsonProcessingException {
        var studentToken = ImmutableStudentToken.builder()
                .tokenId(UUID.randomUUID())
                .createdWithApiKey(UUID.randomUUID())
                .email("dannycarey@toolband.com")
                .id(UUID.randomUUID())
                .teacherId(UUID.randomUUID())
                .createdTimestamp(LocalDateTime.now())
                .build();

        String studentSerialized = OBJECT_MAPPER.writeValueAsString(studentToken);
        var studentDeserialized = OBJECT_MAPPER.readValue(studentSerialized, StudentToken.class);

        assertEquals(studentToken.createdTimestamp(), studentDeserialized.createdTimestamp());
        assertEquals(studentToken.id(), studentDeserialized.id());
        assertEquals(studentToken.email(), studentDeserialized.email());
    }
}
