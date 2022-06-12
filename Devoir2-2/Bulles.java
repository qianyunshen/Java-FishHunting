import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bulles {
    private double x, y;
    //rayon de la bulle
    private double r;
    // vitesse y
    private double vy;

    public Bulles(double x, double y, double r, double vy) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.vy = vy;
    }
    // update la position
    public void update(double dt) {
        this.y += dt * this.vy;
    }

    public void draw(GraphicsContext context) { ;
        context.setFill(Color.rgb(0, 0, 255, 0.4));
        context.fillOval(this.x - this.r / 2,
                this.y - this.r/ 2,
                this.r,this.r);
    }

}
