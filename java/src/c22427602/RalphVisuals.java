package c22427602;

import ie.tudublin.*;
import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PMatrix3D;
import java.util.ArrayList;

public class RalphVisuals extends Visual {
    private PApplet parent;  // Reference to PApplet app
    public float angle = 0.0f;  // Angle for sine wave calculation
    private ArrayList<PVector> stars;  // Store positions of stars
    private float currentX = 0;
    private float targetX = 0;
    public float cameraX;
    public float cameraY;
    public float x, y, z = 0;
    private int cameraMode = 1;

    private float sphereSizeMultiplier = 1.0f;
    private float cameraSpeedMultiplier = 1.0f;
    private boolean sporadicMovement = false;


    // Method to set the parent PApplet
    public void setParent(PApplet parent) {
        this.parent = parent;
        this.g = parent.g;
        this.width = parent.width;
        this.height = parent.height;
        stars = new ArrayList<>();
        
    }

    // Co ordinates of stars, prints multiple at different places
    public void addStars(int number) {
        for (int i = 0; i < number; i++) {
            float x = parent.random(-parent.width * 1.5f, parent.width * 1.5f);
            float y = parent.random(-parent.height * 1.5f, parent.height * 1.5f);
            float z = parent.random(-5000, 5000);
            stars.add(new PVector(x, y, z));
        }
    }

    public void clearStars() {
        stars.clear();
    }

    public void increaseSphereSize() {
        sphereSizeMultiplier += 0.5; // Increment the size multiplier
        System.out.println("Sphere size increased to: " + sphereSizeMultiplier);
    }

    public void decreaseSphereSize() {
        sphereSizeMultiplier -= 0.5; 
        System.out.println("Sphere size decreased to: " + sphereSizeMultiplier);
    }

    public void increaseCameraSpeed() {
        cameraSpeedMultiplier += 0.4; // Increment the speed multiplier
        System.out.println("Camera speed increased to: " + cameraSpeedMultiplier);
    }

    public void decreaseCameraSpeed() {
        cameraSpeedMultiplier -= 0.4; 
        System.out.println("Camera speed decreased to: " + cameraSpeedMultiplier);
    }

    public void toggleSporadicMovement() {
        sporadicMovement = !sporadicMovement; // Toggle the sporadic movement flag
        System.out.println("Sporadic movement toggled. Now set to: " + sporadicMovement);
    }

    private void drawStars() {
        float smoothedamplitude = parent instanceof Visual ? ((Visual)parent).getSmoothedAmplitude() : 0;
        float amplitude = parent instanceof Visual ? ((Visual)parent).getAmplitude() : 0;
        for (PVector star : stars) {
            parent.pushMatrix();
            parent.noStroke();

            float hue = (parent.frameCount * 10 + 360 * (star.x + parent.width) / (2 * parent.width)) % 360;
            // Brightness that cycles with frameCount, giving a pulsating effect
            float brightness = 100 + 155 * (0.5f * (1 + sin(parent.frameCount / 30.0f + star.y)));
            float bopAmplitude = 30 * smoothedamplitude;
            float rate = map(star.x, -parent.width, parent.width, 5.0f, 15.0f);


            float dynamicY = star.y + bopAmplitude * sin(parent.frameCount/rate);
            float dynamicX = star.x; 
            float dynamicZ = star.z;
            float size = (amplitude * 50) + 5 * sin(parent.frameCount / 40.0f);

            if (sporadicMovement) {
                dynamicY += parent.random(-5, 50);
                dynamicY += parent.random(-5, 50); 
                dynamicZ += parent.random(-5, 50); 
                size *= (1 + parent.random(-0.5f, 0.5f) * 5);
            }

            parent.fill(parent.color(hue, 255, brightness, 128)); 
            parent.translate(dynamicX, dynamicY, dynamicZ);

            
            parent.sphere(size);
            parent.popMatrix();
        }
    }

    private void drawSphere() {
        if (parent instanceof Visual) {
            ((Visual)parent).calculateAverageAmplitude();
        }
        float amplitude = ((Visual)parent).getSmoothedAmplitude();
        float yPosition = map(amplitude, 0, 1, parent.height * 0.9f, parent.height * 0.1f);
    
        if (parent.frameCount % 60 == 0) {
            targetX = parent.random(-10, 1000);
        }
        currentX = PApplet.lerp(currentX*1.032f, targetX, 0.05f);
    
        parent.lights();

        // Adjust the sphere's outer glow using stroke
        float hue = (parent.frameCount * 10 + 360 * (currentX + parent.width) / (2 * parent.width)) % 360;
        float alpha = map(amplitude, 0, 1, 10, 255);  // Adjust alpha based on amplitude for a pulsating effect
        parent.fill(hue, 255, 255, alpha);  
        parent.noStroke();  

        // Sphere settings
        int detail = (int) map(amplitude, 0, 1, 6, 24);
        parent.sphereDetail(detail);
        float size = 300 * (0.5f + 4.5f * amplitude * sphereSizeMultiplier) ;  
    
        parent.pushMatrix();
        parent.translate(currentX, yPosition, -200);
        parent.sphere(size*(amplitude*sphereSizeMultiplier*2));
        parent.popMatrix();

        addShell(currentX, yPosition, size + 20, 60, 160);  // Slightly larger than the sphere
        addHaloRing(currentX, yPosition, 500, hue, alpha);
        angle += 0.05;
    }
    
