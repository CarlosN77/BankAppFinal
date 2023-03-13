package com.projeto.bankapp.controllers;

import com.projeto.bankapp.entities.AccountEntity;
import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.repositories.AccountRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.naming.AuthenticationException;

@Controller
public class AfterLoginController {
    private final AccountRepository accountRepository;

    public AfterLoginController(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @GetMapping("/afterloginpage")
    public String afterlogin(){
        return "afterlogin.html";
    }
    @GetMapping("/createdebit")
    public String debitcardcreation(){
        return "createdebit.html";
    }
    @GetMapping("/createcredit")
    public String creditcardcreation(){
        return "createcredit.html";
    }


    @GetMapping("/cardspage")
    public String cards(){
        return "cards.html";
    }
    @GetMapping("/accountspage")
    public String vercontas(){
        return "accounts.html";
    }

    @GetMapping("/index")
    public String index(){
        return "index.html";
    }

    @GetMapping("/createaccount")
    public String createNewAccount(){
        return "createaccount.html";
    }


    @GetMapping("/createsectitular")
    public String createsectitular(){
        return "createsectitular.html";
    }
    @GetMapping("/transferpage")
    public String transferpage(){
        return "transfer.html";
    }
    @GetMapping("/cancelcardpage")
    public String cancelcardpage(){
        return "cancelcard.html";
    }
    @GetMapping("/debitcardlogin")
    public String debitCardLogin(){
        return "debitcardlogin.html";
    }
    @GetMapping("/login")
    public String login(){
        return "login.html";
    }
    @GetMapping ("/debithome")
    public String debithome (){
        return "debithome.html";

    }
    @GetMapping ("/depositdebit")
    public String depositdebit (){
        return "depositdebit.html";

    }
    @GetMapping ("/debitwithdraw")
    public String debitwithdraw (){
        return "debitwithdraw.html";

    }
    @GetMapping ("/transferdebit")
    public String transferdebit (){
        return "transferdebit.html";

    }

    @GetMapping("/creditcardlogin")
    public String creditcardlogin(){
        return "creditcardlogin.html";
    }
    @GetMapping("/credithome")
    public String credithome(){
        return "credithome.html";
    }
    @GetMapping("/creditwithdraw")
    public String creditwithdraw(){
        return "creditwithdraw.html";
    }
    @GetMapping("/depositcredit")
    public String depositcredit(){
        return "depositcredit.html";
    }
    @GetMapping("/transfercredit")
    public String transfercredit(){
        return "transfercredit.html";
    }
    @GetMapping("/register")
    public String register(){
        return "register.html";
    }



}
