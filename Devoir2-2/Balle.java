import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Balle {
    //position de balle
    private double x,y;
    // rayon de balle
    private double r;
    //vitesse de balle
    private  double v;
    public Balle(double x,double y,double r,double v){
        this.x= x;
        this.y= y;
        this.r= r;
        this.v= v;
    }
    public double getR(){
        return this.r;
    }
    public void draw(GraphicsContext context){
        context.fillOval(this.x-this.r,this.y-r,this.r*2,this.r*2);
        context.setFill(Color.BLACK);
    }
    /**
     * Met à jour la position et la vitesse de la balle
     * @param dt Temps écoulé depuis le dernier update() en secondes
     */
    public void update(double dt){
        if(this.r>0) {
            this.r -= v * dt;
        }
    }
    public boolean hit(Poisson poisson){
        return this.x>=poisson.getX()-poisson.largeur
                && this.x<=poisson.getX()+poisson.largeur
                && this.y >= poisson.getY()-poisson.hauteur
                &&this.y <=poisson.getX()+poisson.hauteur;
    }

}

