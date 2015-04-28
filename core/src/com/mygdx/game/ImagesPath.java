package com.mygdx.game;

/**
 * Created by Maxi on 28/04/2015.
 */
public class ImagesPath  {

    private String stoppedImage;
    private String animatedImage;
    public ImagesPath(String stoppedImage, String animatedImage){
        this.setStoppedImage(stoppedImage);
        this.setAnimatedImage(animatedImage);
    }

    public String getStoppedImage() {
        return stoppedImage;
    }

    public void setStoppedImage(String stoppedImage) {
        this.stoppedImage = stoppedImage;
    }

    public String getAnimatedImage() {
        return animatedImage;
    }

    public void setAnimatedImage(String animatedImage) {
        this.animatedImage = animatedImage;
    }
}
