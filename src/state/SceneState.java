package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;

/// Abstract class representing a state that the game is in.
/// Handles updating, rendering and input handling for the game while
/// it is in this state.
public abstract class SceneState {
    
    /// Objects that make up the game scene.
    protected final Context mContext;
    protected PVector mMousePos;

    /// Initialise state with game context.
    public SceneState(Context context) {
        
        mContext = context;
        mMousePos = new PVector(0f, 0f);

    }

    /// \param delta time since last update.
    public void update(float delta) {}

    /// Renders the scene.
    /// \param core Processing core to use for rendering the scene.
    public void render(PApplet core) {}

    /// Update mouse position.
    /// \param mousePos the new mouse position.
    public void updateMousePos(PVector mousePos) {

        mMousePos = mousePos;

    }

    /// handle mouse press.
    /// \param mouseButton the mouse button pressed.
    /// \param mousePos the position the mouse was pressed.
    public void handleMousePress(int mouseButton, PVector mousePos) {}

    /// handle mouse release.
    /// \param mouseButton the mouse button released.
    /// \param mousePos the position the mouse was released.
    public void handleMouseRelease(int mouseButton, PVector mousePos) {}

    /// Handle key press.
    /// \param key the key that was pressed.
    public void handleKeyPress(char key) {}

    /// Handle key release.
    /// \param key the key that was released.
    public void handleKeyRelease(char key) {}

}
