package es.sanchez.jaime.statusme;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;

public class TextAnimator extends Animation {

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 50; // Velocidad de escritura, puedes ajustarla según tus preferencias
    private TextView mTextView;

    public TextAnimator(CharSequence text, TextView textView) {
        mText = text;
        mTextView = textView;
        mIndex = 0;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int length = mText.length();
        if (interpolatedTime < 1.0f && mIndex < length) {
            int currentLength = (int) (length * interpolatedTime);
            if (currentLength > mIndex) {
                mTextView.setText(mText.subSequence(0, currentLength));
                mIndex = currentLength;
            }
        } else {
            mTextView.setText(mText);
        }
    }

    @Override
    public boolean willChangeTransformationMatrix() {
        return false;
    }

    @Override
    public boolean willChangeBounds() {
        return false;
    }
}
