package dev.sayem;

import dev.sayem.models.XMLNodeCl;
import dev.sayem.parsers.CSVParser;
import dev.sayem.parsers.ClParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;

public class Application {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2)
            throw new RuntimeException("You must provide a source and destination folder!");

//        updateFromZip("test.zip");
        convertToCSV(args);

//        if (args == null || args.length < 2)
//            throw new RuntimeException("You must provide a xsd and xml location!");
//        if (XMLValidator.isValid(args[0], args[1])) {
//            System.out.println("XML is valid!");
//        } else System.out.println("XML is invalid!");
    }


    public static void updateFromZip(String zipPath) throws IOException {
        List<List<XMLNodeCl>> xmlNodes = ClParser.parseXmlFromZip(zipPath);


        for (List<XMLNodeCl> nodes : xmlNodes) { // nodes for each xml file
            if (nodes.isEmpty()) continue;
            for (XMLNodeCl node : nodes.get(0).getChildren()) { // iterate over nodes from a single xml file
                if (!"ns:SimpleItem".equals(node.getName())) continue;
                System.out.println(node.getName());
                System.out.println(node.getCode());
                System.out.println(node.getValidFrom());
                System.out.println(node.getValidTill());
                XMLNodeCl descNode = findDescriptionNode(node);
                System.out.println(descNode == null ? null : descNode.getDescription());
                System.out.println(node.getNodeId());

            }

        }

    }

    private static XMLNodeCl findDescriptionNode(XMLNodeCl node) {
        if (node == null) return null;
        if (node.getChildren() != null && !node.getChildren().isEmpty()) {
            return node.getChildren().stream().filter(n -> "ns:CodeDescription".equals(n.getName())).findFirst().orElse(null);
        }
        return null;
    }

    public static void convertToCSV(String[] args) throws Exception {
        if (args == null || args.length < 2)
            throw new RuntimeException("You must provide a source and destination folder!");
        try {
            long millis = Instant.now().toEpochMilli();
            List<File> files = listFiles(new File(args[0]));
            if (files.isEmpty()) {
                System.out.println("No source files found. Aborted execution.");
                return;
            }
            File[] filesArr = files.toArray(new File[0]);
            File destPath = new File(args[1]);
            if (!destPath.exists()) destPath.mkdirs();
            else if (destPath.isFile()) throw new RuntimeException("Destination path needs to be a directory.");
            File destFile = new File(destPath.getAbsolutePath() + File.separator + UUID.randomUUID() + ".csv");

            boolean copyToDefinedCsv = args.length > 2 && args[2].equalsIgnoreCase("copy");
            File file = new CSVParser().writeToCSV(filesArr, destFile, copyToDefinedCsv);

            System.out.println(file.getAbsolutePath());
            System.out.println("SPENT TIME: " + ((Instant.now().toEpochMilli() - millis)) + " ms\n\n\n");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private static final List<File> files = new ArrayList<>();

    private static List<File> listFiles(File dir) {
        if (!dir.isDirectory() && dir.isFile()) return Collections.singletonList(dir);
        Arrays.stream(Objects.requireNonNull(dir.listFiles())).forEach(f -> {
            if (f.isDirectory()) listFiles(f);
            else if (f.isFile()) files.add(f);
        });
        return files;
    }

}
