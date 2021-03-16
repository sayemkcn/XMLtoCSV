package dev.sayem.models;

import java.util.ArrayList;
import java.util.List;

public class CSVColumn {
    private String title;
    private List<String> values;

    public CSVColumn() {
    }

    public List<String> getAllItems() {
        List<String> items = new ArrayList<>();
        items.add(title);
        items.addAll(getValues());
        return items;
    }

    public CSVColumn(String title) {
        this.title = title;
    }

    public void addValue(String value) {
        if (values == null) values = new ArrayList<>();
        values.add(value);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(title + ",\n");
        values.forEach(v -> builder.append(v).append(",\n"));
        return builder.toString();
    }
}
