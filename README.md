# Music Visualiser Project

Name:Seán Flood & Ralph Cruz

Student Number: C22421292 & C22427602

## Instructions
- Fork this repository and use it a starter project for your assignment
- Create a new package named your student number and put all your code in this package.
- You should start by creating a subclass of ie.tudublin.Visual
- There is an example visualiser called MyVisual in the example package
- Check out the WaveForm and AudioBandsVisual for examples of how to call the Processing functions from other classes that are not subclasses of PApplet

# Description of the assignment
This Java assignment involves creating an interactive visual simulation with two primary components—RalphVisuals and SeanVisuals—using the Processing framework. 
RalphVisuals simulates a dynamic 3D starfield and a central pulsating sphere that react to audio input. It includes functionalities like changing sphere size, camera speed, and star movement. In Ralph's visuals there are 2 different camera options: static and dynamic and it allows the user to play around with viewing the objects in different ways. It is extremely interactive with being able to control the movement using keys and distance using the mouse/trackpad.
SeanVisuals, on the other hand, manages different scenes with changing visuals of cubes and spheres, also influenced by audio. 
User interactions, such as key presses and mouse clicks, toggle between different visual modes and modify simulation parameters like size and movement, integrating audio reactivity and interactive controls to create an immersive visual and auditory experience. Additionally, there are lyrics that appear on the screen.



# Instructions
From Ralph's Visuals
From clicking '1'
This is the first visual that appears
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/32efb275-2d41-4137-8b8c-b523669c7ce3)
Pressing '1' multiple times allows you to add stars.
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/236930be-deef-42ae-aab9-0f73f6d97333)

Pressing left mouse button or left touchpad allows you to change the camera angle. This can be adjusted by moving the touchpad or mouse.
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/25872489-44ba-45bd-acb0-d35398082cf6)
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/c3bfdfd1-1b0c-464f-85ac-5d30452e40ce)
Pressing the key 'o' speeds the camera speed and movement up, and pressing the key 'p' slows the movement down.
Pressing left mouse click or left touchpad again, it returns it to the default static camera.

Pressing 'i' causes the stars to move in a sporadic way.
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/54027ca4-aaac-4752-9ff2-ee431f49fc28)
Pressing 'i' again causes the stars to move back in the default way.

