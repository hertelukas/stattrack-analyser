package de.lukas.stattrackanalyser.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class JsonHolder {

    private List<Entry> entries;

    public LoadStatus loadFile(File file) {
        try {
            entries = parseEntries(new JSONObject(FileUtils.readFileToString(file, "utf-8")));
            return LoadStatus.SUCCESS;
        } catch (IOException e) {
            System.out.println("Failed to load file: " + e.getMessage());
            return LoadStatus.FILE_ERROR;
        } catch (JSONException e) {
            System.out.println("Failed to read JSON: " + e.getMessage());
            return LoadStatus.JSON_ERROR;
        }
    }

    public int getAmountOfEntries() {
        return entries.size();
    }

    public DataType getDataType(String key) {
        for (Entry entry : entries) {
            for (Entry.Field field : entry.getFields()) {
                if (key.equals(field.getKey())) {
                    return field.getDataType();
                }
            }
        }
        return null;
    }

    public List<String> getUniqueKeys() {
        List<String> result = new ArrayList<>();

        for (Entry entry : entries) {
            for (String uniqueKey : entry.uniqueKeys()) {
                if (!result.contains(uniqueKey)) {
                    result.add(uniqueKey);
                }
            }
        }

        return result;
    }

    public List<String> getUniqueKeys(Predicate<Entry.Field> predicate) {
        List<String> result = new ArrayList<>();

        for (Entry entry : entries) {
            for (Entry.Field field : entry.getFields()) {
                if (!result.contains(field.getKey()) && predicate.test(field)) {
                    result.add(field.getKey());
                }
            }
        }

        return result;
    }

    public List<Entry.Field> getFields(String key, DataType type) {
        List<Entry.Field> result = new ArrayList<>();

        for (Entry entry : entries) {
            result.add(entry.getFirst(key, type));
        }

        return result;
    }

    public List<LocalDateTime> getDates() {
        List<LocalDateTime> result = new ArrayList<>();

        for (Entry entry : entries) {
            result.add(entry.getDate());
        }

        return result;
    }

    public Map<LocalDateTime, Entry.Field> getDateFields(Predicate<Entry.Field> predicate) {
        Map<LocalDateTime, Entry.Field> result = new HashMap<>();

        // Iterate through all entries
        for (Entry entry : entries) {
            for (Entry.Field field : entry.getFields()) {
                // Check whether a field matches the given predicate
                if (predicate.test(field)) {
                    result.put(entry.getDate(), field);
                }
            }
        }

        return result;
    }

    public Map<LocalDateTime, Entry.Field> getDateFields(String key) {
        Map<LocalDateTime, Entry.Field> result = new HashMap<>();

        // Iterate through all entries
        for (Entry entry : entries) {
            for (Entry.Field field : entry.getFields()) {
                // Check whether a field with a given key exists
                if (field.getKey().equals(key)) {
                    result.put(entry.getDate(), field);
                }
            }
        }

        return result;
    }

    private List<Entry> parseEntries(JSONObject jsonObject) throws JSONException {
        List<Entry> result = new ArrayList<>();
        JSONArray document;
        document = jsonObject.getJSONArray("entries");

        for (int i = 0; i < document.length(); i++) {
            JSONObject current = document.getJSONObject(i);
            try {
                LocalDateTime tempDate = LocalDateTime.parse(current.getString("date"));

                List<Entry.Field> fields = new ArrayList<>();
                JSONObject tempFields = current.getJSONObject("fields");
                for (String name : tempFields.keySet()) {
                    if (tempFields.get(name) instanceof String) {
                        fields.add(new Entry.TextField(name, tempFields.getString(name)));
                    } else if (tempFields.get(name) instanceof Number) {
                        fields.add(new Entry.NumberField(name, tempFields.getNumber(name)));
                    } else if (tempFields.get(name) instanceof Boolean) {
                        fields.add(new Entry.BooleanField(name, tempFields.getBoolean(name)));
                    }
                }

                result.add(new Entry(tempDate, fields));

            } catch (DateTimeParseException e) {
                throw new JSONException("Cannot parse date");
            }
        }

        return result;
    }
}
