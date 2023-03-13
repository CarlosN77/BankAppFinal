package com.projeto.bankapp.repositories;

import com.projeto.bankapp.entities.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<AccountEntity, Long> {
    // ...
    List<AccountEntity> findByTitularprincipal(Integer nif);
    List<AccountEntity> findAllByNumerodeconta(Integer numerodeconta);
    AccountEntity findOneByNumerodeconta(Integer numerodeconta);
    List<AccountEntity> findByTitularprincipalOrTitularessecundariosContaining(Integer titularPrincipal, Integer secondaryHolder);
    AccountEntity findByNumerodecontaAndTitularprincipal(int accountnumber, Integer nif);


    Integer countByTitularprincipal(Integer nif);


}