    private void addShell(float x, float y, float size, float hue, float alpha) {
        parent.pushMatrix();
        parent.translate(x, y, -200);
        parent.noFill();

        // First halo layer
        parent.stroke(hue, 50, 255, alpha / 2);  // Soft yellow with reduced saturation
        parent.sphereDetail(10);
        parent.sphere(size);

        // Additional outer halo layer with a different color or transparency
        float outerHue = hue + 30; // Shift the hue for visual distinction, optional
        if (outerHue > 360) {
            outerHue -= 360; // Wrap the hue value to stay within color wheel limits
        }
        float outerAlpha = alpha / 4; // More subtle than the inner halo
        parent.stroke(outerHue, 100, 255, outerAlpha); // More saturated and less transparent
        parent.sphere(size + 20 * sphereSizeMultiplier); // Slightly larger radius for the outer halo

        parent.popMatrix();
    }

    private void addHaloRing(float x, float y, float baseSize, float hue, float alpha) {
        parent.pushMatrix();
        parent.translate(x, y, -200);
    
        // Calculate the orbit radius to ensure it's clearly separated from the sphere
        float orbitRadius = baseSize + 170 * sphereSizeMultiplier; // Increased for clear separation
    
        // Radius outside of orbit radius
        float visualRadius = orbitRadius + 10 * sphereSizeMultiplier;
    
        // Interpolated angle for rotation
        float startAngle = PApplet.PI / 2;
        float endAngle = 3 * PApplet.PI / 2;
        float interpolationFactor = 0.5f * (1 + PApplet.sin(parent.millis() * 0.001f)); // Slowly oscillates between 0 and 1
        float interpolatedAngle = PApplet.lerp(startAngle, endAngle, interpolationFactor);

        // Rotate the plane of the ring
        parent.rotateX(interpolatedAngle);
        parent.noFill();
        parent.stroke(hue, 255, 255, alpha); 
        parent.strokeWeight(5); 
        float diameter = visualRadius * 2;
        parent.ellipse(0, 0, diameter, diameter + 20 * sphereSizeMultiplier); 
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

    public void resetCameraAngles() {
        x = 0;
        y = 0;
        z = 0;
    }

    public void draw() {
        // Check the current camera mode 
        if (cameraMode == 1) {
            // Static camera setup
            float fov = PApplet.PI / 3;
            float cameraY = parent.height / 2.0f;
            float cameraZ = (cameraY / PApplet.tan(fov / 2.0f)) * 4; // Increased distance
    
            parent.perspective(fov, (float) parent.width / (float) parent.height, 1, 10000);
            parent.camera(parent.width / 2.0f, cameraY, cameraZ, 
                          parent.width / 2.0f, cameraY, 0, 
                          0, 1, 0);
        } else if (cameraMode == 2) {
            // Dynamic camera that reacts to mouse position and orbits around
            float fov = PApplet.PI / 6; 
            if (parent.mouseX != 0) {  // Update field of view based on mouse X position for a dynamic effect
                fov = parent.mouseX / (float) parent.width * PApplet.PI / 2;
            }
    
            cameraY = parent.height / 1.5f;  
            float cameraZ = cameraY / PApplet.tan(fov / 2.0f);  
    
            parent.perspective(fov, (float) parent.width / (float) parent.height, cameraZ / 10.0f, cameraZ * 10.0f);
    
            float angleIncrement = 0.05f * cameraSpeedMultiplier; // Apply speed multiplier
            angle += angleIncrement;
            cameraX = parent.width / 2.0f + cameraZ * PApplet.sin(angle);
            float cameraZPosition = cameraZ * PApplet.cos(angle);
    
            parent.camera(cameraX, cameraY, cameraZPosition, 
                          parent.width / 2.0f, cameraY, 0, 
                          0, 1, 0);
        }
    
        parent.background(0);  // Clear the screen to black
        drawStars();
        drawSphere();
    }

    public void mousePressed() {
        if (parent.mouseButton == PApplet.LEFT) {
            cameraMode = (cameraMode == 1) ? 2 : 1;
        }
    }

}
