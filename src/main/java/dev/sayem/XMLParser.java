package dev.sayem;

import dev.sayem.models.CSVColumn;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class XMLParser {
    private static StringBuilder title = new StringBuilder();

    private XMLParser() {
    }

    public static List<CSVColumn> parse(File file) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(file);

        List<CSVColumn> columns = new ArrayList<>();

        NodeList nodeList = document.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node cNode = nodeList.item(i);
            if (cNode.getNodeType() != Node.TEXT_NODE) {
                columns.addAll(childNodes(columns, cNode));
            }
        }

        return columns;
    }

    private static List<CSVColumn> childNodes(List<CSVColumn> columns, Node node) {

        title.append(node.getNodeName());


        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node cNode = node.getChildNodes().item(i);
            if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                title.append(".").append(cNode.getNodeName());
                childNodes(columns, cNode);
            } else if (cNode.getNodeType() == Node.TEXT_NODE && cNode.getChildNodes().getLength() == 0) {
                title.append(cNode.getNodeName());
                CSVColumn column = new CSVColumn(title.toString());
                column.addValue(cNode.getTextContent());
                columns.add(column);
            }
        }

        return columns;
    }

}
