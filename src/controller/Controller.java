package controller;

import processing.core.PVector;
import scene.GameManager;

/// Controller that manages how the game is interacted with.
public abstract class Controller {

    /// Handle a click.
    /// \param gameManager game to handle click for.
    /// \param mousePos position of click.
    public void handleClick(GameManager gameManager, PVector mousePos) {}

    /// Update the game.
    /// \param gameManager game to update.
    /// \param mousePos position at time of update.
    /// \param delta time since last update.
    public abstract void update(GameManager gameManager, PVector mousePos, float delta);

}
