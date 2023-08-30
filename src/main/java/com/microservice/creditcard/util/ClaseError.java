package com.microservice.creditcard.util;

import com.microservice.creditcard.model.Card;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClaseError extends Card {

    private  String mensajeError;
    private static ClaseError instance;



    private ClaseError(){

    }

    public static ClaseError getInstance(String mensaje){
        if(instance == null){
            instance = new ClaseError();
        }
        instance.mensajeError=mensaje;
        return instance;
    }
}
