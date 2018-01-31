package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import texture.Texture;
import config.Config;
import menu.Menu;
import menu.Button;
import menu.ActionButton;
import java.util.ArrayList;
import java.lang.Runnable;
import gameobject.TextureObject;
import scene.SymmetricLevel;
import scene.Level1;
import controller.Controller;
import controller.PlayerController;
import controller.AIController;

public class MainMenu extends SceneState {

    private static final float SPACING = 50f;

    private TextureObject mTitle;
    private TextureObject mSnake;
    private Menu mMenu;

    public MainMenu(Context context) {

        super(context);
        initTitle();
        initMenu();

    }

    public void render(PApplet core) {

        mTitle.render(core);
        mSnake.render(core);
        mMenu.render(core);

    }

    public void update(float delta) {

        mMenu.highlight(mMousePos);

    }

    public void handleMousePress(int mouseButton, PVector mousePos) {

        if (mouseButton == PConstants.LEFT) {
            mMenu.select(mousePos);
        }

    }

    private void initTitle() {

        Texture titleTexture = mContext.textureManager.getTexture(Config.TITLE_TEXTURE_ID);
        Texture snakeTexture = mContext.textureManager.getTexture(Config.TITLE_SNAKE_TEXTURE_ID);
        mTitle = new TextureObject(titleTexture);
        mSnake = new TextureObject(snakeTexture);

        mTitle.setTranslation(Config.WINDOW_WIDTH / 2f, mTitle.getHeight() / 2f);
        mSnake.setTranslation(Config.WINDOW_WIDTH / 4f * 3f, Config.WINDOW_HEIGHT / 4f * 3f);

    }

    private void initMenu() {

        Texture buttonTexture = mContext.textureManager.getTexture(Config.BUTTON_TEXTURE_ID);

        Runnable levelsInit = 
           () -> {
               mContext.gameManager = new Level1(mContext.textureManager);
               mContext.scene.setState(new InGame(mContext));
           };

        Runnable pvpInit = 
           () -> {
               Controller[] controllers = new Controller[] {new PlayerController(),
                                                            new PlayerController()};
               mContext.gameManager = new SymmetricLevel(mContext.textureManager, controllers);
               mContext.scene.setState(new InGame(mContext));
           };

        Runnable pvaInit = 
           () -> {
               Controller[] controllers = new Controller[] {new PlayerController(),
                                                            new AIController()};
               mContext.gameManager = new SymmetricLevel(mContext.textureManager, controllers);
               mContext.scene.setState(new InGame(mContext));
           };

        Runnable avaInit = 
           () -> {
               Controller[] controllers = new Controller[] {new AIController(),
                                                            new AIController()};
               mContext.gameManager = new SymmetricLevel(mContext.textureManager, controllers);
               mContext.scene.setState(new InGame(mContext));
           };

        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(new ActionButton(buttonTexture, "Levels", 
                                     Config.BUTTON_TEXT_SIZE, levelsInit));
        buttons.add(new ActionButton(buttonTexture, "Player VS Player", 
                                     Config.BUTTON_TEXT_SIZE, pvpInit));
        buttons.add(new ActionButton(buttonTexture, "Player VS AI", 
                                     Config.BUTTON_TEXT_SIZE, pvaInit));
        buttons.add(new ActionButton(buttonTexture, "AI VS AI", 
                                     Config.BUTTON_TEXT_SIZE, avaInit));

        for (Button button : buttons) {
            button.setClip(1);
        }
        
        mMenu = new Menu(buttons, SPACING);
        mMenu.setTranslation(Config.WINDOW_WIDTH / 3f, Config.WINDOW_HEIGHT / 5f * 3f);

    }

}
