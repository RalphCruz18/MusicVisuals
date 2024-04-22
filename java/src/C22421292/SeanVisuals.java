package c22421292;

import ie.tudublin.*;
import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PMatrix3D;
import java.util.ArrayList;

public class SeanVisuals extends Visual{
    private PApplet parent;  // Reference to PApplet app

    private float angle = 0.0f;  // Angle for sine wave calculation
    public float cubeSize = 0.0f;

    public int scene = 0;
    public float x, y, z = 0;
    public float cameraX, cameraY;
    public int sunColor, moonColor;
    public int hue, bgHue, bgBrightness = 0;
    public int bandColor, bandSaturation = 0;
    private boolean spamMode = false;
    private ArrayList<PVector> stars;  // Store positions of stars

    // Method to set the parent PApplet
    public void setParent(PApplet parent) {
        this.parent = parent;
        this.g = parent.g;
        this.width = parent.width;
        this.height = parent.height;
        stars = new ArrayList<>();
        addStars(500); //add stars in background
    }

    public void sceneChange() {
        if (scene < 3) {
            scene++;
        } else {
            scene = 1;
        }

        
        parent.noTint();
        parent.noFill();
    }

    public void spamMode() {
        if (spamMode == true) {
            spamMode = false;
        }
        else {
            spamMode = true;
        }
    }

    private void drawSingleCube(float xPosition, float yPosition, float sizeMod, int cubeColor) {
        parent.noFill();
        parent.noTint();
        float amplitude = ((Visual)parent).getSmoothedAmplitude();
    
        parent.pushMatrix();
        parent.translate(xPosition, yPosition, -200);
        parent.rotateX(PApplet.radians(-15));
        parent.rotateY(-angle);
        float size = 1000 * (0.5f + 1.1f * amplitude);  // Cube size based on amplitude
        parent.fill(cubeColor);
        parent.box(size*sizeMod);  // Draw the cube
        parent.popMatrix();
    }
    

    private void drawCube() {
        parent.noFill();
        parent.noTint();
        // Variables to hold color values
        int colorBottomCube, colorTopLeftCube, colorTopRightCube;
    
        // Define colors based on the scene
        switch(scene) {
            case 1:
                colorBottomCube = parent.color(180, 155, 255);
                colorTopLeftCube = parent.color(300, 155, 255);
                colorTopRightCube = parent.color(60, 190, 255);
                sunColor = parent.color(300, 155, 255);
                moonColor = parent.color(60, 190, 255);
                bandColor = 180;
                bandSaturation = 155;
                noTint();
                noFill();
                break;
            case 2:
                colorBottomCube = parent.color(60, 190, 255);
                colorTopLeftCube = parent.color(180, 155, 255);
                colorTopRightCube = parent.color(300, 155, 255);
                sunColor = parent.color(180, 155, 255);
                moonColor = parent.color(300, 155, 255);
                bandColor = 60;
                bandSaturation = 190;
                noTint();
                noFill();
                break;
            case 3:
                colorBottomCube = parent.color(300, 155, 255);
                colorTopLeftCube = parent.color(60, 190, 255);
                colorTopRightCube = parent.color(180, 155, 255);
                sunColor = parent.color(60, 190, 255);
                moonColor = parent.color(180, 155, 255);
                bandColor = 300;
                bandSaturation = 155;
                noTint();
                noFill();
                break;
            default:
                colorBottomCube = colorTopLeftCube = colorTopRightCube = parent.color(0, 0, 255);
        }
    
        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = ((Visual)parent).getSmoothedAmplitude();
        float baseY = map(amplitude, 0, 1, parent.height * 0.9f, parent.height * 0.1f);
    
        parent.lights();
        parent.noStroke();
    
        float sizeMod = 1.0f; // Assuming a sizeMod value for scale adjustment
    
        // Coordinates and drawing for the bottom cube
        float bottomX = parent.width * 0.5f;
        float bottomY = baseY;
        drawSingleCube(bottomX, bottomY, 1.45f, colorBottomCube);
    
        // Coordinates and drawing for the top left and right cubes
        float topXOffset = 1800;
        float topY = bottomY - (1000 * (0.5f + 1.1f * amplitude) * sizeMod * 0.8f);
        drawSingleCube(bottomX - topXOffset, topY, 1, colorTopLeftCube);
        drawSingleCube(bottomX + topXOffset, topY, 1, colorTopRightCube);
    
        angle += 0.05;
    }

    public void addStars(int number) {
        for (int i = 0; i < number; i++) {
            float x = parent.random(-parent.width * 1.5f, parent.width * 1.5f);
            float y = parent.random(-parent.height * 1.5f, parent.height * 1.5f);
            float z = parent.random(-5000, 5000);
            stars.add(new PVector(x, y, z));
        }
    }

