package gameobject;

import processing.core.PApplet;
import processing.core.PConstants;
import colour.Colour;

/// A game object that displays text.
public class TextObject extends RenderableObject {

    /// The size of the text.
    private final int mSize;

    /// Whether the text is centred.
    private final boolean mCentred;

    /// Gray scale colour to use for text.
    private int mGrayColour;

    /// Colour to use for text.
    private Colour mColour;

    /// The text displayed.
    private String mText;

    /// Initialise with size and orientation.
    /// \param size text size to use.
    /// \param centred whether or not to use centred orientation.
    public TextObject(int size, boolean centred) {

        mSize = size;
        mCentred = centred;
        mGrayColour = 0;
        mColour = null;
        mText = new String();

    }

    public void setColour(int colour) {
        mGrayColour = colour;
    }

    public void setColour(Colour colour) {
        mColour = colour;
    }

    /// Set the current text being displayed.
    /// \param text the text to display.
    public void setText(String text) {
        mText = new String(text);
    }

    /// Render the text.
    /// \param core Processing core to use for rendering.
    public void renderCurrent(PApplet core) {

        // Align text.
        if (mCentred) {
            core.textAlign(PConstants.CENTER, PConstants.CENTER);
        } else {
            core.textAlign(PConstants.LEFT, PConstants.TOP);
        }
        
        // Set text size.
        core.textSize(mSize);

        // Set colour.
        if (mColour != null) {
            core.fill(mColour.r, mColour.g, mColour.b, mColour.a);
        } else {
            core.fill(mGrayColour);
        }
        core.text(mText, 0f, 0f);

    }

}
