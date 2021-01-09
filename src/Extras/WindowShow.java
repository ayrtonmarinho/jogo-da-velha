/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Extras;

import Base.Jogador;
import Base.Tabuleiro;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author iTuhh Z
 */
public class WindowShow {

    static String nameP1, nameP2;
    static boolean resp;

    public static boolean confirmBox(String title, String mensagem){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(title);
        window.setWidth(300);
        window.setHeight(300);
       
        
        Label msg = new Label();
        msg.setText(mensagem);
        
        Button sim = new Button("Sim");
        Button nao = new Button("Não");
        
        sim.setOnAction(e -> {
            resp = true;
            window.close();
        });
        
        nao.setOnAction(e -> {
            resp = false;
            window.close();
        });
        VBox layout = new VBox(10);
        HBox layout2 = new HBox(10);
        layout2.getChildren().addAll(sim, nao);
        //layout2.setPadding(new Insets(50,10,10,10));
        layout2.setAlignment(Pos.CENTER);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(msg,layout2);
        
        Scene alerta = new Scene(layout);
        window.setScene(alerta);
        window.showAndWait();
        
        return resp;
    }

    public static void exibirResultado(String titulo, String mensagem) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle(titulo);
        window.setWidth(300);
        window.setHeight(300);

        Label msg = new Label();
        msg.setText(mensagem);
        Button fecharBt = new Button("Fechar");
        fecharBt.setOnAction(e -> window.close());

        VBox layout = new VBox(20);
        layout.getChildren().addAll(msg, fecharBt);
        layout.setAlignment(Pos.CENTER);

        Scene alerta = new Scene(layout);
        window.setScene(alerta);
        window.showAndWait();
    }
}
