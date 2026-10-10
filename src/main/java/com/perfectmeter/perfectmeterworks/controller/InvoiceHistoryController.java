package com.perfectmeter.perfectmeterworks.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class InvoiceHistoryController {

    @GetMapping("/invoice/history")
    public String invoiceHistory() {
        return "invoice-history";
    }
}