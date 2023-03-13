package com.projeto.bankapp.controllers;


import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.entities.CreditCardEntity;
import com.projeto.bankapp.entities.DebitCardEntity;
import com.projeto.bankapp.repositories.AccountRepository;
import com.projeto.bankapp.repositories.CreditCardRepository;
import com.projeto.bankapp.repositories.DebitCardRepository;
import com.projeto.bankapp.services.CreditCardService;
import com.projeto.bankapp.services.DebitCardService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@Controller
class CreditCardController {
    private final AccountRepository accountRepository;
    private final CreditCardRepository creditCardRepository;
    @Autowired
    private CreditCardService creditCardService;

    public CreditCardController(AccountRepository accountRepository, CreditCardRepository creditCardRepository, CreditCardService creditCardService) {
        this.accountRepository = accountRepository;
        this.creditCardRepository = creditCardRepository;
        this.creditCardService = creditCardService;
    }

    @PostMapping("/createcredit")
    public String createCreditCard(@RequestParam("accountNumber") long accountNumber,
                                   @RequestParam("creditLimit") double creditLimit,
                                   @ModelAttribute("creditCardForm") CreditCardController.CreditCardForm creditCardForm,
                                   HttpSession session, Model model) {
        AccountEntity account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account number"));

        ClientEntity client = (ClientEntity) session.getAttribute("cliente");
        if (client == null) {
            throw new IllegalArgumentException("No client logged in");
        }

        // Check if the account has the logged-in client as titular principal
        if (account.getTitularprincipal() != client.getNif()) {
            throw new IllegalArgumentException("The account does not belong to the logged-in client");
        }

        // Check if the client already has 3 credit cards
        List<CreditCardEntity> existingCreditCards = creditCardRepository.findAllByTitular(client.getNif());
        if (existingCreditCards.size() >= 3) {
            // The client already has 3 credit cards, return an error view
            model.addAttribute("errorMsg", "The maximum number of credit cards per client is 3");
            return "errorView";
        }

        // Generate a random credit card number
        Random random = new Random();
        int randomNum;
        boolean exists;
        do {
            randomNum = random.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
            exists = creditCardRepository.findByNumerodecartao(randomNum) != null;
        } while (exists);
        CreditCardEntity creditCard = new CreditCardEntity();
        creditCard.setNumerodecartao(randomNum);
        creditCard.setTitular(client.getNif());
        creditCard.setLimite(creditLimit);
        creditCard.setPin(creditCardForm.getPin());
        creditCard.setConta(account.getNumerodeconta());
        creditCardRepository.save(creditCard);

        // Add the accounts of the logged-in client to the model
        List<AccountEntity> accounts = accountRepository.findByTitularprincipal(client.getNif());
        model.addAttribute("accounts", accounts);
        int nif = client.getNif();
        model.addAttribute("nif", nif);

        return "redirect:/afterlogin";
    }


    @Getter
    @Setter
    public static class CreditCardForm {
        private int pin;
        private int limite;
    }

    @GetMapping("/creditCardLogin")
    public String creditCardLogin(@RequestParam int numerocartao, @RequestParam int pin, HttpSession session, Model model) throws AuthenticationException {
        CreditCardEntity creditCard = creditCardService.login(numerocartao,pin);
        if (creditCard == null) {
            throw new AuthenticationException("Cartão não encontrado");
        }

        session.setAttribute("creditCard", creditCard);
        model.addAttribute("creditCard", creditCard);
        return "redirect:/credithome";
    }

    @PostMapping("/depositcredit")
    public String depositcredit(@RequestParam("amounttodeposit") double amounttodeposit, HttpSession session) {
        CreditCardEntity creditCard = (CreditCardEntity) session.getAttribute("creditCard");
        if (creditCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/creditCardLogin";
        }
        AccountEntity account = accountRepository.findOneByNumerodeconta(creditCard.getConta());
        if (account == null) {
            // If the associated account is not found, display an error message
            return "error";
        }
        account.setSaldo(account.getSaldo() + amounttodeposit);
        accountRepository.save(account);
        return "redirect:/credithome";
    }
    @PostMapping("/creditwithdraw")
    public String creditwithdraw(@RequestParam("amounttowithdraw") double amounttowithdraw, HttpSession session) {
        CreditCardEntity creditCard = (CreditCardEntity) session.getAttribute("creditCard");
        if (creditCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/creditCardLogin";
        }
        AccountEntity account = accountRepository.findOneByNumerodeconta(creditCard.getConta());
        if (account == null) {
            // If the associated account is not found, display an error message
            return "error";
        }
        account.setSaldo(account.getSaldo() - amounttowithdraw);
        accountRepository.save(account);
        return "redirect:/credithome";
    }
    @PostMapping("/transfercredit")
    public String transfercredit(@RequestParam("accountNumber") int accountNumber, @RequestParam("amount") double amount, HttpSession session) {
        CreditCardEntity creditCard = (CreditCardEntity) session.getAttribute("creditCard");
        if (creditCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/creditCardLogin";
        }
        AccountEntity accountFrom = accountRepository.findOneByNumerodeconta(creditCard.getConta());
        if (accountFrom == null) {
            // If the associated account is not found, display an error message
            return "error";
        }
        AccountEntity accountTo = accountRepository.findOneByNumerodeconta(accountNumber);
        if (accountTo == null) {
            // If the target account is not found, display an error message
            return "error";
        }
        accountFrom.setSaldo(accountFrom.getSaldo() - amount);
        accountTo.setSaldo(accountTo.getSaldo() + amount);
        accountRepository.save(accountFrom);
        accountRepository.save(accountTo);
        return "redirect:/credithome";
    }


}
