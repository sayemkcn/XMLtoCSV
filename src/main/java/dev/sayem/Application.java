package dev.sayem;

import dev.sayem.models.CSVColumn;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Application {
    public static void main(String[] args) {
        try {
            List<CSVColumn> columns = CSVParser.parse(new File("example.xml"));
            columns.forEach(c -> System.out.println(c.toString() + "\n------------------------------------------\n"));
            System.out.println(columns.size());
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }


}
