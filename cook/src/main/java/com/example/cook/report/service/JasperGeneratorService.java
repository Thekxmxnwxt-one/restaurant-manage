package com.example.cook.report.service;

import com.example.cook.model.ResponseModel;
import com.example.cook.report.repository.ReportCustomRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.*;
import java.sql.Connection;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
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
            String orderId = httpServletRequest.getParameter("orderId");
            if (orderId == null || orderId.isEmpty()) {
                throw new IllegalArgumentException("Missing orderId parameter");
            }

            httpServletResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            httpServletResponse.setStatus(HttpServletResponse.SC_OK);
            httpServletResponse.setHeader("Content-Disposition"
                    , "attachment; filename=" + "normal" + new Date().getTime() + ".pdf");
            OutputStream outputStream = httpServletResponse.getOutputStream();
            String jasperFile = this.jasperFolder + this.jasperFile;

            Map<String, Object> parameters = new HashMap();
            parameters.put("order_id", httpServletRequest.getParameter("orderId"));
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
        if(null != connection) {
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
                if (null != inputStream) {
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
}