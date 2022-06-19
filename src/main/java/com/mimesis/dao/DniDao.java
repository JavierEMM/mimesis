package com.mimesis.dao;

import com.mimesis.dto.DTODni;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class DniDao {

    public List<DTODni> listarProductos(String dni) {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<DTODni[]> response = restTemplate.getForEntity(
                "https://dniruc.apisperu.com/api/v1/dni/"+ dni +"?token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJlbWFpbCI6IlRlYXRyb3NNaW1lc2lzMUBnbWFpbC5jb20ifQ.iVMwZdyRDDp0P4a3q1Jqn0fwgiIJgxvwNVSStolKK7Y", DTODni[].class);

        return Arrays.asList(response.getBody());
    }

}
