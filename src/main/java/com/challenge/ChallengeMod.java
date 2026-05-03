package com.challenge;

import com.challenge.challenges.BaseChallenge;
import com.challenge.challenges.DeleteBlocksOnBreak;
import com.challenge.challenges.HardcoreChallenge;
import com.challenge.challenges.NoCraftingTableChallenge;
import com.challenge.challenges.RandomBlockDropsChallenge;
import com.challenge.challenges.RandomMobDropsChallenge;
import com.challenge.challenges.RandomMobSpeedChallenge;
import com.challenge.challenges.RandomMobsOnDamageChallenge;
import com.challenge.challenges.ScrambledBlockDropsChallenge;
import com.challenge.challenges.ScrambledMobDropsChallenge;
import com.challenge.challenges.ScrambledMobsOnDamageChallenge;
import com.challenge.challenges.SlayEnderDragonChallenge;
import com.challenge.challenges.SlayWardenChallenge;
import com.challenge.challenges.SlayWitherChallenge;
import com.mojang.brigadier.context.CommandContext;
import java.util.LinkedList;
import java.util.List;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ChallengeMod implements DedicatedServerModInitializer {
  public static final String MOD_ID = "challenge";

  // This logger is used to write text to the console and the log file.
  // It is considered best practice to use your mod id as the logger's name.
  // That way, it's clear which mod wrote info, warnings, and errors.
  public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

  private final ChallengeTimeController challengeTimeController = new ChallengeTimeController();
  private final ChallengeCollection challengeCollection = new ChallengeCollection();
  private List<ServerPlayer> players = new LinkedList<>();

  public int startChallengeCommand(CommandContext<CommandSourceStack> ctx) {
    CommandSourceStack source = ctx.getSource();

    startChallenge();
    source.sendSuccess(() -> Component.literal("Challenge started"), true);

    return 1;
  }

  public void startChallenge() {
    this.challengeCollection.start();
    this.challengeTimeController.start();
  }

  public int stopChallengeCommand(CommandContext<CommandSourceStack> ctx) {
    CommandSourceStack source = ctx.getSource();
    stopChallenge(true);
    source.sendSuccess(() -> Component.literal("Challenge stopped"), true);
    return 1;
  }

  public void stopChallenge(boolean success) {
    this.challengeCollection.stop();
    this.challengeTimeController.stop();

    String elapsedTime = this.challengeTimeController.getElapsedTimeFormated();
    MutableComponent message;
    if (success) {
      TextColor green = TextColor.parseColor("green").getOrThrow();
      message =
          Component.literal(
              String.format("You completed the challenge! Your final time is: %s", elapsedTime));
      message = message.setStyle(Style.EMPTY.withColor(green).withBold(true));
    } else {
      TextColor red = TextColor.parseColor("red").getOrThrow();
      message =
          Component.literal(
              String.format(
                  "You failed to complete the challenge! Your final time is: %s", elapsedTime));
      message = message.setStyle(Style.EMPTY.withColor(red).withBold(true));
    }

    for (ServerPlayer player : this.players) {
      player.sendSystemMessage(message);
    }
  }

  public int pauseChallengeCommand(CommandContext<CommandSourceStack> ctx) {
    CommandSourceStack source = ctx.getSource();

    pauseChallenge();
    source.sendSystemMessage(Component.literal("Challenge paused"));

    return 1;
  }

  public void pauseChallenge() {
    for (BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
      challenge.stop();
    }
    this.challengeTimeController.pause();
  }

  public int resumeChallengeCommand(CommandContext<CommandSourceStack> ctx) {
    CommandSourceStack source = ctx.getSource();

    this.resumeChallenge();

    source.sendSuccess(() -> Component.literal("Challenge resumed"), true);

    return 1;
  }

  public void resumeChallenge() {
    for (BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
      challenge.start();
    }
    this.challengeTimeController.resume();
  }

  public int configChallengeCommand(CommandContext<CommandSourceStack> ctx) {
    CommandSourceStack source = ctx.getSource();
    ServerPlayer player = source.getPlayer();

    player.openMenu(
        new SimpleMenuProvider(
            (containerId, inventory, p) ->
                new ChallengeConfigManager(containerId, p, this.challengeCollection),
            Component.literal("Challenge Config")));

    return 1;
  }

  public void onChallengeFinished(BaseChallenge challenge, boolean success) {
    stopChallenge(success);
  }

  private void addChallenge(BaseChallenge challenge, MinecraftServer server) {
    challenge.setFinishedCallback(this::onChallengeFinished);
    challenge.setServer(server);
    challengeCollection.addChallenge(challenge);
  }

  @Override
  public void onInitializeServer() {
    ServerPlayConnectionEvents.JOIN.register(
        (handler, sender, server) -> {
          ServerPlayer newPlayer = handler.getPlayer();
          this.players.add(newPlayer);
          this.challengeTimeController.addPlayer(newPlayer);
        });

    ServerPlayConnectionEvents.DISCONNECT.register(
        (handler, server) -> {
          ServerPlayer oldPlayer = handler.getPlayer();
          this.players.remove(oldPlayer);
          this.challengeTimeController.removePlayer(oldPlayer);
        });

    ServerLifecycleEvents.SERVER_STARTED.register(
        (server) -> {
          this.addChallenge(new HardcoreChallenge(), server);
          this.addChallenge(new SlayEnderDragonChallenge(), server);
          this.addChallenge(new SlayWitherChallenge(), server);
          this.addChallenge(new SlayWardenChallenge(), server);
          this.addChallenge(new RandomMobDropsChallenge(), server);
          this.addChallenge(new ScrambledMobDropsChallenge(), server);
          this.addChallenge(new ScrambledBlockDropsChallenge(), server);
          this.addChallenge(new RandomBlockDropsChallenge(), server);
          this.addChallenge(new RandomMobsOnDamageChallenge(), server);
          this.addChallenge(new ScrambledMobsOnDamageChallenge(), server);
          this.addChallenge(new NoCraftingTableChallenge(), server);
          this.addChallenge(new RandomMobSpeedChallenge(), server);
          this.addChallenge(new DeleteBlocksOnBreak(), server);
        });

    CommandRegistrationCallback.EVENT.register(
        (dispatcher, registryAccess, environment) -> {
          dispatcher.register(
              Commands.literal("challenge")
                  // If the command does not have a permission check, the mod initialization fails
                  // on the  prod server.
                  // Not sure why it is required, but this noop fixes it...
                  .requires(source -> true)
                  .then(Commands.literal("config").executes(this::configChallengeCommand))
                  .then(Commands.literal("start").executes(this::startChallengeCommand))
                  .then(Commands.literal("stop").executes(this::stopChallengeCommand))
                  .then(Commands.literal("pause").executes(this::pauseChallengeCommand))
                  .then(Commands.literal("resume").executes(this::resumeChallengeCommand)));
        });
  }
}
