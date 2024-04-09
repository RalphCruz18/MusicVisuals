package ie.tudublin;

import java.util.Random;

import processing.core.PVector;

public class Seno extends Visual {

    private float sX = -100;
    private float nX;
    private final float textSpeed = 45;
    private boolean seInPlace = false; // Checks if SE is in place
    private boolean displayStarted = false;
    private int displayStartTime;
    private final int displayDuration = 700; // 0.5 seconds in milliseconds

    // VALUES FOR STICK FIGURE
    // circle values
    private float circleRadius = 200; // Default circle radius
    private int circleColor = color(255, 0, 0); // Default circle color (red)
    
    //figure values
    private float figureAngle = 0; // Angle for stick figure's position around the circle
    private float figureSize = 20; // Default stick figure size
    private int figureColor = color(0, 0, 255); // Default stick figure color (blue)

    //values for square and figure
    private float cubeRadius = 200; // Default cube radius
    private int cubeColor = color(0, 255, 0); // Default cube color
    private float figurePositionX = 100; // Position for stick figure
    private float figurePositionY = 100; // Position for stick figure
    private float figureSizecube = 20; // Default stick figure size
    private int figureColorcube = color(255, 0, 0); //Default stick figure size
    private boolean dance = true;
    private int danceCounter = 0; //counter for moving the stickman every certain amount of time
    int minY = 4; // Set the minimum value (inclusive)
    int maxY = 20; // Set the maximum value (exclusive)
    int minX = -15; // Set the minimum value (inclusive)
    int maxX = 15; // Set the maximum value (exclusive)


    Random random = new Random();

    public void settings() 
    {
        size(1000, 1000, P3D);
        //fullScreen(P3D, SPAN);
    }

    public void keyPressed() 
    {
        if (key == ' ') {
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }

        // PLACEHOLDER (subject to change, you can edit or remove this part)
        // Add key controls for circle and stick figure properties
        switch (key) {
            case 'q': // Change circle color
                circleColor = color(random(255), random(255), random(255));
                break;
            case 'a': // Change circle radius bigger
                circleRadius += 10;
                break;
            case 'z': // Change circle radius smaller (if you go too small it like turns inside out but its kinda cool it can be a feature👍)
                circleRadius -= 10;
                break;
            case 's': // Change stick figure size bigger
                figureSize += 5;
                break;
            case 'x': // Change stick figure size smaller
                figureSize -= 5;
                break;
            case 'w': // Change stick figure color
                figureColor = color(random(255), random(255), random(255));
                break;
        }
        //END OF PLACEHOLDER
    }

    public void setup() {
        colorMode(HSB, 360, 100, 100);
        //noCursor();

        startMinim();
        loadAudio("Renai Circulation恋愛サーキュレーション歌ってみたなみりん.mp3");
        getAudioPlayer().play();

        textSize(270); // Set the text size for width calculation

        float textWidthNO = textWidth("NO");
        nX = width + textWidthNO; // Start "NO" off-screen to the right
    }

    public void draw() 
    {
        background(0);

        //have other files function here(couldnt get it to work)
        if (!displayStarted || millis() - displayStartTime < displayDuration) 
        {
            fill(328, 90, 82); // Pink color in HSB mode
            noStroke();

            updatePositions();

            // Draw "SE" and "NO" as they move into place
            text("SE", (sX - 210), height / 2);
            if (seInPlace) {
                text("NO", (nX + 210), height / 2);

                // Below is to clear text once it is completed
                if (!displayStarted) 
                {
                    displayStarted = true;
                    displayStartTime = millis();
                }
            }
        }

        // Check if the display time for the text has ended
        /*if (displayStarted && millis() - displayStartTime >= displayDuration) {
            drawCircleAndFigure(); // Draw the circle and figure 
        }*/

        // ADD SOME KEY PRESSES
        switch (key) {
            case '1': // Change circle color
                if (displayStarted && millis() - displayStartTime >= displayDuration) {
                    drawCircleAndFigure(); // Draw the circle and figure 
                }
                break;
            case '2': // Change circle radius bigger
                if (displayStarted && millis() - displayStartTime >= displayDuration) {
                    drawCubeAndFigure(); // Draw the circle and figure 
                }
                break;
        }

    }

