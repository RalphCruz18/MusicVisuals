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
        for (PVector star : stars) {
            parent.pushMatrix();
            parent.noStroke();

            float hue = 50 + 10 * sin(parent.frameCount / 60.0f + star.x); // Adds a wave-like effect in hue

            // Dynamic brightness that cycles with frameCount, giving a pulsating effect
            float brightness = 100 + 155 * (0.5f * (1 + sin(parent.frameCount / 30.0f + star.y)));
            parent.fill(color(hue, 255, brightness)); 

            parent.translate(star.x, star.y, star.z);
            parent.sphere(20);
            parent.popMatrix();
        }
    }

    private void drawSphere() {

        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = parent instanceof Visual ? ((Visual)parent).getSmoothedAmplitude() : 0;
        float yPosition = map(amplitude , 0, 1, height * 0.9f, height * 0.1f);

        parent.lights();
        parent.noFill();
        parent.stroke(255);
        parent.pushMatrix();
        parent.translate(width / 2, yPosition, -200);
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
