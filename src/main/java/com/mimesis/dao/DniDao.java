package com.mimesis.dao;

import com.mimesis.dto.DTODni;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class DniDao {

    public Boolean ConsultarDNI(DTODni dni) {
        try{
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://dniruc.apisperu.com/api/v1/dni/"+ dni.getDni() +"?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IlRlYXRyb3NNaW1lc2lzMUBnbWFpbC5jb20ifQ.iVMwZdyRDDp0P4a3q1Jqn0fwgiIJgxvwNVSStolKK7Y";
            ResponseEntity<DTODni> responseMap = restTemplate.getForEntity(url, DTODni.class);
            DTODni dtoDni = responseMap.getBody();
            System.out.println(dtoDni);
            System.out.println("TRY");
            return true;
        }catch (Exception e){
            System.out.println("CATCH");
            return false;
        }
    }

}
