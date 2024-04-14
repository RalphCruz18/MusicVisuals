package ie.tudublin;

import c22427602.RalphVisuals;
import processing.core.PApplet;
import processing.core.PMatrix3D;

import java.util.ArrayList;
import java.util.Arrays;

import java.util.Random;

import processing.core.PVector;

public class Seno extends Visual {
    RalphVisuals Ralph;
    private boolean drawSphere = false;
    ArrayList<LyricLine> lyrics = new ArrayList<LyricLine>();

    
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
        setupLyrics();
    }

    class LyricLine {
        String text;
        float startTime; // Time in seconds
    
        LyricLine(String text, double d) {
            this.text = text;
            this.startTime = (float) d;
        }
    }


    void setupLyrics() {
        // Intro
        lyrics.add(new LyricLine("Seno! Demo sonnan ja dame", 0.1));  // Timing in seconds
        lyrics.add(new LyricLine("Mou sonnan ja hora", 2.8));
        lyrics.add(new LyricLine("Kokoro wa shinka suru yo", 4.6));
        lyrics.add(new LyricLine("Motto motto", 6.4));


        lyrics.add(new LyricLine("Instrumentals", 10));
        lyrics.add(new LyricLine("Instrumentals", 15));
        lyrics.add(new LyricLine("Instrumentals", 20));
        lyrics.add(new LyricLine("Instrumentals", 23));



        lyrics.add(new LyricLine("Kotoba ni sureba kiechau kankei nara kotoba o keseba ii ya tte", 27.4));
        lyrics.add(new LyricLine("Omotteta osoreteta Dakedo are? Nanka chigau kamo", 30.3));
        lyrics.add(new LyricLine("Senri no michi mo ippo kara ishi no you ni katai sonna ishi de", 34.7));
        lyrics.add(new LyricLine("Chiri mo tsumoreba Yamato Nadeshiko?", 39.5));
        lyrics.add(new LyricLine("Shi nuki de iya shinu ki de!", 45.2));

        // // Pre-Chorus
        // lyrics.add(new LyricLine("Fuwafuwari fuwafuwaru anata ga namae o yobu sore dake de chuu e ukabu", 45.0));
        // lyrics.add(new LyricLine("Fuwafuwaru fuwafuwari anata ga waratte iru sore dake de egao ni naru", 52.0));

        // Chorus
        //lyrics.add(new LyricLine("Kami-sama arigatou unmei no itazura demo",  62.0));
        // lyrics.add(new LyricLine("Meguriaeta koto ga", 40.0));
        // lyrics.add(new LyricLine("Shiawase na no", 42.0));

        // // Post-Chorus
        // lyrics.add(new LyricLine("Demo sonnan ja dame", 44.0));
        // lyrics.add(new LyricLine("Mou sonnan ja hora", 46.0));
        // lyrics.add(new LyricLine("Kokoro wa shinka suru yo", 48.0));
        // lyrics.add(new LyricLine("Motto motto", 50.0));
        // lyrics.add(new LyricLine("Sou sonnan ja ya da", 52.0));
        // lyrics.add(new LyricLine("Nee sonnan ja mada", 54.0));
        // lyrics.add(new LyricLine("Watashi no koto mitete ne", 56.0));
        // lyrics.add(new LyricLine("Zutto zutto", 58.0));
    }


    void drawLyrics() {
        float currentTime = (float) (millis() / 1000.0);  // Convert milliseconds to seconds
        for (LyricLine line : lyrics) {
            if (currentTime >= line.startTime && currentTime < line.startTime + 5) {  // Display each line for 5 seconds
                pushMatrix();  // Save the current transformation matrix
                translate(50, height - 80, 0);

                // Text to face the camera
                float[] camNorm = getCameraNormal();
                float angleY = atan2(camNorm[0], camNorm[2]);
                float angleX = asin(-camNorm[1]);
                rotateY(-angleY);
                rotateX(-angleX);

                rotateY(PI); 

                fill(255);
                textSize(64);
                textAlign(LEFT, BOTTOM);
                text(line.text, 0, 0);
                popMatrix();
                break;  // Only show one line at a time
            }
        }
    }
    
    // Calculates the normal vector of the camera facing direction
    public float[] getCameraNormal() {
        PMatrix3D m = (PMatrix3D)this.g.getMatrix();
        float[] camNormal = new float[3];
        camNormal[0] = -m.m02;
        camNormal[1] = -m.m12;
        camNormal[2] = -m.m22;
        return camNormal;
    }

    public void draw() {
        background(0);
        if (drawSphere) {
            Ralph.draw();
        }
        drawLyrics();
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


