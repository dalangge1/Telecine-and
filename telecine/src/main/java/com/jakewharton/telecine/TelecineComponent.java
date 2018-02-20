package com.jakewharton.telecine;

import android.app.Application;
import dagger.BindsInstance;
import dagger.Component;
import dagger.Subcomponent;
import dagger.android.AndroidInjectionModule;
import dagger.android.ContributesAndroidInjector;
import javax.inject.Singleton;

@Singleton @Component(modules = { AndroidInjectionModule.class, TelecineModule.class })
interface TelecineComponent {
  void inject(TelecineApplication app);

  For24Plus for24Plus();

  @Component.Builder interface Builder {
    @BindsInstance Builder application(Application application);

    TelecineComponent build();
  }

  @Subcomponent(modules = For24Plus.Module.class)
  interface For24Plus {
    void inject(TelecineApplication app);

    @dagger.Module
    abstract class Module {
      @ContributesAndroidInjector abstract TelecineTileService contributeTelecineTileService();
    }
  }
}
