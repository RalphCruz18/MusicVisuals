package c22427602;

import ie.tudublin.*;
import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PShape;
import java.util.ArrayList;

public class RalphVisuals extends Visual {
    private PApplet parent;  // Reference to PApplet app
    private float angle = 0.0f;  // Angle for sine wave calculation
    private ArrayList<PVector> stars;  // Store positions of stars
    private float currentX = 0;
    private float targetX = 0;

    // Method to set the parent PApplet
    public void setParent(PApplet parent) {
        this.parent = parent;
        this.g = parent.g;
        this.width = parent.width;
        this.height = parent.height;
        stars = new ArrayList<>();
        
    }

    public void addStars(int number) {
        for (int i = 0; i < number; i++) {
            float x = parent.random(-parent.width, parent.width);
            float y = parent.random(-parent.height, parent.height+150);
            float z = parent.random(-50, 50);
            stars.add(new PVector(x, y, z));
        }
    }

    private void drawStars() {
        float amplitude = parent instanceof Visual ? ((Visual)parent).getSmoothedAmplitude() : 0;
        for (PVector star : stars) {
            parent.pushMatrix();
            parent.noStroke();

            float hue = (parent.frameCount * 10 + 360 * (star.x + parent.width) / (2 * parent.width)) % 360;
            // Brightness that cycles with frameCount, giving a pulsating effect
            float brightness = 100 + 155 * (0.5f * (1 + sin(parent.frameCount / 30.0f + star.y)));
            float bopAmplitude = 30 * amplitude;
            float rate = map(star.x, -parent.width, parent.width, 5.0f, 15.0f);
            float dynamicY = star.y + bopAmplitude * sin(parent.frameCount/rate);

            parent.fill(parent.color(hue, 255, brightness, 128)); 
            parent.translate(star.x, dynamicY, star.z);

            float size = 20 + 5 * sin(parent.frameCount / 40.0f);
            parent.sphere(size);
            parent.popMatrix();
        }
    }

    private void drawSphere() {

        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = parent instanceof Visual ? ((Visual)parent).getSmoothedAmplitude() : 0;
        float yPosition = map(amplitude , 0, 1, height * 0.9f, height * 0.1f);
        if (parent.frameCount % 60 == 0) {
            targetX = parent.random(-10, 1000);
        }
        currentX = PApplet.lerp(currentX, targetX, 0.05f); 

        parent.lights();
        parent.noFill();
        parent.stroke(255);
        parent.pushMatrix();
        parent.translate(currentX, yPosition, -200);
        parent.sphere(280);
        parent.popMatrix();
        angle += 0.05;
    }

    // Unified draw function to manage all drawing
    public void draw() {
        float fov = PApplet.PI / 3;  // A moderate field of view for a good initial perspective
        if (parent.mouseX != 0) {  // Check if the mouse has moved from the default position
            fov = parent.mouseX / (float) parent.width * PApplet.PI / 2;
        }
        
        float cameraY = parent.height / 1.5f;  // Start with the camera at half the height of the window
        float cameraZ = cameraY / PApplet.tan(fov / 2.0f);  // Calculate camera Z based on the updated FOV

        parent.perspective(fov, (float) parent.width / (float) parent.height, cameraZ / 10.0f, cameraZ * 10.0f);
        
        // Camera setup to initially focus on the center of the scene
        parent.camera(parent.width / 2.0f, cameraY, cameraZ, 
                    parent.width / 2.0f, cameraY, 0, 
                    0, 1, 0);
        
        parent.background(0);  // Clear the screen
        drawStars();
        drawSphere();
    }
}
