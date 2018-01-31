package scene;

import processing.core.PApplet;
import texture.Texture;
import gameobject.RenderableObject;
import gameobject.TextObject;
import gameobject.TextureObject;

public class InfoBox extends RenderableObject {

    private static final int TEXT_SIZE = 15;
    private static final float CONTINUE_OFFSET = 140f;

    private TextureObject mBack;
    private TextObject mText;
    private TextObject mContinue;

    public InfoBox(Texture backTexture, String text) {

        mBack = new TextureObject(backTexture);
        mText = new TextObject(TEXT_SIZE, true);
        mText.setText(text);
        mContinue = new TextObject(TEXT_SIZE, true);
        mContinue.setText("Click to continue");
        mContinue.setTranslation(0f, CONTINUE_OFFSET);

    }

    public void renderCurrent(PApplet core) {

        mBack.render(core);
        mText.render(core);
        mContinue.render(core);

    }



}
