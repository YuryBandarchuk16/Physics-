package sample;

import java.awt.*;

/**
 * Created by YuryBandarchuk on 07.12.16.
 */
public class Ball {

    public boolean canMove;

    public int hitsCount;

    private double r;
    private Color color;

    private double x, y;
    private double vx, vy;

    public double G = 9.8;

    public Ball(double x, double y) {
        canMove = true;
        hitsCount = 0;
        this.x = x;
        this.y = y;
        this.r = 10.0;
        this.color = Color.BLUE;
    }

    public double getCx() {
        return x - (r / 2.0);
    }


    public double getCy() {
        return y - (r / 2.0);
    }


    public double getVy() {
        return vy;
    }

    public void setVy(double vy) {
        this.vy = vy;
    }

    public double getVx() {
        return vx;
    }

    public void setVx(double vx) {
        this.vx = vx;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getV() {
        return Math.sqrt(vx * vx + vy * vy);
    }

    private void prepareForMove(double t) {
         vy = vy + G * t;
    }

    public void move(double t) {
        prepareForMove(t);
        if (!canMove) return;
        x = x + vx * t * 0.93;
        y = y + vy * t * 0.93;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
