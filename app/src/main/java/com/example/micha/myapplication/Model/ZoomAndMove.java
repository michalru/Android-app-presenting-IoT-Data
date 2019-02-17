package com.example.micha.myapplication.Model;
//Klasa do zapisu przyblizenia i przesuniecia wykresu
public class ZoomAndMove {
    float ZoomX=1 ;
    float ZoomY=1 ;
    float MoveX=1 ;
    float MoveY=1 ;
    public ZoomAndMove() {
    }

    public float getZoomX() {
        return ZoomX;
    }

    public void setZoomX(float zoomX) {
        ZoomX = zoomX;
    }

    public float getZoomY() {
        return ZoomY;
    }

    public void setZoomY(float zoomY) {
        ZoomY = zoomY;
    }

    public float getMoveX() {
        return MoveX;
    }

    public void setMoveX(float moveX) {
        MoveX = moveX;
    }

    public float getMoveY() {
        return MoveY;
    }

    public void setMoveY(float moveY) {
        MoveY = moveY;
    }
}
