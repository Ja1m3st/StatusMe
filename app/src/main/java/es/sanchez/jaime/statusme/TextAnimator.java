package es.sanchez.jaime.statusme;

import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.TextView;
// Texto animado
public class TextAnimator extends Animation { // Declara una clase TextAnimator que hereda de Animation

    private CharSequence mText;
    private int mIndex;
    private long mDelay = 50;
    private TextView mTextView;

    public TextAnimator(CharSequence text, TextView textView) {
        mText = text;
        mTextView = textView;
        mIndex = 0;
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        int length = mText.length();
        // Comprueba si el tiempo interpolado es menor a 1.0 y si el índice es menor que la longitud del texto
        if (interpolatedTime < 1.0f && mIndex < length) {
            // Calcula la longitud actual del texto animado
            int currentLength = (int) (length * interpolatedTime);
            // Comprueba si la longitud actual es mayor que el índice actual
            if (currentLength > mIndex) {
                // Establece el texto parcial en el TextView
                mTextView.setText(mText.subSequence(0, currentLength));
                // Actualiza el índice con la longitud actual
                mIndex = currentLength;
            }
        } else {
            // Establece el texto completo en el TextView
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