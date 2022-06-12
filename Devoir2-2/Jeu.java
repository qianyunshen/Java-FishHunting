import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import java.io.*;
import java.util.*;

public class Jeu {

    public static final int WIDTH = 640, HEIGHT = 480;

    private int level;

    private int vie;

    private int score;

    private ArrayList<Poisson> poissons;

    private ArrayList<Balle> balles;

    private Bulles[][] bulles;

    private boolean debut;
    //level up or game over
    private boolean pause;
    // save top 10
    private ArrayList<String> bestScores = new ArrayList<>();
    File file = new File("scores.txt");

    //initialiser
    public Jeu() {
        this.vie = 3;
        this.score = 0;
        this.level = 0;
        this.debut = false;
        this.pause = false;
        this.poissons = new ArrayList<>();
        this.balles = new ArrayList<>();
        this.bulles = new Bulles[3][5];
        this.creerBulles();
    }

    //begin or not
    public void debuter() {
        this.debut = true;
    }

   // if vie = 0,lose
    public boolean perdu() {
       if(this.vie == 0){
           return true;
       }
       return false;
    }

    public int getLevel()
    { return this.level; }
    // lancer a ball
    public void lancerBalle(double posX, double posY) {
        balles.add(new Balle(posX, posY, 50, 300));
    }
    public void pause() {
        this.pause = !this.pause;
    }
// gain one point
    public void scoreUp() {
        this.score += 1;
    }

    // level +1
    public void levelUp() {
        this.level += 1;
    }

    // get the life
    public void vieUp() {
        if (this.vie < 3) {
            this.vie += 1;
        }
    }

    // lose life
    public void vieDown() {
        if (this.vie > 0) {
            this.vie -= 1;
        }
    }

    // creer un poisson normal
    public void creerPoissonNor() {
        poissons.add(new PoissonNormal(this.level));
    }
    // creer un poisson special
    public void creerPoissonSpecial() {
        poissons.add(new PoissonSpecial(this.level));
    }

    //creer 3 group de bulle
    public void creerBulles() {
        for (int i = 0; i < this.bulles.length; i++) {
            double posX = random(0, WIDTH);
            for (int j = 0; j < this.bulles[i].length; j++) {
                double taille = random(10, 40);
                double x = random(posX - 20, posX + 20);
                double y = 530;
                double vy = random(350, 450);
                bulles[i][j] = new Bulles(x, y, taille, -vy);
            }
        }

    }
//update
    public void update(double deltaTime) {

        // Poissons :
        if (debut && !pause) {
            for (int i = 0; i < poissons.size(); i++) {
                poissons.get(i).update(deltaTime);
                if (poissons.get(i).getX() < -76 ||
                        poissons.get(i).getX() > WIDTH + 76 ||
                        poissons.get(i).getY() < -76 ||
                        poissons.get(i).getY() > HEIGHT + 76) {
                    vieDown();
                    poissons.remove(poissons.get(i));
                }
            }
        } else {
            poissons.clear();
        }

        // Balles :
        for (int i = 0; i < balles.size(); i++) {
            balles.get(i).update(deltaTime);
            if (balles.get(i).getR() <= 0) {
                for (int j = 0; j < poissons.size(); j++) {
                    if (balles.get(i).hit(poissons.get(j))) {
                        scoreUp();
                        poissons.remove(poissons.get(j));
                        if (score % 5 == 0) {
                            levelUp();
                            pause();
                        }
                    }
                }
                balles.remove(balles.get(i));
            }
        }

        // Bulles :
        for (int i = 0; i < bulles.length; i++) {
            for (int j = 0; j < bulles[i].length; j++) {
                bulles[i][j].update(deltaTime);
            }
        }

    }

    // dessiner plusieurs par second
    public void draw(GraphicsContext context) {

        // Clear de la surface :
        context.clearRect(0,0, WIDTH, HEIGHT);
        context.setFill(Color.DARKBLUE);
        context.fillRect(0, 0, WIDTH, HEIGHT);

        // Poissons :
        if (!pause) {
            for (Poisson poisson : poissons) {
                poisson.draw(context);
            }
        }

        // Balles :
        for (Balle balle : balles) {
            balle.draw(context);
        }

        // Bulles :
        for (Bulles[] bulle : bulles) {
            for (int j = 0; j < bulle.length; j++) {
                bulle[j].draw(context);
            }
        }

        // sceneDeJeu
        context.setFill(Color.WHITE);
        context.setFont(Font.font("serial", 30));
        context.setTextAlign(TextAlignment.CENTER);
        context.fillText("" + score, Math.round((double)WIDTH / 2), 30);
        for (int z = 0; z < vie; z++) {
            context.drawImage(new Image("images/fish/00.png"),
                    (double)WIDTH / 2 - 70 + 55 * z,
                    70,
                    35,
                    35);
        }
        if (perdu()) {
            context.setFont(Font.font("serial", 70));
            context.setFill(Color.RED);
            context.fillText("Game Over",
                    Math.round((double)WIDTH / 2),
                    Math.round((double)WIDTH / 2) - 70);
        } else if (pause) {
            context.setFont(Font.font("serial", 70));
            context.setFill(Color.WHITE);
            context.fillText("Level " + level,
                    Math.round((double)WIDTH / 2),
                    Math.round((double)WIDTH / 2) - 70);
        }

    }

    public static double random(double min, double max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }


    public boolean isPause() {
        return pause;
    }
//return the
    public ArrayList getBestScores(){
        return this.bestScores;
    }

    public void loadScoreBoard() throws IOException {
            try {
                bestScores.clear();

                BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
                String line = bufferedReader.readLine();
                while(line != null){
                    bestScores.add(line);
                    line = bufferedReader.readLine();
                }

            } catch (FileNotFoundException e) {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    public boolean isNewRecord(){
        if(bestScores.size() == 10){
            return this.score > Integer.parseInt(bestScores.get(9).split("- ")[2]);
        }
        return true;
    }

    public int getCurrentScore(){
        return this.score;
    }

    public void updateScore(String name, String point) throws IOException {

        bestScores.add("# - "+name+" - "+score);    // ajouter le résultat à la fin de la liste

        //trier tous les résultats selon le score
        bestScores.sort(Comparator.comparing(s -> Integer.parseInt(s.split("- ")[2]), Comparator.reverseOrder()));

        if(bestScores.size() > 10){
            bestScores.remove(bestScores.get(10));
        }

        for (int i = 0; i < bestScores.size(); i++) {
            String playerName = bestScores.get(i).split("- ")[1];
            String playerScore = bestScores.get(i).split("- ")[2];
            bestScores.add((i+1),"#" + (i + 1) + " - " + playerName + "- " + playerScore);
            bestScores.remove(i);
        }

        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file));
            for(String s : bestScores){
                bufferedWriter.write(s);
                bufferedWriter.newLine();
            }
            bufferedWriter.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
