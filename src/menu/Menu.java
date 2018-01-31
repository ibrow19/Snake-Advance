package menu;

import processing.core.PApplet;
import processing.core.PVector;
import java.util.ArrayList;
import java.util.Iterator;
import transform.Transformable;
import gameobject.RenderableObject;
import rect.Rect;

/// Menu - collection of buttons that can be highlighted and selected.
public class Menu extends RenderableObject {

    /// Buttons in menu.
    private final ArrayList<Button> mButtons;

    /// Currently highlighted button.
    private Button mHighlighted;

    /// Bounding rectangle of menu.
    private Rect mBounds;

    /// Initialise menu.
    /// \param buttons buttons to use for menu.
    /// \param spacing space between each button.
    public Menu(ArrayList<Button> buttons, float spacing) {

        mButtons = buttons;
        mHighlighted = null;
        mBounds = new Rect(0f, 0f, 0f, 0f);
        setLayout(spacing);

    }

    /// Get bounds of menu.
    public Rect getBounds() {

        Rect bounds = mBounds.copy();
        return bounds.transform(this);

    }

    /// Highlight position in menu.
    /// \param position position in menu to highlight.
    public boolean highlight(PVector position) {

        // Unhighlight any currently highlighted button.
        if (mHighlighted != null) {
            mHighlighted.highlight(false);
            mHighlighted = null;
        }

        // Find button that position overlaps with to highlight.
        Iterator<Button> it = mButtons.iterator();
        while (mHighlighted == null && it.hasNext()) {

            Button current = it.next();
            Rect bounds = current.getBounds();
            bounds.transform(this);
            if (bounds.contains(position)) {
                current.highlight(true);
                mHighlighted = current;
            }

        }
        return mHighlighted != null;

    }

    /// Select position in menu.
    /// \param position position in menu to select.
    public boolean select(PVector position) {
        
        // Find button overlapping with position to select.
        boolean selected = false;
        Iterator<Button> it = mButtons.iterator();
        while (!selected && it.hasNext()) {

            Button current = it.next();
            Rect bounds = current.getBounds();
            bounds.transform(this);
            if (bounds.contains(position)) {
                current.select();
                selected = true;
            }

        }
        return selected;

    }

    /// Render all the buttons in the menu.
    public void renderCurrent(PApplet core) {

        for (Button b : mButtons) {
            b.render(core);
        }

    }

    /// Set the layout of buttons in the menu.
    /// \param spacing space to leave between buttons.
    private void setLayout(float spacing) {

        Button first = mButtons.get(0);
        first.setTranslation(0f, first.getHeight() / 2f);

        mBounds.width = first.getWidth();

        float height = first.getHeight();
        for (int i = 1; i < mButtons.size(); ++i) {
            
            height += spacing;
            Button current = mButtons.get(i);

            float currentWidth = current.getWidth();
            if (currentWidth > mBounds.width) {
                mBounds.width = currentWidth;
            }

            float currentHeight = current.getHeight();
            current.setTranslation(0f, height + (currentHeight / 2f));
            height += currentHeight;

        }
        setOrigin(0f, height / 2f);
        mBounds.height = height;

    }

}
