/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Base;

import java.io.Serializable;

/**
 *
 * @author iTuhh Z
 */
public class Jogador implements Serializable {
    private String nome;
    private int idJogada;
    private String simbolo;

    public Jogador(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public int getIdJogada() {
        return idJogada;
    }

    public void setIdJogada(int idJogada) {
        this.idJogada = idJogada;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getSimbolo() {
        return simbolo;
    }

    public void setSimbolo(String simbolo) {
        this.simbolo = simbolo;
    }

    @Override
    public String toString() {
        return "Jogador{" + "nome=" + nome + ", idJogada=" + idJogada + ", simbolo=" + simbolo + '}';
    }
    
    
    
}