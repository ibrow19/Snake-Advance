package texture;

import processing.core.PApplet;
import processing.core.PImage;
import java.lang.RuntimeException;
import java.io.IOException;
import java.util.ArrayList;
import java.lang.String;
import rect.Rect;

/// Class encapsulating an image with clips to render different areas of the image.
public class Texture {

    /// Image to use for rendering.
    private final PImage mImage;

    /// Clips that can be used to render different areas of the image.
    private ArrayList<Rect> mClips;

    /// Initialise with path to load image from.
    /// \param core Processing core to use for loading image.
    /// \param path Image file path.
    public Texture(PApplet core, String path) throws IOException {

        // Load image, throw exception on failure.
        mImage = core.loadImage(path); 
        if (mImage == null) {

            throw new IOException("Could not read texture from " + path);

        }
        mClips = new ArrayList<Rect>();

        // Add default clip for entire image.
        mClips.add(new Rect(0f, 0f, mImage.width, mImage.height));

    }

    /// Get the number of clips stored by the texture.
    /// \return the number of clips the texture has.
    public int getClipCount() {

        return mClips.size();

    }

    public Rect getClip(int clipIndex) {

        return mClips.get(clipIndex).copy();

    }

    /// Get the width of a texture clip.
    /// \param clipIndex the index of the clip to get the width of.
    /// \return the width of the clip.
    public float getWidth(int clipIndex) {

        return mClips.get(clipIndex).width;
        
    }

    /// Get the height of a texture clip.
    /// \param clipIndex the index of the clip to get the height of.
    /// \return the height of the clip.
    public float getHeight(int clipIndex) {

        return mClips.get(clipIndex).height;
        
    }

    /// Add a new clip that can be used with the texture.
    /// \param clip the clip to add.
    public void addClip(Rect clip) {

        mClips.add(clip.copy());

    }

    /// Render the entire image using the default clip.
    /// \param Processing core to use to render the image.
    public void render(PApplet core) {

        render(core, 0);

    }

    /// Render the image cropped by a clip.
    /// \param core Processing core to use to render the image.
    /// \param clipIndex the index of the clip to crop the image with.
    public void render(PApplet core, int clipIndex) {
        
        // clip image then render, this includes translating so that clip is centred
        // at current transformation.
        Rect clip = mClips.get(clipIndex);
        core.translate(-clip.x, -clip.y);
        core.clip(clip.x, clip.y, clip.width, clip.height);
        core.image(mImage, 0, 0);
        core.noClip();
        core.translate(clip.x, clip.y);

    }

}
