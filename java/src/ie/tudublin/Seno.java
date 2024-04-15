package ie.tudublin;

import c22427602.RalphVisuals;
import c22421292.SeanVisuals;

import processing.core.PApplet;
import processing.core.PMatrix3D;
import java.util.ArrayList;
import processing.core.PVector;

public class Seno extends Visual {
    RalphVisuals Ralph;
    private boolean drawSphere = false;
    SeanVisuals Sean;
    private boolean drawCube = false;
    ArrayList<Lyric> lyrics = new ArrayList<Lyric>();


    int bgColor = color(0);  //Adjusting the background colour. Default to black

    

    public void settings() {
        fullScreen(P3D, SPAN);
        //size(300, 300, P3D);
    }
    
    public void setup() {
        colorMode(HSB, 360, 255, 255);
        //noCursor();
        startMinim();
        loadAudio("Renai Circulation恋愛サーキュレーションKana Hanazawa.mp3");
        getAudioPlayer().play();

        Ralph = new RalphVisuals();  // Instantiate Ralph object
        Ralph.setParent(this);

        Sean = new SeanVisuals(); // Instantiate Sean object
        Sean.setParent(this);

        loadLyrics();
    }

    void loadLyrics() {
        String[] lines = loadStrings("[Japanese] Renai Circulation「恋愛サーキュレーション」Kana Hanazawa [DownSub.com].srt");
        int i = 0;
        while (i < lines.length) {
            if (lines[i].trim().matches("\\d+")) {  // Checks if the line is a sequence number
                i++;
                if (i < lines.length && lines[i].contains("-->")) {
                    String[] timeCodes = lines[i].trim().split(" --> ");
                    float startTime = parseSrtTime(timeCodes[0]);
                    float endTime = parseSrtTime(timeCodes[1]);
    
                    i++;
                    StringBuilder textBuilder = new StringBuilder();
                    while (i < lines.length && !lines[i].trim().isEmpty()) {
                        if (textBuilder.length() > 0) textBuilder.append("\n");
                        textBuilder.append(lines[i].trim());
                        i++;
                    }
                    lyrics.add(new Lyric(textBuilder.toString(), startTime, endTime));
                    //DEBUG
                    println("Loaded lyric: " + textBuilder.toString() + " [" + startTime + " to " + endTime + "]");
                }
            } else {
                i++;
            }
        }
    }
    
    float parseSrtTime(String srtTime) {
        String[] parts = srtTime.split(":");
        String[] secParts = parts[2].split(",");
        float hours = Float.parseFloat(parts[0]);
        float minutes = Float.parseFloat(parts[1]);
        float seconds = Float.parseFloat(secParts[0]);
        float milliseconds = Float.parseFloat(secParts[1]);
        return (float) (hours * 3600 + minutes * 60 + seconds + milliseconds / 1000.0);
    }
    
    class Lyric {
        String text;
        float startTime; // in seconds
        float endTime; // in seconds
    
        Lyric(String text, double d, double e) {
            this.text = text;
            this.startTime = (float) d;
            this.endTime = (float) e;
        }
    }
    
    // Calculates the vector of the camera facing direction
    public float[] getCameraNormal() {
        PMatrix3D m = (PMatrix3D)this.g.getMatrix();
        float[] camNormal = new float[3];
        camNormal[0] = -m.m02;
        camNormal[1] = -m.m12;
        camNormal[2] = -m.m22;
        return camNormal;
    }

    public void draw() {
        background(bgColor);

        float currentTime = getAudioPlayer().position() / 1000.0f;
    
        if (drawSphere) {
            Ralph.draw(); 
        }
        else if (drawCube) {
            Sean.draw(); 
        }



        // SEPARATION //
    
        hint(DISABLE_DEPTH_TEST);
    
        textSize(80);
        textAlign(CENTER, BOTTOM);
    
        // Get the rotation angles from RalphVisuals
        PVector rotationAngles = Ralph.getCameraRotation();  //EULAR ANGLES
        colorMode(HSB, 360, 255, 255, 255);

        for (Lyric lyric : lyrics) {
            int lyricColor = 255;
            if (currentTime >= lyric.startTime && currentTime <= lyric.endTime) {
                pushMatrix();
    
                // Set the position for text in front of the camera
                translate(width / 2, height - 70, 10); // Center and a bit in front
    
                // Inverse rotation to face cam
                rotateX(rotationAngles.x);
                rotateY(rotationAngles.y);
                rotateZ(rotationAngles.z);
    
                // colourful lyrics
                // float hue = (frameCount * 10 + 360 * (width / 2 + width) / (2 * width)) % 360;
                // float brightness = 100 + 155 * (0.5f * (1 + sin(frameCount / 30.0f)));
                // fill(color(hue, 255, brightness, 128));

                // white lyrics
                fill(lyricColor);

                text(lyric.text, 0, 0); // Draw at the adjusted position
                popMatrix();
                break;
            }
        }
        hint(ENABLE_DEPTH_TEST);

    }

    public void keyPressed() {
        println("Key pressed: " + key);  // Debug output to check key press
        switch(key) {
            case '1':
                drawSphere = true;  // Enable drawing the sphere
                drawCube = false;
                //Ralph.addStars(200); //commented out while testing cause too many fricking stars bro
                bgColor=color(0);
                break;
            case '2':
                Sean.sceneChange();
                bgColor=color(30, 255, 255);
                drawSphere = false; // Disable drawing the sphere to show only cube
                drawCube = true;
                break;
            case ' ':
                if (getAudioPlayer().isPlaying()) {
                    getAudioPlayer().pause();
                } else {
                    getAudioPlayer().play();
                }
            case 'r':
                drawSphere = false;
                drawCube = false;
                break;
            default:
                println("No function assigned to this key");
        }
    }
}


