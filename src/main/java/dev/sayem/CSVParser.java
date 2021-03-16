package dev.sayem;

import dev.sayem.models.CSVColumn;
import dev.sayem.models.XMLNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CSVParser {
    private static StringBuilder title = new StringBuilder();

    private CSVParser() {
    }

    public static File writeToCSV(File src, File dest) throws Exception {
        if (dest == null) dest = File.createTempFile("XMLtoCSV", ".csv");
        else if (!dest.exists()) dest.createNewFile();
        List<CSVColumn> columns = CSVParser.parse(src);

//        CsvColumnWriter writer = new CsvColumnWriter(dest, File.createTempFile("wcsv", ".txt"), columns.size());
//
//        columns.forEach(c -> {
//            try {
//                List<String> col = new ArrayList<>();
//                col.add(c.getTitle());
//                col.addAll(c.getValues());
//                writer.writeNextCol(col);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        });

        List<String[]> rows = toCsvRow(columns);

        try (PrintWriter writer = new PrintWriter(dest)) {

            StringBuilder sb = new StringBuilder();

            for (String[] row: rows) {
                Arrays.stream(row).forEach(c->sb.append(c).append(","));
                sb.append("\n");
            }
//            sb.append("id,");
//            sb.append(',');
//            sb.append("Name");
//            sb.append('\n');
//
//            sb.append("1");
//            sb.append(',');
//            sb.append("Prashant Ghimire");
//            sb.append('\n');

            writer.write(sb.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return dest;
    }

    public static List<CSVColumn> parse(File file) throws ParserConfigurationException, IOException, SAXException {

        List<XMLNode> nodes = XMLParser.parse(file, null);

        List<CSVColumn> columns = new ArrayList<>();

        for (XMLNode cNode : nodes) {
            if (cNode.hasChildren()) {
                columns.addAll(childNodes(columns, cNode));
            }
        }

        return columns;
    }

    private static List<CSVColumn> childNodes(List<CSVColumn> columns, XMLNode node) {
        String toAppend;
        if (!title.toString().isEmpty()) toAppend = "." + node.getName();
        else toAppend = node.getName();
        title.append(toAppend);

        for (XMLNode cNode : node.getChildren()) {
            if (cNode.hasChildren()) {
                childNodes(columns, cNode);
            } else {
                String columnTitle = title.toString() + "." + cNode.getName();
                columnTitle = fixTitleForMultipleEncounter(columns, columnTitle);
                CSVColumn column = new CSVColumn(columnTitle);
                column.addValue(cNode.getValue());
                columns.add(column);
            }
        }

        title = new StringBuilder(title.toString().replace(toAppend, ""));

        return columns;
    }

    private static String fixTitleForMultipleEncounter(List<CSVColumn> columns, String columnTitle) {
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

    private static List<String[]> toCsvRow(List<CSVColumn> columns) {
        int columnSize = columns.size();
        int rowSize = columns.stream().mapToInt(c -> c.getAllItems().size()).max().orElse(0);
        List<String[]> rows = new ArrayList<>();

        for (int i = 0; i < rowSize; i++) {
            String[] row = new String[columnSize];
            for (int j=0;j<columnSize;j++) {
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
