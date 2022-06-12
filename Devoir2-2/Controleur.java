import javafx.scene.canvas.GraphicsContext;

import java.io.IOException;
import java.util.ArrayList;

public class Controleur {

    /**
     * Modèle du jeu
     */
    private Jeu jeu;

    public Controleur() throws IOException {
        resetJeu();
    }

    public void resetJeu() throws IOException {
        this.jeu = new Jeu();
        this.jeu.loadScoreBoard();
    }

    public void debuter() {
        this.jeu.debuter();
        levelUp();
    }

    public void update(double deltaTime) {
        this.jeu.update(deltaTime);
    }

    public void draw(GraphicsContext context) {
        this.jeu.draw(context);
    }

    public void lancerBalle(double curseurX, double curseurY) {
        this.jeu.lancerBalle(curseurX, curseurY);
    }

    public boolean perdu() {
        return this.jeu.perdu();
    }

    public void levelUp() {
        this.jeu.levelUp();
        if (!this.jeu.isPause()) {
            this.jeu.pause();
        }
    }

    public boolean pauseDebut() {
        return this.jeu.isPause();
    }

    public void pauseFin() {
        if (this.jeu.isPause()) {
            this.jeu.pause();
        }
    }

    public void scoreUp() {
        this.jeu.scoreUp();
    }

    public void vieUp() {
        this.jeu.vieUp();
    }

    public void vieDown() {
        this.jeu.vieDown();
    }

    public void creerPoissonNor() {
        this.jeu.creerPoissonNor();
    }

    public void creerPoissonSpecial() {
        this.jeu.creerPoissonSpecial();
    }

    public void creerBulles() {
        this.jeu.creerBulles();
    }

    public int getLevel() {
        return this.jeu.getLevel();
    }

    public ArrayList getScoreList(){
        return this.jeu.getBestScores();
    }

    public boolean isNewRecord(){
        return this.jeu.isNewRecord();
    }

    public int getCurrentScore(){return this.jeu.getCurrentScore();}

    public void updateScore(String name, String points) throws IOException { this.jeu.updateScore(name, points);}

}
