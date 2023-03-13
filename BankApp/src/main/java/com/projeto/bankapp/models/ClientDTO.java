package com.projeto.bankapp.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ClientDTO {

    private int nif;
    private String senha;
    private String primeironome;
    private String segundonome;
    private Account conta;
    private String datanascimento;
    private int telefone;
    private int telemovel;
    private String email;
    private String profissao;
}
