package com.jakewharton.telecine;

import android.app.Service;

import dagger.Subcomponent;
import dagger.android.DispatchingAndroidInjector;

@Subcomponent(modules = Api24OrGreaterServiceModule.class)
public interface Api24OrGreaterServiceComponent {
  DispatchingAndroidInjector<Service> injector();
}