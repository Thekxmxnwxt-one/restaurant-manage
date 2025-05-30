package com.example.cook.report.service;

import com.example.cook.model.OrderItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.report.exception.ExcelGenerationException;
import com.example.cook.report.exception.PdfGenerationException;
import com.example.cook.report.repository.ReportCustomRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class GeneratorReportService {


    @Value("${app.jasperFile}")
    private String jasperFile;
    @Value("${app.jasperFolder}")
    private String jasperFolder;

    private ReportCustomRepository reportCustomRepository;

    public GeneratorReportService(ReportCustomRepository reportCustomRepository) {
        this.reportCustomRepository = reportCustomRepository;
    }

    public void getPdf(HttpServletRequest request, HttpServletResponse response) {
        log.info("getPdf");

        try {
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                throw new PdfGenerationException("Missing orderId parameter");
            }

            Integer orderId = Integer.valueOf(orderIdStr);

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setHeader("Content-Disposition", "attachment; filename=normal" + new Date().getTime() + ".pdf");

            String jasperFile = this.jasperFolder + this.jasperFile;

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("order_id", orderId);
            parameters.put("jasper_folder", this.jasperFolder);

            byte[] pdfBytes = generateCustomerReport(jasperFile, parameters);
            try (OutputStream outputStream = response.getOutputStream()) {
                outputStream.write(pdfBytes);
                outputStream.flush();
            }

        } catch (Exception e) {
            throw new PdfGenerationException("Failed to generate PDF: " + e.getMessage(), e);
        }
    }

    private byte[] generateCustomerReport(String jasperFile, Map<String, Object> parameters) {
        try (
                Connection connection = reportCustomRepository.getConnection();
                FileInputStream fis = new FileInputStream(jasperFile)
        ) {
            Instant start = Instant.now();
            log.info("start time");

            JasperPrint jasperPrint = JasperFillManager.fillReport(fis, parameters, connection);
            byte[] r = JasperExportManager.exportReportToPdf(jasperPrint);

            long timeElapsed = Duration.between(start, Instant.now()).toMillis();
            log.info("total time {}", timeElapsed);

            return r;

        } catch (FileNotFoundException e) {
            throw new PdfGenerationException("Jasper file not found: " + jasperFile, e);
        } catch (JRException | SQLException | IOException e) {
            throw new PdfGenerationException("Error while generating report", e);
        }
    }

    public void getCsv(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String orderIdStr = request.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            throw new IllegalArgumentException("Missing orderId parameter");
        }

        int orderId = Integer.parseInt(orderIdStr);

        response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setHeader("Content-Disposition", "attachment; filename=csv" + new Date().getTime() + ".csv");

        OutputStream outputStream = response.getOutputStream();
        generateCustomerReportCsv(orderId, outputStream); // ถ้า error จะถูก Global handler จัดการ
    }

    public void generateCustomerReportCsv(int orderId, OutputStream outputStream) throws IOException {
        outputStream.write(new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF});

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        String header = String.format("%s|%s|%s|%s|%s|%s|%s|%s|%s",
                "name", "phone", "tableNo", "name employee", "orderID", "orderedAt", "menu_name", "quantity", "price") + "\n";
        writer.write(header);

        OrderModel order = this.reportCustomRepository.findOrderById(orderId);

        if (order != null && order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItemModel item = order.getItems().get(i);

                String data = String.format("%s|%s|%s|%s|%d|%s|%s|%d|%.2f",
                        order.getCustomer().getName(),
                        order.getCustomer().getPhone(),
                        order.getTable().getTableNumber(),
                        order.getEmployee().getName(),
                        order.getId(),
                        order.getOrderedAt().toString(),
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()) + "\n";

                writer.write(data);
                if (i % 20 == 0) {
                    writer.flush();
                }
            }
            writer.flush();
        } else {
            writer.write("No data found\n");
            writer.flush();
        }

        writer.close();
    }


    public void getExcel(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        log.info("getExcel");

        try {
            int orderId = Integer.parseInt(httpServletRequest.getParameter("orderId"));
            Workbook wb = this.generateCustomerReportExcel(orderId);

            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition", "attachment; filename=excel" + new Date().getTime() + ".xlsx");

            OutputStream outputStream = httpServletResponse.getOutputStream();
            wb.write(outputStream);
            outputStream.flush();
        } catch (Exception e) {
            log.error("Excel generation failed", e);
            throw new ExcelGenerationException("Failed to generate Excel report", e);
        }
    }

    private Workbook generateCustomerReportExcel(int orderId){

        Workbook wb = new XSSFWorkbook();
        Sheet sheet1 = wb.createSheet("sheet1");
        int row  = 2;
        Row headerRow = sheet1.createRow(row++);
        int headerCol = 0;
        Cell h1 = headerRow.createCell(headerCol++);
        h1.setCellValue("name");

        Cell h2 = headerRow.createCell(headerCol++);
        h2.setCellValue("phone");

        Cell h3 = headerRow.createCell(headerCol++);
        h3.setCellValue("tableNo");

        Cell h4 = headerRow.createCell(headerCol++);
        h4.setCellValue("name employee");

        Cell h5 = headerRow.createCell(headerCol++);
        h5.setCellValue("orderID");

        Cell h6 = headerRow.createCell(headerCol++);
        h6.setCellValue("orderAt");

        Cell h7 = headerRow.createCell(headerCol++);
        h7.setCellValue("menu_name");

        Cell h8 = headerRow.createCell(headerCol++);
        h8.setCellValue("quantity");

        Cell h9 = headerRow.createCell(headerCol++);
        h9.setCellValue("price");

        OrderModel order = this.reportCustomRepository.findOrderById(orderId);
        if (order != null && order.getItems() != null) {
            for (int i = 0; i < order.getItems().size(); i++) {
                OrderItemModel item = order.getItems().get(i);

                int dataCol = 0;
                int rownum = i+1;

                Row dataRow = sheet1.createRow(row++);

                Cell name = dataRow.createCell(dataCol++);
                name.setCellValue(order.getCustomer().getName());

                Cell phone = dataRow.createCell(dataCol++);
                phone.setCellValue(order.getCustomer().getPhone());

                Cell tableNo = dataRow.createCell(dataCol++);
                tableNo.setCellValue(order.getTable().getTableNumber());

                Cell nameEmployee = dataRow.createCell(dataCol++);
                nameEmployee.setCellValue(order.getEmployee().getName());

                Cell orderID = dataRow.createCell(dataCol++);
                orderID.setCellValue(order.getId());

                Cell orderAt = dataRow.createCell(dataCol++);
                orderAt.setCellValue(order.getOrderedAt());

                Cell menuName = dataRow.createCell(dataCol++);
                menuName.setCellValue(item.getMenuItem().getName());

                Cell quantity = dataRow.createCell(dataCol++);
                quantity.setCellValue(item.getQuantity());

                Cell price = dataRow.createCell(dataCol++);
                price.setCellValue(item.getUnitPrice().toString());
            }
        }

        return wb;
    }

}