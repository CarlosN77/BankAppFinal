package com.projeto.bankapp.controllers;

import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.entities.DebitCardEntity;
import com.projeto.bankapp.repositories.AccountRepository;
import com.projeto.bankapp.repositories.DebitCardRepository;
import com.projeto.bankapp.services.DebitCardService;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.List;
import java.util.Random;

@Controller
public class DebitCardController {

    private final AccountRepository accountRepository;
    private final DebitCardRepository debitCardRepository;
    @Autowired
    private DebitCardService debitCardService;

    public DebitCardController(AccountRepository accountRepository, DebitCardRepository debitCardRepository, DebitCardService debitCardService) {
        this.accountRepository = accountRepository;
        this.debitCardRepository = debitCardRepository;
        this.debitCardService = debitCardService;
    }




    @PostMapping("/createdebit")
    public String createDebitCard(@RequestParam("accountNumber") long accountNumber,
                                  @ModelAttribute("debitCardForm") DebitCardForm debitCardForm,
                                  HttpSession session, Model model) {
        AccountEntity account = accountRepository.findById(accountNumber)
                .orElseThrow(() -> new IllegalArgumentException("Invalid account number"));

        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");
        if (cliente != null) {
            // Check if the account has the logged-in client as titular principal
            if (account.getTitularprincipal() != cliente.getNif()) {
                throw new IllegalArgumentException("The account does not belong to the logged-in client");
            }
        }

        // Check if there is already a debit card associated with the account
        DebitCardEntity existingDebitCard = debitCardRepository.findByConta(accountNumber);
        if (existingDebitCard != null) {
            // A debit card for this account already exists, return an error view
            model.addAttribute("errorMsg", "A debit card for this account already exists");
            return "a";
        }

        Random random = new Random();
        int randomNum;
        boolean exists;
        do {
            randomNum = random.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
            exists = debitCardRepository.findByNumerodecartao(randomNum) != null;
        } while (exists);
        DebitCardEntity debitCard = new DebitCardEntity();
        debitCard.setNumerodecartao(randomNum);
        debitCard.setTitular(cliente.getNif());
        debitCard.setConta(account.getNumerodeconta());
        debitCard.setPin(debitCardForm.getPin());
        debitCardRepository.save(debitCard);

        if (cliente != null) {
            // Get the accounts of the logged-in client
            List<AccountEntity> accounts = accountRepository.findByTitularprincipal(cliente.getNif());
            model.addAttribute("accounts", accounts);
            int nif = cliente.getNif();
            model.addAttribute("nif", nif);
        }

        return "redirect:/afterlogin";
    }


    @GetMapping("/debitCardLogin")
    public String debitCardLogin(@RequestParam int numerocartao, @RequestParam int pin, HttpSession session, Model model) throws AuthenticationException {
        DebitCardEntity debitCard = debitCardService.login(numerocartao,pin);
        if (debitCard == null) {
            throw new AuthenticationException("Cartão não encontrado");
        }

        session.setAttribute("debitCard", debitCard);
        model.addAttribute("debitCard", debitCard);
        return "redirect:/debithome";
    }

    @PostMapping("/deposit")
    public String deposit(@RequestParam("amounttodeposit") double amounttodeposit, HttpSession session) {
        DebitCardEntity debitCard = (DebitCardEntity) session.getAttribute("debitCard");
        if (debitCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/debitCardLogin";
        }
        AccountEntity account = accountRepository.findOneByNumerodeconta(debitCard.getConta());
        if (account == null) {
            // If the associated account is not found, display an error message
            return "error";
        }
        account.setSaldo(account.getSaldo() + amounttodeposit);
        accountRepository.save(account);
        return "redirect:/debithome";
    }
    @PostMapping("/withdraw")
    public String withdraw(@RequestParam("amounttowithdraw") double amounttowithdraw, HttpSession session) {
        DebitCardEntity debitCard = (DebitCardEntity) session.getAttribute("debitCard");
        if (debitCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/debitCardLogin";
        }
        AccountEntity account = accountRepository.findOneByNumerodeconta(debitCard.getConta());
        if (account == null) {
            // If the associated account is not found, display an error message
            return "error";
        }
        account.setSaldo(account.getSaldo() - amounttowithdraw);
        accountRepository.save(account);
        return "redirect:/debithome";
    }
    @PostMapping("/transferdebit")
    public String transferdebit(@RequestParam("accountNumber") int accountNumber, @RequestParam("amount") double amount, HttpSession session) {
        DebitCardEntity debitCard = (DebitCardEntity) session.getAttribute("debitCard");
        if (debitCard == null) {
            // If no card is currently logged in, redirect to login page
            return "redirect:/debitCardLogin";
        }
        AccountEntity accountFrom = accountRepository.findOneByNumerodeconta(debitCard.getConta());
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
        return "redirect:/debithome";
    }








    @Getter
    @Setter
    public static class DebitCardForm {
        private int pin;
    }



}