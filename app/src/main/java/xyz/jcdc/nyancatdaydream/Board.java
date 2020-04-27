package xyz.jcdc.nyancatdaydream;

import android.animation.TimeAnimator;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.appcompat.widget.AppCompatImageView;

import java.util.Random;

import xyz.jcdc.nyancatdaydream.utils.SharedPrefsUtil;

public class Board extends FrameLayout
{
    public static final boolean FIXED_STARS = true;

    static Random sRNG = new Random();

    static float lerp(float a, float b, float f) {
        return (b-a)*f + a;
    }

    static float randfrange(float a, float b) {
        return lerp(a, b, sRNG.nextFloat());
    }

    static int randsign() {
        return sRNG.nextBoolean() ? 1 : -1;
    }

    static <E> E pick(E[] array) {
        if (array.length == 0) return null;
        return array[sRNG.nextInt(array.length)];
    }

    public class FlyingCat extends AppCompatImageView {
        public static final float VMAX = 1000.0f;
        public static final float VMIN = 100.0f;

        public float v, vr;

        public float dist;
        public float z;

        public ComponentName component;

        public FlyingCat(Context context, AttributeSet as) {
            super(context, as);
            setImageResource(R.drawable.nyandroid_anim); // @@@
        }

        public String toString() {
            return String.format("<cat (%.1f, %.1f) (%d x %d)>",
                    getX(), getY(), getWidth(), getHeight());
        }

        public void reset() {
            final float scale = lerp(0.1f,2f,z);
            setScaleX(scale); setScaleY(scale);

            setX(-scale*getWidth()+1);
            setY(randfrange(0, Board.this.getHeight()-scale*getHeight()));
            v = lerp(VMIN, VMAX, z);

            dist = 0;

//                android.util.Log.d("Nyandroid", "reset cat: " + this);
        }

        public void update(float dt) {
            dist += v * dt;
            setX(getX() + v * dt);
        }
    }

    TimeAnimator mAnim;

    public Board(Context context, AttributeSet as) {
        super(context, as);

        setLayerType(View.LAYER_TYPE_HARDWARE, null);
        setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        setBackgroundColor(0xFF003366);
    }

    private void reset() {
//            android.util.Log.d("Nyandroid", "board reset");
        removeAllViews();

        final ViewGroup.LayoutParams wrap = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        if (FIXED_STARS) {
            for(int i=0; i<40; i++) {
                AppCompatImageView fixedStar = new AppCompatImageView(getContext(), null);
                fixedStar.setImageResource(R.drawable.star_anim); // @@@
                addView(fixedStar, wrap);
                final float scale = randfrange(0.1f, 1f);
                fixedStar.setScaleX(scale); fixedStar.setScaleY(scale);
                fixedStar.setX(randfrange(0, getWidth()));
                fixedStar.setY(randfrange(0, getHeight()));
                final AnimationDrawable anim = (AnimationDrawable) fixedStar.getDrawable();
                postDelayed(new Runnable() {
                    public void run() {
                        anim.start();
                    }}, (int) randfrange(0, 1000));
            }
        }

        final int count_cats = SharedPrefsUtil.getCount(getContext());
        final float speed_cat = SharedPrefsUtil.getSpeed(getContext()) * 1000f;

        for(int i=0; i<count_cats; i++) {
            FlyingCat nv = new FlyingCat(getContext(), null);
            addView(nv, wrap);
            nv.z = ((float)i/count_cats);
            nv.z *= nv.z;
            nv.reset();
            nv.setX(randfrange(0,Board.this.getWidth()));
            final AnimationDrawable anim = (AnimationDrawable) nv.getDrawable();
            postDelayed(new Runnable() {
                public void run() {
                    anim.start();
                }}, (int) randfrange(0, 1000));
        }

        if (mAnim != null) {
            mAnim.cancel();
        }
        mAnim = new TimeAnimator();
        mAnim.setTimeListener(new TimeAnimator.TimeListener() {
            public void onTimeUpdate(TimeAnimator animation, long totalTime, long deltaTime) {
                // setRotation(totalTime * 0.01f); // not as cool as you would think
//                    android.util.Log.d("Nyandroid", "t=" + totalTime);

                for (int i=0; i<getChildCount(); i++) {
                    View v = getChildAt(i);
                    if (!(v instanceof FlyingCat)) continue;
                    FlyingCat nv = (FlyingCat) v;
                    nv.update(deltaTime / speed_cat);
                    final float catWidth = nv.getWidth() * nv.getScaleX();
                    final float catHeight = nv.getHeight() * nv.getScaleY();
                    if (   nv.getX() + catWidth < -2
                            || nv.getX() > getWidth() + 2
                            || nv.getY() + catHeight < -2
                            || nv.getY() > getHeight() + 2)
                    {
                        nv.reset();
                    }
                }
            }
        });
    }

    @Override
    protected void onSizeChanged (int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w,h,oldw,oldh);
//            android.util.Log.d("Nyandroid", "resized: " + w + "x" + h);
        post(new Runnable() { public void run() {
            reset();
            mAnim.start();
        } });
    }


    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mAnim.cancel();
    }

    @Override
    public boolean isOpaque() {
        return true;
    }
}