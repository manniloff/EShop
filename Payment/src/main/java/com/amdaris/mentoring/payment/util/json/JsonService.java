package com.amdaris.mentoring.payment.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class JsonService {
    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    private final Gson GSON = createGson();

    public String toJson(Object value) {
        return GSON.toJson(value);
    }

    public <T> T fromJson(String json, Class<T> valueClass) {
        return GSON.fromJson(json, valueClass);
    }

    private Gson createGson() {
        return new GsonBuilder()
                .registerTypeAdapter(
                        LocalDateTime.class,
                        (JsonDeserializer<LocalDateTime>) (json, type, jsonDeserializationContext) ->
                                LocalDateTime.parse(json.getAsString()))
                .registerTypeAdapter(LocalDateTime.class,
                        (JsonSerializer<LocalDateTime>) (src, type, context) ->
                                context.serialize(DATE_TIME_FORMATTER.format(src)))
                .create();
    }
}