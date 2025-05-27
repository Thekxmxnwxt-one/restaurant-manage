package com.example.cook.report.controller;

import com.example.cook.report.service.JasperGeneratorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReportController {
    private JasperGeneratorService jasperGeneratorService;

    public ReportController(JasperGeneratorService jasperGeneratorService) {
        this.jasperGeneratorService = jasperGeneratorService;
    }

    @GetMapping("/generate/normal/pdf")
    public void getNormalPdf(@RequestParam("orderId") String orderId
            , HttpServletRequest request, HttpServletResponse response) {
        this.jasperGeneratorService.getPdf(request, response);
    }
}
