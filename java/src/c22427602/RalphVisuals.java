package c22427602;

import ie.tudublin.*;
import processing.core.PVector;
import processing.core.PApplet;

public class RalphVisuals extends Visual{
    private PApplet parent;  // To reference PApplet app
    private float angle = 0.0f;  // Angle for sine wave calculation
    
    // Method to set the parent PApplet
    public void setParent(PApplet parent) {
        this.parent = parent;
        this.g = parent.g;  // Set the graphics object to the parent's graphics object
        this.width = parent.width;  // Inherit the width 
        this.height = parent.height;  // Inherit the height 
    }

    public void RalphSphere() {
        if (g == null) {
            println("Graphics context not available.");
            return;
        }

        float fov = parent.mouseX / (float) parent.width * PApplet.PI / 2;
        float cameraY = parent.height / 2.0f;
        float cameraZ = cameraY / PApplet.tan(fov / 2.0f);
        parent.perspective(fov, (float) parent.width / (float) parent.height, cameraZ / 10.0f, cameraZ * 10.0f);
        parent.camera(parent.width / 2.0f, cameraY, cameraZ,  // eye position
                      parent.width / 2.0f, cameraY, 0,         // center position
                      0, 1, 0);        

        // Analyze the audio to get the amplitude
        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        
        // Retrieve the smoothed amplitude from the parent Visual
        float amplitude = parent instanceof Visual ? ((Visual)parent).getSmoothedAmplitude() : 0;
        
        // Map the amplitude to a bouncing motion
        // The higher the amplitude, the more dramatic the bouncing
        float yPosition = map(amplitude * 2, 0, 1, height * 0.8f, height * 0.1f);  // Adjusted range for more dramatic movement

        lights();
        // USED CODE FROM PROCESSING.ORG //
        noFill();
        stroke(255);  // Disable stroke for a cleaner look
        pushMatrix();  // Save the current transformation matrix
        // Position the sphere based on the amplitude and animate it
        translate(width / 2, height/2, -200);  // Use yPosition for dynamic vertical movement
        sphere(280);  // Draw a sphere with a radius of 280 pixels
        popMatrix();  // Restore the previous transformation matrix
        angle += 0.05;  // Adjust this value to change the speed of other animations
    }
}
