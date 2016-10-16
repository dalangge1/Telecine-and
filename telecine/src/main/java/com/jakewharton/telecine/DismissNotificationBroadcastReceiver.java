package com.jakewharton.telecine;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.inject.Inject;

public final class DismissNotificationBroadcastReceiver extends BroadcastReceiver {
  @Inject @NotificationDismissed AtomicBoolean notificationDismissed;

  @Override public void onReceive(Context context, Intent intent) {
    ((TelecineApplication) context.getApplicationContext()).injector().inject(this);
    notificationDismissed.set(true);
  }
}
