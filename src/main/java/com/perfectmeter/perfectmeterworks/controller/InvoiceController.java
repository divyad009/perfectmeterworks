package com.perfectmeter.perfectmeterworks.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.perfectmeter.perfectmeterworks.pdf.InvoicePdfGenerator;

@Controller
public class InvoiceController {

    @GetMapping("/invoice/new")
    public String newInvoice() {
        return "new-invoice";
    }

    @GetMapping("/generate-pdf")
    public ResponseEntity<byte[]> generatePdf(
            @RequestParam String vehicleNumber) {

        try {

            LocalDateTime now = LocalDateTime.now();

            // PDF filename date/time
            DateTimeFormatter fileDateFormatter =
                    DateTimeFormatter.ofPattern(
                            "yyyy-MM-dd_HH-mm-ss"
                    );

            String fileDateTime =
                    now.format(fileDateFormatter);

            // Invoice number
            DateTimeFormatter invoiceFormatter =
                    DateTimeFormatter.ofPattern(
                            "a_yyyyMMddHHmmss"
                    );

            String invoiceNumber =
                    now.format(invoiceFormatter).toUpperCase();;

            File pdfFile =
                    InvoicePdfGenerator.generateInvoice(
                            vehicleNumber,
                            invoiceNumber,
                            fileDateTime
                    );

            byte[] pdfBytes =
                    Files.readAllBytes(pdfFile.toPath());

            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" +
                            pdfFile.getName() +
                            "\""
                    )
                    .contentType(
                            MediaType.APPLICATION_PDF
                    )
                    .body(pdfBytes);

        } catch (IOException e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}