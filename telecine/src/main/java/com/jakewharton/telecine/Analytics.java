package com.jakewharton.telecine;

import android.os.Bundle;
import com.google.firebase.analytics.FirebaseAnalytics;

// Event name should be 1 to 32 alphanumeric according to FirebaseAnalytics
interface Analytics {
  String EVENT_CAPTURE_INTENT_LAUNCH = "settings_overlay_launch";
  String EVENT_CAPTURE_INTENT_RESULT = "settings_overlay_result_";
  String EVENT_CHANGE_VIDEO_SIZE = "settings_change_video_size_";
  String EVENT_CHANGE_SHOW_COUNTDOWN_YES = "settings_show_countdown_yes";
  String EVENT_CHANGE_SHOW_COUNTDOWN_NO = "settings_show_countdown_no";
  String EVENT_CHANGE_HIDE_RECENTS_YES = "settings_hide_in_recents_yes";
  String EVENT_CHANGE_HIDE_RECENTS_NO = "settings_hide_in_recents_no";
  String EVENT_CHANGE_RECORDING_NOTIFICATION_YES = "settings_record_notification_yes";
  String EVENT_CHANGE_RECORDING_NOTIFICATION_NO = "settings_record_notification_no";
  String EVENT_CHANGE_SHOW_TOUCHES_YES = "settings_show_touches_yes";
  String EVENT_CHANGE_SHOW_TOUCHES_NO = "settings_show_touches_no";
  String EVENT_OVERLAY_SHOW = "recording_overlay_show";
  String EVENT_OVERLAY_HIDE = "recording_overlay_hide";
  String EVENT_OVERLAY_CANCEL = "recording_overlay_cancel";
  String EVENT_RECORDING_START = "recording_recording_start";
  String EVENT_RECORDING_STOP = "recording_recording_stop";
  String EVENT_RECORDING_LENGTH = "recording_length";
  String EVENT_SHORTCUT_ADDED = "shortcut_added";
  String EVENT_SHORTCUT_LAUNCHED = "shortcut_launched";
  String EVENT_QUICK_TILE_ADDED = "quick_tile_added";
  String EVENT_QUICK_TILE_LAUNCHED = "quick_tile_launched";
  String EVENT_QUICK_TILE_REMOVED = "quick_tile_removed";
  String EVENT_STATIC_SHORTCUT_RECORD = "static_shortcut_record";
  String EVENT_STATIC_SHORTCUT_OVERLAY = "static_shortcut_overlay";

  void send(String name);
  void send(String name, long value);

  class FirebaseAnalyticsImpl implements Analytics {
    private final com.google.firebase.analytics.FirebaseAnalytics analytics;

    FirebaseAnalyticsImpl(com.google.firebase.analytics.FirebaseAnalytics analytics) {
      this.analytics = analytics;
    }

    @Override public void send(String name) {
      analytics.logEvent(name, null);
    }
    @Override public void send(String name, long value) {
      Bundle bundle = new Bundle(1);
      bundle.putLong(FirebaseAnalytics.Param.VALUE, value);
      analytics.logEvent(name, bundle);
    }
  }
}
