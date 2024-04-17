package c22421292;

import ie.tudublin.*;
import processing.core.PVector;
import processing.core.PApplet;
import processing.core.PMatrix3D;

public class SeanVisuals extends Visual{
    private PApplet parent;  // Reference to PApplet app
    private float angle = 0.0f;  // Angle for sine wave calculation

    public float cameraX;
    public float cameraY;
    public float x, y, z = 0;

    public int scene = 0;
    int hue = 0;
    int bgHue = 0;
    int bgBrightness = 0;
    
    float cubeSize = 0.0f;

    public int bandColor = 0;
    public int bandSaturation = 0;
    
    // Method to set the parent PApplet
    public void setParent(PApplet parent) {
        this.parent = parent;
        this.g = parent.g;
        this.width = parent.width;
        this.height = parent.height;
    }

    public void sceneChange() {
        bgBrightness = 0;
        //scene count checker
        if (scene < 3 && scene > 0) {
            scene += 1;
        }
        else if (scene >= 3) {
            scene = 1;
        }
        else if (scene <= 0) {
            scene = 1;
        }

        println("Current scene: " + scene);  // Debug output to check scene
    }

    private void drawSingleCube(float xPosition, float yPosition, float sizeMod, int cubeColor) {
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
        // Variables to hold color values
        int colorBottomCube, colorTopLeftCube, colorTopRightCube;
    
        // Define colors based on the scene
        switch(scene) {
            case 1:
                colorBottomCube = parent.color(180, 155, 255);
                colorTopLeftCube = parent.color(300, 155, 255);
                colorTopRightCube = parent.color(60, 190, 255);
                bandColor = 180;
                bandSaturation = 155;
                break;
            case 2:
                colorBottomCube = parent.color(60, 190, 255);
                colorTopLeftCube = parent.color(180, 155, 255);
                colorTopRightCube = parent.color(300, 155, 255);
                bandColor = 60;
                bandSaturation = 190;
                break;
            case 3:
                colorBottomCube = parent.color(300, 155, 255);
                colorTopLeftCube = parent.color(60, 190, 255);
                colorTopRightCube = parent.color(180, 155, 255);
                bandColor = 300;
                bandSaturation = 155;
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

        // // dynamic cam
        // float fov = PApplet.PI / 6;  // A moderate field of view for a good initial perspective
        // if (parent.mouseX != 0) {  // Check if the mouse has moved from the default position
        //     fov = parent.mouseX / (float) parent.width * PApplet.PI / 2;
        // }

        // cameraY = parent.height / 1.5f;  // Start with the camera at half the height of the window
        // float cameraZ = cameraY / PApplet.tan(fov / 2.0f);  // Calculate camera Z based on the updated FOV

        // parent.perspective(fov, (float) parent.width / (float) parent.height, cameraZ / 10.0f, cameraZ * 10.0f);

        // // Calculate angle for circular camera movement
        // float angle = parent.frameCount * 0.03f; // Change 0.01 to adjust speed of rotation
        // cameraX = parent.width / 2.0f + cameraZ * PApplet.sin(angle);
        // float cameraZPosition = cameraZ * PApplet.cos(angle);

        // // Updated camera setup to circle around the sphere
        // parent.camera(cameraX, cameraY, cameraZPosition, 
        //             parent.width / 2.0f, cameraY, 0, 
        //             0, 1, 0);

        //Static cam
        float fov = PApplet.PI / 3;
        float cameraY = parent.height / 2.0f;
        float cameraZ = (cameraY / PApplet.tan(fov / 2.0f)) * 4; // Increase distance
    
        parent.perspective(fov, (float) parent.width / (float) parent.height, 1, 10000);
        parent.camera(parent.width / 2.0f, cameraY, cameraZ, 
                        parent.width / 2.0f, cameraY, 0, 
                        0, 1, 0);

        parent.background(bgHue, 255, bgBrightness);  // Set background color

        drawCube();
    }
}