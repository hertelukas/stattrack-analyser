package de.lukas.stattrackanalyser.model;

import java.time.LocalDateTime;
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

    public static class Field {
        private final DataType dataType;
        private final String key;

        private Field(DataType dataType, String key) {
            this.dataType = dataType;
            this.key = key;
        }

        public DataType getDataType() {
            return dataType;
        }
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
    }

}
