package ie.tudublin;

import processing.core.*;

// This is an example of a visual that uses the audio bands
public class AudioBandsVisual extends PApplet
{
    Seno mv;
    
    public AudioBandsVisual(Seno mv)
    {
        this.mv = mv;
    }

    public void render(int bandColor, int bandSaturation)
    {
        float gap = mv.width / (float) mv.getBands().length * 4.0f;
        mv.noStroke();
        mv.fill(bandColor, bandSaturation, 255);

        for(int i = 0 ; i < mv.getBands().length ; i ++)
        {
            //draw rectangle for each band
            mv.rect((i * gap - gap / 2)-3120, mv.height+2300, gap,-mv.getSmoothedBands()[i] * 1f); 
        }
    }
}