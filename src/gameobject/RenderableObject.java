package gameobject;

import processing.core.PApplet;
import transform.Transformable;

/// Object which can be rendered.
public abstract class RenderableObject extends Transformable {

    /// Implementation of rendering for this object.
    /// \param
    public abstract void renderCurrent(PApplet core);

    /// Render using the object's transformation.
    /// \param core Processing core to use for rendering.
    public void render(PApplet core) {

        core.pushMatrix();
        applyTransform(core);
        renderCurrent(core);
        core.popMatrix();

    }

    /// Combine this object's transformation with another then render it.
    /// \param core Processing core to use for rendering.
    /// \param combined transformation to combine with.
    public void render(PApplet core, Transformable combined) {

        core.pushMatrix();
        combined.applyTransform(core);
        applyTransform(core);
        renderCurrent(core);
        core.popMatrix();

    }


} 
