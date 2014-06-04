package com.goodow.drive.android.svg;

/**
 * Created by liudenghui on 14-6-4.
 */

import com.goodow.realtime.core.Handler;
import com.goodow.realtime.java.JavaPlatform;
import com.goodow.realtime.java.JavaWebSocket;
import com.goodow.realtime.store.CollaborativeMap;
import com.goodow.realtime.store.CollaborativeString;
import com.goodow.realtime.store.Document;
import com.goodow.realtime.store.Model;
import com.goodow.realtime.store.Store;
import com.goodow.realtime.store.impl.DefaultStore;

import org.junit.Test;
import org.vertx.testtools.TestVerticle;
import org.vertx.testtools.VertxAssert;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * To extend org.vertx.testtools.JavaClassRunner's default 5 minutes timeout, you can set timeout to
 * 1 hour by define a system property using -Dvertx.test.timeout=3600
 */
public class ServerStoreTest extends TestVerticle {
  private Store store;
  private static final Logger log = Logger.getLogger(ServerStoreTest.class.getName());

  @Override
  public void start() {
    initialize();
    JavaPlatform.register();
    Logger.getLogger(JavaWebSocket.class.getName()).setLevel(Level.ALL);
    store = new DefaultStore("ws://realtime.goodow.com:1986/channel/websocket", null);
    startTests();
  }

  @Test
  public void test() {
    store.load("ldh/svg", new Handler<Document>() {
      @Override
      public void handle(Document doc) {
        Model mod = doc.getModel();
        CollaborativeMap root = mod.getRoot();
        log.info(root.toString());
        VertxAssert.assertEquals("Larry Tin", root.<CollaborativeString> get("").getText());
        VertxAssert.testComplete();
      }
    }, new Handler<Model>() {
      @Override
      public void handle(Model mod) {
        // CollaborativeString str = mod.createString("Larry Tin");
        // mod.getRoot().set("name", str);
      }
    }, null);
  }
}