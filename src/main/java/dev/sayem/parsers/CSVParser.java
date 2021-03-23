package dev.sayem.parsers;

import dev.sayem.models.CSVColumn;
import dev.sayem.models.XMLNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;


public class CSVParser {
    private static StringBuilder title = new StringBuilder();

    public File writeToCSV(File[] src, File dest) throws Exception {
        if (dest == null) dest = File.createTempFile("XMLtoCSV", ".csv");
        else if (!dest.exists()) dest.createNewFile();

        List<List<CSVColumn>> columnList = Arrays.stream(src).map(f -> {
            try {
                return parse(f);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        List<CSVColumn> columns = merge(columnList);

        List<String[]> rows = toCsvRow(columns);

        try (PrintWriter writer = new PrintWriter(dest)) {

            StringBuilder sb = new StringBuilder();

            for (String[] row : rows) {
                Arrays.stream(row).forEach(c -> sb.append(c).append(","));
                sb.append("\n");
            }

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return dest;
    }

    private List<CSVColumn> merge(List<List<CSVColumn>> columnsList) {
        List<CSVColumn> columns = new ArrayList<>();

        for (int i = 0; i < columnsList.size(); i++) {
            List<CSVColumn> cols = columnsList.get(i);

            // Add all element of columns for first index
            if (i == 0) {
                columns.addAll(cols);
                continue;
            }

            /*
             iterate over columns which needs to be merged, if column exists then update the existing column values.
             */
//            List<String> alreadyMerged = new ArrayList<>();
            for (CSVColumn column : columns) {
                Optional<CSVColumn> colOpt = cols.stream().filter(c -> c.getTitle().equals(column.getTitle())).findFirst();
                if (colOpt.isPresent()) {
                    CSVColumn col = colOpt.get();
//                    if (alreadyMerged.contains(col.getTitle())) continue;
                    int index = columns.indexOf(column);
                    column.addValues(col.getValues());
                    columns.set(index, column);
//                    alreadyMerged.add(col.getTitle());
                } else {
                    int index = columns.indexOf(column);
                    column.addValue("");
                    columns.set(index, column);
                }
            }
//            for (CSVColumn colToMerge : cols) {
//                Optional<CSVColumn> colOpt = columns.stream().filter(c -> c.getTitle().equals(colToMerge.getTitle())).findFirst();
//                if (colOpt.isPresent()) {
//                    CSVColumn col = colOpt.get();
////                    if (alreadyMerged.contains(col.getTitle())) continue;
//                    int index = columns.indexOf(col);
//                    col.addValues(colToMerge.getValues());
//                    columns.set(index, col);
////                    alreadyMerged.add(col.getTitle());
//                }
//            }

        }

        return columns;
    }

    public List<CSVColumn> parse(File file) throws ParserConfigurationException, IOException, SAXException {

        List<XMLNode> nodes = XMLParser.parse(file, null);

        List<CSVColumn> columns = new ArrayList<>();

        for (XMLNode cNode : nodes) {
            if (cNode.hasChildren()) {
                List<CSVColumn> colsToAdd = childNodes(columns, cNode);
                colsToAdd.forEach(col -> {
                    if (columns.stream().noneMatch(c -> c.getTitle().equals(col.getTitle())))
                        columns.add(col);
                });
//                columns.addAll(childNodes(columns, cNode));
            }
        }

        return columns;
    }

    private List<CSVColumn> childNodes(List<CSVColumn> columns, XMLNode node) {
        String toAppend;
        if (!title.toString().isEmpty()) toAppend = ".'" + node.getName() + "'";
        else toAppend = "'" + node.getName() + "'";
        title.append(toAppend);

        for (XMLNode cNode : node.getChildren()) {
            if (cNode.hasChildren()) {
                childNodes(columns, cNode);
            } else {
                String columnTitle = title.toString() + ".'" + cNode.getName() + "'";
                columnTitle = fixTitleForMultipleEncounter(columns, columnTitle);
                String finalColumnTitle = columnTitle;
                Optional<CSVColumn> columnOpt = columns.stream().filter(c -> finalColumnTitle.equals(c.getTitle())).findFirst();

                if (!columnOpt.isPresent()) {
                    CSVColumn column = new CSVColumn(columnTitle);
                    column.addValue(cNode.getValue());
                    columns.add(column);
                } else {
                    CSVColumn column = columnOpt.get();
                    column.addValue(cNode.getValue());
                    columns.set(columns.indexOf(columnOpt.get()), column);
                }
            }
        }

        title = new StringBuilder(title.toString().replace(toAppend, ""));

        return columns;
    }

    private String fixTitleForMultipleEncounter(List<CSVColumn> columns, String columnTitle) {
        String title = columnTitle;
        int i = 1;
        try {
            i = Integer.parseInt(columnTitle.charAt(columnTitle.length() - 1) + "");
        } catch (NumberFormatException ignored) {
        }

        if (columns.stream().anyMatch(c -> c.getTitle().equals(columnTitle))) {
            title = columnTitle + i;
        }
        return title;
    }

    private List<String[]> toCsvRow(List<CSVColumn> columns) {
        int columnSize = columns.size();
        int rowSize = columns.stream().mapToInt(c -> c.getAllItems().size()).max().orElse(0);
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < rowSize; i++) {
            String[] row = new String[columnSize];
            for (int j = 0; j < columnSize; j++) {
                CSVColumn c = columns.get(j);
                if (c.getAllItems().size() > i)
                    row[j] = c.getAllItems().get(i);
                else row[j] = "";
            }
            rows.add(row);
        }

        return rows;
    }
}
