package es.sanchez.jaime.statusme;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class CircularImageView extends AppCompatImageView {

    private Paint paint;

    public CircularImageView(Context context) {
        super(context);
        initialize();
    }

    public CircularImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public CircularImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        paint = new Paint();
        paint.setAntiAlias(true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();

        if (drawable == null) {
            return;
        }

        if (getWidth() == 0 || getHeight() == 0) {
            return;
        }

        Bitmap bitmap = drawableToBitmap(drawable);
        Bitmap circleBitmap = getCircularBitmap(bitmap);
        canvas.drawBitmap(circleBitmap, 0, 0, null);
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        int radius = Math.min(bitmap.getWidth(), bitmap.getHeight()) / 2;
        BitmapShader shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);

        Bitmap circularBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(circularBitmap);
        canvas.drawCircle(radius, radius, radius, paint);
        paint.setShader(shader);
        canvas.drawCircle(radius, radius, radius, paint);

        return circularBitmap;
    }
}
