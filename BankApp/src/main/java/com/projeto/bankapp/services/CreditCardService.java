package com.projeto.bankapp.services;

import com.projeto.bankapp.entities.CreditCardEntity;
import com.projeto.bankapp.entities.DebitCardEntity;
import com.projeto.bankapp.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class CreditCardService {

    @Autowired
    private CreditCardRepository creditCardRepository;

    public CreditCardEntity createCreditCard(int numerodecartao, int numconta, int pin, double limite) {
        CreditCardEntity creditCard = new CreditCardEntity();
        creditCard.setNumerodecartao(numerodecartao);
        creditCard.setConta(numconta);
        creditCard.setPin(pin);
        creditCard.setLimite(limite);
        return creditCardRepository.save(creditCard);
    }
    public CreditCardEntity login(Integer numerocartao, int pin) throws AuthenticationException {
        return creditCardRepository.findByNumerodecartaoAndPin(numerocartao,pin);
    }
}

