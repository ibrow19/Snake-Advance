package controller;

import processing.core.PVector;
import scene.GameManager;

/// Controller that uses user input to control the game.
public class PlayerController extends Controller {

    /// Handle a click by selecting relevant objects in the game.
    /// \param gameManager game to handle click for.
    /// \param mousePos position of click.
    public void handleClick(GameManager gameManager, PVector mousePos) {
        gameManager.select(mousePos);
    }

    /// Update the game by highlighting relevant game objects.
    /// \param gameManager game to update.
    /// \param mousePos position at time of update.
    /// \param delta time since last update.
    public void update(GameManager gameManager, PVector mousePos, float delta) {
        gameManager.highlight(mousePos);
    }
}
