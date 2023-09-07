package com.microservice.creditcard.service.mapper;

import com.microservice.creditcard.util.MovementDto;
import org.mapstruct.Mapper;

/**
 * Interfaz de mapstruct que mapea las clases.
 * */
@Mapper(componentModel = "spring")
public interface MapMovement {

  MovementDto setValues(Double amount, String clientDocument,
                        String cardNumber, String movementType);
}
