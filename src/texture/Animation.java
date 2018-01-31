package texture;

public class Animation {

    /// Index of start clip.
    private final int mStartClip;

    /// Index of end clip.
    private final int mEndClip;

    /// Number of clips in animation.
    private final int mClips;

    /// Duration of animation in seconds.
    private final float mDuration;

    /// Time passed in animation.
    private float mTime;

    /// Whether the animation has finished.
    private boolean mFinished;

    private boolean mLoop;

    /// Initialise clips and duration.
    /// \param start index of start clip.
    /// \param end index of end clip.
    /// \param duration the animation in seconds.
    public Animation(int start, int end, float duration) {

        mStartClip = start;
        mEndClip = end;
        mClips = end - start + 1;

        mDuration = duration;
        mTime = 0f;

        mFinished = false;
        mLoop = false;

    }

    public void setLoop(boolean loop) {

        mLoop = loop; 

    }

    /// Check whether the animation has finished.
    /// \return whether the animation has finished.
    public boolean isFinished() {

        return mFinished;

    }

    /// Get the current clip in the animation.
    /// \return the index of the current clip.
    public int getClip() {

        /// If finished return the last clip.
        if (mFinished) {

            return mEndClip;

        }

        /// Calculate current clip based on time passed in animation.
        int current = (int)((mClips / mDuration) * mTime);
        return current + mStartClip;

    }

    /// Reset the animation to the start.
    public void reset() {
        
        mTime = 0;
        mFinished = false;

    }

    /// Update the animation based on the time since the last update.
    /// \param delta the difference in time since the last update.
    public void update(float delta) {

        // Update the time passed in the animation.
        if (!mFinished) {
            
            mTime += delta;

        } 
        // finish the animation if it has reached its full duration.
        if (mTime >= mDuration) {

            if (mLoop) {

                mTime -= mDuration;

            } else {

                mFinished = true;

            }

        }

    }

}
