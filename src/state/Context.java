package state;

import processing.core.PApplet;
import processing.core.PVector;
import texture.TextureManager;
import scene.Scene;
import scene.GameManager;
import controller.Controller;

/// Game context holding objects that make up the scene.
public class Context {

    /// Texture manager to use for initialisation.
    public final TextureManager textureManager;
    public final Scene scene;

    public GameManager gameManager;

    public Context(Scene scene, TextureManager textureManager) {

        this.textureManager = textureManager;
        this.scene = scene;
        this.gameManager = null;//new GameManager(textureManager);

    }

}
