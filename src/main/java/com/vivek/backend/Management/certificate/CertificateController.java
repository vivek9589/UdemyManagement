package com.vivek.backend.Management.certificate;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/certificate")
public class CertificateController {





    @Autowired
    private CertificateService certificateService;

    @Autowired
    private HtmlToPdfGenerator pdfGenerator;

    @GetMapping("/generate")
    public ResponseEntity<byte[]> generateCertificate(
            @RequestParam String name,
            @RequestParam Long courseId) {

        String html = certificateService.generateHtml(name, courseId);

        byte[] pdfBytes = pdfGenerator.generatePdfFromHtml(html);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificate.pdf");

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }



}
