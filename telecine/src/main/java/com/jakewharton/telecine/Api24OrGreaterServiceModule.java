package com.jakewharton.telecine;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class Api24OrGreaterServiceModule {
  @ContributesAndroidInjector() abstract TelecineTileService bindTelecineTileService();
}
