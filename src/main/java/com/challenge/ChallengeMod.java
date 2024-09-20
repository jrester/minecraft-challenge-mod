package com.challenge;

import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.challenges.BaseChallenge;
import com.challenge.challenges.HardcoreChallenge;
import com.challenge.challenges.RandomBlockDropsChallenge;
import com.challenge.challenges.RandomMobsOnDamageChallenge;
import com.challenge.challenges.ScrambledMobDropsChallenge;
import com.challenge.challenges.ScrambledMobsOnDamageChallenge;
import com.challenge.challenges.ScrambledBlockDropsChallenge;
import com.challenge.challenges.SlayEnderDragonChallenge;
import com.challenge.challenges.SlayWitherChallenge;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.context.CommandContext;

public class ChallengeMod implements DedicatedServerModInitializer {
	public static final String MOD_ID = "challenge";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private final ChallengeTimeController challengeTimeController = new ChallengeTimeController(LOGGER);
	private final ChallengeCollection challengeCollection = new ChallengeCollection();
	private List<ServerPlayerEntity> players = new LinkedList<>();


	public int startChallengeCommand(CommandContext<ServerCommandSource> ctx) {
		ServerCommandSource source = ctx.getSource();


		startChallenge();
		source.sendFeedback(() -> Text.of("Challenge started"), false);

		return 1;
	}

	public void startChallenge() {
		for(BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
			challenge.start();
		}
		this.challengeTimeController.start();
	}
	
	public int stopChallengeCommand(CommandContext<ServerCommandSource> ctx) {
		ServerCommandSource source = ctx.getSource();
		stopChallenge(true);
		source.sendFeedback(() -> Text.of("Challenge stopped"), false);
		return 1;
	}

	public void stopChallenge(boolean success) {
		for(BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
			challenge.stop();
			challenge.disable();
		}
		this.challengeTimeController.stop();
		String elapsedTime = this.challengeTimeController.getElapsedTimeFormated();
		Text message;
		if (success) {
				message = Text.literal(String.format("You completed the challenge! Your final time is: %s", elapsedTime)).setStyle(Style.EMPTY.withColor(TextColor.parse("green").getOrThrow()).withBold(true));
		} else {
				message = Text.literal(String.format("You failed to complete the challenge! Your final time is: %s", elapsedTime)).setStyle(Style.EMPTY.withColor(TextColor.parse("red").getOrThrow()).withBold(true));
		}
		
		for (ServerPlayerEntity player : this.players) {
			player.sendMessage(message);
		}
		
	}

	public int pauseChallengeCommand(CommandContext<ServerCommandSource> ctx) {
    ServerCommandSource source = ctx.getSource();

    pauseChallenge();
    source.sendFeedback(() -> Text.of("Challenge paused"), false);

    return 1;
	}

	public void pauseChallenge() {
		for(BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
			challenge.stop();
		}
		this.challengeTimeController.pause();
	}

	public int resumeChallengeCommand(CommandContext<ServerCommandSource> ctx) {
    ServerCommandSource source = ctx.getSource();

    this.resumeChallenge();

    source.sendFeedback(() -> Text.of("Challenge resumed"), false);

    return 1;
	}

	public void resumeChallenge() {
		for(BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
			challenge.start();
		}
		this.challengeTimeController.resume();
	}

	public int configChallengeCommand(CommandContext<ServerCommandSource> ctx) {
		ServerCommandSource source = ctx.getSource();
		GameProfile playerProfile = source.getPlayer().getGameProfile();
		MinecraftServer server = source.getServer();
		PlayerEntity player = server.getPlayerManager().getPlayer(playerProfile.getId());

		player.openHandledScreen(new SimpleNamedScreenHandlerFactory((syncId, playerInventory, playerx) -> new ChallengeConfigManager(syncId, playerInventory, this.challengeCollection), Text.of("Challenge Config")));	
		if(this.challengeTimeController.isRunning()) {
			// If a challenge is active and the user modified the settings, all enabled challenges should be started
			for(BaseChallenge challenge : this.challengeCollection.getEnabledChallenges()) {
				challenge.start();
			}
		}
		
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

		ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
			ServerPlayerEntity newPlayer = handler.getPlayer();
			this.players.add(newPlayer);
			this.challengeTimeController.addPlayer(newPlayer);
		});

		ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
			ServerPlayerEntity oldPlayer = handler.getPlayer();
			this.players.remove(oldPlayer);
			this.challengeTimeController.removePlayer(oldPlayer);
		});

		ServerLifecycleEvents.SERVER_STARTED.register((server) -> {
			this.addChallenge(new HardcoreChallenge(), server);
			this.addChallenge(new SlayEnderDragonChallenge(), server);
			this.addChallenge(new SlayWitherChallenge(), server);
			this.addChallenge(new ScrambledMobDropsChallenge(), server);
			this.addChallenge(new ScrambledBlockDropsChallenge(), server);
			this.addChallenge(new RandomBlockDropsChallenge(), server);
			this.addChallenge(new RandomMobsOnDamageChallenge(), server);
			this.addChallenge(new ScrambledMobsOnDamageChallenge(), server);
		});
		
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
			dispatcher.register(
				CommandManager.literal("challenge")
					.requires(source -> source.hasPermissionLevel(4))
					.then(CommandManager.literal("config").executes(this::configChallengeCommand))
					.then(CommandManager.literal("start").executes(this::startChallengeCommand))
					.then(CommandManager.literal("stop").executes(this::stopChallengeCommand))
					.then(CommandManager.literal("pause").executes(this::pauseChallengeCommand))
					.then(CommandManager.literal("resume").executes(this::resumeChallengeCommand))
			);
		});
	}
}
