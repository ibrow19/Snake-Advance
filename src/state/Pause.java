package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.ArrayList;
import menu.Menu;
import menu.Button;
import menu.ActionButton;
import texture.Texture;
import config.Config;

public class Pause extends SceneState {

    private Menu mMenu;

    public Pause(Context context) {

        super(context);
        initMenu();

    }

    public void update(float delta) {

        mMenu.highlight(mMousePos);

    }

    public void render(PApplet core) {

        mContext.gameManager.render(core);
        mMenu.render(core);

    }

    public void handleMousePress(int mouseButton, PVector mousePos) {

        if (mouseButton == PConstants.LEFT) {
            mMenu.select(mMousePos);
        }

    }

    public void handleKeyPress(char key) {

        if (key == ' ') {
            mContext.scene.setState(new InGame(mContext));
        }

    }

    private void initMenu() {

        Texture buttonTexture = mContext.textureManager.getTexture(Config.BUTTON_TEXTURE_ID);

        Runnable skip = 
            () -> {
                mContext.gameManager = mContext.gameManager.getNext();
                if (mContext.gameManager == null) {
                    mContext.scene.setState(new MainMenu(mContext));
                } else {
                    mContext.scene.setState(new InGame(mContext));
                }
            };

        Runnable mainMenu = 
            () -> {
                mContext.gameManager = null;
                mContext.scene.setState(new MainMenu(mContext));
            };

        Runnable unpause = 
            () -> {
                mContext.scene.setState(new InGame(mContext));
            };

        ArrayList<Button> buttons = new ArrayList<>();
        buttons.add(new ActionButton(buttonTexture, "Skip", 
                                     Config.BUTTON_TEXT_SIZE, skip));
        buttons.add(new ActionButton(buttonTexture, "Main Menu", 
                                     Config.BUTTON_TEXT_SIZE, mainMenu));
        buttons.add(new ActionButton(buttonTexture, "Unpause", 
                                     Config.BUTTON_TEXT_SIZE, unpause));

        for (Button button : buttons) {
            button.setClip(1);
        }
        
        mMenu = new Menu(buttons, 0);
        mMenu.setTranslation(Config.WINDOW_WIDTH / 2f, Config.WINDOW_HEIGHT / 2f);

    }

}
