package com.projeto.bankapp.controllers;

import com.projeto.bankapp.entities.ClientEntity;
import com.projeto.bankapp.repositories.ClientRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class DetailsController {
    private final ClientRepository clientRepository;

    public DetailsController(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }


    @GetMapping("/clientdetails")
    public String updateClientDetails(HttpSession session, @RequestParam(name = "primeironome", required = false) String primeironome,
                                      @RequestParam(name = "segundonome", required = false) String segundonome,
                                      @RequestParam(name = "password", required = false) String password,
                                      @RequestParam(name = "telefone", required = false) Integer telefone,
                                      @RequestParam(name = "telemovel", required = false) Integer telemovel,
                                      @RequestParam(name = "email", required = false) String email,
                                      @RequestParam(name = "profissao", required = false) String profissao,
                                      Model model) {

        // Check if the session is still valid
        if (session == null || session.getAttribute("cliente") == null || session.isNew()) {
            return "redirect:/login";
        }

        // Get the client from the session
        ClientEntity cliente = (ClientEntity) session.getAttribute("cliente");

        // Get the client's details from the database
        ClientEntity dbClient = clientRepository.findByNif(cliente.getNif());
        if (dbClient == null) {
            throw new IllegalArgumentException("Invalid client ID");
        }

        // Update the client's details with any provided parameters
        if (primeironome != null && !primeironome.isEmpty()) {
            dbClient.setPrimeironome(primeironome);
        }
        if (segundonome != null && !segundonome.isEmpty()) {
            dbClient.setSegundonome(segundonome);
        }
        if (password != null && !password.isEmpty()) {
            dbClient.setPassword(password);
        }
        if (telefone != null) {
            dbClient.setTelefone(telefone);
        }
        if (telemovel != null) {
            dbClient.setTelemovel(telemovel);
        }
        if (email != null && !email.isEmpty()) {
            dbClient.setEmail(email);
        }
        if (profissao != null && !profissao.isEmpty()) {
            dbClient.setProfissao(profissao);
        }

        // Save the updated client details to the database
        clientRepository.save(dbClient);
        model.addAttribute("cliente", dbClient);
        // Add a success message to the model
        model.addAttribute("message", "Client details updated successfully!");

        // Return the client details page
        return "clientdetails";
    }

}



