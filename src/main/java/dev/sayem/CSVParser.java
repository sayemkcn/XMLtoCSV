package dev.sayem;

import dev.sayem.models.CSVColumn;
import dev.sayem.models.XMLNode;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class CSVParser {
    private static StringBuilder title = new StringBuilder();

    private CSVParser() {
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

        if (!title.toString().isEmpty()) title.append(".");
        title.append(node.getName());

        for (XMLNode cNode : node.getChildren()) {
            if (cNode.hasChildren()) {
                childNodes(columns, cNode);
            } else {
                CSVColumn column = new CSVColumn(title.toString() + "." + cNode.getName());
                column.addValue(cNode.getValue());
                columns.add(column);
            }
        }

        title = new StringBuilder(title.toString().replace("." + node.getName(), ""));

        return columns;
    }

}
