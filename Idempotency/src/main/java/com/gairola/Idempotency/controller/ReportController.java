package com.gairola.Idempotency.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
@Controller
public class ReportController {
    @GetMapping("/report-form")
    public String showForm() {
        return "report-form"; // Thymeleaf template name
    }
    @GetMapping("/view-report")
    public ResponseEntity<byte[]> viewReport(@RequestParam String deptId) {
        String reportServerUrl = "http://your-report-server:9002/reports/rwservlet";
        String reportName = "your_report.rdf";
        String dbUser = "username/password@db";

        URI reportUri = UriComponentsBuilder.fromHttpUrl(reportServerUrl)
                .queryParam("report", reportName)
                .queryParam("userid", dbUser)
                .queryParam("destype", "cache")
                .queryParam("desformat", "pdf")
                .queryParam("paramform", "no")
                .queryParam("P_DEPT_ID", deptId)
                .build()
                .encode()
                .toUri();

        byte[] pdf = WebClient.create()
                .get()
                .uri(reportUri)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=report.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
