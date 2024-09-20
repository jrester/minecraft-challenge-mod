package com.challenge;

import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;
import java.util.LinkedList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;

class ChallengeTimeController {
    private final Logger LOGGER;
  
	private List<ServerPlayerEntity> players = new LinkedList<>();
	private ScheduledThreadPoolExecutor executor;
	private ScheduledFuture<?> timer;

	private long startTime = 0;
	private long alreadyElapsed = 0;
	private boolean paused = false;
	private boolean running = false;

  	public ChallengeTimeController(Logger logger) {this.LOGGER = logger;}

  	class ChallengeTimer implements Runnable {
    	public void run() {
    		TextColor red = TextColor.parse("red").getOrThrow();
    		Text message;
    	  if(paused) {
    	    message = Text.literal("Challenge Paused").setStyle(Style.EMPTY.withColor(red));
    	  } else {
      		long currentTime = System.currentTimeMillis();
      		long elapsedTime = currentTime - startTime + alreadyElapsed;
    	    String timeFormated = ChallengeTimeController.formatElapsedTime(elapsedTime);
      		message = Text.literal(timeFormated).setStyle(Style.EMPTY.withColor(red).withBold(true));
      	}
    		for (ServerPlayerEntity player : players) {
    			player.sendMessage(message, true);
    		}
      }
  	}

  	public void addPlayer(ServerPlayerEntity player) {
  	  if (!this.players.contains(player)) {
    	  this.players.add(player);
  	  }
  	}

  	public void removePlayer(ServerPlayerEntity player) {
  	  if (!this.players.contains(player)) {
    	  this.players.remove(player);
  	  }
  	}

  	public static String formatElapsedTime(long elapsedTime) {
  		long hours = TimeUnit.MILLISECONDS.toHours(elapsedTime) % 24;
  		long minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime) % 60;
  		long seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60;

  		return String.format("%02d:%02d:%02d", hours, minutes, seconds);
  	  
  	}

  	  	
	public void start() {
		if (running) return;
		this.alreadyElapsed = 0;
	    this.startTime = System.currentTimeMillis();
	    this.executor = new ScheduledThreadPoolExecutor(1);
	    Runnable _timer = new ChallengeTimer();
	    this.timer = this.executor.scheduleAtFixedRate(_timer, 0, 500, TimeUnit.MILLISECONDS);
	    this.running = true;
	}

	public void pause() {
		if(!running) return;
		if (paused) return;
		long currentTime = System.currentTimeMillis();
		this.alreadyElapsed += currentTime - this.startTime;
		this.startTime = 0;
		this.paused = true;
	}

	public void resume() {
		if(!running) return;
		if (!paused) return;
		this.startTime = System.currentTimeMillis();
		this.paused = false;
	}

	public void stop() {
		if(!running) return;
		this.timer.cancel(true);
		this.executor.close();

		long totalElapsedTime = this.alreadyElapsed;
		if (!this.paused) {
		long currentTime = System.currentTimeMillis();
			totalElapsedTime = totalElapsedTime + (currentTime - this.startTime);
			this.alreadyElapsed = totalElapsedTime;
		}

		this.startTime = 0;
		this.paused = false;
		this.running = false;
	}
	
	public boolean isRunning() {
		return running;
	}

	public boolean isPaused() {
		return paused;
	}

	public String getElapsedTimeFormated() {
		return ChallengeTimeController.formatElapsedTime(this.alreadyElapsed);
	}
}
