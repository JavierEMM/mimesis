package com.mimesis.dao;

import com.mimesis.dto.DTOTarjeta;
import com.mimesis.dto.ResultDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;


@Component
public class TarjetaDao {

    public Boolean consultaTarjeta(DTOTarjeta tarjeta){

        HttpHeaders headers =  new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String url = "http://20.213.81.205:8086/tarjetas/verificacion";
        HttpEntity<DTOTarjeta> httpEntity = new HttpEntity<>(tarjeta,headers);
        RestTemplate restTemplate = new RestTemplate();
        try{
            restTemplate.postForEntity(url,httpEntity,DTOTarjeta.class);
            System.out.println("TRY");
            return true;
        }catch (Exception e){
            System.out.println("CATCH");
            return false;
        }
    }
}
