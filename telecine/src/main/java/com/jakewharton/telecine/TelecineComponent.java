package com.jakewharton.telecine;

import android.content.ContentResolver;
import android.content.SharedPreferences;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = TelecineModule.class)
public interface TelecineComponent {

  Analytics analytics();

  ContentResolver contentResolver();

  SharedPreferences sharedPreferences();

  void inject(final TelecineActivity activity);

  void inject(final TelecineService service);

  void inject(final TelecineShortcutConfigureActivity activity);

  void inject(final TelecineShortcutLaunchActivity activity);
}
