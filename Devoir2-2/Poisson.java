import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public abstract class Poisson {

    /**
     * position du poisson
     */
    protected double x, y;
    /**
     * Vitesse du poisson
     */
    protected double vX, vY;
    /**
     * accélération du poisson
     */
    protected double aX, aY;
    /**
     * Dimension du poisson
     */
    protected double largeur, hauteur;

    /**
     * Couleur du poisson
     */
    protected Color color;

    /**
     * Image du poisson
     */
    protected Image image;

    /**
     * Constructeur d'un poisson
     */
    public Poisson(){
        this.hauteur = 110;
        this.largeur = 110;
        this.y = random((double) 1 / 5 * Jeu.HEIGHT, (double) 4 / 5 * Jeu.HEIGHT);
        if (Math.random() <= 0.5) {
            this.x = -10;
        } else {
            this.x = Jeu.WIDTH + 10;
        }
        this.color = Color.color(Math.random(), Math.random(), Math.random());
    }

    public void update(double deltaTime) {
        vX += deltaTime * aX;
        vY += deltaTime * aY;
        x += deltaTime * vX;
        y += deltaTime * vY;
    }

    /**
     * Coloriage et changement de direction des poissons
     * @param context
     */
    public void draw(GraphicsContext context){
        this.image = ImageHelpers.colorize(this.image, this.color);
        if (this.vX > 0) {
            context.drawImage(image, x, y, largeur, hauteur);
        } else {
            context.drawImage(ImageHelpers.flop(image), x, y, largeur, hauteur);
        }
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
    /**
     * Nombre aléatoire entre min et max inclus
     *
     * @param min nbMin inclus
     * @param max nbMax inclus
     * @return le nombre aléatoire
     */
    public static double random(double min, double max) {
        return min + (int)(Math.random() * ((max - min) + 1));
    }
}
