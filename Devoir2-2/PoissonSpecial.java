import javafx.scene.image.Image;

public class PoissonSpecial extends Poisson {

    /**
     * déterminer si c'est un crabe ou étoile de mer
     */
    private boolean isCrabe;
    /**
     * enregistrer un changement de direction du mouvement
     */
    boolean flipSide = false;
    /**
     * temps écoulé
     */
    double timePassed = 0;

    /**
     * constructeur du poisson spécial
     * @param level
     */
    public PoissonSpecial(int level) {
        super();
        this.vY = 0;
        this.aY = 0;
        if (Math.random() <= 0.5) {
            isCrabe = true;
            this.image = new Image("images/crabe.png");
            this.vX = 1.3 * (100 * Math.pow(level, (double) 1 / 3) + 200);
        } else {
            isCrabe = false;
            this.image = new Image("images/star.png");
            this.vX = (100 * Math.pow(level, (double) 1 / 3) + 200);
        }
        if(this.x > 0){
            vX = -vX;
        }
    }

    /**
     *
     * les mouvements de crabe et étoile de mer
     * @param deltaTime
     */
    @Override
    public void update(double deltaTime) {
        if(isCrabe) {
            timePassed += deltaTime;
            if (!flipSide) {
                if (timePassed <= 0.5) {
                    x += deltaTime * vX;
                } else {
                    timePassed = 0;
                    flipSide = true;
                }
            } else {
                if (timePassed <= 0.25) {
                    x += deltaTime * -vX;
                } else {
                    timePassed = 0;
                    flipSide = false;
                }
            }
        }
        else{
            x += deltaTime * vX;
            timePassed += deltaTime;
            if (!flipSide) {
                if (timePassed <= 0.5) {
                    y += 50 * Math.sin(2 * Math.PI * deltaTime);
                } else {
                    timePassed = 0;
                    flipSide = true;
                }
            } else {
                if (timePassed <= 0.5) {
                    y += -50 * Math.sin(2 * Math.PI * deltaTime);
                } else {
                    timePassed = 0;
                    flipSide = false;
                }
            }
        }
    }
}