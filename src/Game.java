import processing.core.PApplet;
import processing.core.PVector;
import java.lang.Exception;
import java.util.ArrayList;
import java.util.List;
import scene.Scene;
import texture.TextureManager;
import texture.Texture;
import rect.Rect;
import config.Config;

/// Class to initialise game and handle processing main loop.
public class Game extends PApplet {

    // Constants.
    private static final int FPS = 60;
    private static final int UPDATE_RATE = 120;
    private static final float STEP_SIZE = 1f / UPDATE_RATE;

    /// Accumulated time since last frame.
    private float mAccumulator;

    /// The time at the start of the frame.
    private float mStartTime;

    /// The game scene to update and draw.
    private Scene mScene;

    /// Use this class for processing main loop.
    public static void main(String[] args) {

        PApplet.main("Game");

    }

    /// Initialise screen size settings.
    public void settings() {

        size(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);

    }

    /// Setup the game by loading images and initialising the scene.
    public void setup() {

        // Set frame rate and start time.
        frameRate(FPS);
        mStartTime = millis() / 1000f;

        TextureManager textureManager = new TextureManager();

        // Attempt to load and initialise textures, exit on failure.
        try {

            Texture title = new Texture(this, "title.png");
            textureManager.addTexture(Config.TITLE_TEXTURE_ID, title);

            Texture titleSnake = new Texture(this, "titlesnake.png");
            textureManager.addTexture(Config.TITLE_SNAKE_TEXTURE_ID, titleSnake);

            // Load button texture.
            Texture button = new Texture(this, "button.png");
            addClips(button, 0, 2, 200, 50);
            textureManager.addTexture(Config.BUTTON_TEXTURE_ID, button);

            Texture info = new Texture(this, "infoback.png");
            textureManager.addTexture(Config.INFO_BACK_TEXTURE_ID, info);

            // Load tile highlighting texture.
            Texture highlight = new Texture(this, "highlight.png");
            textureManager.addTexture(Config.HIGHLIGHT_TEXTURE_ID, highlight);

            Texture selectable = new Texture(this, "selectable.png");
            addClips(selectable, 0, 2, 75, 75);
            textureManager.addTexture(Config.SELECTABLE_TEXTURE_ID, selectable);

            // Load terrain texture.
            Texture terrain = new Texture(this, "terrain.png");
            addClips(terrain, 0, 5, 75, 75);
            textureManager.addTexture(Config.TERRAIN_TEXTURE_ID, terrain);

            // Load unit textures.
            Texture hq = new Texture(this, "hq2.png");
            addClips(hq, 0, 2, 75, 75);
            textureManager.addTexture(Config.HQ_TEXTURE_ID, hq);

            Texture snake = new Texture(this, "snake.png");
            addClips(snake, 0, 12, 75, 75);
            textureManager.addTexture(Config.SNAKE_TEXTURE_ID, snake);

            Texture wheel = new Texture(this, "wheelsnake2.png");
            addClips(wheel, 0, 24, 75, 75);
            textureManager.addTexture(Config.WHEEL_SNAKE_TEXTURE_ID, wheel);

            Texture tank = new Texture(this, "tanksnake.png");
            addClips(tank, 0, 6, 75, 75);
            textureManager.addTexture(Config.TANK_SNAKE_TEXTURE_ID, tank);

            Texture jet = new Texture(this, "jetsnake.png");
            addClips(jet, 0, 6, 75, 75);
            textureManager.addTexture(Config.JET_SNAKE_TEXTURE_ID, jet);

            // Load flag textures.
            Texture flag = new Texture(this, "flag2.png");
            addClips(flag, 0, 3, 75, 75);
            textureManager.addTexture(Config.FLAG_TEXTURE_ID, flag);

            mScene = new Scene(textureManager);

        } catch (Exception e) {

            e.printStackTrace();
            exit();

        }

    }

    /// Draw the game.
    public void draw() {

        // Calculate time since last frame.
        float currentTime = millis()/1000f;
        float frameTime = currentTime - mStartTime;

        mStartTime = currentTime;
        mAccumulator += frameTime;

        mScene.updateMousePos(new PVector(mouseX, mouseY)); 

        // While there is still enough time left in accumulator update the
        // game with the fixed timestep.
        while (mAccumulator >= STEP_SIZE) {

            mScene.update(STEP_SIZE); 
            mAccumulator -= STEP_SIZE;

        }

        // Clear screen then render current game state.
        background(255);
        mScene.render(this);

    }

    /// Handle mouse press event.
    public void mousePressed() {

        // Delegate handling to scene.
        mScene.handleMousePress(mouseButton, new PVector(mouseX, mouseY));

    }

    /// Handle mouse release event.
    public void mouseReleased() {

        // Delegate handling to scene.
        mScene.handleMouseRelease(mouseButton, new PVector(mouseX, mouseY));

    }

    /// Handle key pressed event.
    public void keyPressed() {

        // Delegate handling to scene.
        mScene.handleKeyPress(key);

    }

    /// Handle key released event.
    public void keyReleased() {

        // Delegate handling to scene.
        mScene.handleKeyRelease(key);

    }

    /// Add contiguous clips of a set size to a texture starting at specified index.
    /// \param texture the texture to add clips to.
    /// \param startIndex the index to start adding clips at.
    /// \param clips the number of clips to add.
    /// \param width the width of each clip to add.
    /// \param height the height of each clip to add.
    private void addClips(Texture texture, int startIndex, int clips, int width, int height) {

        for (int i = startIndex; i < clips; ++i) {

            texture.addClip(new Rect(i * width, 0, width, height));

        }

    }

}
