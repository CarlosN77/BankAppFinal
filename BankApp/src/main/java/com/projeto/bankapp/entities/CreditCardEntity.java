package com.projeto.bankapp.entities;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cartaocredito")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreditCardEntity {

    @Id
    @Column(name = "numero_de_cartao", nullable = false)
    private int numerodecartao;

    @Column
    private int titular;

    @Column(name = "conta")
    private int conta;

    @Column(name = "pin")
    private int pin;

    @Column(name = "limite")
    private double limite;

}
