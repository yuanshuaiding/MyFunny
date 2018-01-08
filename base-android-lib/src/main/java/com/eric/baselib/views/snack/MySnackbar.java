package com.eric.baselib.views.snack;

/**
 * author : Eric
 * e-mail : yuanshuai@bertadata.com
 * time   : 2017/10/12
 * desc   : 自定义snackbar
 * version: 1.0
 */

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.SwipeDismissBehavior;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eric.baselib.R;
import com.eric.baselib.utils.AppResUtil;
import com.eric.baselib.utils.ScreenUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * MySnackbar provides lightweight feedback about an operation. They show a brief message at the
 * top of the screen on mobile. MySnackbar appear above all other
 * elements on screen and only one can be displayed at a time.
 * <p>
 * They automatically disappear after a timeout or after user interaction elsewhere on the screen,
 * particularly after interactions that summon a new surface or activity. Snackbars can be swiped
 * off screen.
 * <p>
 * Snackbars can contain an action which is set via
 * {@link #setAction(CharSequence, View.OnClickListener)}.
 * <p>
 * To be notified when a snackbar has been shown or dismissed, you can provide a {@link Callback}
 * via {@link #setCallback(Callback)}.</p>
 */
public final class MySnackbar {

    public static abstract class Callback {
        /**
         * Indicates that the MySnackbar was dismissed via a swipe.
         */
        public static final int DISMISS_EVENT_SWIPE = 0;
        /**
         * Indicates that the MySnackbar was dismissed via an action click.
         */
        public static final int DISMISS_EVENT_ACTION = 1;
        /**
         * Indicates that the MySnackbar was dismissed via a timeout.
         */
        public static final int DISMISS_EVENT_TIMEOUT = 2;
        /**
         * Indicates that the MySnackbar was dismissed via a call to {@link #dismiss()}.
         */
        public static final int DISMISS_EVENT_MANUAL = 3;
        /**
         * Indicates that the MySnackbar was dismissed from a new Snackbar being shown.
         */
        public static final int DISMISS_EVENT_CONSECUTIVE = 4;


        @IntDef({DISMISS_EVENT_SWIPE, DISMISS_EVENT_ACTION, DISMISS_EVENT_TIMEOUT,
                DISMISS_EVENT_MANUAL, DISMISS_EVENT_CONSECUTIVE})
        @Retention(RetentionPolicy.SOURCE)
        public @interface DismissEvent {
        }

        public void onDismissed(MySnackbar MySnackbar, @DismissEvent int event) {
        }

        public void onShown(MySnackbar MySnackbar) {

        }
    }

    @IntDef({APPEAR_FROM_TOP_TO_DOWN, APPEAR_FROM_BOTTOM_TO_TOP})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OverSnackAppearDirection {
    }

    /**
     * Show the MySnackbar from top to down.
     */
    public static final int APPEAR_FROM_TOP_TO_DOWN = 0;

    /**
     * Show the MySnackbar from top to down.
     */
    public static final int APPEAR_FROM_BOTTOM_TO_TOP = 1;

    @IntDef({LENGTH_INDEFINITE, LENGTH_SHORT, LENGTH_LONG})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Duration {
    }

    /**
     * Show the MySnackbar indefinitely. This means that the MySnackbar will be displayed from the time
     * that is {@link #show() shown} until either it is dismissed, or another MySnackbar is shown.
     *
     * @see #setDuration
     */
    public static final int LENGTH_INDEFINITE = -2;

    /**
     * Show the MySnackbar for a short period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_SHORT = -1;

    /**
     * Show the MySnackbar for a long period of time.
     *
     * @see #setDuration
     */
    public static final int LENGTH_LONG = 0;

    private static final int ANIMATION_DURATION = 250;
    private static final int ANIMATION_FADE_DURATION = 180;

    private static final Handler sHandler;
    private static final int MSG_SHOW = 0;
    private static final int MSG_DISMISS = 1;
    static final Interpolator FAST_OUT_SLOW_IN_INTERPOLATOR = new FastOutSlowInInterpolator();

    private
    @OverSnackAppearDirection
    int appearDirection = APPEAR_FROM_TOP_TO_DOWN;

    static {
        sHandler = new Handler(Looper.getMainLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case MSG_SHOW:
                        ((MySnackbar) message.obj).showView();
                        return true;
                    case MSG_DISMISS:
                        ((MySnackbar) message.obj).hideView(message.arg1);
                        return true;
                }
                return false;
            }
        });
    }

    private final ViewGroup mParent;
    private final Context mContext;
    private final SnackbarLayout mView;
    private int mDuration;
    private Callback mCallback;

    private final AccessibilityManager mAccessibilityManager;

    private MySnackbar(ViewGroup parent) {
        appearDirection = APPEAR_FROM_TOP_TO_DOWN;

        mParent = parent;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (appearDirection == APPEAR_FROM_BOTTOM_TO_TOP) {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_bsnackbar_layout, mParent, false);
        } else {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_tsnackbar_layout, mParent, false);
        }
        mAccessibilityManager = (AccessibilityManager)
                mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);

    }

    private MySnackbar(ViewGroup parent, @OverSnackAppearDirection int appearDirection) {
        this.appearDirection = appearDirection;
        mParent = parent;
        mContext = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        if (appearDirection == APPEAR_FROM_BOTTOM_TO_TOP) {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_bsnackbar_layout, mParent, false);
        } else {
            mView = (SnackbarLayout) inflater.inflate(R.layout.view_tsnackbar_layout, mParent, false);
        }
        mAccessibilityManager = (AccessibilityManager)
                mContext.getSystemService(Context.ACCESSIBILITY_SERVICE);
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            setMinHeight(0, 0);
        }
    }

    public MySnackbar setMinHeight(int stateBarHeight, int actionBarHeight) {
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                if (stateBarHeight > 0 || actionBarHeight > 0) {
                    mView.setPadding(0, stateBarHeight, 0, 0);
                    mView.setMinimumHeight(stateBarHeight + actionBarHeight);
                } else {
                    mView.setPadding(0, ScreenUtil.getStatusHeight(mContext), 0, 0);
                    mView.setMinimumHeight(ScreenUtil.getActionBarHeight(mContext) + ScreenUtil.getStatusHeight(mContext));
                }
            } else {
                if (stateBarHeight > 0 || actionBarHeight > 0) {
                    mView.setMinimumHeight(actionBarHeight);
                    ScreenUtil.setMargins(mView, 0, stateBarHeight, 0, 0);
                } else {
                    mView.setMinimumHeight(ScreenUtil.getActionBarHeight(mContext));
                    ScreenUtil.setMargins(mView, 0, ScreenUtil.getStatusHeight(mContext), 0, 0);
                }
            }
        }
        return this;
    }


    @NonNull
    public static MySnackbar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration) {
        MySnackbar MySnackbar = new MySnackbar(findSuitableParent(view), APPEAR_FROM_TOP_TO_DOWN);
        MySnackbar.setText(text);
        MySnackbar.setDuration(duration);
        return MySnackbar;
    }

    @NonNull
    public static MySnackbar make(@NonNull View view, @NonNull CharSequence text, @Duration int duration, @OverSnackAppearDirection int appearDirection) {
        MySnackbar MySnackbar = new MySnackbar(findSuitableParent(view), appearDirection);
        MySnackbar.setText(text);
        MySnackbar.setDuration(duration);
        return MySnackbar;
    }

    @NonNull
    public static MySnackbar make(@NonNull View view, @StringRes int resId, @Duration int duration) {
        return make(view, view.getResources().getText(resId), duration);
    }

    private static ViewGroup findSuitableParent(View view) {
        ViewGroup fallback = null;
        do {
            if (view instanceof CoordinatorLayout) {
                // We've found a CoordinatorLayout, use it
                return (ViewGroup) view;
            } else if (view instanceof FrameLayout) {
                if (view.getId() == android.R.id.content) {
                    // If we've hit the decor content view, then we didn't find a CoL in the
                    // hierarchy, so use it.
                    return (ViewGroup) view;
                } else {
                    // It's not the content view but we'll use it as our fallback
                    fallback = (ViewGroup) view;
                }
            }

            if (view != null) {
                // Else, we will loop and crawl up the view hierarchy and try to find a parent
                final ViewParent parent = view.getParent();
                view = parent instanceof View ? (View) parent : null;
            }
        } while (view != null);

        // If we reach here then we didn't find a CoL or a suitable content view so we'll fallback
        return fallback;
        //return (ViewGroup) view;
    }


    public MySnackbar addIcon(int resource_id) {
        final TextView tv = mView.getMessageView();
        tv.setCompoundDrawablesWithIntrinsicBounds(mContext.getResources().getDrawable(resource_id), null, null, null);
        return this;
    }

    public MySnackbar addIcon(int resource_id, int width, int height) {
        final TextView tv = mView.getMessageView();
        if (width > 0 || height > 0) {
            tv.setCompoundDrawablesWithIntrinsicBounds(new BitmapDrawable(Bitmap.createScaledBitmap(((BitmapDrawable) (mContext.getResources().getDrawable(resource_id))).getBitmap(), width, height, true)), null, null, null);
        } else {
            addIcon(resource_id);
        }
        return this;
    }

    public MySnackbar addIconProgressLoading(int resource_id, boolean left, boolean right) {
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.rotate);
        if (resource_id > 0) {
            drawable = mContext.getResources().getDrawable(resource_id);
        }
        addIconProgressLoading(drawable, left, right);
        return this;
    }

    public MySnackbar addIconProgressLoading(Drawable drawable, boolean left, boolean right) {
        final ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", 0, 10000);
        animator.setDuration(1000);
        animator.setInterpolator(new LinearInterpolator());
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        mView.setBackgroundColor(mContext.getResources().getColor(SnackType.SUCCESS.getBackgroundColor()));
        if (left) {
            mView.getMessageView().setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
        }
        if (right) {
            mView.getMessageView().setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mCallback != null) {
                    mCallback.onShown(MySnackbar.this);
                }
                SnackbarManager.getInstance().onShown(mManagerCallback);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
        return this;
    }


    /**
     * default style {ERROR , WARNING , SUCCESS}
     *
     * @param snackType
     * @return
     */
    public MySnackbar setPromptThemBackground(SnackType snackType) {
        if (snackType == SnackType.SUCCESS) {
            setBackgroundColor(AppResUtil.getColor(mContext, SnackType.SUCCESS.getBackgroundColor()));
            addIcon(SnackType.SUCCESS.getResIcon(), 0, 0);
        } else if (snackType == SnackType.ERROR) {
            setBackgroundColor(AppResUtil.getColor(mContext, SnackType.ERROR.getBackgroundColor()));
            addIcon(SnackType.ERROR.getResIcon(), 0, 0);
        } else if (snackType == SnackType.WARNING) {
            setBackgroundColor(AppResUtil.getColor(mContext, SnackType.WARNING.getBackgroundColor()));
            addIcon(SnackType.WARNING.getResIcon(), 0, 0);
        }
        return this;
    }

    public MySnackbar setBackgroundColor(int colorId) {
        mView.setBackgroundColor(colorId);
        return this;
    }

    @NonNull
    public MySnackbar setAction(@StringRes int resId, View.OnClickListener listener) {
        return setAction(mContext.getText(resId), listener);
    }

    @NonNull
    public MySnackbar setAction(CharSequence text, final View.OnClickListener listener) {
        final TextView tv = mView.getActionView();
        if (TextUtils.isEmpty(text) || listener == null) {
            tv.setVisibility(View.GONE);
            tv.setOnClickListener(null);
        } else {
            tv.setVisibility(View.VISIBLE);
            tv.setText(text);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick(view);

                    dispatchDismiss(Callback.DISMISS_EVENT_ACTION);
                }
            });
        }
        return this;
    }

    @NonNull
    public MySnackbar setActionTextColor(ColorStateList colors) {
        final Button btn = mView.getActionView();
        btn.setTextColor(colors);
        return this;
    }

    @NonNull
    public MySnackbar setActionTextSize(int size) {
        final Button btn = mView.getActionView();
        btn.setTextSize(size);
        return this;
    }

    @NonNull
    public MySnackbar setMessageTextSize(int size) {
        final TextView tv = mView.getMessageView();
        tv.setTextSize(size);
        return this;
    }


    @NonNull
    public MySnackbar setActionTextColor(@ColorInt int color) {
        final Button btn = mView.getActionView();
        btn.setTextColor(color);
        return this;
    }

    @NonNull
    public MySnackbar setTextColor(@ColorInt int color) {
        setActionTextColor(color);
        setMessageTextColor(color);
        return this;
    }

    @NonNull
    public MySnackbar setColor(ColorStateList colors) {
        setActionTextColor(colors);
        setMessageTextColor(colors);
        return this;
    }

    @NonNull
    public MySnackbar setMessageTextColor(@ColorInt int color) {
        final TextView tv = mView.getMessageView();
        tv.setTextColor(color);
        return this;
    }

    @NonNull
    public MySnackbar setMessageTextColor(ColorStateList colors) {
        final TextView tv = mView.getMessageView();
        tv.setTextColor(colors);
        return this;
    }

    @NonNull
    public MySnackbar setText(@NonNull CharSequence message) {
        final TextView tv = mView.getMessageView();
        tv.setText(message);
        return this;
    }

    @NonNull
    public MySnackbar setText(@StringRes int resId) {
        return setText(mContext.getText(resId));
    }

    @NonNull
    public MySnackbar setDuration(@Duration int duration) {
        mDuration = duration;
        return this;
    }

    @Duration
    public int getDuration() {
        return mDuration;
    }


    @NonNull
    public View getView() {
        return mView;
    }

    public void show() {
        SnackbarManager.getInstance().show(mDuration, mManagerCallback);
    }

    public void dismiss() {
        dispatchDismiss(Callback.DISMISS_EVENT_MANUAL);
    }

    private void dispatchDismiss(@Callback.DismissEvent int event) {
        SnackbarManager.getInstance().dismiss(mManagerCallback, event);
    }

    @NonNull
    public MySnackbar setCallback(Callback callback) {
        mCallback = callback;
        return this;
    }

    public boolean isShown() {
        return SnackbarManager.getInstance().isCurrent(mManagerCallback);

    }

    public boolean isShownOrQueued() {
        return SnackbarManager.getInstance().isCurrentOrNext(mManagerCallback);
    }

    private final SnackbarManager.Callback mManagerCallback = new SnackbarManager.Callback() {
        @Override
        public void show() {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_SHOW, MySnackbar.this));
        }

        @Override
        public void dismiss(int event) {
            sHandler.sendMessage(sHandler.obtainMessage(MSG_DISMISS, event, 0, MySnackbar.this));
        }
    };

    final void showView() {
        if (mView.getParent() == null) {
            final ViewGroup.LayoutParams lp = mView.getLayoutParams();
            if (lp instanceof CoordinatorLayout.LayoutParams) {
                // If our LayoutParams are from a CoordinatorLayout, we'll setup our Behavior

                final Behavior behavior = new Behavior();
                behavior.setStartAlphaSwipeDistance(0.1f);
                behavior.setEndAlphaSwipeDistance(0.6f);
                behavior.setSwipeDirection(SwipeDismissBehavior.SWIPE_DIRECTION_START_TO_END);
                behavior.setListener(new SwipeDismissBehavior.OnDismissListener() {
                    @Override
                    public void onDismiss(View view) {
                        view.setVisibility(View.GONE);
                        dispatchDismiss(Callback.DISMISS_EVENT_SWIPE);
                    }

                    @Override
                    public void onDragStateChanged(int state) {
                        switch (state) {
                            case SwipeDismissBehavior.STATE_DRAGGING:
                            case SwipeDismissBehavior.STATE_SETTLING:

                                SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                                break;
                            case SwipeDismissBehavior.STATE_IDLE:

                                SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                                break;
                        }
                    }
                });
                ((CoordinatorLayout.LayoutParams) lp).setBehavior(behavior);

                ((CoordinatorLayout.LayoutParams) lp).setMargins(0, -30, 0, 0);
            }
            mParent.addView(mView);
        }
        if (ViewCompat.isLaidOut(mView)) {
            animateViewIn();
        } else {

            mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    animateViewIn();
                    mView.setOnLayoutChangeListener(null);
                }
            });
        }

        mView.setOnAttachStateChangeListener(new SnackbarLayout.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
            }

            @Override
            public void onViewDetachedFromWindow(View v) {
                if (isShownOrQueued()) {
                    // If we haven't already been dismissed then this event is coming from a
                    // non-user initiated action. Hence we need to make sure that we callback
                    // and keep our state up to date. We need to post the call since removeView()
                    // will call through to onDetachedFromWindow and thus overflow.
                    sHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            onViewHidden(Callback.DISMISS_EVENT_MANUAL);
                        }
                    });
                }
            }
        });

        if (ViewCompat.isLaidOut(mView)) {
            if (shouldAnimate()) {
                // If animations are enabled, animate it in
                animateViewIn();
            } else {
                // Else if anims are disabled just call back now
                onViewShown();
            }
        } else {
            // Otherwise, add one of our layout change listeners and show it in when laid out
            mView.setOnLayoutChangeListener(new SnackbarLayout.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View view, int left, int top, int right, int bottom) {
                    mView.setOnLayoutChangeListener(null);

                    if (shouldAnimate()) {
                        // If animations are enabled, animate it in
                        animateViewIn();
                    } else {
                        // Else if anims are disabled just call back now
                        onViewShown();
                    }
                }
            });
        }
    }

    private void animateViewIn() {
        Animation anim;
        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            anim = getAnimationInFromTopToDown();
        } else {
            anim = getAnimationInFromBottomToTop();
        }
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onViewShown();
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mView.startAnimation(anim);
    }

    private void animateViewOut(final int event) {
        Animation anim;

        if (appearDirection == APPEAR_FROM_TOP_TO_DOWN) {
            anim = getAnimationOutFromTopToDown();
        } else {
            anim = getAnimationOutFromBottomToTop();
        }
        anim.setInterpolator(FAST_OUT_SLOW_IN_INTERPOLATOR);
        anim.setDuration(ANIMATION_DURATION);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                onViewHidden(event);
            }

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
        mView.startAnimation(anim);
    }


    private Animation getAnimationInFromTopToDown() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_in);
    }

    private Animation getAnimationInFromBottomToTop() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_in);
    }

    private Animation getAnimationOutFromTopToDown() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.top_out);
    }

    private Animation getAnimationOutFromBottomToTop() {
        return AnimationUtils.loadAnimation(mView.getContext(), R.anim.design_snackbar_out);
    }

    final void hideView(@Callback.DismissEvent final int event) {
        if (shouldAnimate() && mView.getVisibility() == View.VISIBLE) {
            animateViewOut(event);
        } else {
            // If anims are disabled or the view isn't visible, just call back now
            onViewHidden(event);
        }
    }

    private void onViewShown() {
        SnackbarManager.getInstance().onShown(mManagerCallback);
        if (mCallback != null) {
            mCallback.onShown(this);
        }
    }

    private void onViewHidden(int event) {
        // First tell the SnackbarManager that it has been dismissed
        SnackbarManager.getInstance().onDismissed(mManagerCallback);
        // Now call the dismiss listener (if available)
        if (mCallback != null) {
            mCallback.onDismissed(this, event);
        }
        // Lastly, remove the view from the parent (if attached)
        final ViewParent parent = mView.getParent();
        if (parent instanceof ViewGroup) {
            ((ViewGroup) parent).removeView(mView);
        }
    }

    /**
     * Returns true if we should animate the Snackbar view in/out.
     */
    private boolean shouldAnimate() {
        return !mAccessibilityManager.isEnabled();
    }

    /**
     * @return if the view is being being dragged or settled by {@link SwipeDismissBehavior}.
     */
    private boolean isBeingDragged() {
        final ViewGroup.LayoutParams lp = mView.getLayoutParams();
        if (lp instanceof CoordinatorLayout.LayoutParams) {
            final CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) lp;
            final CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
            if (behavior instanceof SwipeDismissBehavior) {
                return ((SwipeDismissBehavior) behavior).getDragState() != SwipeDismissBehavior.STATE_IDLE;
            }
        }
        return false;
    }


    public static class SnackbarLayout extends LinearLayout {
        private TextView mMessageView;
        private Button mActionView;

        private int mMaxWidth;
        private int mMaxInlineActionWidth;

        interface OnLayoutChangeListener {
            void onLayoutChange(View view, int left, int top, int right, int bottom);
        }

        interface OnAttachStateChangeListener {
            void onViewAttachedToWindow(View v);

            void onViewDetachedFromWindow(View v);
        }

        private OnLayoutChangeListener mOnLayoutChangeListener;
        private OnAttachStateChangeListener mOnAttachStateChangeListener;

        public SnackbarLayout(Context context) {
            this(context, null);
        }

        public SnackbarLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SnackbarLayout);
            mMaxWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_android_maxWidth, -1);
            mMaxInlineActionWidth = a.getDimensionPixelSize(R.styleable.SnackbarLayout_maxActionInlineWidth, -1);
            if (a.hasValue(R.styleable.SnackbarLayout_elevation)) {
                ViewCompat.setElevation(this, a.getDimensionPixelSize(
                        R.styleable.SnackbarLayout_elevation, 0));
            }
            a.recycle();
            setClickable(true);

            // Now inflate our content. We need to do this manually rather than using an <include>
            // in the layout since older versions of the Android do not inflate includes with
            // the correct Context.
            LayoutInflater.from(context).inflate(R.layout.view_tsnackbar_layout_include, this);
            ViewCompat.setAccessibilityLiveRegion(this,
                    ViewCompat.ACCESSIBILITY_LIVE_REGION_POLITE);
            ViewCompat.setImportantForAccessibility(this,
                    ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_YES);
        }

        @Override
        protected void onFinishInflate() {
            super.onFinishInflate();
            mMessageView = findViewById(R.id.snackbar_text);
            mActionView = findViewById(R.id.snackbar_action);
        }

        TextView getMessageView() {
            return mMessageView;
        }

        Button getActionView() {
            return mActionView;
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            if (mMaxWidth > 0 && getMeasuredWidth() > mMaxWidth) {
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(mMaxWidth, MeasureSpec.EXACTLY);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
            final int multiLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical_2lines);
            final int singleLineVPadding = getResources().getDimensionPixelSize(
                    R.dimen.design_snackbar_padding_vertical);
            final boolean isMultiLine = mMessageView.getLayout().getLineCount() > 1;
            boolean remeasure = false;
            if (isMultiLine && mMaxInlineActionWidth > 0
                    && mActionView.getMeasuredWidth() > mMaxInlineActionWidth) {
                if (updateViewsWithinLayout(VERTICAL, multiLineVPadding,
                        multiLineVPadding - singleLineVPadding)) {
                    remeasure = true;
                }
            } else {
                final int messagePadding = isMultiLine ? multiLineVPadding : singleLineVPadding;
                if (updateViewsWithinLayout(HORIZONTAL, messagePadding, messagePadding)) {
                    remeasure = true;
                }
            }
            if (remeasure) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }

        void animateChildrenIn(int delay, int duration) {
            ViewCompat.setAlpha(mMessageView, 0f);
            ViewCompat.animate(mMessageView).alpha(1f).setDuration(duration)
                    .setStartDelay(delay).start();
            if (mActionView.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(mActionView, 0f);
                ViewCompat.animate(mActionView).alpha(1f).setDuration(duration)
                        .setStartDelay(delay).start();
            }
        }

        void animateChildrenOut(int delay, int duration) {
            ViewCompat.setAlpha(mMessageView, 1f);
            ViewCompat.animate(mMessageView).alpha(0f).setDuration(duration)
                    .setStartDelay(delay).start();
            if (mActionView.getVisibility() == VISIBLE) {
                ViewCompat.setAlpha(mActionView, 1f);
                ViewCompat.animate(mActionView).alpha(0f).setDuration(duration)
                        .setStartDelay(delay).start();
            }
        }

        @Override
        protected void onLayout(boolean changed, int l, int t, int r, int b) {
            super.onLayout(changed, l, t, r, b);
            if (changed && mOnLayoutChangeListener != null) {
                mOnLayoutChangeListener.onLayoutChange(this, l, t, r, b);
            }
        }

        @Override
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewAttachedToWindow(this);
            }
        }

        @Override
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            if (mOnAttachStateChangeListener != null) {
                mOnAttachStateChangeListener.onViewDetachedFromWindow(this);
            }
        }

        void setOnLayoutChangeListener(OnLayoutChangeListener onLayoutChangeListener) {
            mOnLayoutChangeListener = onLayoutChangeListener;
        }

        void setOnAttachStateChangeListener(OnAttachStateChangeListener listener) {
            mOnAttachStateChangeListener = listener;
        }

        private boolean updateViewsWithinLayout(final int orientation,
                                                final int messagePadTop, final int messagePadBottom) {
            boolean changed = false;
            if (orientation != getOrientation()) {
                setOrientation(orientation);
                changed = true;
            }
            if (mMessageView.getPaddingTop() != messagePadTop
                    || mMessageView.getPaddingBottom() != messagePadBottom) {
                updateTopBottomPadding(mMessageView, messagePadTop, messagePadBottom);
                changed = true;
            }
            return changed;
        }

        private static void updateTopBottomPadding(View view, int topPadding, int bottomPadding) {
            if (ViewCompat.isPaddingRelative(view)) {
                ViewCompat.setPaddingRelative(view,
                        ViewCompat.getPaddingStart(view), topPadding,
                        ViewCompat.getPaddingEnd(view), bottomPadding);
            } else {
                view.setPadding(view.getPaddingLeft(), topPadding,
                        view.getPaddingRight(), bottomPadding);
            }
        }
    }

    final class Behavior extends SwipeDismissBehavior<SnackbarLayout> {
        @Override
        public boolean canSwipeDismissView(View child) {
            return child instanceof SnackbarLayout;
        }

        @Override
        public boolean onInterceptTouchEvent(CoordinatorLayout parent, SnackbarLayout child,
                                             MotionEvent event) {
            // We want to make sure that we disable any Snackbar timeouts if the user is
            // currently touching the Snackbar. We restore the timeout when complete
            if (parent.isPointInChildBounds(child, (int) event.getX(), (int) event.getY())) {
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        SnackbarManager.getInstance().cancelTimeout(mManagerCallback);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        SnackbarManager.getInstance().restoreTimeout(mManagerCallback);
                        break;
                }
            }

            return super.onInterceptTouchEvent(parent, child, event);
        }
    }
}
