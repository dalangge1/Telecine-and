package com.jakewharton.telecine;

import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjectionModule;

@Singleton @Component(modules = { AndroidInjectionModule.class, TelecineModule.class })
interface TelecineComponent {
  void inject(TelecineApplication app);

  Api24OrGreaterServiceComponent api24OrGreaterServiceComponent();

  @Component.Builder interface Builder {
    @BindsInstance Builder application(Application application);

    TelecineComponent build();
  }
}
