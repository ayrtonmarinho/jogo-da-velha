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
public class Tabuleiro implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private String[] bt;
    private Jogador player1;
    private Jogador player2;
    private int plc1;
    private int plc2;
    private int nJogadas;
    private int vezJogador;
    private int[] btStatus;
    
    public Tabuleiro(Jogador player1, Jogador player2){
        this.bt = new String[9];
        this.btStatus = new int[9];
        this.player1 = player1;
        this.player2 = player2;
    }
    
    public Tabuleiro(){
        this.bt = new String[9];
    }

    public String getBt(int i) {
        return bt[i];
    }

    public void setBt(String valor, int i) {
        this.bt[i] = valor;
    }

    public Jogador getPlayer1() {
        return player1;
    }

    public void setPlayer1(Jogador player1) {
        this.player1 = player1;
    }

    public Jogador getPlayer2() {
        return player2;
    }

    public void setPlayer2(Jogador player2) {
        this.player2 = player2;
    }

    public int getPlc1() {
        return plc1;
    }

    public void setPlc1(int plc1) {
        this.plc1 = plc1;
    }

    public int getPlc2() {
        return plc2;
    }

    public void setPlc2(int plc2) {
        this.plc2 = plc2;
    }

    public int getnJogadas() {
        return nJogadas;
    }

    public void setnJogadas(int nJogadas) {
        this.nJogadas = nJogadas;
    }

    public int getVezJogador() {
        return vezJogador;
    }

    public void setVezJogador(int vezJogador) {
        this.vezJogador = vezJogador;
    }

    @Override
    public String toString() {
        return "Tabuleiro{" + "bt=" + bt + ", player1=" + player1 + ", player2=" + player2 + ", plc1=" + plc1 + ", plc2=" + plc2 + ", nJogadas=" + nJogadas + '}';
    }

    public int getBtStatus(int i) {
        return btStatus[i];
    }

    public void setBtStatus(int i, int valor) {
        this.btStatus[i] = valor;
    }
    
        
    
    
}
