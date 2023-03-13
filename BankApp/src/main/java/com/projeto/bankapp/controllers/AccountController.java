package com.projeto.bankapp.controllers;

import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.repositories.AccountRepository;

import com.projeto.bankapp.repositories.ClientRepository;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


@Controller
public class AccountController {

    @Autowired
    private final AccountRepository accountRepository;
    @Autowired
    private final ClientRepository clientRepository;


    public AccountController(AccountRepository accountRepository, ClientRepository clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    private static final Logger logger = LoggerFactory.getLogger(AccountController.class);

    @Transactional
    @GetMapping("/b")
    public String addSecondaryHolder(int accountNumber, int secondaryHolderNif) {
        // Find the account entity by its account number
        AccountEntity account = accountRepository.findOneByNumerodeconta(accountNumber);

        // If the account entity was found, add the secondary holder's NIF to the list of secondary holders
        if (account != null) {
            List<Integer> secondaryHolders = account.getTitularessecundarios();
            if (secondaryHolders == null) {
                secondaryHolders = new ArrayList<>();
            }
            secondaryHolders.add(secondaryHolderNif);
            account.setTitularessecundarios(secondaryHolders);

            // Update the account entity in the database
            accountRepository.save(account);
            logger.info("Secondary holder with NIF " + secondaryHolderNif + " added to account " + accountNumber);
        } else {
            logger.warn("Account " + accountNumber + " not found");
        }
        return "redirect:/afterlogin";
    }


    @GetMapping("/accounts")
    public String getAccountsForClient(HttpSession session, Model model) throws AuthenticationException {
        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");
        if (cliente == null) {
            throw new AuthenticationException("User not logged in");
        }

        List<AccountEntity> accounts = accountRepository.findByTitularprincipalOrTitularessecundariosContaining(cliente.getNif(), cliente.getNif());
        if (accounts.isEmpty()) {
            System.out.println("No accounts found");
        } else {
            List<CustomObject> customObjects = new ArrayList<>();
            for (AccountEntity account : accounts) {
                // Get the titular principal's name based on the NIF
                ClientEntity titularPrincipal = clientRepository.findByNif(account.getTitularprincipal());
                if (titularPrincipal != null) {
                    String primPrimeiroNome = titularPrincipal.getPrimeironome();
                    String primSegundoNome = titularPrincipal.getSegundonome();

                    // Get the list of secondary holders' names based on their NIFs
                    List<Integer> secondaryHolders = account.getTitularessecundarios();
                    List<String> secPrimeiroNome = null;
                    List<String> secSegundoNome = null;
                    if (secondaryHolders != null && !secondaryHolders.isEmpty()) {
                        secPrimeiroNome = new ArrayList<>();
                        secSegundoNome = new ArrayList<>();
                        for (Integer secondaryHolderNif : secondaryHolders) {
                            ClientEntity secondaryHolder = clientRepository.findByNif(secondaryHolderNif);
                            if (secondaryHolder != null) {
                                secPrimeiroNome.add(secondaryHolder.getPrimeironome());
                                secSegundoNome.add(secondaryHolder.getSegundonome());
                            }
                        }

                    }
                    CustomObject customObject = new CustomObject(primPrimeiroNome, primSegundoNome, secPrimeiroNome, secSegundoNome);
                    customObjects.add(customObject);
                }
            }
            model.addAttribute("customObjects", customObjects);
        }

        model.addAttribute("accounts", accounts);
        return "accounts";
    }
    @GetMapping("/createNewAccount")
    public String createNewAccount(HttpSession session) throws AuthenticationException, Exception {
        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");
        if (cliente == null) {
            throw new AuthenticationException("User not logged in");
        }

        // Check if the client already has three accounts with the same titularprincipal
        int numAccounts = accountRepository.countByTitularprincipal(cliente.getNif());
        if (numAccounts >= 3) {
            throw new Exception("Client already has three accounts");
        }

        // Generate a unique 4-digit account number
        Random random = new Random();
        int accountNumber;
        boolean exists;
        do {
            accountNumber = random.nextInt(9000) + 1000; // Generates a number between 1000 and 9999
            exists = accountRepository.findAllByNumerodeconta(accountNumber) != null;
        } while (!exists);

        // Create the new account entity and set its properties
        AccountEntity newAccount = new AccountEntity();
        newAccount.setNumerodeconta(accountNumber);
        newAccount.setSaldo(50);
        newAccount.setTitularprincipal(cliente.getNif());

        // Save the new account to the database
        logger.info("Saving new account to database...");
        accountRepository.save(newAccount);
        logger.info("New account saved to database.");

        return "redirect:/afterlogin"; // Redirect to the account created page
    }
    @PostMapping("/transfer")
    public String transferAmount(HttpSession session, @RequestParam int fromAccount, @RequestParam int toAccount,
                                 @RequestParam double amount, Model model) throws Exception, AuthenticationException {

        ClientEntity loggedInClient = (ClientEntity) session.getAttribute("cliente");
        if (loggedInClient == null) {
            throw new AuthenticationException("User not logged in");
        }

        AccountEntity from = accountRepository.findByNumerodecontaAndTitularprincipal(fromAccount, loggedInClient.getNif());
        if (from == null) {
            throw new Exception("Invalid source account number or not authorized to transfer from this account");
        }

        AccountEntity to = accountRepository.findOneByNumerodeconta(toAccount);
        if (to == null) {
            throw new Exception("Invalid destination account number");
        }

        if (from.getSaldo() < amount) {
            throw new Exception("Insufficient funds");
        }

        from.setSaldo(from.getSaldo() - amount);
        to.setSaldo(to.getSaldo() + amount);

        accountRepository.save(from);
        accountRepository.save(to);

        return "redirect:/accounts";
    }


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    class CustomObject {
        private String primPrimeiroNome;
        private String primSegundoNome;
        private List<String> secPrimeiroNome;
        private List<String> secSegundoNome;







    }

    @GetMapping("/transfer")
    public String showTransferForm(Model model) {
        model.addAttribute("transferForm", new TransferForm());
        return "transfer";
    }
    public class TransferForm {
        private int fromAccount;
        private int toAccount;
        private double amount;

        // getters and setters
    }
}


