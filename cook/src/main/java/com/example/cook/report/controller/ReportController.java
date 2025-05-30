package com.example.cook.report.controller;

import com.example.cook.report.service.GeneratorReportService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class ReportController {
    private GeneratorReportService generatorReportService;

    public ReportController(GeneratorReportService generatorReportService) {
        this.generatorReportService = generatorReportService;
    }

    @GetMapping("/generate/normal/pdf")
    public void getNormalPdf(@RequestParam("orderId") String orderId
            , HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.generatorReportService.getPdf(request, response);
    }

    @GetMapping("/generate/normal/csv")
    public void getNormalCsv(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.generatorReportService.getCsv(request, response);
    }

    @GetMapping("/generate/normal/excel")
    public void getNormalExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
        this.generatorReportService.getExcel(request, response);
    }
}
