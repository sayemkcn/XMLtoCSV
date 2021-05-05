package dev.sayem.models;

import java.util.ArrayList;
import java.util.List;

public class CSVColumn {
    private String title;
    private List<String> values;
    private int fileIndex;

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

    public void addValues(List<String> values) {
        if (values == null) return;
        if (this.values == null) this.values = new ArrayList<>();
        this.values.addAll(values);
    }

    public void addValue(String value) {
        if (values == null) values = new ArrayList<>();

        if (value == null)
            values.add("");
        else
            values.add(value);
    }

    public int getFileIndex() {
        return fileIndex;
    }

    public void setFileIndex(int fileIndex) {
        this.fileIndex = fileIndex;
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
