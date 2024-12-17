package BouncingBall;

public class Brick {
    float x,y;
    int colorIndex;
    boolean isVisible;

    public Brick(float xBrick, float yBrick,int color) {
        this.x = xBrick;
        this.y = yBrick;
        this.colorIndex = color;
        this.isVisible = true;
    }
}
