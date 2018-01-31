package menu;

import processing.core.PApplet;
import gameobject.TextureObject;
import gameobject.TextObject;
import gameobject.RenderableObject;
import texture.Texture;
import rect.Rect;

/// Button that can be selected.
public class Button extends RenderableObject {

    private TextureObject mTexture;
    private TextObject mText;

    public Button(Texture texture, String text, int textSize) {

        mTexture = new TextureObject(texture);
        mText = new TextObject(textSize, true);
        mText.setText(text);

    }

    public Rect getBounds() {

        Rect bounds = mTexture.getBounds();
        bounds.transform(this);
        return bounds;

    }

    public float getWidth() {

        return mTexture.getWidth();

    }

    public float getHeight() {

        return mTexture.getHeight();

    }

    public void setClip(int clip) {

        mTexture.setClip(clip);

    }

    public void renderCurrent(PApplet core) {
        
        mTexture.render(core);
        mText.render(core);

    }

    /// Default button does nothing when selected.
    protected void select() {}

    protected void highlight(boolean highlighted) {

        if (highlighted) {
            mText.setColour(255);
        } else {
            mText.setColour(0);
        }

    }

}
