package transform;

import processing.core.PVector;
import processing.core.PApplet;

/// Object that can be transformed (rotates, scaled and translated).
public class Transformable {

    /// Rotation of object in degrees.
    private float mRotation;

    /// Translation of object from origin.
    private PVector mOrigin;

    /// Translation of object.
    private PVector mTranslation;

    /// Scale of object.
    private PVector mScale;

    /// Initialise transformation properties.
    public Transformable() {

        mRotation = 0f;
        mOrigin = new PVector(0f, 0f);
        mTranslation = new PVector(0f, 0f);
        mScale = new PVector(1f, 1f);

    }

    /// Apply the objects current transformation to the world.
    /// \param core Processing core to use to carry out the transformation.
    public void applyTransform(PApplet core) {

        // Use get methods, allowing children to overload them for custom transformations.
        PVector translation = getTranslation();
        float rotation = getRadianRotation();
        PVector origin = getOrigin();
        PVector scale = getScale();

        // Scale, rotate, then translate to match object's current translation.
        core.translate(translation.x, translation.y);
        core.rotate(rotation);
        core.scale(scale.x, scale.y);
        core.translate(-origin.x, -origin.y);

    }

    /// Get the object's current rotation in degrees.
    /// \return the object's current rotation.
    public float getRotation() {

        // Ensure rotation is from 0 to 360 when fetched.
        while (mRotation >= 360f) {

            mRotation -= 360f;

        }
        while (mRotation < 0f) {

            mRotation += 360f;

        }
        return mRotation;

    }


    /// Get the object's current rotation in radians.
    /// \return object's current rotation in radians.
    public float getRadianRotation() {

        return getRotation() * ((float)Math.PI / 180f);

    }

    /// Get the object's current origin.
    /// \return the object's current origin.
    public PVector getOrigin() {

        return mOrigin.copy(); 

    }

    /// Get the origin translation of the object on the x axis.
    /// \return the origin translation of the object on the x axis.
    public float getXOrigin() {

        return mOrigin.x; 

    }

    /// Get the origin translation of the object on the y axis.
    /// \return the origin translation of the object on the y axis.
    public float getYOrigin() {

        return mOrigin.y;

    }

    /// Get the object's current translation.
    /// \return the object's current translation.
    public PVector getTranslation() {

        return mTranslation.copy(); 

    }

    /// Get the translation of the object on the x axis.
    /// \return the translation of the object on the x axis.
    public float getXTranslation() {

        return mTranslation.x; 

    }

    /// Get the translation of the object on the y axis.
    /// \return the translation of the object on the y axis.
    public float getYTranslation() {

        return mTranslation.y;

    }

    /// Get the current scale of the object.
    /// \return the current scale of the object.
    public PVector getScale() {

        return mScale.copy();

    }

    /// Get the current scale of the object on the x axis.
    /// \return the current scale of the object on the x axis.
    public float getXScale() {

        return mScale.x;

    }

    /// Get the current scale of the object on the y axis.
    /// \return the current scale of the object on the y axis.
    public float getYScale() {

        return mScale.y;

    }

    /// Rotate the object.
    /// \param angle the angle in degrees to modify the object's rotation with.
    public void rotate(float angle) {

        mRotation += angle;

    }

    /// Set the object's rotation.
    /// \param angle the angle in degrees to set the object's rotation to.
    public void setRotation(float angle) {

        mRotation = angle;

    }

    /// Set the object's origin.
    /// \param translation point to set the object's origin to.
    public void setOrigin(PVector origin) {

        mOrigin.set(origin);

    }

    /// Set the object's origin.
    /// \param x x component of object's new origin.
    /// \param y y component of object's new origin.
    public void setOrigin(float x, float y) {

        mOrigin.set(x, y);

    }

    /// Translate the object.
    /// \param move vector to translate the object with.
    public void translate(PVector move) {

        mTranslation.add(move); 

    }

    /// Translate the object.
    /// \param x x component of translation.
    /// \param y y component of translation.
    public void translate(float x, float y) {

        mTranslation.add(x, y); 

    }

    /// Set the object's translation.
    /// \param translation point to set the object's translation to.
    public void setTranslation(PVector translation) {

        mTranslation.set(translation);

    }

    /// Set the object's translation.
    /// \param x x component of object's new translation.
    /// \param y y component of object's new translation.
    public void setTranslation(float x, float y) {

        mTranslation.set(x, y);

    }

    /// Scale the object.
    /// \param scaling vector containing x and y components to scale object with.
    public void scale(PVector scaling) {

        mScale.x *= scaling.x; 
        mScale.y *= scaling.y; 

    }

    /// Scale the object.
    /// \param x the scaling on the x axis.
    /// \param y the scaling on the y axis.
    public void scale(float x, float y) {

        mScale.x *= x; 
        mScale.y *= y; 

    }

    /// Set the scale of the object.
    /// \param scale the new scale to set the object to use.
    public void setScale(PVector scale) {

        mScale.set(scale);

    }

    /// Set the scale of the object.
    /// \param x the new scale on the x axis.
    /// \param y the new scale on the y axis.
    public void setScale(float x, float y) {

        mScale.set(x, y);

    }

}
