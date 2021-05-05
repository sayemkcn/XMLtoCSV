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
    private final Calendar cal = Calendar.getInstance();

    public File writeToCSV(File[] src, File dest, boolean copyToDefinedCsv) throws Exception {
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

        // if copy param is passed, then use existing template to copy to.
        if (copyToDefinedCsv)
            rows = this.copyToDefinedCSV(rows);

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

    private List<String[]> copyToDefinedCSV(List<String[]> rows) {
        if (rows == null || rows.isEmpty()) return rows;

        String[] newTitleRow = {
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'analysis-time')",
                "Date&Time",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'mrn')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'finalResult')",
                "Year D&T",
                "Month D&T",
                "Day D&T",
                "Weekday D&T",
                "Hour D&T",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RIKSDeclarationType')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RIKSCustomsOffice')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'messageCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'channel')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'typeOfDeclaration1')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'totalNumberOfItems')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'totalNumberOfPackages')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'countryOfExportCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'countryOfDestinationCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'identityAtDepartureOnArrival')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'nationalityAtDepartureOnArrival')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'containerisedIndicator')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'identityCrossingBorder')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'nationalityCrossingBorder')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'placeOfLoading')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'placeOfUnloading')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'totalGrossMass')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ECSNTCSAccompanyingDocumentLanguageCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'specificCircumstance')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'transportCharges')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'security')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'declarationAcceptanceDate')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'declarationIssuingDate')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsigneeSecurity', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsigneeSecurity', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsigneeSecurity', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsigneeSecurity', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsigneeSecurity', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'tin')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Principal', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'EntryCarrier', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'EntryCarrier', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'EntryCarrier', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'EntryCarrier', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'EntryCarrier', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem1', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem1', 'identity')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem1', 'nationality')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem2', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem2', 'identity')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem2', 'nationality')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Itinerary0', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Itinerary0', 'countryOfRoutingCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Itinerary2', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Itinerary2', 'countryOfRoutingCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'MAPSAlertFlag')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'countryOfExportCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'countryOfDestinationCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'goodsDescription')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'itemNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'grossMass')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsigneeSecurity', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsigneeSecurity', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsigneeSecurity', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsigneeSecurity', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsigneeSecurity', 'countryCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'marks')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'kindOfPackages')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'numberOfPackages')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CommodityCode', 'commodityCodeJoined')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CommodityCode', 'commodityCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'type')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'reference')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'complementOfInformation')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'type')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'reference')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'complementOfInformation')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DepartureCustomsOffice', 'referenceNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DestinationCustomsOffice', 'referenceNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ControlResult', 'controlResultCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ControlResult', 'dateLimit')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'SealsInfo', 'sealsNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'SealsInfo', 'SealsId1', 'elementID')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'SealsInfo', 'SealsId1', 'identity')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RIKSGoodsOffice')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RIKSGoodsPlace')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'lrn')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'typeOfDeclaration2')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'identityAtDepartureOnArrivalLNG')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'transportModeAtBorder')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'inlandTransportMode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'grandTotalTaxesAmount')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'apportionmentMode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'dateOfDeclaration')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'declarationPlace')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'declarationPlaceLNG')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'declarationPerson')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'nadLNG')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'tin')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'nadLNG')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignee', 'vatNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'name')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'streetAndNumber')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'postalCode')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'city')",
                "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'countryCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Seller', 'nadLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'name')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'streetAndNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'postalCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'city')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'countryCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'nadLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Buyer', 'vatNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RepresentativeStatus', 'representativeStatusCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'name')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'streetAndNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'postalCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'city')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'countryCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'nadLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'vatNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem1', 'identityLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DeliveryTerms', 'incotermCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DeliveryTerms', 'complementOfInfo')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DeliveryTerms', 'complementOfInfoLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'DeliveryTerms', 'complementaryCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'TransactionData', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'TransactionData', 'totalAmountInvoiced')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'TransactionData', 'exchangeRate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'TransactionData', 'natureOfTransactionCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary0', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary0', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary0', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary0', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary0', 'exchangeRate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary2', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary2', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary2', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary2', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary2', 'exchangeRate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary4', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary4', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary4', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary4', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary4', 'exchangeRate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary6', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary6', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary6', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary6', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ValueAdjustmentSummary6', 'exchangeRate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PostponedPayment0', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PostponedPayment0', 'typeOfGuarantee')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PostponedPayment0', 'authorizationReference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Warehouse', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Warehouse', 'identification')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Warehouse', 'country')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'isVMVTSupervision')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'isVMVTPhysicalInspection')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValuationIndicators')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'goodsDescriptionLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'countryOfOrigin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'preference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'procedureRequested')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'previousProcedure')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'procedureRequestedPrevious')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'netMass')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'itemPrice')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'valuationMethod')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'averageValue')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'declaredValue')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'differenceOfAverageAndDeclaredValue')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'percentageOfAverageAndDeclaredValue')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'statisticalValueCurrency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'statisticalValueAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'customsValueAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'totalItemTaxesAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'nadLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'nadLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignee', 'vatNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'marksLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'description')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CommodityCode', 'combinedNomenclature')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CommodityCode', 'taricCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CommodityCode', 'combinedNomenclatureTaricCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'PreviousDocument1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'PreviousDocument1', 'category')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'PreviousDocument1', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'PreviousDocument1', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'PreviousDocument1', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument1', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument2', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument4', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument4', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument4', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument4', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument4', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument5', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument5', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument5', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument5', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument5', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment1', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment1', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment1', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment1', 'nationalCurrencyAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment2', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment2', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment2', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment2', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment2', 'nationalCurrencyAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment3', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment3', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment3', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment3', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment3', 'nationalCurrencyAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment4', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment4', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment4', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment4', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ValueAdjustment4', 'nationalCurrencyAmount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'typeOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'taxBase')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'rateOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'amountOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax1', 'methodOfPayment')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'typeOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'taxBase')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'rateOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'amountOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax2', 'methodOfPayment')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ImportCustomsOffice', 'referenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PresentationCustomsOffice', 'referenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PostponedPayment0', 'currency')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'PostponedPayment0', 'amount')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'typeOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'taxBase')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'rateOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'amountOfTax')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'CalculationOfTax3', 'methodOfPayment')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'customsSubPlace')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'additionalCommunityProcedure1')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Package1', 'numberOfPieces')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SupplementaryUnit1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SupplementaryUnit1', 'unit')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SupplementaryUnit1', 'quantity')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'commercialReferenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'identityCrossingBorderLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'agreedLocationOfGoodsCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'dialogLanguageAtDeparture')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Consignor', 'vatNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Declarant', 'aeoStatus')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'IdentityAtDepartureOnArrivalCrossingBorderListItem2', 'identityLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'commercialReferenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'Consignor', 'vatNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SpecialMention1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SpecialMention1', 'code')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SpecialMention1', 'text')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'SpecialMention1', 'textLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ExportCustomsOffice', 'referenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ExitCustomsOffice', 'referenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'authorisedLocationOfGoodsCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'ConsignorSecurity', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Representative', 'name')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'RepresentativeCapacity', 'representativeCapacity')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'guaranteeType')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'GuaranteeReference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'GuaranteeReference', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'GuaranteeReference', 'guaranteeReferenceNumber')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'Guarantee1', 'GuaranteeReference', 'accessCode')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ConsignorSecurity', 'tin')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument3', 'complementOfInformation')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'issueDate')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument6', 'documentReferenceValidity')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument7', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument7', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument7', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument7', 'referenceLNG')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument8', 'elementID')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument8', 'type')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument8', 'reference')", "('XmlImageToXmlFileCmd', 'XMLImage', 'message', 'data', 'Header', 'GoodsItem1', 'ProducedDocument8', 'referenceLNG')"};
        List<String[]> newRows = new ArrayList<>();
        newRows.add(Arrays.stream(newTitleRow).map(t -> "\"" + t + "\"").toArray(String[]::new));
        String[] titleRow = rows.get(0);

        // Iterate over each row
        for (int i = 0; i < rows.size(); i++) {
            if (i == 0) continue;
            String[] row = rows.get(i);
            String[] newRow = new String[newTitleRow.length];

            //  Iterate over each column for new template titles
            for (int j = 0; j < newTitleRow.length; j++) {
                String newTitle = newTitleRow[j];
                int titlePosition = findTitlePosition(titleRow, newTitle);
                if (titlePosition == -1)
                    newRow[j] = "";
                else
                    newRow[j] = row[titlePosition];

                // update time to cells containing timestamp fields from a1 cel
                this.updateTimeStampRows(newRow, j);

            }
            newRows.add(newRow);
        }

        return newRows;
    }

    private void updateTimeStampRows(String[] newRow, int j) {
        if (newRow == null || newRow[0] == null || newRow[0].trim().isEmpty()) return;

        if (j == 0) {
            long timeMillis;
            try {
                timeMillis = Long.parseLong(newRow[0]);
            } catch (NumberFormatException e) {
                System.out.println("Could not parse to long: " + newRow[0] + "\nException: " + e.getMessage());
                return;
            }
            cal.setTimeInMillis(timeMillis);
        } else if (j == 4) {
            newRow[j] = String.valueOf(cal.get(Calendar.YEAR));
        } else if (j == 5) {
            newRow[j] = String.valueOf(cal.get(Calendar.MONTH));
        } else if (j == 6) {
            newRow[j] = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        } else if (j == 7) {
            newRow[j] = String.valueOf(cal.get(Calendar.DAY_OF_WEEK));
        } else if (j == 8) {
            newRow[j] = String.valueOf(cal.get(Calendar.HOUR));
        }
    }

    private int findTitlePosition(String[] titleRow, String title) {
        if (titleRow == null || titleRow.length == 0) return 0;
        for (int i = 0; i < titleRow.length; i++) {
            String oldTitle = titleRow[i].replace("'StdCmdMacro'.", "").replace(".", ",");
            String newTitle = title.replace("(", "").replace(")", "").replace(" ", "");
            if (oldTitle.equalsIgnoreCase(newTitle))
                return i;
        }
        return -1;
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
             iterate over columns in which new one needs to be merged, if column exists then update the existing column values.
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
            /*
            Columns that hasn't been added before needs to be added to existing columns
             */
            List<CSVColumn> newColumns = cols.stream().filter(col -> {
                return columns.stream().noneMatch(c -> c.getTitle().equals(col.getTitle()));
            }).collect(Collectors.toList());
            int rowSize = columns.isEmpty() ? 0 : columns.get(0).getValues().size();
            newColumns.forEach(nc -> {
                List<String> values = new ArrayList<>();
                for (int r = 1; r < rowSize; r++)
                    values.add("");
                values.addAll(nc.getValues());
                nc.setValues(values);
                columns.add(nc);
            });

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
//                columnTitle = fixTitleForMultipleEncounter(columns, columnTitle);
//                String finalColumnTitle = columnTitle;
                Optional<CSVColumn> columnOpt = columns.stream().filter(c -> columnTitle.equals(c.getTitle())).findFirst();

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
                    row[j] = c.getAllItems().get(i).replace(",", ".");
                else row[j] = "";
            }
            rows.add(row);
        }

        return rows;
    }
}
