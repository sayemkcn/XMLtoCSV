package dev.sayem;

import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.Instant;

public class Application {
    public static void main(String[] args) throws Exception {
        try {
            long millis = Instant.now().toEpochMilli();
//            List<CSVColumn> columns = CSVParser.parse(new File("example.xml"));
//            System.out.println("SPEND TIME: " + ((Instant.now().toEpochMilli() - millis)) + "\n\n\n");
//            columns.forEach(c -> System.out.println(c.toString() + "\n------------------------------------------\n"));
            File file = CSVParser.writeToCSV(new File[]{new File("example.xml"), new File("example.xml"), new File("example.xml")}, new File("result.csv"));
            System.out.println(file.getAbsolutePath());
            System.out.println("SPEND TIME: " + ((Instant.now().toEpochMilli() - millis)) + "\n\n\n");
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }


}
