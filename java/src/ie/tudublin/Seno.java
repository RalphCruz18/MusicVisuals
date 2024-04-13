package ie.tudublin;

import c22427602.RalphVisuals;
import processing.core.PApplet;

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
        lyrics.add(new LyricLine("Seno!", 0.0));  // Timing in seconds
        lyrics.add(new LyricLine("Demo sonnan ja dame", 1.8));
        lyrics.add(new LyricLine("Mou sonnan ja hora", 3.6));
        lyrics.add(new LyricLine("Kokoro wa shinka suru yo", 5.7));
        lyrics.add(new LyricLine("Motto motto", 6.8));

        lyrics.add(new LyricLine("Kotoba ni sureba kiechau kankei nara", 8.0));
        lyrics.add(new LyricLine("Kotoba o keseba ii ya tte", 10.0));
        lyrics.add(new LyricLine("Omotteta osoreteta", 12.0));
        lyrics.add(new LyricLine("Dakedo are? Nanka chigau kamo", 14.0));
        lyrics.add(new LyricLine("Senri no michi mo ippo kara", 16.0));
        lyrics.add(new LyricLine("Ishi no you ni katai sonna ishi de", 18.0));
        lyrics.add(new LyricLine("Chiri mo tsumoreba Yamato Nadeshiko?", 20.0));
        lyrics.add(new LyricLine("'Shi' nuki de iya shinu ki de", 22.0));

        // Pre-Chorus
        lyrics.add(new LyricLine("Fuwafuwari fuwafuwaru", 24.0));
        lyrics.add(new LyricLine("Anata ga namae o yobu", 26.0));
        lyrics.add(new LyricLine("Sore dake de chuu e ukabu", 28.0));
        lyrics.add(new LyricLine("Fuwafuwaru fuwafuwari", 30.0));
        lyrics.add(new LyricLine("Anata ga waratte iru", 32.0));
        lyrics.add(new LyricLine("Sore dake de egao ni naru", 34.0));

        // Chorus
        lyrics.add(new LyricLine("Kami-sama arigatou", 36.0));
        lyrics.add(new LyricLine("Unmei no itazura demo", 38.0));
        lyrics.add(new LyricLine("Meguriaeta koto ga", 40.0));
        lyrics.add(new LyricLine("Shiawase na no", 42.0));

        // Post-Chorus
        lyrics.add(new LyricLine("Demo sonnan ja dame", 44.0));
        lyrics.add(new LyricLine("Mou sonnan ja hora", 46.0));
        lyrics.add(new LyricLine("Kokoro wa shinka suru yo", 48.0));
        lyrics.add(new LyricLine("Motto motto", 50.0));
        lyrics.add(new LyricLine("Sou sonnan ja ya da", 52.0));
        lyrics.add(new LyricLine("Nee sonnan ja mada", 54.0));
        lyrics.add(new LyricLine("Watashi no koto mitete ne", 56.0));
        lyrics.add(new LyricLine("Zutto zutto", 58.0));
    }


    void drawLyrics() {
        float currentTime = (float) (millis() / 1000.0);  // Convert milliseconds to seconds
        for (LyricLine line : lyrics) {
            if (currentTime >= line.startTime && currentTime < line.startTime + 5) {  // Display each line for 5 seconds
                fill(255);
                textSize(64);
                textAlign(CENTER, CENTER);
                text(line.text, width / 2, height / 2);
                break;  // Only show one line at a time
            }
        }
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


