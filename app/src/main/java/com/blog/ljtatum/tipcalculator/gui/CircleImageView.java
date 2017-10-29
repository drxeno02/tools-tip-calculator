package com.blog.ljtatum.tipcalculator.gui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;
import com.app.framework.utilities.FrameworkUtils;
import com.blog.ljtatum.tipcalculator.R;


/**
 * Class is a custom imageView transformer to create circular
 * images with border padding options
 *
 * @author Tatum
 */
public class CircleImageView extends NetworkImageView {

    private static final ScaleType SCALE_TYPE = ScaleType.CENTER_CROP;
    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    private static final int COLOR_DRAWABLE_DIMENSION = 2;
    private static final int DEFAULT_BORDER_WIDTH = 0;
    private static final int DEFAULT_BORDER_COLOR = Color.WHITE;
    private static final boolean DEFAULT_BORDER_OVERLAY = false;

    private final RectF mDrawableRect = new RectF();
    private final RectF mBorderRect = new RectF();
    private final Matrix mShaderMatrix = new Matrix();
    private final Paint mBitmapPaint = new Paint();
    private final Paint mBorderPaint = new Paint();
    private ColorFilter mColorFilter;

    @Nullable
    private Bitmap mBitmap;
    @Nullable
    private BitmapShader mBitmapShader;
    private int mBitmapWidth;
    private int mBitmapHeight;
    private int mBorderColor = DEFAULT_BORDER_COLOR;
    private int mBorderWidth = DEFAULT_BORDER_WIDTH;
    private float mDrawableRadius;
    private float mBorderRadius;
    private boolean mReady;
    private boolean mSetupPending;
    private boolean mBorderOverlay;

    /**
     * Constructor
     *
     * @param context Interface to global information about an application environment
     */
    public CircleImageView(Context context) {
        super(context);
        init();
    }

    /**
     * Constructor
     *
     * @param context Interface to global information about an application environment
     * @param attrs   A collection of attributes, as found associated with a tag in an XML document
     */
    public CircleImageView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    /**
     * Constructor
     *
     * @param context  Interface to global information about an application environment
     * @param attrs    A collection of attributes, as found associated with a tag in an XML document
     * @param defStyle The defined style
     */
    public CircleImageView(@NonNull Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        if (!isInEditMode()) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleImageView, defStyle, 0);
            mBorderWidth = a.getDimensionPixelSize(R.styleable.CircleImageView_border_width, DEFAULT_BORDER_WIDTH);
            mBorderColor = a.getColor(R.styleable.CircleImageView_border_color, DEFAULT_BORDER_COLOR);
            mBorderOverlay = a.getBoolean(R.styleable.CircleImageView_border_overlay, DEFAULT_BORDER_OVERLAY);
            a.recycle();
        }
        init();
    }

    /**
     * Method is used to initialize bitmap attributes by calling setup()
     */
    private void init() {
        super.setScaleType(SCALE_TYPE);
        mReady = true;
        if (mSetupPending) {
            setup();
            mSetupPending = false;
        }
    }

    @Override
    public void setAdjustViewBounds(boolean adjustViewBounds) {
        if (adjustViewBounds) {
            throw new IllegalArgumentException("adjustViewBounds not supported.");
        }
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        mBitmap = getBitmapFromDrawable(getDrawable());
        setup();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        mBitmap = getBitmapFromDrawable(drawable);
        setup();
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        mBitmap = bm;
        setup();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setup();
    }

    /**
     * Method is used to set the border color
     *
     * @param borderColor The color of the border
     */
    public void setBorderColor(int borderColor) {
        if (borderColor == mBorderColor) {
            return;
        }
        mBorderColor = borderColor;
        mBorderPaint.setColor(mBorderColor);
        invalidate();
    }

    @NonNull
    @Override
    public ScaleType getScaleType() {
        return SCALE_TYPE;
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (scaleType != SCALE_TYPE) {
            throw new IllegalArgumentException(String.format("ScaleType %s not supported.", scaleType));
        }
    }

    /**
     * Method is used to set the border width
     *
     * @param borderWidth The width of the border
     */
    public void setBorderWidth(int borderWidth) {
        if (borderWidth == mBorderWidth) {
            return;
        }
        mBorderWidth = borderWidth;
        setup();
    }

    /**
     * Method is used to retrieve a bitmap from drawable
     *
     * @param drawable A Drawable is a general abstraction for "something that can be drawn."
     * @return bitmap A mutable bitmap with the specified width and height
     */
    private Bitmap getBitmapFromDrawable(Drawable drawable) {
        if (FrameworkUtils.checkIfNull(drawable)) {
            return null;
        }

        if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        try {
            Bitmap bitmap;
            if (drawable instanceof ColorDrawable) {
                bitmap = Bitmap.createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG);
            } else {
                bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), BITMAP_CONFIG);
            }
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
            drawable.draw(canvas);
            return bitmap;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    /**
     * Method is used to setup the bitmap (image) with style, color, and other attributes
     * <p>It is necessary invalidate the whole view. If the view is visible,
     * {@link #onDraw(android.graphics.Canvas)} will be called at some point in
     * the future<p/>
     */
    private void setup() {
        if (!mReady) {
            mSetupPending = true;
            return;
        }

        if (FrameworkUtils.checkIfNull(mBitmap)) {
            return;
        }
        mBitmapShader = new BitmapShader(mBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mBitmapPaint.setAntiAlias(true);
        mBitmapPaint.setShader(mBitmapShader);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setColor(mBorderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBitmapHeight = mBitmap.getHeight();
        mBitmapWidth = mBitmap.getWidth();
        mBorderRect.set(0, 0, getWidth(), getHeight());
        mBorderRadius = Math.min((mBorderRect.height() - mBorderWidth) / 2, (mBorderRect.width() - mBorderWidth) / 2);
        mDrawableRect.set(mBorderRect);
        if (!mBorderOverlay) {
            mDrawableRect.inset(mBorderWidth, mBorderWidth);
        }
        mDrawableRadius = Math.min(mDrawableRect.height() / 2, mDrawableRect.width() / 2);
        updateShaderMatrix();
        invalidate();
    }

    /**
     * Method is used to update shader properties
     */
    private void updateShaderMatrix() {
        float scale;
        float dx = 0;
        float dy = 0;
        mShaderMatrix.set(null);
        if (mBitmapWidth * mDrawableRect.height() > mDrawableRect.width() * mBitmapHeight) {
            scale = mDrawableRect.height() / (float) mBitmapHeight;
            dx = (mDrawableRect.width() - mBitmapWidth * scale) * 0.5f;
        } else {
            scale = mDrawableRect.width() / (float) mBitmapWidth;
            dy = (mDrawableRect.height() - mBitmapHeight * scale) * 0.5f;
        }
        mShaderMatrix.setScale(scale, scale);
        mShaderMatrix.postTranslate((int) (dx + 0.5f) + mDrawableRect.left, (int) (dy + 0.5f) + mDrawableRect.top);

        if (!FrameworkUtils.checkIfNull(mBitmapShader)) {
            mBitmapShader.setLocalMatrix(mShaderMatrix);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        if (FrameworkUtils.checkIfNull(getDrawable())) {
            return;
        }
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDrawableRadius, mBitmapPaint);
        if (mBorderWidth != 0) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, mBorderRadius, mBorderPaint);
        }
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        if (cf == mColorFilter) {
            return;
        }
        mColorFilter = cf;
        mBitmapPaint.setColorFilter(mColorFilter);
        invalidate();
    }
}
