package com.projeto.bankapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "cartaodebito")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class DebitCardEntity {

    @Id
    @Column(name = "numero_de_cartao", nullable = false)
    private int numerodecartao;

    @Column(name = "conta")
    private int conta;
    @Column(name = "titular")
    private int titular;
    @Column(name = "pin")
    private int pin;


}
