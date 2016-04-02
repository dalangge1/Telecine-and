package com.jakewharton.telecine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import butterknife.ButterKnife;
import static android.graphics.PixelFormat.TRANSLUCENT;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
import static android.view.WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
import static android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
import static android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;

@SuppressLint("ViewConstructor") // Lint, in this case, I am smarter than you.
final class FlashView extends FrameLayout {

    private final Listener listener;

    static FlashView create(Context context, Listener listener) {
      return new FlashView(context, listener);
    }

    private FlashView(Context context, Listener listener) {
      super(context);

      this.listener = listener;
      inflate(context, R.layout.flash_view, this);
      ButterKnife.bind(this);
    }

    @Override
    protected void onAttachedToWindow() {
      super.onAttachedToWindow();

      animate().alpha(100)
          .setDuration(200)
          .withEndAction(new Runnable() {
              @Override
              public void run() {
                  listener.onFlashComplete();
              }
          })
      .setInterpolator(new DecelerateInterpolator());
    }

    static WindowManager.LayoutParams createLayoutParams() {

      final WindowManager.LayoutParams params =
          new WindowManager.LayoutParams(MATCH_PARENT, MATCH_PARENT, TYPE_SYSTEM_ERROR, FLAG_NOT_FOCUSABLE
              | FLAG_NOT_TOUCH_MODAL
              | FLAG_LAYOUT_NO_LIMITS
              | FLAG_LAYOUT_INSET_DECOR
              | FLAG_LAYOUT_IN_SCREEN, TRANSLUCENT);

      return params;
    }

    interface Listener {
      /** Called when flash animation has completed. */
      void onFlashComplete();
    }

}
