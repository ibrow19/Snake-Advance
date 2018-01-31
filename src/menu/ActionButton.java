package menu;

import scene.Scene;
import state.Context;
import texture.Texture;
import java.lang.Runnable;

/// Button that carries out action when selected.
public class ActionButton extends Button {

    private final Runnable mAction;

    public ActionButton(Texture texture, 
                        String text,
                        int textSize,
                        Runnable action) {

        super(texture, text, textSize);
        mAction = action;

    }

    protected void select() {
        mAction.run();
    }

}
