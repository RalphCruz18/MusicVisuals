package ie.tudublin;

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
    // Values for the stick figure
    private float circleRadius = 200; // Default circle radius
    private int circleColor = color(255, 0, 0); // Default circle color (red)
    private float figureAngle = 0; // Angle for stick figure's position around the circle
    private float figureSize = 20; // Default stick figure size
    private int figureColor = color(0, 0, 255); // Default stick figure color (blue)

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
            case '1': // Change circle color
                circleColor = color(random(255), random(255), random(255));
                break;
            case '2': // Change circle radius
                circleRadius += 10;
                break;
            case '3': // Change stick figure size
                figureSize += 5;
                break;
            case '4': // Change stick figure color
                figureColor = color(random(255), random(255), random(255));
                break;
        }
        //END OF PLACEHOLDER
    }

    public void setup() {
        colorMode(HSB, 360, 100, 100);
        noCursor();

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

        // ADD SOME KEY PRESSES 
        // Check if the display time for the text has ended
        if (displayStarted && millis() - displayStartTime >= displayDuration) {
            drawCircleAndFigure(); // Draw the circle and figure 
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


