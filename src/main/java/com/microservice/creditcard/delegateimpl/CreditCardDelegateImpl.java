package com.microservice.creditcard.delegateimpl;

import com.microservice.creditcard.api.CardApiDelegate;
import com.microservice.creditcard.model.Card;
import com.microservice.creditcard.model.CardConsume;
import com.microservice.creditcard.model.CardPayment;
import com.microservice.creditcard.model.CardRequest;
import com.microservice.creditcard.service.CreditCardService;
import com.microservice.creditcard.util.ClaseError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardDelegateImpl implements CardApiDelegate {

    @Autowired
    private CreditCardService creditCardService;

    @Override
    public ResponseEntity<Card> createCreditCard(CardRequest cardRequest){

        if(cardRequest.getCustomerDocument().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El ID del cliente no puede estar vacío"));
        }

        if(cardRequest.getCardAmount() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El monto no puede estar vacío"));
        }

        return creditCardService.customerRequestExist(cardRequest.getCustomerDocument())
                ? ResponseEntity.status(HttpStatus.OK).body(creditCardService.createCard(cardRequest))
                : ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El ID del cliente no existe"));
    }

    @Override
    public ResponseEntity<Card> consumeCreditCard(CardConsume cardConsume){

        if(cardConsume.getCardNumber() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El número de tarjeta puede estar vacío"));
        }

        if(cardConsume.getCardConsumeAmount() == null || cardConsume.getCardConsumeAmount() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El monto a consumir no puede estar vacío"));
        }

        if(! creditCardService.validateIfCardExist(cardConsume.getCardNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("La tarjeta que ingresaste no existe"));
        }

        if(! creditCardService.validateCardAmountAvailable(cardConsume.getCardNumber(), cardConsume.getCardConsumeAmount())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("Saldo insuficiente"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(creditCardService.cardConsume(cardConsume.getCardNumber(), cardConsume.getCardConsumeAmount()));
    }

    @Override
    public ResponseEntity<Card> payCreditCard(CardPayment cardPayment){

        if(cardPayment.getCardNumber() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El número de tarjeta no puede estar vacío"));
        }

        if(cardPayment.getCardPaymentAmount() == null || cardPayment.getCardPaymentAmount() == 0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("El monto a pagar no puede estar vacío"));
        }

        if(! creditCardService.validateIfCardExist(cardPayment.getCardNumber())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("La tarjeta que ingresaste no existe"));
        }

        if(! creditCardService.validateIfYouCanPayCard(cardPayment.getCardNumber(), cardPayment.getCardPaymentAmount())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ClaseError.getInstance("Verifica tu monto consumido para realizar el pago correcto"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(creditCardService.cardPayment(cardPayment.getCardNumber(), cardPayment.getCardPaymentAmount()));

    }

    @Override
    public ResponseEntity<List<Card>> getCreditCardsByCustomer(String customerDocument){
        return ResponseEntity.status(HttpStatus.OK).body(creditCardService.getCardsByClient(customerDocument));
    }
}
