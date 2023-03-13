package com.projeto.bankapp.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Account {

    public int numerodeconta;
    public int titularPrincipal;
    public double saldo;
}
