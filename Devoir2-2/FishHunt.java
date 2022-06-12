import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

// par Qianyun Shen
public class FishHunt extends Application {
    public static final int WIDTH = 640;
    public static final int HEIGHT = 480;
    private double curseurX, curseurY;
    private boolean debut;
    private boolean pause;
    private ArrayList<String> bestScores = new ArrayList<>();
    File file = new File("scores.txt");
    private static boolean inputLineAdded = false;
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {

        Controleur controleur = new Controleur();

        // -------------------------------------------------------------------------------------------------------------
        // Scène 1 : Menu

        Pane rootMenu = new Pane();
        Scene sceneMenu = new Scene(rootMenu, WIDTH, HEIGHT);
        rootMenu.setStyle("-fx-background-color:#00008B;");

        // logo
        Image img = new Image("images/logo.png");
        ImageView logoView = new ImageView(img);
        logoView.setFitWidth(410);
        logoView.setFitHeight(250);
        logoView.setX((double) WIDTH / 2 - logoView.getFitWidth() / 2);
        logoView.setY((double) HEIGHT / 2 - logoView.getFitHeight() / 2 - 30);

        // bouton Nouvelle partie!
        Button nouvellePartie = new Button("Nouvelle partie!");
        nouvellePartie.setMinSize(120, 20);
        nouvellePartie.setLayoutX((double) WIDTH / 2 - nouvellePartie.getMinWidth() / 2);
        nouvellePartie.setLayoutY(360);

        // bouton Meilleurs scores
        Button scores = new Button("Meilleurs scores");
        scores.setMinSize(120, 20);
        scores.setLayoutX((double) WIDTH / 2 - scores.getMinWidth() / 2);
        scores.setLayoutY(395);


        rootMenu.getChildren().addAll(logoView, nouvellePartie, scores);

        // -------------------------------------------------------------------------------------------------------------
        // Scène 2 : Meilleurs scores

        VBox rootScore = new VBox();
        Scene sceneScore = new Scene(rootScore, WIDTH, HEIGHT);
        Text titre = new Text("Meilleurs Scores");
        titre.setFont(Font.font("Arial", 25));


        ListView<String> list = new ListView<String>();
        list.getItems().addAll(controleur.getScoreList());

        list.setMaxSize(580, 330);

        Button menu = new Button("Menu");
        menu.setMinSize(25, 10);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.setSpacing(10);
        Text name = new Text("Votre nom : ");
        TextField enterName = new TextField();
        Text text = new Text("");
        Button ajouter = new Button("Ajouter!");
        hBox.getChildren().addAll(name, enterName, text, ajouter);

        rootScore.setAlignment(Pos.CENTER);
        rootScore.setSpacing(10);
        rootScore.getChildren().addAll(titre, list, menu);

        // -------------------------------------------------------------------------------------------------------------
        // Scène 3 : Jeu

        Pane rootJeu = new Pane();
        Scene sceneJeu = new Scene(rootJeu, WIDTH, HEIGHT);

        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        rootJeu.getChildren().add(canvas);

        // cible
        Image cible = new Image("images/cible.png");
        ImageView cibleView = new ImageView(cible);
        cibleView.setFitWidth(50);
        cibleView.setFitHeight(50);
        rootJeu.getChildren().add(cibleView);

        GraphicsContext context = canvas.getGraphicsContext2D();

        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;
            private double timePassedBulles = 0;
            private double timePassedPause = 0;
            private double timePassedReg = 0;
            private double timePassedSpecial = 0;

            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) * 1e-9;
                timePassedBulles += deltaTime;

                if (timePassedBulles >= 3) {
                    controleur.creerBulles();
                    timePassedBulles = 0;
                }

                // Poisson normal 3s,poisson special 5s

                if (!controleur.pauseDebut()) {
                    timePassedReg += deltaTime;
                    timePassedSpecial += deltaTime;
                    if (timePassedReg >= 3) {
                        controleur.creerPoissonNor();
                        timePassedReg = 0;
                    }
                    if (controleur.getLevel() >= 2 && timePassedSpecial >= 5) {
                        controleur.creerPoissonSpecial();
                        timePassedSpecial = 0;
                    }
                }

                // pause de 3 secondes (si LevelUp) si perdu et
                // changement de scène après 3s
                if (controleur.pauseDebut() || controleur.perdu()) {
                    sceneJeu.setOnMouseClicked(null);
                    timePassedPause += deltaTime;
                    if (timePassedPause >= 3 && controleur.perdu()) {
                        if (controleur.isNewRecord() && !FishHunt.inputLineAdded) {
                            FishHunt.inputLineAdded = true;
                            text.setText("a fait "+ controleur.getCurrentScore() +" points!");
                            rootScore.getChildren().add(2, hBox);
                        }
                        primaryStage.setScene(sceneScore);
                        timePassedPause = 0;
                    } else if (timePassedPause >= 3) {
                        controleur.pauseFin();
                        sceneJeu.setOnMouseClicked((event1) -> {
                            controleur.lancerBalle(curseurX, curseurY);
                        });
                        timePassedPause = 0;
                    }
                }

                controleur.update(deltaTime);
                controleur.draw(context);

                lastTime = now;
            }
        };
        timer.start();

        // -------------------------------------------------------------------------------------------------------------
        // Events :

        // update de la position du curseur et placement de la cible par rapport au curseur dans la scène Jeu
        rootJeu.setOnMouseMoved((event) -> {
            curseurX = event.getX();
            curseurY = event.getY();

            double w = cibleView.getBoundsInLocal().getWidth();
            double h = cibleView.getBoundsInLocal().getHeight();
            cibleView.setX(curseurX - w / 2);
            cibleView.setY(curseurY - h / 2);
        });

        // lance une balle à chaque click de la souris dans la scène Jeu
        sceneJeu.setOnMouseClicked((event) -> {
            controleur.lancerBalle(curseurX, curseurY);
        });

        // features pour aider a debug le jeu dans la scène Jeu
        sceneJeu.setOnKeyPressed((event) -> {
            switch (event.getCode()) {
                case H:
                    controleur.levelUp();
                    break;
                case J:
                    controleur.scoreUp();
                    break;
                case K:
                    controleur.vieUp();
                    break;
                case L:
                    for (int i = 0; i < 3; i++) {
                        controleur.vieDown();
                    }
                    break;
            }
        });

        // Bouton pour lancer une nouvelle partie qui change la scène Menu -> Jeu
        nouvellePartie.setOnAction((event) -> {
            primaryStage.setScene(sceneJeu);
            controleur.debuter();
        });

        scores.setOnAction((event) -> {
            primaryStage.setScene(sceneScore);
        });

        menu.setOnAction(event -> {
            try {
                controleur.resetJeu();
            } catch (IOException e) {
                e.printStackTrace();
            }
            inputLineAdded = false;
            ajouter.setDisable(false);
            text.setText("a fait 0 points!");
            enterName.clear();
            rootScore.getChildren().remove(hBox);
            primaryStage.setScene(sceneMenu);
        });

        ajouter.setOnAction(event -> {
            String newName = enterName.getCharacters().toString();
            String newScore = String.valueOf(controleur.getCurrentScore());
            try {
                controleur.updateScore(newName, newScore);
                ajouter.setDisable(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            list.getItems().clear();
            list.getItems().addAll(controleur.getScoreList());
        });

        // -------------------------------------------------------------------------------------------------------------
        // Interface :

        primaryStage.setScene(sceneMenu);
        primaryStage.setResizable(false);
        primaryStage.getIcons().add(new Image("images/logo.png"));
        primaryStage.setTitle("Fish Hunt");
        primaryStage.show();
    }

}