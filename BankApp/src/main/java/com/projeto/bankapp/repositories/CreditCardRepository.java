package com.projeto.bankapp.repositories;

import com.projeto.bankapp.entities.CreditCardEntity;
import com.projeto.bankapp.entities.DebitCardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CreditCardRepository extends JpaRepository<CreditCardEntity, Integer> {
    CreditCardEntity findByTitular(int nif);
    List<CreditCardEntity> findAllByTitular(int nif);
    CreditCardEntity findByNumerodecartao(int numerodecartao);
    List<CreditCardEntity> findAllByNumerodecartao(int numerodecartao);
    CreditCardEntity findByNumerodecartaoAndTitular(int numerodecartao, int titular);
    CreditCardEntity findByConta(long conta);
    CreditCardEntity findByNumerodecartaoAndPin(int numerodecartao, int pin);

}

