package com.jakewharton.telecine;

import android.app.ActivityManager;
import android.app.ActivityManager.TaskDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.Switch;
import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;
import butterknife.OnItemSelected;
import javax.inject.Inject;
import timber.log.Timber;

import static android.graphics.Bitmap.Config.ARGB_8888;

public final class TelecineActivity extends AppCompatActivity {
  @BindView(R.id.spinner_video_size_percentage) Spinner videoSizePercentageView;
  @BindView(R.id.switch_show_countdown) Switch showCountdownView;
  @BindView(R.id.switch_hide_from_recents) Switch hideFromRecentsView;
  @BindView(R.id.switch_recording_notification) Switch recordingNotificationView;
  @BindView(R.id.switch_show_touches) Switch showTouchesView;
  @BindView(R.id.launch) View launchView;

  @BindString(R.string.app_name) String appName;
  @BindColor(R.color.primary_normal) int primaryNormal;

  @Inject @VideoSizePercentage IntPreference videoSizePreference;
  @Inject @ShowCountdown BooleanPreference showCountdownPreference;
  @Inject @HideFromRecents BooleanPreference hideFromRecentsPreference;
  @Inject @RecordingNotification BooleanPreference recordingNotificationPreference;
  @Inject @ShowTouches BooleanPreference showTouchesPreference;

  @Inject Analytics analytics;

  private VideoSizePercentageAdapter videoSizePercentageAdapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    if ("true".equals(getIntent().getStringExtra("crash"))) {
      throw new RuntimeException("Crash! Bang! Pow! This is only a test...");
    }

    ((TelecineApplication) getApplication()).injector().inject(this);

    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);

    CheatSheet.setup(launchView);

    setTaskDescription(new TaskDescription(appName, rasterizeTaskIcon(), primaryNormal));

    videoSizePercentageAdapter = new VideoSizePercentageAdapter(this);

    videoSizePercentageView.setAdapter(videoSizePercentageAdapter);
    videoSizePercentageView.setSelection(
        VideoSizePercentageAdapter.getSelectedPosition(videoSizePreference.get()));

    showCountdownView.setChecked(showCountdownPreference.get());
    hideFromRecentsView.setChecked(hideFromRecentsPreference.get());
    recordingNotificationView.setChecked(recordingNotificationPreference.get());
    showTouchesView.setChecked(showTouchesPreference.get());
  }

  @NonNull private Bitmap rasterizeTaskIcon() {
    Drawable drawable = getResources().getDrawable(R.drawable.ic_videocam_white_24dp, getTheme());

    ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
    int size = am.getLauncherLargeIconSize();
    Bitmap icon = Bitmap.createBitmap(size, size, ARGB_8888);

    Canvas canvas = new Canvas(icon);
    drawable.setBounds(0, 0, size, size);
    drawable.draw(canvas);

    return icon;
  }

  @OnClick(R.id.launch) void onLaunchClicked() {
    Timber.d("Attempting to acquire permission to screen capture.");
    CaptureHelper.fireScreenCaptureIntent(this, analytics);
  }

  @OnItemSelected(R.id.spinner_video_size_percentage) void onVideoSizePercentageSelected(
      int position) {
    int newValue = videoSizePercentageAdapter.getItem(position);
    int oldValue = videoSizePreference.get();
    if (newValue != oldValue) {
      Timber.d("Video size percentage changing to %s%%", newValue);
      videoSizePreference.set(newValue);

      analytics.send(Analytics.EVENT_CHANGE_VIDEO_SIZE + newValue);
    }
  }

  @OnCheckedChanged(R.id.switch_show_countdown) void onShowCountdownChanged() {
    boolean newValue = showCountdownView.isChecked();
    boolean oldValue = showCountdownPreference.get();
    if (newValue != oldValue) {
      Timber.d("Hide show countdown changing to %s", newValue);
      showCountdownPreference.set(newValue);

      analytics.send(newValue ? Analytics.EVENT_CHANGE_SHOW_COUNTDOWN_YES : Analytics.EVENT_CHANGE_SHOW_COUNTDOWN_NO);
    }
  }

  @OnCheckedChanged(R.id.switch_hide_from_recents) void onHideFromRecentsChanged() {
    boolean newValue = hideFromRecentsView.isChecked();
    boolean oldValue = hideFromRecentsPreference.get();
    if (newValue != oldValue) {
      Timber.d("Hide from recents preference changing to %s", newValue);
      hideFromRecentsPreference.set(newValue);

      analytics.send(newValue ? Analytics.EVENT_CHANGE_HIDE_RECENTS_YES : Analytics.EVENT_CHANGE_HIDE_RECENTS_NO);
    }
  }

  @OnCheckedChanged(R.id.switch_recording_notification) void onRecordingNotificationChanged() {
    boolean newValue = recordingNotificationView.isChecked();
    boolean oldValue = recordingNotificationPreference.get();
    if (newValue != oldValue) {
      Timber.d("Recording notification preference changing to %s", newValue);
      recordingNotificationPreference.set(newValue);

      analytics.send(newValue ? Analytics.EVENT_CHANGE_RECORDING_NOTIFICATION_YES : Analytics.EVENT_CHANGE_RECORDING_NOTIFICATION_NO);
    }
  }

  @OnCheckedChanged(R.id.switch_show_touches) void onShowTouchesChanged() {
    boolean newValue = showTouchesView.isChecked();
    boolean oldValue = showTouchesPreference.get();
    if (newValue != oldValue) {
      Timber.d("Show touches preference changing to %s", newValue);
      showTouchesPreference.set(newValue);

      analytics.send(newValue ? Analytics.EVENT_CHANGE_SHOW_TOUCHES_YES : Analytics.EVENT_CHANGE_SHOW_TOUCHES_NO);
    }
  }

  @Override protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (!CaptureHelper.handleActivityResult(this, requestCode, resultCode, data, analytics)) {
      super.onActivityResult(requestCode, resultCode, data);
    }
  }

  @Override protected void onStop() {
    super.onStop();
    if (hideFromRecentsPreference.get() && !isChangingConfigurations()) {
      Timber.d("Removing task because hide from recents preference was enabled.");
      finishAndRemoveTask();
    }
  }
}
