package com.perfectmeter.perfectmeterworks.pdf;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;

import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;

public class InvoicePdfGenerator {

	public static File generateInvoice(
	        String vehicleNumber,
	        String invoiceNumber,
	        String fileDateTime) throws IOException {

        // ---------------------------------------------------------
        // DATE
        // ---------------------------------------------------------

        String date = LocalDate.now()
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        // ---------------------------------------------------------
        // LOAD HTML TEMPLATE
        // ---------------------------------------------------------

        InputStream inputStream =
                InvoicePdfGenerator.class
                        .getClassLoader()
                        .getResourceAsStream("templates/invoice.html");

        if (inputStream == null) {
            throw new IOException(
                    "Could not find templates/invoice.html"
            );
        }

        String html;

        try (InputStream is = inputStream) {
            html = new String(
                    is.readAllBytes(),
                    StandardCharsets.UTF_8
            );
        }

        // ---------------------------------------------------------
        // VALUES FOR HTML TEMPLATE
        // ---------------------------------------------------------

        Map<String, String> values = new HashMap<>();

        values.put("vehicleNumber", vehicleNumber);
        values.put("invoiceNumber", invoiceNumber);
        values.put("invoiceDate", date);

        // Item 1
        values.put("item1Description", "METER CERTIFICATE");
        values.put("item1Qty", "1");
        values.put("item1Price", "145.00");
        values.put("item1Amount", "145.00");

        // Item 2
        values.put("item2Description", "PROGRAM");
        values.put("item2Qty", "1");
        values.put("item2Price", "280.00");
        values.put("item2Amount", "280.00");

        // Item 3
        values.put("item3Description", "LABOUR");
        values.put("item3Qty", "1");
        values.put("item3Price", "275.00");
        values.put("item3Amount", "275.00");

        // Total
        values.put("total", "Rs. 700.00");

        // ---------------------------------------------------------
        // REPLACE TEMPLATE VALUES
        // ---------------------------------------------------------

        for (Map.Entry<String, String> entry : values.entrySet()) {

            String placeholder =
                    "${" + entry.getKey() + "}";

            html = html.replace(
                    placeholder,
                    entry.getValue()
            );
        }

        // ---------------------------------------------------------
        // CONVERT HTML TO XHTML
        // ---------------------------------------------------------

        Document document = Jsoup.parse(html);

        document.outputSettings()
                .syntax(Document.OutputSettings.Syntax.xml);

        document.outputSettings()
                .escapeMode(Entities.EscapeMode.xhtml);

        document.outputSettings()
                .charset(StandardCharsets.UTF_8);

        String xhtml = document.html();

        // ---------------------------------------------------------
        // CREATE OUTPUT DIRECTORY
        // ---------------------------------------------------------

        File outputDir = new File("invoices");

        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        String dateTime =
                invoiceNumber.replace("INV-", "");

        File outputFile =
                new File(
                        outputDir,
                        vehicleNumber + "_" +
                        fileDateTime + ".pdf"
                );

        // ---------------------------------------------------------
        // HTML → PDF
        // ---------------------------------------------------------

        try (OutputStream outputStream =
                     new FileOutputStream(outputFile)) {

            PdfRendererBuilder builder =
                    new PdfRendererBuilder();

            String baseUrl =
                    InvoicePdfGenerator.class
                            .getClassLoader()
                            .getResource("")
                            .toExternalForm();

            builder.withHtmlContent(
                    xhtml,
                    baseUrl
            );

            builder.toStream(outputStream);

            builder.run();
        }

        return outputFile;
    }
}