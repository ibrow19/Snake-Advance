package gameobject;

import processing.core.PImage;
import processing.core.PVector;
import processing.core.PApplet;
import transform.Transformable;
import texture.Texture;
import rect.Rect;

/// A game object that displays a texture.
public class TextureObject extends RenderableObject {
    
    /// The texture displayed by this object. 
    private final Texture mTexture;

    /// The current clip index used for drawing the texture.
    private int mClipIndex;

    /// Initialise texture.
    /// \param texture the texture to use for displaying this object.
    public TextureObject(Texture texture) {

        mTexture = texture;
        mClipIndex = 0;

        // Centre origin.
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }

    public void renderCurrent(PApplet core) {

        mTexture.render(core, mClipIndex);

    }

    /// Get width taking into account scale.
    /// \return the current width of the texture scaled by the game 
    ///         object's current x axis scaling.
    public float getWidth() {

        return mTexture.getWidth(mClipIndex) * Math.abs(getXScale());

    }

    /// Get height taking into account scale.
    /// \return the current height of the texture scaled by the game 
    ///         object's current y axis scaling.
    public float getHeight() {

        return mTexture.getHeight(mClipIndex) * Math.abs(getYScale());

    }

    /// Set the current texture clip.
    /// \param clip the index of the clip to use.
    public void setClip(int clip) {

        mClipIndex = clip;

        // Centre origin.
        setOrigin(getWidth() / 2f, getHeight() / 2f);

    }

    /// Get the current number of clips of the associated texture.
    /// \return the number of clips the texture has.
    protected int getClipCount() {

        return mTexture.getClipCount();

    }

    /// Get transformed clip (Does not include rotation).
    public Rect getBounds() {
        
        Rect bounds = mTexture.getClip(mClipIndex);
        bounds.x = 0f;
        bounds.y = 0f;
        bounds.transform(this);
        return bounds;

    }

}
