package com.projeto.bankapp.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {

    @Id
    @Column(name = "numerodeconta", nullable = false)
    private Integer numerodeconta;

    @Column(nullable = false)
    private int titularprincipal;

    @Column(nullable = false)
    private double saldo;

    @ElementCollection
    @CollectionTable(name = "titulares_secundarios", joinColumns = @JoinColumn(name = "numerodeconta"))
    @Column(name = "titular_secundario")
    private List<Integer> titularessecundarios;


    @Override
    public String toString() {
        return "AccountEntity{" +
                "numerodeconta=" + numerodeconta +
                ", saldo=" + saldo +
                ", titularprincipal='" + titularprincipal + '\'' +
                ", titularessecundarios=" + titularessecundarios +
                '}';
    }

}
