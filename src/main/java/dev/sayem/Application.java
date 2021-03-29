package dev.sayem;

import dev.sayem.models.XMLNodeCl;
import dev.sayem.parsers.CSVParser;
import dev.sayem.parsers.ClParser;
import dev.sayem.utils.Compressor;
import dev.sayem.utils.FileUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

public class Application {

    public static void main(String[] args) throws Exception {
        if (args == null || args.length < 2)
            throw new RuntimeException("You must provide a source and destination folder!");

//        parseXmlFromZip();
        convertToCSV(args);

//        if (args == null || args.length < 2)
//            throw new RuntimeException("You must provide a xsd and xml location!");
//        if (XMLValidator.isValid(args[0], args[1])) {
//            System.out.println("XML is valid!");
//        } else System.out.println("XML is invalid!");
    }

    private static void parseXmlFromZip() throws IOException {
        String destDir = Compressor.unzip("test.zip", "test");
        List<File> files = FileUtil.getInstance().listFiles(new File(destDir));

        List<List<XMLNodeCl>> nodes = files.stream().map(file -> {
            try {
                return ClParser.parse(file, null);
            } catch (ParserConfigurationException | IOException | SAXException e) {
                e.printStackTrace();
                return new ArrayList<XMLNodeCl>();
            }
        }).collect(Collectors.toList());

        System.out.println("Nodes count: " + nodes.size());
    }

    public static void convertToCSV(String[] args) throws Exception {
        if (args == null || args.length < 2)
            throw new RuntimeException("You must provide a source and destination folder!");
        try {
            long millis = Instant.now().toEpochMilli();
            List<File> files = listFiles(new File(args[0]));
            File[] filesArr = files.toArray(new File[0]);
            File destPath = new File(args[1]);
            if (!destPath.exists()) destPath.mkdirs();
            else if (destPath.isFile()) throw new RuntimeException("Destination path needs to be a directory.");
            File destFile = new File(destPath.getAbsolutePath() + File.separator + UUID.randomUUID() + ".csv");
            File file = new CSVParser().writeToCSV(filesArr, destFile);
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