Pressing the key 'q' increases the size of the sphere.
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/dac4016f-faaf-4d85-a29d-30aba67ef96a)
Pressing the key 'w' decreases the size.
![image](https://github.com/RalphCruz18/MusicVisuals/assets/124162769/2e4abb05-11b7-4261-a916-edb1f8bb4650)

Pressing 2 causes the scene to change to Sean's Visuals.

‘ ‘ (spacebar) pauses and plays the song
![' '](https://i.imgur.com/J8qGTCL.png)
![' ' again](https://i.imgur.com/7RC3WF0.png)

‘r’ clears the current scene just leaving the screen black with the lyrics still playing
!['r'](https://i.imgur.com/YgO8pgW.png)
!['r again'](https://i.imgur.com/dPyangf.png)

‘s’ enters “spam mode” on when scene 2 is active. Spam mode just calls the “sceneChange” function in SeanVisuals.java very fast.
Not actually screenshotable because it uses motion and screenshots are freezeframes.

# How it works
In the Main.java file we call our "Seno.java" file and create that sketch
```Java
package ie.tudublin;

public class Main {

    public void startUI() {
        String[] a = { "MAIN" };
        processing.core.PApplet.runSketch(a, new Seno()); // <-- Call Seno file here
    }

    public static void main(String[] args) {
        Main main = new Main();
        main.startUI();
    }
}
```

In the Seno.java file we then import our respective files
```Java
import c22427602.RalphVisuals;
import c22421292.SeanVisuals;

public class Seno extends Visual {
    RalphVisuals Ralph;
    private boolean drawSphere = false;
    SeanVisuals Sean;
    private boolean drawCube = false;

    public void setup() {
	Ralph = new RalphVisuals();  // Instantiate Ralph object
        Ralph.setParent(this);

        Sean = new SeanVisuals(); // Instantiate Sean object
        Sean.setParent(this);

    public void draw() {
	if (drawSphere) {
            Ralph.draw();
        }
        else if (drawCube) {
            Sean.draw(); 
        }
```


How Ralph's visuals work:
```Java
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
```
This code block defines a method called drawSphere() used to render a dynamic, pulsating sphere in a 3D environment. It adjusts its vertical position and size based on audio input amplitude. The sphere's position horizontally interpolates between randomly chosen targets, creating a smooth transition effect. The hue and transparency of the sphere also change dynamically with the audio input. 

``` Java
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
```
This code block adds two glowing halo layers around a sphere to create a visual effect of radiance or glow. It positions these layers in 3D space, adjusts their colours, and sets different transparency levels to enhance the visual level and depth.

```Java
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
```
This code block creates a halo ring around a sphere, enhancing the 3D visual effect. It sets the ring's position and size based on input parameters, and applies a rotation based on a time-based interpolation to animate the ring's orientation. The ring is styled with specified colour and transparency, and rendered without fill to appear as a glowing outline.


```Java
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
```
The code block above is code to set the camera options between dynamic and static.

```Java
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
```
This code block is designed to draw stars in a 3D space, where each star's colour, brightness, and position dynamically change based on sound amplitude and a timer. The stars pulse and move sporadically, simulating a twinkling effect. If the 'sporadicMovement' flag is enabled, the stars exhibit even more random motion and changes in size to create a more dynamic and lively visual scene.


```Java
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
```
The code blocks above allow the different options a user can select that can alter movement of camera, speed and size of sphere and movement of stars.


```Java
void loadLyrics() { //Load lyrics from srt file
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
```
This code block manages the loading and parsing of lyrics from an SRT (SubRip subtitle) file to synchronize them with an audio track. It reads the file line by line, extracting time codes and corresponding lyric text. Time codes determine when each lyric appears and disappears. These lyrics are stored in a list of Lyric objects, each containing the lyric text, start time, and end time. The method parseSrtTime() converts time strings from the SRT format into seconds for easier handling in the program.


# What I am most proud of in the assignment
Sean Flood:\
I am most proud of making a scene that I beleive is fun to watch and interactable in many ways which can keep the user entertained and interested. There are many objects that move based on the music which makes it visually apealing and being able to change the appearance using keyboard keys makes it interesting.
Ralph Cruz:\
I'm very satisfied with how I effectively incorporated audio reactivity into the project's graphic features and visuality. The dynamic reaction of the images to the music not only adds to their aesthetic appeal, but also deeply engages the user. The ability to interact with the scene using keyboard and mouse instructions provides a degree of personalization, allowing users to explore and change the graphics in real time, resulting in a more immersive and enjoyable experience. This involvement, combined with the visual effects triggered by the music, elevates the program from a mere presentation to an interactive experience that engages and entertains.

# Markdown Tutorial

This is *emphasis*

This is a bulleted list

- Item
- Item

This is a numbered list

1. Item
1. Item

This is a [hyperlink](http://bryanduggan.org)

# Headings
## Headings
#### Headings
##### Headings

This is code:

```Java
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

So is this without specifying the language:

```
public void render()
{
	ui.noFill();
	ui.stroke(255);
	ui.rect(x, y, width, height);
	ui.textAlign(PApplet.CENTER, PApplet.CENTER);
	ui.text(text, x + width * 0.5f, y + height * 0.5f);
}
```

This is an image using a relative URL:

![An image](images/p8.png)

This is an image using an absolute URL:

![A different image](https://bryanduggandotorg.files.wordpress.com/2019/02/infinite-forms-00045.png?w=595&h=&zoom=2)

This is a youtube video:

[![YouTube](http://img.youtube.com/vi/J2kHSSFA4NU/0.jpg)](https://www.youtube.com/watch?v=J2kHSSFA4NU)

This is a table:

| Heading 1 | Heading 2 |
|-----------|-----------|
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |
|Some stuff | Some more stuff in this column |

