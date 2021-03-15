package dev.sayem.models;

import java.util.ArrayList;
import java.util.List;

public class CSVColumn {
    private String title;
    private List<String> values;

    public CSVColumn() {
    }

    public CSVColumn(String title) {
        this.title = title;
    }

    public void addValue(String value){
        if (values==null) values = new ArrayList<>();
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
        builder.append(title);
        values.forEach(v->builder.append("\n").append(v));
        return builder.toString();
    }
}
