package com.microservice.creditcard.util;

import com.microservice.creditcard.model.Card;
import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase extiende de Card
 * Solo tendrá una instancia y cuando se llame dicha instancia
 * Se asignará un mensaje.
 * */
@Getter
@Setter
public class ErrorC extends Card {

  private static ErrorC instance;

  private ErrorC() {

  }

  /**
   * Esta método permite genera una sola instancia
   * Permite asignarle un valor a la variable mensaje.
   * */
  public static ErrorC getInstance(String mensaje) {
    if (instance == null) {
      instance = new ErrorC();
    }
    instance.setMessage(mensaje);
    return instance;
  }
}
