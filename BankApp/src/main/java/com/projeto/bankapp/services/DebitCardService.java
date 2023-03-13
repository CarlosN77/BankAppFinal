package com.projeto.bankapp.services;

import com.projeto.bankapp.entities.DebitCardEntity;
import com.projeto.bankapp.repositories.DebitCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;

@Service
public class DebitCardService {

    @Autowired
    private DebitCardRepository debitCardRepository;

    public DebitCardEntity createDebitCard(int numerodecartao, int numconta, int pin) {
        DebitCardEntity debitCard = new DebitCardEntity();
        debitCard.setNumerodecartao(numerodecartao);
        debitCard.setConta(numconta);
        debitCard.setPin(pin);
        return debitCardRepository.save(debitCard);
    }
    public DebitCardEntity login(Integer numerocartao, int pin) throws AuthenticationException {
        return debitCardRepository.findByNumerodecartaoAndPin(numerocartao,pin);
    }
}
