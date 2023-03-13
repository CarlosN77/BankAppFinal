package com.projeto.bankapp.controllers;

import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.repositories.AccountRepository;
import com.projeto.bankapp.repositories.ClientRepository;
import com.projeto.bankapp.services.AccountService;
import com.projeto.bankapp.services.ClientService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.naming.AuthenticationException;
import java.time.LocalDate;
import java.time.Period;
import java.util.*;

@Controller
public class  ClientController {

    private final ClientRepository clientRepository;
    private final ClientService clientservice;
    private final AccountService accountservice;
    private final AccountRepository accountRepository;


    @Autowired
    public ClientController(ClientRepository clientRepository, ClientService clientservice, AccountService accountservice, AccountRepository accountRepository){
        this.clientRepository = clientRepository;
        this.clientservice = clientservice;
        this.accountservice = accountservice;
        this.accountRepository = accountRepository;
    }


    @RequestMapping("/register")
    public ModelAndView registerForm() {
        ModelAndView modelAndView = new ModelAndView("register");
        return modelAndView;
    }

    @RequestMapping("/afterlogin")
    public ModelAndView afterregisterForm() {
        ModelAndView modelAndView = new ModelAndView("afterlogin");
        return modelAndView;
    }

    @PostMapping("/register")
    public ModelAndView register(
            @RequestParam String primeironome,
            @RequestParam String segundonome,
            @RequestParam String datanascimento,
            @RequestParam int telefone,
            @RequestParam int telemovel,
            @RequestParam String email,
            @RequestParam String profissao,
            @RequestParam int nif,
            @RequestParam String password) {

        // check if client with the same NIF already exists
        ClientEntity existingClient = clientRepository.findByNif(nif);
        if (existingClient != null) {
            // a client with the same NIF already exists, return an error view
            ModelAndView mav = new ModelAndView("a");
            mav.addObject("errorMsg", "A client with the same NIF already exists");
            return mav;
        }

        // calculate age of client
        LocalDate birthdate = LocalDate.parse(datanascimento);
        LocalDate now = LocalDate.now();
        int age = Period.between(birthdate, now).getYears();
        if (age < 18) {
            // client is less than 18 years old, return an error view
            ModelAndView mav = new ModelAndView("a");
            mav.addObject("errorMsg", "You must be at least 18 years old to create an account");
            return mav;
        }

        // create a new client account
        ClientEntity client = new ClientEntity();
        Random rand = new Random();
        int accountNumber = rand.nextInt(9000) + 1000; // generate random 4-digit account number
        AccountEntity account = new AccountEntity();
        account.setNumerodeconta(accountNumber);
        account.setTitularprincipal(nif);
        account.setSaldo(50.0);
        client.setPrimeironome(primeironome);
        client.setSegundonome(segundonome);
        client.setPassword(password);
        client.setNif(nif);
        client.setEmail(email);
        client.setTelefone(telefone);
        client.setTelemovel(telemovel);
        client.setDatanascimento(datanascimento);
        client.setProfissao(profissao);

        accountRepository.save(account);
        clientservice.register(primeironome, segundonome,datanascimento, telefone,telemovel, email, profissao, nif, password);

        return new ModelAndView("login");
    }



    @PostMapping("/login")
    public String login(
            @RequestParam Integer nif,
            @RequestParam String password,
            HttpSession session, Model model) throws AuthenticationException {
        ClientEntity cliente = clientservice.login(nif, password);
        if (cliente == null) {
            throw new AuthenticationException("Invalid login credentials");
        }
        else {
            session.setAttribute("cliente", cliente);
            model.addAttribute("cliente", cliente);
            return "redirect:/afterlogin";
        }
    }


    @GetMapping("/afterlogin")
    public String afterlogin(HttpSession session, Model model) throws AuthenticationException {
        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");
        if (cliente == null) {
            throw new AuthenticationException("User not logged in");
        }
        model.addAttribute("cliente", cliente);

        List<AccountEntity> accounts = accountRepository.findByTitularprincipal(cliente.getNif());

        return "afterlogin";
    }




    @RequestMapping ("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @GetMapping
    public List<ClientEntity> getAllClients() {
        return clientRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientEntity> getClientById(@PathVariable Long id) {
        ClientEntity client = clientRepository.findById(String.valueOf(id)).orElse(null);
        if (client == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(client);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClientEntity createClient(@RequestBody ClientEntity client) {
        return clientRepository.save(client);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        if (!clientRepository.existsById(String.valueOf(id))) {
            return ResponseEntity.notFound().build();
        }
        clientRepository.deleteById(String.valueOf(id));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void returnNoFavicon() {
        // Do nothing, just return an empty response body
    }



}