    private void updatePositions() 
    {
        // Delay
        int startDelayFrames = 50;
    
        if (frameCount > startDelayFrames) {
            if (sX < width / 2 - textWidth("SE") / 2) 
            {
                sX += textSpeed;
            } 
            else if (!seInPlace) 
            {
                seInPlace = true; // Mark "SE" as in place
            }
    
            if (seInPlace && nX > width / 2 + textWidth("SE") / 2 - textWidth("NO")) 
            {
                nX -= (textSpeed + 5);
            }
        }
    }

    private void drawCircleAndFigure() 
    {
        fill(circleColor);
        noStroke();
        ellipse(width / 2, height / 2, circleRadius * 2, circleRadius * 2); // Draw the circle
    
        // Height of the stick figure
        float totalFigureHeight = figureSize + figureSize * 0.75f + figureSize * 0.25f;
    
        // Calculation to make the feet touch the circle edge
        PVector figurePos = new PVector
        (
            cos(figureAngle) * (circleRadius + totalFigureHeight) + width / 2, 
            sin(figureAngle) * (circleRadius + totalFigureHeight) + height / 2
        );
    
        drawStickFigure(figurePos, figureSize); // Draw the stick figure
    
        // Figure position
        figureAngle += radians(2); // Increase angle to move the figure
    }

    private void drawCubeAndFigure()
    {
        // Define the cube size
        float cubeSize = cubeRadius;
        
        // Calculate the perspective factor
        float perspective = 0.5f; // Smaller value gives more extreme perspective

        // Center position for the cube
        float centerX = width / 2;
        float centerY = height / 2;
        float centerZ = -cubeSize / 2;

        // Define the 8 vertices of the cube for perspective view
        // Front face vertices are closer to viewer so they appear larger
        // Back face vertices are further away so they appear smaller due to perspective
        PVector[] v = new PVector[8];
        v[0] = new PVector(centerX - cubeSize, centerY - cubeSize, centerZ + cubeSize);
        v[1] = new PVector(centerX + cubeSize, centerY - cubeSize, centerZ + cubeSize);
        v[2] = new PVector(centerX + cubeSize * perspective, centerY - cubeSize * perspective, centerZ - cubeSize * perspective);
        v[3] = new PVector(centerX - cubeSize * perspective, centerY - cubeSize * perspective, centerZ - cubeSize * perspective);
        v[4] = new PVector(centerX - cubeSize, centerY + cubeSize, centerZ + cubeSize);
        v[5] = new PVector(centerX + cubeSize, centerY + cubeSize, centerZ + cubeSize);
        v[6] = new PVector(centerX + cubeSize * perspective, centerY + cubeSize * perspective, centerZ - cubeSize * perspective);
        v[7] = new PVector(centerX - cubeSize * perspective, centerY + cubeSize * perspective, centerZ - cubeSize * perspective);

        // Draw the edges of the cube
        stroke(cubeColor);
        strokeWeight(2);
        fill(cubeColor);

        
        // Draw the front face (larger, closer to the screen)
        line(v[0].x, v[0].y, v[0].z, v[1].x, v[1].y, v[1].z);
        line(v[1].x, v[1].y, v[1].z, v[5].x, v[5].y, v[5].z);
        line(v[5].x, v[5].y, v[5].z, v[4].x, v[4].y, v[4].z);
        line(v[4].x, v[4].y, v[4].z, v[0].x, v[0].y, v[0].z);

        // Draw the back face (smaller, further from the screen)
        line(v[2].x, v[2].y, v[2].z, v[3].x, v[3].y, v[3].z);
        line(v[3].x, v[3].y, v[3].z, v[7].x, v[7].y, v[7].z);
        line(v[7].x, v[7].y, v[7].z, v[6].x, v[6].y, v[6].z);
        line(v[6].x, v[6].y, v[6].z, v[2].x, v[2].y, v[2].z);

        // Connect the front and back faces
        line(v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z);
        line(v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z);
        line(v[4].x, v[4].y, v[4].z, v[7].x, v[7].y, v[7].z);
        line(v[5].x, v[5].y, v[5].z, v[6].x, v[6].y, v[6].z);


        

        // Draw the back face (furthest from the viewer)
        beginShape(QUADS);
        vertex(v[2].x, v[2].y, v[2].z);
        vertex(v[3].x, v[3].y, v[3].z);
        vertex(v[7].x, v[7].y, v[7].z);
        vertex(v[6].x, v[6].y, v[6].z);
        endShape();

        // Draw the bottom face (if we consider the cube sitting on a surface)
        beginShape(QUADS);
        vertex(v[7].x, v[7].y, v[7].z);
        vertex(v[4].x, v[4].y, v[4].z);
        vertex(v[5].x, v[5].y, v[5].z);
        vertex(v[6].x, v[6].y, v[6].z);
        endShape();

        // You can still draw the other edges as lines if needed
        // But if you don't want the front face filled, don't define it with beginShape/endShape
        // Just draw its edges
        stroke(cubeColor);
        line(v[4].x, v[4].y, v[4].z, v[5].x, v[5].y, v[5].z);
        line(v[5].x, v[5].y, v[5].z, v[6].x, v[6].y, v[6].z);
        line(v[6].x, v[6].y, v[6].z, v[7].x, v[7].y, v[7].z);
        line(v[7].x, v[7].y, v[7].z, v[4].x, v[4].y, v[4].z);

        // Draw the remaining visible edges as lines
        line(v[0].x, v[0].y, v[0].z, v[3].x, v[3].y, v[3].z);
        line(v[1].x, v[1].y, v[1].z, v[2].x, v[2].y, v[2].z);
        line(v[4].x, v[4].y, v[4].z, v[7].x, v[7].y, v[7].z);
        line(v[5].x, v[5].y, v[5].z, v[6].x, v[6].y, v[6].z);



        noFill();

        // Draw the stick figure on one of the cube vertices if needed
        // Adjust the stick figure's draw function to work with 3D coordinates if it currently does not
    
        // Calculation to make the feet touch the circle edge
        PVector figurePos = new PVector
        (
            (figurePositionX) * (5), 
            (figurePositionY) * (5)
        );
    
        drawStickFigure(figurePos, figureSize+10); // Draw the stick figure
    
        int randomX = minX + random.nextInt(maxX - minX);
        int randomY = minY + random.nextInt(maxY - minY);
        
        // Figure position
        while(danceCounter==9) {
            figurePositionX=randomX+100;
            figurePositionY=randomY+100;
            break;
        }

        if(danceCounter<10) {
            danceCounter++;
        }
        else {
            danceCounter=0;
        }
        
    }

