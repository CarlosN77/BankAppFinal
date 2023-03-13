package com.projeto.bankapp.services;

import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.repositories.AccountRepository;
import com.projeto.bankapp.repositories.ClientRepository;
import com.projeto.bankapp.entities.ClientEntity;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
@Transactional
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private AccountRepository accountRepository;

    public void register(String primeironome, String segundonome, String datanascimento,
                         int telefone, int telemovel, String email, String profissao, int nif,
                         String password) {
        ClientEntity client = new ClientEntity();
        client.setPrimeironome(primeironome);
        client.setSegundonome(segundonome);
        client.setDatanascimento(datanascimento);
        client.setTelefone(telefone);
        client.setTelemovel(telemovel);
        client.setEmail(email);
        client.setProfissao(profissao);
        client.setNif(nif);
        client.setPassword(password);
        clientRepository.save(client);
    }

    public ClientEntity login(Integer nif, String password) throws AuthenticationException {
        return clientRepository.findByNifAndPassword(nif, password);
    }

}



