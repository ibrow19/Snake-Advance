package scene;

import processing.core.PApplet;
import processing.core.PVector;
import texture.TextureManager;
import state.SceneState;
import state.MainMenu;
import state.Context;


/// Game scene. Manages and renders the game world.
public class Scene {

    private SceneState mState;

    /// Initialise scene.
    /// \param textureManager central storage of textures that can be used in the game.
    public Scene(TextureManager textureManager) {

        mState = new MainMenu(new Context(this, textureManager));

    }

    public void setState(SceneState state) {

        mState = state;

    }

    /// Update the scene.
    /// \param delta time since the last update.
    public void update(float delta) {

        mState.update(delta);

    }

    /// Render the scene.
    /// \param core Processing core to use for rendering.
    public void render(PApplet core) {

        mState.render(core);

    }

    /// Update the mouse position.
    /// \param mousePos the mouse position to use.
    public void updateMousePos(PVector mousePos) {

        // Delegate to current state.
        mState.updateMousePos(mousePos);

    }

    /// Handle mouse press event and update state based on result.
    public void handleMousePress(int mouseButton, PVector mousePos) {

        // Delegate to current state.
        mState.handleMousePress(mouseButton, mousePos);

    }

    /// Handle mouse release event and update state based on result.
    public void handleMouseRelease(int mouseButton, PVector mousePos) {

        // Delegate to current state.
        mState.handleMouseRelease(mouseButton, mousePos);

    }

    /// Handle key press event and update state based on result.
    public void handleKeyPress(char key) {

        // Delegate to current state.
        mState.handleKeyPress(key);

    }

    /// Handle key release release and update state based on result.
    public void handleKeyRelease(char key) {

        // Delegate to current state.
        mState.handleKeyRelease(key);

    }

}