    private void drawStickFigure(PVector position, float size) 
    {
        float headDiameter = size / 2;
        float bodyHeight = size;
        float armLength = size * 0.75f;
        float legLength = size;
        float feetSize = size * 0.25f;
        float handSize = size * 0.2f;
    
        // Head 
        fill(figureColor);
        ellipse(position.x, position.y - headDiameter, headDiameter, headDiameter); // Head
        // Eyes
        fill(0);
        ellipse(position.x - headDiameter / 4, position.y - headDiameter - headDiameter / 4, headDiameter / 5, headDiameter / 5);
        ellipse(position.x + headDiameter / 4, position.y - headDiameter - headDiameter / 4, headDiameter / 5, headDiameter / 5);
        // Body
        stroke(figureColor);
        strokeWeight(2);
        line(position.x, position.y, position.x, position.y + bodyHeight);
        // Arms with hands
        line(position.x, position.y + bodyHeight / 4, position.x - armLength, position.y + bodyHeight / 2);
        line(position.x, position.y + bodyHeight / 4, position.x + armLength, position.y + bodyHeight / 2);
        // Adding hands
        noStroke();
        ellipse(position.x - armLength, position.y + bodyHeight / 2, handSize, handSize);
        ellipse(position.x + armLength, position.y + bodyHeight / 2, handSize, handSize);
        // Legs with feet
        stroke(figureColor);
        line(position.x, position.y + bodyHeight, position.x - armLength / 2, position.y + bodyHeight + legLength);
        line(position.x, position.y + bodyHeight, position.x + armLength / 2, position.y + bodyHeight + legLength);
        // Adding feet
        noStroke();
        ellipse(position.x - armLength / 2, position.y + bodyHeight + legLength, feetSize, feetSize / 2);
        ellipse(position.x + armLength / 2, position.y + bodyHeight + legLength, feetSize, feetSize / 2);
    
        strokeWeight(1); // Resetting stroke weight
    }
}


