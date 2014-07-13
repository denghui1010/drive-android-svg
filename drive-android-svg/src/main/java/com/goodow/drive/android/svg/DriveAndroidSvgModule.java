/*
 * Copyright 2014 Goodow.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.goodow.drive.android.svg;

import com.goodow.realtime.android.AndroidPlatform;
import com.goodow.realtime.channel.Bus;
import com.goodow.realtime.channel.Message;
import com.goodow.realtime.channel.impl.ReconnectBus;
import com.goodow.realtime.channel.impl.ReliableSubscribeBus;
import com.goodow.realtime.core.Handler;
import com.goodow.realtime.java.JavaWebSocket;
import com.goodow.realtime.store.Store;
import com.goodow.realtime.store.impl.StoreImpl;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Maintains a singleton instance for obtaining the bus. Ideally this would be replaced with a more
 * efficient means such as through injection directly into interested classes.
 */
public class DriveAndroidSvgModule extends AbstractModule {
  private static final String SERVER = "realtime.goodow.com:1986";
  private static final String URL = "ws://" + SERVER + "/channel/websocket";

  static {
    AndroidPlatform.register();
    // adb shell setprop log.tag.JavaWebSocket DEBUG
    Logger.getLogger(JavaWebSocket.class.getName()).setLevel(Level.ALL);
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  Store provideStore() {
    return new StoreImpl("ws://" + SERVER + "/channel/websocket", null);
  }

  @Provides
  @Singleton
  Bus provideBus(Store store) {
    ReliableSubscribeBus bus = (ReliableSubscribeBus) store.getBus();
    final ReconnectBus reconnectBus = (ReconnectBus) bus.getDelegate();
    bus.subscribeLocal("Bus_Reconnet", new Handler<Message>() {
      @Override
      public void handle(Message event) {
        reconnectBus.connect(URL, null);
      }
    });
    return bus;
  }

}
