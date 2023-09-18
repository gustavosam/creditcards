package com.microservice.creditcard.util;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase permite almancenar la información obtenida
 * en el microservicio de clientes.
 * */

@Getter
@Setter
public class ClientDto {

  private String document;

  private String name;

  private String clientType;

  private String email;

  private Boolean isActive;

  private Boolean expiredDebt;

  private LocalDate clientCreationDate;
}
