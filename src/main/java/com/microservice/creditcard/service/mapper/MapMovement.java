package com.microservice.creditcard.service.mapper;

import com.microservice.creditcard.util.MovementDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface MapMovement {

  MovementDto setValues(Double amount, String clientDocument,
                        String cardNumber, String movementType);
}
