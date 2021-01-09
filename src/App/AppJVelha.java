/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package App;

import Base.Jogador;
import Base.Tabuleiro;
import Extras.ResourceManager;
import Extras.WindowShow;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.JOptionPane;

/**
 *
 * @author iTuhh Z
 */
public class AppJVelha extends Application {

    Stage window;
    Scene cena1, cena2, cena3, cenaLoad;
    Button[] bt;
    Button continuar, loadbt;
    int btStatus[];
    ArrayList<String> listaSaves;
    private int vezJogador = 0;
    private String simbolo = "X";
    private Tabuleiro tabuleiro;
    private int nJogadas = 0;
    boolean win = false, velha = false;
    private String nameP1, nameP2;
    private Jogador pl1, pl2;
    ListView<String> listV;
    Label p1, p2, result;
    int placar1 = 0, placar2 = 0;
    File file = new File("ListaSave.dwp");

    @Override
    public void start(Stage primaryStage) {
        window = primaryStage;
        window.setResizable(false);

        //Icone
        //Status do botão.
        btStatus = new int[9];
        for (int i = 0; i < 9; i++) {
            btStatus[i] = 0;
        }

        //Instancia o ArrayList listaSaves.
        listaSaves = new ArrayList<String>();

        //Criação dos Componentes da Tela 1
        Button newGame, loadGame, exitGame;
        newGame = new Button("Novo Jogo");
        newGame.setFont(new Font("Verdana", 25));
        newGame.setMaxSize(300, 300);
        newGame.setId("btCena1");
        loadGame = new Button("Continuar");
        loadGame.setMaxSize(300, 300);
        loadGame.setFont(new Font("Verdana", 25));
        loadGame.setId("btCena1");
        exitGame = new Button("Sair");
        exitGame.setFont(new Font("Verdana", 25));
        exitGame.setMaxSize(300, 300);
        exitGame.setId("btCena1");

        // Cena 2: Pegar nomes dos jogadores.
        VBox j1, j2;
        TextField txP1, txP2;
        j1 = new VBox(5);
        j2 = new VBox(5);
        txP1 = new TextField();
        txP1.setMaxWidth(220);
        txP2 = new TextField();
        txP2.setMaxWidth(220);

        Label info1, info2;
        info1 = new Label("Jogador 1");
        info1.setId("playerSet");
        info2 = new Label("Jogador 2");
        info2.setId("playerSet");

        j1.getChildren().addAll(info1, txP1);
        j1.setAlignment(Pos.CENTER);
        j2.getChildren().addAll(info2, txP2);
        j2.setAlignment(Pos.CENTER);

        //Botão envia jogadores
        Button sim = new Button("Jogar");
        sim.setId("btPlay");
        sim.setMaxSize(220, 220);
        sim.setFont(new Font("Verdana", 20));
        sim.setOnAction(e -> { // Jogar da Cena 2
            if (txP1.getText().equalsIgnoreCase("") || txP2.getText().equalsIgnoreCase("")) {
                WindowShow.exibirResultado("Aviso", "Campos vazios, por favor escreva os nomes.");
            } else if (verificaNomesRepetidos(txP1.getText(), txP2.getText())) {
                WindowShow.exibirResultado("Aviso", "Nomes repetidos, por favor mude um deles.");
            } else {
                SecureRandom random = new SecureRandom();
                int primeiro = random.nextInt(2);
                if (primeiro == 0) {
                    pl1 = new Jogador(txP1.getText());
                    pl2 = new Jogador(txP2.getText());
                } else {
                    pl2 = new Jogador(txP1.getText());
                    pl1 = new Jogador(txP2.getText());
                }

                defineSimbolo(pl1, pl2);
                tabuleiro = new Tabuleiro(pl1, pl2);
                p1.setText(tabuleiro.getPlayer1().getNome() + " " + placar1 + " pts");
                p2.setText(tabuleiro.getPlayer2().getNome() + " " + placar2 + " pts.");
                window.setScene(cena3);
                result.setText(tabuleiro.getPlayer1().getNome() + " é a sua vez.");
            }
        });

        //Layout da Cena que pega os nomes dos jogadores.
        VBox layoutName = new VBox(20);
        layoutName.getChildren().addAll(j1, j2, sim);
        layoutName.setAlignment(Pos.CENTER);
        cena2 = new Scene(layoutName, 800, 600);
        cena2.getStylesheets().addAll(this.getClass().getResource("PlayersSet.css").toExternalForm());

        //Action Event do botão Novo Jogo
        newGame.setOnAction(e -> window.setScene(cena2));

        ObservableList<String> listaC = FXCollections.observableArrayList(listaSaves);
        VBox loadLayout = new VBox(20);
        listV = new ListView<>();
        listV.setItems(listaC);
        listV.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        listV.setMaxSize(400, 500);
        loadbt = new Button("Carregar");
        loadbt.setId("btLoad");
        loadLayout.setAlignment(Pos.CENTER);
        loadLayout.getChildren().addAll(listV, loadbt);
        cenaLoad = new Scene(loadLayout, 800, 600);
        cenaLoad.getStylesheets().addAll(this.getClass().getResource("Load.css").toExternalForm());
        loadGame.setOnAction(e -> {
            try {
                loadListSave();
                if (listaSaves.isEmpty()) {
                    WindowShow.exibirResultado("Aviso", "Não há jogos salvos");
                } else {
                    for (String item : listaSaves) {
                        listaC.add(item);
                    }
                    listV.getSelectionModel().selectFirst();
                    window.setScene(cenaLoad);
                }

            } catch (Exception ex) {
                Logger.getLogger(AppJVelha.class.getName()).log(Level.SEVERE, null, ex);
            }

        });

        loadbt.setOnAction(e -> {
            limparTabuleiro();
            carregar(listV.getSelectionModel().getSelectedItem());
            window.setScene(cena3);
            listaC.clear();
        });

        exitGame.setOnAction(e -> {
            System.exit(0);
        });

        //Criação dos Botões 0 a 8;
        bt = new Button[9];
        for (int i = 0; i < 9; i++) {
            bt[i] = new Button("");
            bt[i].setFont(new Font("Arial", 35));
            bt[i].setMaxSize(100, 100);
            bt[i].setMinSize(100, 100);
            bt[i].setId("btJogada");
        }

        bt[0].setOnAction(e -> {
            System.out.println("Botao 0 pressionado");
            jogada(0);
        });

        bt[1].setOnAction(e -> {
            System.out.println("Botao 1 pressionado");
            jogada(1);
        });

        bt[2].setOnAction(e -> {
            System.out.println("Botao 2 pressionado");
            jogada(2);
        });

        bt[3].setOnAction(e -> {
            System.out.println("Botao 3 pressionado");
            jogada(3);
        });

        bt[4].setOnAction(e -> {
            System.out.println("Botao 4 pressionado");
            jogada(4);
        });

        bt[5].setOnAction(e -> {
            System.out.println("Botao 5 pressionado");
            jogada(5);
        });

        bt[6].setOnAction(e -> {
            System.out.println("Botao 6 pressionado");
            jogada(6);
        });

        bt[7].setOnAction(e -> {
            System.out.println("Botao 7 pressionado");
            jogada(7);
        });

        bt[8].setOnAction(e -> {
            System.out.println("Botao 8 pressionado");
            jogada(8);
        });

        //Criação do Grid;
        GridPane grade = new GridPane();
        grade.setPadding(new Insets(25, 25, 10, 25));
        grade.setHgap(10);
        grade.setVgap(10);
        grade.setAlignment(Pos.CENTER);
        int n = 0;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                grade.add(bt[n], j, i);
                n++;
            }
        }

        //FileChoser
        FileChooser fileComp = new FileChooser();

        //Barra de Menu
        MenuBar barraMenu = new MenuBar();
        barraMenu.setId("barraMenu");

        //BarraMenu > Arquivo e Submenus.
        Menu menu = new Menu("Arquivo");
        menu.setId("menu");
        MenuItem nJogo = new MenuItem("Novo Jogo");
        nJogo.setId("itemMenu");
        MenuItem cJogo = new MenuItem("Carregar Jogo");
        cJogo.setId("itemMenu");
        //Novo jogo dentro de uma partida.
        nJogo.setAccelerator(KeyCombination.keyCombination("Ctrl+N")); // Define uma combinação de teclas para chamar o evento.
        nJogo.setOnAction(e -> {
            boolean op = WindowShow.confirmBox("Aviso", "Tem certeza que deseja iniciar um novo jogo?\n"
                    + "Irá resetar o placa e os jogadores.");
            if (op == true) {
                newGameReset();
                txP1.setText("");
                txP2.setText("");
                window.setScene(cena2);
            }

        });
        // Load Game com Tecla e Atalho.
        cJogo.setAccelerator(KeyCombination.keyCombination("Ctrl+L"));
        cJogo.setOnAction(e -> {
            try {
                loadListSave();
                if (listaSaves.isEmpty()) {
                    WindowShow.exibirResultado("Aviso", "Não há jogos salvos");
                } else {
                    for (String item : listaSaves) {
                        listaC.add(item);
                    }
                    listV.getSelectionModel().selectFirst();
                    window.setScene(cenaLoad);
                }
            } catch (Exception ex) {
                Logger.getLogger(AppJVelha.class.getName()).log(Level.SEVERE, null, ex);
            }

        });
        MenuItem save = new MenuItem("Salvar Jogo");
        save.setId("itemMenu");
        save.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        save.setOnAction(e -> {
            try {
                if (win == false && velha == false) {
                    salvar();
                } else {
                    WindowShow.exibirResultado("Aviso", "Não é possivel salvar quando\n"
                            + "estiver na tela de vitoria, ou na tela de velha.\n"
                            + "clique em continuar.");
                }

            } catch (Exception ex) {
                Logger.getLogger(AppJVelha.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Salvou");
        });
        menu.getItems().addAll(nJogo, cJogo, save);
        barraMenu.getMenus().addAll(menu);

        //Botão Continuar.
        continuar = new Button("Continuar");
        continuar.setVisible(false);
        continuar.setId("btContinuar");
        HBox btCenter = new HBox();
        btCenter.setPadding(new Insets(10, 10, 10, 10));
        btCenter.setAlignment(Pos.CENTER);
        btCenter.getChildren().add(continuar);

        //Continuar partidas, apenas se for velha ou se alguém tiver vencidio.
        continuar.setOnAction(e -> {
            if (win == true) {
                limparTabuleiro();
            } else if (velha == true) {
                limparTabuleiro();
            } else {
                WindowShow.exibirResultado("Aviso", "Você so pode continuar se houver vitoria de algum jogador ou velha.");
            }
            continuar.setVisible(false);
        });

        //Organização dos Labels P1 e P2.
        HBox pvp = new HBox();
        HBox resultAlg = new HBox();
        pvp.setAlignment(Pos.CENTER);
        pvp.setSpacing(275);
        p1 = new Label("Player1");
        p2 = new Label("Player2");
        p1.setFont(new Font("Arial", 16));
        p1.setId("playerOne");
        p2.setFont(new Font("Arial", 16));
        p2.setId("playerTwo");
        result = new Label("Resultado!");
        result.setFont(new Font("Arial", 20));
        result.setId("vez");
        pvp.setPadding(new Insets(15, 10, 10, 10));
        pvp.getChildren().addAll(p1, p2);
        resultAlg.setAlignment(Pos.CENTER);
        resultAlg.getChildren().add(result);

        //Cena 3
        VBox layout2 = new VBox();
        cena3 = new Scene(layout2, 800, 600);
        cena3.getStylesheets().addAll(this.getClass().getResource("Game.css").toExternalForm());
        ((VBox) cena3.getRoot()).getChildren().addAll(barraMenu, pvp, resultAlg, grade, btCenter);

        //BarraMenu
        // Layout da Cena 1
        VBox layout1 = new VBox();
        Label creator = new Label("Desenvolvido por Ayrton Marinho");
        creator.setId("CreatorName");
        layout1.setAlignment(Pos.CENTER);
        layout1.setPadding(new Insets(170, 50, 50, 50));
        layout1.setSpacing(10);
        layout1.getChildren().addAll(newGame, loadGame, exitGame, creator);
        cena1 = new Scene(layout1, 800, 600);
        cena1.getStylesheets().addAll(this.getClass().getResource("Start.css").toExternalForm());

        window.setScene(cena1);
        window.setTitle("Xis ou Bola - Jogo da Velha");
        window.show();

    }

    // Procedimento de jogadas.
    public void jogada(int simbol) { // por 2 parametros a mais
        if (vezJogador == 0) {
            result.setText(tabuleiro.getPlayer2().getNome() + " é sua vez");
            simbolo = tabuleiro.getPlayer1().getSimbolo();
            vezJogador = 1;
            bt[simbol].setText(simbolo);
            bt[simbol].setDisable(true);
            btStatus[simbol] = 1;
            vitoria(tabuleiro.getPlayer1());
        } else {
            result.setText(tabuleiro.getPlayer1().getNome() + " é sua vez");
            simbolo = tabuleiro.getPlayer2().getSimbolo();
            vezJogador = 0;
            bt[simbol].setText(simbolo);
            bt[simbol].setDisable(true);
            btStatus[simbol] = 1;
            vitoria(tabuleiro.getPlayer2());
        }
        nJogadas++;
        velha();
        System.out.println(nJogadas);
        //chamada para jogar no tabuleiro (tabuleiro.jogada(simbol))
    }

    //Verifica vitoria
    public void vitoria(Jogador jg) {
        if (bt[0].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[1].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[2].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[0].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[3].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[6].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[1].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[4].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[7].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[2].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[5].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[8].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[3].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[4].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[5].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[6].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[7].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[8].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[0].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[4].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[8].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        } else if (bt[2].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[4].getText().equalsIgnoreCase(jg.getSimbolo()) && bt[6].getText().equalsIgnoreCase(jg.getSimbolo())) {
            System.out.println(jg.getNome());
            desativarButtons();
            addPts(jg);
            win = true;
            WindowShow.exibirResultado("Vencedor", jg.getNome() + " venceu o jogo!");
        }
    }

    //Desativa todos os botões.
    public void desativarButtons() {
        for (int i = 0; i < 9; i++) {
            bt[i].setDisable(true);
        }
        continuar.setVisible(true);
    }

    //Limpa o tabuleiro.
    public void limparTabuleiro() {
        for (int i = 0; i < 9; i++) {
            bt[i].setText("");
            bt[i].setDisable(false);
            btStatus[i] = 0;
        }
        nJogadas = 0;
        win = false;
        velha = false;
    }

    //Verifica se deu velha
    public void velha() {
        if (this.isFull() && this.win == false) {
            System.out.println("Deu velha!");
            WindowShow.exibirResultado("Velha", "Deu velha!");
            velha = true;
            continuar.setVisible(true);
        }
    }

    //Função auxiliar para saber se esgotaram as jogadas.
    public boolean isFull() {
        if (this.nJogadas == 9) {
            return true;
        }
        return false;
    }

    //Main
    public static void main(String[] args) {
        launch(args);
    }

    //Verifica os nomes repetidos.
    public boolean verificaNomesRepetidos(String nome1, String nome2) {
        if (nome2.equalsIgnoreCase(nome1)) {
            return true;
        }
        return false;
    }

    //Define o simbolo.S
    public void defineSimbolo(Jogador player1, Jogador player2) {
        SecureRandom random = new SecureRandom();
        int primeiro = random.nextInt(2);
        if (primeiro == 0) {
            player1.setSimbolo("X");
            player2.setSimbolo("O");
        } else {
            player1.setSimbolo("O");
            player2.setSimbolo("X");
        }
    }

    //Define primeiro jogador.
    public void definePrimeiro(String jogador1, String jogador2) {
        SecureRandom random = new SecureRandom();
        int primeiro = random.nextInt(2);
        if (primeiro == 0) {
            pl1.setNome(jogador1);
            pl1.setSimbolo("X");
            pl2.setNome(jogador2);
            pl2.setSimbolo("O");
        } else {
            pl1.setNome(jogador2);
            pl1.setSimbolo("X");
            pl2.setNome(jogador1);
            pl2.setSimbolo("O");
        }
    }

    //Adiciona pontos.
    public void addPts(Jogador jogador) {
        if (jogador.getNome().equalsIgnoreCase(tabuleiro.getPlayer1().getNome())) {
            placar1++;
            p1.setText(tabuleiro.getPlayer1().getNome() + " " + placar1 + " pts");

        } else {
            placar2++;
            p2.setText(tabuleiro.getPlayer2().getNome() + " " + placar2 + " pts.");
        }
    }

    //Salvar(Novo Teste)
    public void salvar() throws Exception {

        loadListSave();
        tabuleiro.setPlc1(placar1);
        tabuleiro.setPlc2(placar2);
        tabuleiro.setnJogadas(nJogadas);
        tabuleiro.setVezJogador(vezJogador);
        for (int i = 0; i < 9; i++) {
            tabuleiro.setBt(bt[i].getText(), i);
        }
        String saveName = tabuleiro.getPlayer1().getNome() + "Vs" + tabuleiro.getPlayer2().getNome() + ".dwp";
        Iterator<String> lista = listaSaves.iterator();
        while (lista.hasNext()) {
            String checar = lista.next();
            while (checar.equalsIgnoreCase(saveName)) {
                boolean resp = WindowShow.confirmBox("Aviso", "Já existe um save com esse nome.\n"
                        + "Deseja sobrescrever?");
                if (resp == true) {
                    break;
                } else if (resp == false) {
                    String newNameFile = JOptionPane.showInputDialog("Digite um novo nome para o arquivo");
                    while (newNameFile == null || newNameFile.equalsIgnoreCase("")) {
                        newNameFile = JOptionPane.showInputDialog("Por favor digite um nome diferente e que não seja vazio.");
                    }
                    saveName = newNameFile + ".dwp";
                }
            }
        }
        if (checaRepetidos(saveName) == false) {
            listaSaves.add(saveName);
        }
        ResourceManager.save(listaSaves, "ListaSave.dwp");
        ResourceManager.save(tabuleiro, saveName);
        System.out.println("Saved");
        imprimeLista();
    }

    //Load Teste
    public void loadListSave() throws Exception {
        try {
            if (file.exists() == false) {
                file.createNewFile();
                ResourceManager.save(listaSaves, "ListaSave.dwp");
            }
        } catch (IOException e) {
        }
        listaSaves = (ArrayList<String>) ResourceManager.load("ListaSave.dwp");
    }

    //Procedimento para realizar testes.
    private void imprimeLista() {
        Iterator<String> lista = listaSaves.iterator();
        while (lista.hasNext()) {
            String nomes = lista.next();
            System.out.println(nomes);
        }
    }

    public boolean checaRepetidos(String nome) {
        for (String atual : listaSaves) {
            if (atual.equalsIgnoreCase(nome)) {
                return true; //Há Repetidos
            }
        }
        return false;
    }

    public void carregar(String nome) {
        try {
            tabuleiro = (Tabuleiro) ResourceManager.load(nome);
            placar1 = tabuleiro.getPlc1();
            placar2 = tabuleiro.getPlc2();
            nJogadas = tabuleiro.getnJogadas();
            vezJogador = tabuleiro.getVezJogador();
            p1.setText(tabuleiro.getPlayer1().getNome() + " " + placar1 + " pts");
            p2.setText(tabuleiro.getPlayer2().getNome() + " " + placar2 + " pts");

            for (int i = 0; i < 9; i++) {
                bt[i].setText(tabuleiro.getBt(i));
                if (!bt[i].getText().equalsIgnoreCase("")) {
                    bt[i].setDisable(true);
                }
            }
            if (vezJogador == 0) {
                result.setText(tabuleiro.getPlayer1().getNome() + " é a sua vez.");
            } else {
                result.setText(tabuleiro.getPlayer2().getNome() + " é a sua vez.");
            }
        } catch (Exception ex) {
            Logger.getLogger(AppJVelha.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void newGameReset() {
        for (int i = 0; i < 9; i++) {
            bt[i].setText("");
            bt[i].setDisable(false);
        }
        placar1 = 0;
        placar2 = 0;
        vezJogador = 0;
        nJogadas = 0;
        win = false;
        velha = false;
    }

}
