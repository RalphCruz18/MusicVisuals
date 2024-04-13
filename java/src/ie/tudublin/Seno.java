package ie.tudublin;

import c22427602.RalphVisuals;
import processing.core.PApplet;

import java.util.Random;

import processing.core.PVector;

public class Seno extends Visual {
    RalphVisuals Ralph;
    private boolean drawSphere = false;

    
    public void settings() {
        fullScreen(P3D, SPAN);
        //size(300, 300, P3D);
    }
    public void setup() {
        colorMode(HSB, 360, 255, 255);
        //noCursor();
        startMinim();
        loadAudio("Renai Circulation恋愛サーキュレーション歌ってみたなみりん.mp3");
        getAudioPlayer().play();
        Ralph = new RalphVisuals();  // Instantiate Ralph object
        Ralph.setParent(this);
        
    }

    public void draw() {
        background(0);
        if (drawSphere) {
            Ralph.draw();
        }
    }

    public void keyPressed() {
        println("Key pressed: " + key);  // Debug output to check key press
        switch(key) {
            case '1':
                drawSphere = true;  // Enable drawing the sphere
                Ralph.addStars(100);
                break;
            default:
                println("No function assigned to this key");
        }
    }
}


