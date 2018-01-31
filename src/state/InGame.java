package state;

import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PVector;
import java.util.function.Supplier;
import java.util.function.Consumer;
import java.util.ArrayList;
import menu.Menu;
import menu.Button;
import texture.Texture;
import config.Config;

public class InGame extends SceneState {

    public InGame(Context context) {

        super(context);

    }

    public void update(float delta) {

        mContext.gameManager.update(mMousePos, delta);
        checkFinished();

    }

    public void render(PApplet core) {

        mContext.gameManager.render(core);

    }

    public void handleMousePress(int mouseButton, PVector mousePos) {

        if (mouseButton == PConstants.LEFT) {
            mContext.gameManager.handleClick(mousePos);
            checkFinished();
        }

    }

    public void handleKeyPress(char key) {

        if (key == ' ') {
            mContext.scene.setState(new Pause(mContext));
        }

    }

    private void checkFinished() {

        if (mContext.gameManager.gameIsOver()) {
            mContext.gameManager = mContext.gameManager.getNext();
        }
        if (mContext.gameManager == null) {
            mContext.scene.setState(new MainMenu(mContext));
        }

    }

}
