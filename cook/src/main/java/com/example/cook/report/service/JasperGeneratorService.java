package com.example.cook.report.service;

import com.example.cook.model.MenuItemModel;
import com.example.cook.model.OrderItemModel;
import com.example.cook.model.OrderModel;
import com.example.cook.model.ResponseModel;
import com.example.cook.report.repository.ReportCustomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class JasperGeneratorService {


    @Value("${app.jasperFile}")
    private String jasperFile;
    @Value("${app.jasperFolder}")
    private String jasperFolder;

    private ReportCustomRepository reportCustomRepository;

    public JasperGeneratorService(ReportCustomRepository reportCustomRepository) {
        this.reportCustomRepository = reportCustomRepository;
    }

    public void getPdf(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        log.info("getPdf");
        try {


            String orderIdStr = httpServletRequest.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                throw new IllegalArgumentException("Missing orderId parameter");
            }

            Integer orderId = Integer.valueOf(orderIdStr);


            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition"
                    , "attachment; filename=" + "normal" + new Date().getTime() + ".pdf");
            OutputStream outputStream = httpServletResponse.getOutputStream();
            String jasperFile = this.jasperFolder + this.jasperFile;

            Map<String, Object> parameters = new HashMap();
            parameters.put("order_id", orderId);
            parameters.put("jasper_folder", this.jasperFolder);

            outputStream.write(this.generateCustomerReport(jasperFile, parameters));
            outputStream.flush();

        } catch (Exception e) {
            log.info("getPdf error {}", e.getMessage());

            ResponseModel<Void> result = new ResponseModel<>();
            result.setStatus(500);
            result.setDescription("getPdf error " + e.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            httpServletResponse.setHeader("Content-Disposition"
                    , "inline");
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            OutputStream outputStream = null;
            try {
                outputStream = httpServletResponse.getOutputStream();
                outputStream.write(mapper.writeValueAsBytes(result));
                outputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    private byte[] generateCustomerReport(String jasperFile, Map<String, Object> parameters) throws FileNotFoundException {

        Connection connection = this.reportCustomRepository.getConnection();
        byte[] r = new byte[1024];
        if (connection != null) {
            Instant start = Instant.now();
            log.info("start time ");

            FileInputStream fis = null;
            InputStream inputStream = null;
            JasperPrint jasperPrint = null;
            try {
                fis = new FileInputStream(jasperFile);
                inputStream = fis;
                jasperPrint = JasperFillManager.fillReport(inputStream, parameters, connection);
                r = JasperExportManager.exportReportToPdf(jasperPrint);
                Instant finish = Instant.now();
                long timeElapsed = Duration.between(start, finish).toMillis();
                log.info("total time {}",timeElapsed);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw e;
            } catch (JRException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return r;
    }

    public void getCsv(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){

        String orderIdStr = httpServletRequest.getParameter("orderId");
        if (orderIdStr == null || orderIdStr.isEmpty()) {
            throw new IllegalArgumentException("Missing orderId parameter");
        }

        Integer orderId = Integer.valueOf(orderIdStr);

        try{
            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition"
                    , "attachment; filename=" + "csv"+ new Date().getTime()+".csv");
            OutputStream outputStream = httpServletResponse.getOutputStream();

            this.generateCustomerReportCsv(orderId, outputStream);

        } catch (Exception e) {

            ResponseModel<Void> result = new ResponseModel<>();
            result.setStatus(500);
            result.setDescription("getCsv error "+e.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            httpServletResponse.setHeader("Content-Disposition"
                    , "inline");
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            OutputStream outputStream = null;
            try {
                outputStream = httpServletResponse.getOutputStream();
                outputStream.write(mapper.writeValueAsBytes(result));
                outputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public void generateCustomerReportCsv(int orderId, OutputStream outputStream) throws IOException {
        // เขียน BOM UTF-8 3 ไบต์ ช่วยให้ Excel อ่าน encoding ถูกต้อง
        outputStream.write(new byte[] {(byte)0xEF, (byte)0xBB, (byte)0xBF});

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));

        // Header
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
                    writer.flush();  // flush writer แทน outputStream
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
            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition"
                    , "attachment; filename=" + "excel" + new Date().getTime() + ".xlsx");

            OutputStream outputStream = httpServletResponse.getOutputStream();

            int orderId = Integer.parseInt(httpServletRequest.getParameter("orderId"));
            Workbook wb = this.generateCustomerReportExcel(orderId);

            wb.write(outputStream);
            outputStream.flush();

        } catch (Exception e) {
            log.info("getExcel error {}", e.getMessage());

            ResponseModel<Void> result = new ResponseModel<>();
            result.setStatus(500);
            result.setDescription("getExcel error " + e.getMessage());
            ObjectMapper mapper = new ObjectMapper();
            httpServletResponse.setHeader("Content-Disposition"
                    , "inline");
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);

            try {
                OutputStream outputStream = httpServletResponse.getOutputStream();
                outputStream.write(mapper.writeValueAsBytes(result));
                outputStream.flush();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
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