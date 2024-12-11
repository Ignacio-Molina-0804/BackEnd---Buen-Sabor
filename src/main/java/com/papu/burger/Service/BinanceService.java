package com.papu.burger.Service;

import java.net.http.HttpClient;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.papu.burger.Model.BinancePayRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class BinanceService {

    @Value("${binance.apiKey}")
    private String apiKey;

    @Value("${binance.secretKey}")
    private String secretKey;

    @Value("${binance.baseUrl}")
    private String baseUrl;

    private final HttpClient httpClient = HttpClient.newHttpClient();

    public String createPayment(BinancePayRequest payRequest) throws Exception {
        String nonce = generateNonce();
        String timestamp = String.valueOf(System.currentTimeMillis());

        // Request creation
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> env = new HashMap<>();
        env.put("terminalType", "APP");
        body.put("env", env);
        body.put("merchantTradeNo", generateIdentifier());
        body.put("orderAmount", payRequest.getOrderAmount());
        body.put("currency", payRequest.getCurrency());

        Map<String, Object> goods = new HashMap<>();
        goods.put("goodsType", "01");
        goods.put("goodsCategory", "D000");
        goods.put("referenceGoodsId", "12345ABC");
        goods.put("goodsName", "El buen sabor");
        goods.put("goodsDetail", payRequest.getMessage());

        body.put("goods", goods);

        String jsonBody = new ObjectMapper().writeValueAsString(body);
        System.out.println(jsonBody);
        // Create a payload with signature
        String payload = timestamp + "\n" + nonce + "\n" + jsonBody + "\n";
        String signature = generateSignature(payload);

        // Creations of headers
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        headers.set("BinancePay-Timestamp", timestamp);
        headers.set("BinancePay-Nonce", nonce);
        headers.set("BinancePay-Certificate-SN", apiKey);
        headers.set("BinancePay-Signature", signature);

        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<String> entity = new HttpEntity<>(jsonBody, headers);
        // Send request to Binancepay with credentials
        String url = "https://bpay.binanceapi.com/binancepay/openapi/v2/order";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    private String generateNonce() {
        String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        StringBuilder nonce = new StringBuilder();
        for (int i = 0; i < 32; i++) {
            int pos = (int) (Math.random() * chars.length());
            nonce.append(chars.charAt(pos));
        }
        return nonce.toString();
    }

    private String generateSignature(String payload) throws Exception {
        Mac sha256HMAC = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA512");
        sha256HMAC.init(secretKeySpec);
        byte[] hash = sha256HMAC.doFinal(payload.getBytes());
        StringBuilder result = new StringBuilder();
        for (byte b : hash) {
            result.append(String.format("%02x", b));
        }
        return result.toString().toUpperCase();
    }

    private static final String CHAR_POOL = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

    // Longitud del identificador
    private static final int LENGTH = 9;

    // Genera un identificador aleatorio de longitud 'LENGTH'
    public static String generateIdentifier() {
        SecureRandom random = new SecureRandom();
        StringBuilder identifier = new StringBuilder(LENGTH);

        // Generar el identificador con caracteres aleatorios
        for (int i = 0; i < LENGTH; i++) {
            int randomIndex = random.nextInt(CHAR_POOL.length());
            identifier.append(CHAR_POOL.charAt(randomIndex));
        }

        return identifier.toString();
    }
}
