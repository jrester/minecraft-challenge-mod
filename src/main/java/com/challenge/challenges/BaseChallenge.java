package com.challenge.challenges;

import java.util.function.BiConsumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.ChallengeMod;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.MinecraftServer;

public abstract class BaseChallenge {
	public static final Logger LOGGER = LoggerFactory.getLogger(ChallengeMod.MOD_ID);

  private boolean enabled = false;
  private boolean active = false;
  private boolean eventHandlersRegistered = false;
  

  // finishedCallback and server must be set before enabling. This is not clean, but this way the challenges do not need a constructor...
  // Maybe this could be done better with a builder for challenges...
  private BiConsumer<BaseChallenge, Boolean> finishedCallback;
  private MinecraftServer server;

  public void disable() {
    this.enabled = false;
    this.active = false;
  }

  public void enable()  {
    if(this.server == null) {
      LOGGER.error("Failed to enable challenge " + this.getName() + " because server is not set!");
      return;
    }
    if(this.finishedCallback == null) {
      LOGGER.error("Failed to enable challenge " + this.getName() + " because finishedCallback is not set!");
      return;
    }
    this.enabled = true;
  }

  public void start() {
    if(!this.enabled) return;

    if(!this.eventHandlersRegistered) {
      this.registerEventHandlers();
      this.eventHandlersRegistered = true;
      LOGGER.info("Registered event handlers for " + this.getName());
    }

    this.active = true;
    LOGGER.info("Started challenge " + this.getName());
  }

  public void stop() {
    if(!this.enabled) return;
    this.active = false;
    LOGGER.info("Stopped challenge " + this.getName());
  }

  protected void registerEventHandlers() {};
  
  public boolean isActive() {
    return this.enabled && this.active;
  }

  public boolean isEnabled() {
    return this.enabled;
  }

  public void setServer(MinecraftServer server) {
    this.server = server;
  }

  public MinecraftServer getServer() {
    return this.server;
  }

  public void setFinishedCallback(BiConsumer<BaseChallenge, Boolean> finishedCallback) {
    this.finishedCallback = finishedCallback;
  }

  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      return Items.LAVA_BUCKET.getDefaultStack();
     } else {
      return Items.BUCKET.getDefaultStack();
     }
  }
  
  protected void challengeFinished(boolean success) {
    this.finishedCallback.accept(this, success);
  }

  public abstract String getName();
}
