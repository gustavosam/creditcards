package com.microservice.creditcard.service.mapper;

import com.microservice.creditcard.util.MovementDto;

/**
 * Esta clase contiene un método que genera un objeto MovementsDocuments
 * Para mandar esa información al microservicio de movements
 * y posteriormente guardar el movimiento en mongo db.
 * */
public class MapperMovement {

  /**
   * Esta método recibe como parámetro información para guardar movimientos
   * Se obtienen todos los valores y se asignan a un objeto MovementsDocuments.
   * */
  public static MovementDto setValues(Double amount, String clientDocument,
                                      String cardNumber, String movementType) {

    MovementDto movement = new MovementDto();
    movement.setAmount(amount);
    movement.setClientDocument(clientDocument);
    movement.setCardNumber(cardNumber);
    movement.setMovementType(movementType);

    return movement;
  }
}
