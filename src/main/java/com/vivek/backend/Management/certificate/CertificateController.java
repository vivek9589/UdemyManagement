package com.vivek.backend.Management.certificate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/certificate")
public class CertificateController {


    @Autowired
    private CertificateService certificateService;

    @Autowired
    private HtmlToPdfGenerator pdfGenerator;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(
            @RequestHeader ("Authorization") String authHeader,
            @RequestParam Long courseId) {

        // 1. Extract token from "Bearer <token>"
        String token = authHeader.replace("Bearer ", "");


        String html = certificateService.generateHtml(token, courseId);

        byte[] pdfBytes = pdfGenerator.generatePdfFromHtml(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }



}