    private void drawStars() {
        parent.noFill();
        parent.noTint();
        float amplitude = parent instanceof Visual ? ((Visual) parent).getSmoothedAmplitude() : 0;
        for (PVector star : stars) {
            parent.pushMatrix();
            parent.noStroke();
    
            float hue = bandColor;
            float saturation = bandSaturation;
            float brightness = 255; // Full brightness
    
            float bopAmplitude = 30 * amplitude;
            float rate = map(star.x, -parent.width, parent.width, 5.0f, 15.0f);
            float dynamicY = star.y + bopAmplitude * sin(parent.frameCount / rate);
    
            parent.fill(parent.color(hue, saturation, brightness, 128));
            parent.translate(star.x, dynamicY, star.z);
    
            float size = 10 + 5 * sin(parent.frameCount / 40.0f);
            parent.sphere(size);
    
            parent.popMatrix();
        }
    }

    private void drawSunSphere() {
        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = ((Visual)parent).getSmoothedAmplitude();
        // Start higher and move vertically based on amplitude
        float yPosition = parent.height-15000 * 0.2f + map(amplitude, 0, 1, 50, -50); // Starts near the top and moves slightly
    
        // Start closer to the top left corner
        float baseX = parent.width * -0.9f; // Closer to the left edge
        float oscillationRange = parent.width * 0.4f * amplitude; // Less horizontal movement range
        float xPosition = baseX - oscillationRange * sin(parent.frameCount * 0.05f);
    
        parent.lights();
        parent.noStroke();
        parent.fill(sunColor);
    
        int detail = (int) map(amplitude, 0, 1, 6, 24);
        parent.sphereDetail(detail);
        float size = 300 * (0.5f + 3.0f * amplitude);
    
        parent.pushMatrix();
        parent.translate(xPosition, yPosition, -200);
        parent.sphere(size);
        parent.popMatrix();
    }

    private void drawMoonSphere() {
        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = ((Visual)parent).getSmoothedAmplitude();
        // Start higher and move vertically based on amplitude
        float yPosition = parent.height-15000 * 0.2f + map(amplitude, 0, 1, 50, -50); // Starts near the top and moves slightly
    
        // Start closer to the top right corner
        float baseX = parent.width+2400 * 0.9f; // Closer to the right edge
        float oscillationRange = parent.width * 0.4f * amplitude; // Less horizontal movement range
        float xPosition = baseX + oscillationRange * sin(parent.frameCount * 0.05f);
    
        parent.lights();
        parent.noStroke();
        parent.fill(moonColor);
    
        int detail = (int) map(amplitude, 0, 1, 6, 24);
        parent.sphereDetail(detail);
        float size = 300 * (0.5f + 3.0f * amplitude);
    
        parent.pushMatrix();
        parent.translate(xPosition, yPosition, -200);
        parent.sphere(size);
        parent.popMatrix();
    }

    public PVector getCameraRotation() {
        PMatrix3D m = (PMatrix3D)parent.g.getMatrix();
    
        // Calculate Euler angles
        float sy = -m.m02;
        float cy = PApplet.sqrt(m.m00 * m.m00 + m.m01 * m.m01);
        boolean singular = cy < 1e-6; // If close to singular

        float x, y, z;
        if (!singular) {
            x = PApplet.atan2(m.m12, m.m22);
            y = PApplet.atan2(sy, cy);
            z = PApplet.atan2(m.m01, m.m00);
        } else {
            x = PApplet.atan2(-m.m21, m.m11);
            y = PApplet.atan2(sy, cy);
            z = 0;
        }

        return new PVector(x, y, z);
    }

    public void draw() {
        //Static cam
        float fov = PApplet.PI / 3;
        float cameraY = parent.height / 2.0f;
        float cameraZ = (cameraY / PApplet.tan(fov / 2.0f)) * 4; // Increase distance
        parent.perspective(fov, (float) parent.width / (float) parent.height, 1, 10000);
        parent.camera(parent.width / 2.0f, cameraY, cameraZ, 
                        parent.width / 2.0f, cameraY, 0, 
                        0, 1, 0);
        parent.background(bgHue, 255, bgBrightness);  // Set background color

        parent.noFill();  // Ensure no fill color is set globally
        parent.noTint();  // Ensure no tint is applied globally
        parent.noStroke();  // Ensure no stroke settings influence the drawings

        drawStars();  // Draw stars, ensure this function doesn't alter global tint/fill
        drawCube();   // Ensure drawCube handles its fill without affecting the global state
        //drawSunMoon();  // Draw the sun and moon with correct reset states
        drawSunSphere();
        drawMoonSphere();

        if (spamMode) {
            sceneChange();  // This function now only changes the scene state without drawing
        }
    }
}