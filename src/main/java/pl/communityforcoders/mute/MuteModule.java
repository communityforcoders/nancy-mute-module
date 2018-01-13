package pl.communityforcoders.mute;

import pl.communityforcoders.nancy.module.annotation.ModuleManifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;

@ModuleManifest(name = "TestModule", author = "kacperduras", version = "1.0.0.2")
public class MuteModule {

  @OnEnable
  public void onEnable() {

  }

  @OnDisable
  public void onDisable() {

  }

}
