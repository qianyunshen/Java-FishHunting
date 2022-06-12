import javafx.scene.image.Image;

public class PoissonNormal extends Poisson{

    /**
     * Constructeur des poissons réguliers
     * @param level niveau courant du jeu
     */
    public PoissonNormal(int level) {
        super();
        this.vX = 100 * Math.pow(level, (double) 1 / 3) + 200;
        this.vY = -random(100, 200);
        this.aY = 100;
        if(this.x > 0){
            vX = -vX;
        }
        int i = (int) Jeu.random(0, 7);
        this.image = new Image("images/fish/0" + i + ".png");
    }

}
