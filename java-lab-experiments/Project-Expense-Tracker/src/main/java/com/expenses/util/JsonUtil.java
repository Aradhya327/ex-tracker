package com.expenses.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

/**
 * Utility class for JSON serialization/deserialization using Gson.
 */
public class JsonUtil {
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(YearMonth.class, new YearMonthAdapter())
            .create();

    public static String toJson(Object object) {
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    public static void toJsonFile(Object object, String filePath) {
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(object, writer);
        } catch (IOException e) {
            System.err.println("Error writing JSON to file: " + e.getMessage());
        }
    }

    public static <T> List<T> fromJsonFile(String filePath, Class<T[]> arrayClass) {
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        try (FileReader reader = new FileReader(file)) {
            Type listType = TypeToken.getParameterized(List.class, arrayClass.getComponentType()).getType();
            return gson.fromJson(reader, listType);
        } catch (IOException e) {
            System.err.println("Error reading JSON from file: " + e.getMessage());
            return null;
        }
    }

    // Custom adapters for LocalDate and YearMonth
    private static class LocalDateAdapter implements com.google.gson.JsonSerializer<LocalDate>, 
                                                      com.google.gson.JsonDeserializer<LocalDate> {
        @Override
        public com.google.gson.JsonElement serialize(LocalDate src, Type typeOfSrc, 
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }

        @Override
        public LocalDate deserialize(com.google.gson.JsonElement json, Type typeOfT, 
                                     com.google.gson.JsonDeserializationContext context) {
            return LocalDate.parse(json.getAsString());
        }
    }

    private static class YearMonthAdapter implements com.google.gson.JsonSerializer<YearMonth>, 
                                                       com.google.gson.JsonDeserializer<YearMonth> {
        @Override
        public com.google.gson.JsonElement serialize(YearMonth src, Type typeOfSrc, 
                                                     com.google.gson.JsonSerializationContext context) {
            return new com.google.gson.JsonPrimitive(src.toString());
        }

        @Override
        public YearMonth deserialize(com.google.gson.JsonElement json, Type typeOfT, 
                                     com.google.gson.JsonDeserializationContext context) {
            return YearMonth.parse(json.getAsString());
        }
    }
}