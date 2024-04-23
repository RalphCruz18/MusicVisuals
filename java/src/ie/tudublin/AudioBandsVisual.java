package ie.tudublin;

import processing.core.*;

public class AudioBandsVisual extends PApplet
{
    Seno seno;
    
    public AudioBandsVisual(Seno seno)
    {
        this.seno = seno;
    }

    public void render(int bandColor)
    {
        float gap = seno.width / (float) seno.getBands().length * 4.58f;
        seno.noStroke();
        seno.fill(bandColor);

        for(int i = 0 ; i < seno.getBands().length ; i ++)
        {
            //draw rectangle for each band
            seno.rect((i * gap - gap / 2)-2535, seno.height+1750, gap,-seno.getSmoothedBands()[i] * 1.5f); 
        }
    }
}