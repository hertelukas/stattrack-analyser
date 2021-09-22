package de.lukas.stattrackanalyser.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a JSON entry
 */
public class Entry {
    private final LocalDateTime date;
    private final List<Field> fields;

    public Entry(LocalDateTime date, List<Field> fields) {
        this.date = date;
        this.fields = fields;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public List<Field> getFields() {
        return fields;
    }

    public Field getFirst(String key, DataType type) {
        for (Field field : fields) {
            if (key.equals(field.getKey()) && type == field.getDataType()) {
                return field;
            }
        }
        return null;
    }

    public List<String> uniqueKeys() {
        List<String> result = new ArrayList<>();

        for (Field field : fields) {
            if (!result.contains(field.getKey())) {
                result.add(field.getKey());
            }
        }

        return result;
    }

    public abstract static class Field {
        private final DataType dataType;
        private final String key;

        private Field(DataType dataType, String key) {
            this.dataType = dataType;
            this.key = key;
        }

        public DataType getDataType() {
            return dataType;
        }

        public String getKey() {
            return key;
        }

        public abstract String getValueAsString();
    }

    public static class TextField extends Field {
        private final String value;

        public TextField(String key, String value) {
            super(DataType.STRING, key);
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            return getValue();
        }
    }

    public static class BooleanField extends Field {
        private final boolean value;

        public BooleanField(String key, boolean value) {
            super(DataType.BOOLEAN, key);
            this.value = value;
        }

        public boolean getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            return String.valueOf(getValue());
        }
    }

    public static class NumberField extends Field {
        private final Number value;

        public NumberField(String key, Number value) {
            super(DataType.NUMBER, key);
            this.value = value;
        }

        public Number getValue() {
            return value;
        }

        @Override
        public String getValueAsString() {
            return String.valueOf(getValue());
        }
    }

}
