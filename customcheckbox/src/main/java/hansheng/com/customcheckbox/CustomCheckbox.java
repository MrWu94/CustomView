package hansheng.com.customcheckbox;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;

/**
 * Created by hansheng on 2016/6/2.
 */
public class CustomCheckbox extends View implements Checkable {


    private final static float BOUNCE_VALUE = 0.2F;

    private Drawable checkDrawable;

    //绘制背景画笔
    private Paint bitmapPaint;
    //擦除背景的橡皮擦
    private Paint bitmapEraser;
    //绘制选中图标的画笔
    private Paint checkEraser;
    //绘制边框的画笔
    private Paint borderPaint;
    //背景画布
    private Canvas bitmapCanvas;
    //绘制选中图标的画布
    private Canvas checkCanvas;
    private Bitmap drawBitmap;
    private Bitmap checkBitmap;
    private float progress;
    private ObjectAnimator checkAnim;

    private boolean attachedToWindow;
    private boolean isChecked;

    private int size = 22;
    private int bitmapColor = 0xFF3F51B5;
    private int borderColor = 0xFFFFFFFF;


    public CustomCheckbox(Context context) {
        this(context, null);
    }

    public CustomCheckbox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomCheckbox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == VISIBLE && drawBitmap == null) {
            drawBitmap = Bitmap.createBitmap(dp(size), dp(size), Bitmap.Config.ARGB_8888);
            bitmapCanvas = new Canvas(drawBitmap);
            checkBitmap = Bitmap.createBitmap(dp(size), dp(size), Bitmap.Config.ARGB_8888);
            checkCanvas = new Canvas(checkBitmap);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (getVisibility() != VISIBLE) {
            return;
        }
        checkEraser.setStrokeWidth(size);

        drawBitmap.eraseColor(0);
        float rad = getMeasuredWidth() / 2;

        float bitmapProgress = progress >= 0.5f ? 1.0f : progress / 0.5f;
        float checkProgress = progress < 0.5f ? 0.0f : (progress - 0.5f) / 0.5f;
        float p = isChecked ? progress : (1.0f - progress);

        if (p < BOUNCE_VALUE) {
            rad -= dp(2) * p;
        } else if (p < BOUNCE_VALUE * 2) {
            rad -= dp(2) - dp(2) * p;
        }
        borderPaint.setColor(borderColor);
        canvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad - dp(1), borderPaint);

        bitmapPaint.setColor(bitmapColor);

        bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad, bitmapPaint);
        bitmapCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad * (1 - bitmapProgress), bitmapEraser);
        canvas.drawBitmap(drawBitmap, 0, 0, null);

        checkBitmap.eraseColor(0);
        int w = checkDrawable.getIntrinsicWidth();
        int h = checkDrawable.getIntrinsicHeight();
        int x = (getMeasuredWidth() - w) / 2;
        int y = (getMeasuredHeight() - h) / 2;

        checkDrawable.setBounds(x, y, x + w, y + h);
        checkDrawable.draw(checkCanvas);
        checkCanvas.drawCircle(getMeasuredWidth() / 2, getMeasuredHeight() / 2, rad * (1 - checkProgress), checkEraser);

        canvas.drawBitmap(checkBitmap, 0, 0, null);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        attachedToWindow = true;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        attachedToWindow = false;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int newSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.getMode(Math.min(widthMeasureSpec, heightMeasureSpec)));
        super.onMeasure(newSpec, newSpec);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = getContext().obtainStyledAttributes(attrs, R.styleable.CheckBox_CustomView);
        size = ta.getDimensionPixelSize(R.styleable.CheckBox_CustomView_size, dp(size));
        bitmapColor = ta.getColor(R.styleable.CheckBox_CustomView_color_background, bitmapColor);
        borderColor = ta.getColor(R.styleable.CheckBox_CustomView_color_border, borderColor);

        //Paint.ANTI_ALIAS_FLAG 表示抗锯齿，为了在执行动画时候不出现你不想看到的东西

        bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        bitmapEraser.setColor(0);
        bitmapEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        checkEraser = new Paint(Paint.ANTI_ALIAS_FLAG);
        checkEraser.setColor(0);
        checkEraser.setStyle(Paint.Style.STROKE);
        checkEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(dp(2));
        //为了让选中的图标更美观就通过图片的形式添加，
        checkDrawable = context.getResources().getDrawable(R.mipmap.check);
        setVisibility(VISIBLE);
        ta.recycle();
    }

    public void setProgress(float value) {
        if (progress == value) {
            return;
        }
        progress = value;
        invalidate();
    }

    public void setSize(int size) {
        this.size = size;
    }

    public float getProgress() {
        return progress;
    }

    public void setCheckedColor(int value) {
        bitmapColor = value;
    }

    public void setBorderColor(int value) {
        borderColor = value;
        borderPaint.setColor(borderColor);
    }

    private void cancelAnim() {
        if (checkAnim != null) {
            checkAnim.cancel();
        }
    }

    private void addAnim(boolean isChecked) {
        checkAnim = ObjectAnimator.ofFloat(this, "progress", isChecked ? 1.0f : 0.0f);
        checkAnim.setDuration(300);
        checkAnim.start();
    }

    public void setChecked(boolean checked, boolean animated) {
        if (checked == isChecked) {
            return;
        }
        isChecked = checked;

        if (attachedToWindow && animated) {
            addAnim(checked);
        } else {
            cancelAnim();
            setProgress(checked ? 1.0f : 0.0f);
        }
    }


    @Override
    public void toggle() {
        setChecked(!isChecked);
    }

    @Override
    public void setChecked(boolean b) {
        setChecked(b, true);
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    public int dp(float value) {
        if (value == 0) {
            return 0;
        }
        float density = getContext().getResources().getDisplayMetrics().density;
        return (int) Math.ceil(density * value);
    }
}
