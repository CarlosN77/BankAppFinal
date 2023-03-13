package com.projeto.bankapp.controllers;


import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.entities.CreditCardEntity;
import com.projeto.bankapp.entities.DebitCardEntity;
import com.projeto.bankapp.models.Cards;
import com.projeto.bankapp.models.ClientDTO;
import com.projeto.bankapp.repositories.AccountRepository;
import com.projeto.bankapp.repositories.CreditCardRepository;
import com.projeto.bankapp.repositories.DebitCardRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
class CardsController {

    private final AccountRepository accountRepository;
    private final DebitCardRepository debitCardRepository;
    private final CreditCardRepository creditCardRepository;

    CardsController(AccountRepository accountRepository, DebitCardRepository debitCardRepository, CreditCardRepository creditCardRepository) {
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
        this.creditCardRepository = creditCardRepository;
    }
    @GetMapping("/cards")
    public String showCards(HttpSession session, Model model) {
        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");
        if (cliente == null) {
            throw new IllegalArgumentException("You must be logged in to access this page");
        }
        String clientName = cliente.getPrimeironome() + " " + cliente.getSegundonome(); // Get the client's name
        List<AccountEntity> accounts = accountRepository.findByTitularprincipal(cliente.getNif());
        List<DebitCardEntity> debitCards = new ArrayList<>();
        List<CreditCardEntity> creditCards = new ArrayList<>();

        for (AccountEntity account : accounts) {
            DebitCardEntity debitCard = debitCardRepository.findByConta(account.getNumerodeconta());
            if (debitCard != null) {
                debitCards.add(debitCard);
            }
            CreditCardEntity creditCard = creditCardRepository.findByConta(account.getNumerodeconta());
            if (creditCard != null) {
                creditCards.add(creditCard);
            }
        }
        model.addAttribute("clientName", clientName); // Add the client's name to the model
        model.addAttribute("debitCards", debitCards);
        model.addAttribute("creditCards", creditCards);

        return "cards";
    }







}


