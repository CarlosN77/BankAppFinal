package com.projeto.bankapp.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientEntity {

    @Id
    @Column(name = "nif", nullable = false)
    private Integer nif;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String primeironome;

    @Column(nullable = false)
    private String segundonome;

    @Column(nullable = false)
    private String datanascimento;

    @Column(nullable = false)
    private int telefone;

    @Column(nullable = false)
    private int telemovel;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String profissao;





}
