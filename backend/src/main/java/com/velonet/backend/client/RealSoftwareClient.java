package com.velonet.backend.client;

import org.springframework.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDate;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
// llamadas a la api de GR, solo datos
public class RealSoftwareClient {

    private final RestTemplate restTemplate;
    
    @Value("${realsoftware.api.url}")
    private String apiUrl;

    @Value("${realsoftware.api.cuit}")
    private String cuit;

    @Value("${realsoftware.api.secret}")
    private String secret;

    public RealSoftwareClient(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

     public Map<String, Object> post(Map<String, Object> body) {

        HttpHeaders headers = createAuthHeaders();

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        return restTemplate.postForObject(
                apiUrl,
                request,
                Map.class
        );
    }

    private HttpHeaders createAuthHeaders() {
        try {
            String hoy = LocalDate.now().toString(); // YYYY-MM-DD
            String cadenaAHash = cuit + secret + hoy;

            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(cadenaAHash.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            String passwordMd5 = sb.toString();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(cuit, passwordMd5, StandardCharsets.UTF_8);

            return headers;
        } catch (Exception e) {
            throw new RuntimeException("Error al generar credenciales Basic Auth", e);
        }
    }
}
