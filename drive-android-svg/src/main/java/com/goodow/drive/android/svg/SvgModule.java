package com.goodow.drive.android.svg;

import android.content.Context;
import android.content.Intent;

import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.MessageHandler;
import com.goodow.realtime.json.JsonObject;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;


/**
 * Created by liudenghui on 14-6-30.
 */
public class SvgModule extends AbstractModule {

  @Override
  protected void configure() {
     bind(Binder.class).asEagerSingleton();
  }

  @Singleton
  public static class Binder {
    @Inject
    public Binder(final Provider<Bus> busProvider, final Provider<Context> context) {
      busProvider.get().subscribeLocal("drive.svg", new MessageHandler<JsonObject>() {
        @Override
        public void handle(Message<JsonObject> message) {
          Intent intent = new Intent(context.get(), SvgMainActivity.class);
          intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
          context.get().startActivity(intent);
        }
      });
    }
  }

}
