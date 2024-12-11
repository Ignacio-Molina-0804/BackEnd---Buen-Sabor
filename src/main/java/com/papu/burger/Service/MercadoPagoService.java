package com.papu.burger.Service;

import com.mercadopago.MercadoPagoConfig;
import com.mercadopago.client.preference.PreferenceClient;
import com.mercadopago.client.preference.PreferenceItemRequest;
import com.mercadopago.client.preference.PreferenceRequest;
import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;

import com.mercadopago.resources.preference.Preference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;

@Service
public class MercadoPagoService {
    @Value("${codigo.mercadoLibre}")
    private String accessToken;

    public String createPreference(String title, Double price, int quantity) throws MPException, MPApiException, MPApiException {
        // Configurar el Access Token
        MercadoPagoConfig.setAccessToken(accessToken);

        // Crear el cliente de preferencias
        PreferenceClient client = new PreferenceClient();

        // Crear el ítem de la preferencia
        PreferenceItemRequest itemRequest = PreferenceItemRequest.builder()
                .title(title)
                .quantity(quantity)
                .unitPrice(BigDecimal.valueOf(price))
                .build();

        // Crear la preferencia
        PreferenceRequest preferenceRequest = PreferenceRequest.builder()
                .items(Collections.singletonList(itemRequest))
                .build();

        // Guardar la preferencia y obtener la URL de pago
        Preference preference = client.create(preferenceRequest);

        return preference.getInitPoint();
    }
}
