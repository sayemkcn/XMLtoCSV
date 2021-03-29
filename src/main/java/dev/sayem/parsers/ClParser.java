package dev.sayem.parsers;

import dev.sayem.models.XMLNodeCl;
import dev.sayem.utils.Compressor;
import dev.sayem.utils.FileUtil;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ClParser {
    private ClParser() {
    }

    public static List<List<XMLNodeCl>> parseXmlFromZip(String zipFilePath) throws IOException {
        String destDir = Compressor.unzip(zipFilePath, FileUtil.createTempDirectory().getAbsolutePath());
        List<File> files = FileUtil.getInstance().listFiles(new File(destDir));

        List<List<XMLNodeCl>> nodes = files.stream().map(file -> {
            try {
                return ClParser.parse(file, null);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
                return new ArrayList<XMLNodeCl>();
            }
        }).collect(Collectors.toList());

        System.out.println("Total: " + nodes.size() + " files parsed.");
        return nodes;
    }

    private static List<XMLNodeCl> parse(File file, Map<String, String> labels) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory
                .newInstance();
        DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
        Document document = docBuilder.parse(file);

        List<XMLNodeCl> xmlNodes = new ArrayList<>();

        NodeList nodeList = document.getChildNodes();

        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            XMLNodeCl xmlNode = new XMLNodeCl();
            xmlNode.setName(node.getNodeName());
            xmlNode.setNodeId(findNodeId(node));
            if (labels != null)
                xmlNode.setLabel(labels.get(node.getNodeName()));
            xmlNode.setValue(node.getTextContent());
            xmlNode.setAttributes(findAttributes(node));
            xmlNode.setChildren(ClParser.childNodes(node, labels));
            xmlNodes.add(xmlNode);
        }

        return xmlNodes;
    }

    private static Map<String, String> findAttributes(Node node) {
        if (node == null || !node.hasAttributes()) return null;
        Map<String, String> map = new HashMap<>();
        for (int i = 0; i < node.getAttributes().getLength(); i++) {
            Node n = node.getAttributes().item(i);
            map.put(n.getNodeName(), n.getTextContent());
        }
        return map;
    }

    private static List<XMLNodeCl> childNodes(Node node, Map<String, String> labels) {

        List<XMLNodeCl> xmlNodes = new ArrayList<>();

        for (int i = 0; i < node.getChildNodes().getLength(); i++) {
            Node cNode = node.getChildNodes().item(i);
            if (cNode.getNodeType() == Node.ELEMENT_NODE) {
                XMLNodeCl xmlNode = new XMLNodeCl();
                xmlNode.setName(cNode.getNodeName());
                xmlNode.setNodeId(findNodeId(cNode));
                if (labels != null)
                    xmlNode.setLabel(labels.get(cNode.getNodeName()));
                xmlNode.setValue(cNode.getTextContent());
                xmlNode.setAttributes(findAttributes(cNode));
                xmlNode.setChildren(ClParser.childNodes(cNode, labels));
                xmlNodes.add(xmlNode);
            }
        }

        return xmlNodes;
    }

    private static String findNodeId(Node node) {
        if (!node.hasAttributes() || node.getAttributes().getNamedItem("id") == null)
            return null;
        return node.getAttributes().getNamedItem("id").getTextContent();
    }

}
