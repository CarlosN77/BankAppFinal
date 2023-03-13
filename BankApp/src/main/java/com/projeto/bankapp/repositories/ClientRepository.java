package com.projeto.bankapp.repositories;

import com.projeto.bankapp.entities.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {



    ClientEntity findByNifAndPassword(Integer nif, String password);
    ClientEntity findByNif(Integer nif);


}
