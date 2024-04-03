package ie.tudublin;

public class Seno extends Visual {

    private float sX = -50;
    private float nX;
    private final float textSpeed = 25;
    private boolean seInPlace = false; // Checks if SE is in place
    private boolean displayStarted = false;
    private int displayStartTime;
    private final int displayDuration = 700; // 0.5 seconds in milliseconds

    public void settings() 
    {
        size(800, 800, P3D);
    }

    public void keyPressed() 
    {
        if (key == ' ') {
            getAudioPlayer().cue(0);
            getAudioPlayer().play();
        }
    }

    public void setup() {
        colorMode(HSB, 360, 100, 100);
        noCursor();

        startMinim();
        loadAudio("Renai Circulation恋愛サーキュレーション歌ってみたなみりん.mp3");
        getAudioPlayer().play();

        textSize(170); // Set the text size for width calculation

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
            text("SE", (sX - 130), height / 2);
            if (seInPlace) {
                text("NO", (nX + 130), height / 2);

                // Below is to clear text once it is completed
                if (!displayStarted) 
                {
                    displayStarted = true;
                    displayStartTime = millis();
                }
            }
        }
    }

    private void updatePositions() {
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
}


