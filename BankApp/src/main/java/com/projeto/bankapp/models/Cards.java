package com.projeto.bankapp.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public abstract class Cards {

    public int numconta;
    public static int pin;
    public int numerodecartao;

}
