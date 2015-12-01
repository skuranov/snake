package bodies;

import java.util.ArrayList;


public class Bodie {
    private ArrayList<Integer> coordX;
    private ArrayList<Integer> coordY;

    public Bodie(){
        coordX = new ArrayList<>();
        coordY = new ArrayList<>();
    }

    public ArrayList<Integer> getCoordY() {
        return coordY;
    }

    public void setCoordY(ArrayList<Integer> coordY) {
        this.coordY = coordY;
    }

    public ArrayList<Integer> getCoordX() {
        return coordX;
    }

    public void setCoordX(ArrayList<Integer> coordX) {
        this.coordX = coordX;
    }


}
