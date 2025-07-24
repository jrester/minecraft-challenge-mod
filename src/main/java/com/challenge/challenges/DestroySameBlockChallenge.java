package com.challenge.challenges;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.challenge.events.PlayerEvents;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.world.GameMode;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

public class DestroySameBlockChallenge extends BaseChallenge {
  private final String name = "Destroy Same Block";

  private String partnerServerUrl;
  private int serverPort;
  private HttpClient httpClient;

  private ServerPlayerEntity player;
  private boolean isMyTurn = false;
  private boolean freeSelection;
  private int nextStackCount = -1;
  private int nextStackItem = -1;

  public DestroySameBlockChallenge() {
   this.httpClient = HttpClient.newBuilder()
      .connectTimeout(Duration.ofSeconds(5))
      .build();
  }

 
  private void onChallengeLost() {
      for(ServerPlayerEntity player : this.getServer().getPlayerManager().getPlayerList()) {
        player.kill(player.getServerWorld());
        player.changeGameMode(GameMode.SPECTATOR);
      }
    this.sendMessageToPartner("lost");
  }

  private void transitionFromNextPlayer(int stackCount, int stackItem) {
    this.nextStackCount= stackCount;
    this.nextStackItem = stackItem;
    this.isMyTurn = true;

    ((ServerPlayerEntity)this.player).sendMessage(Text.of("It's your turn! Pick up: " + this.nextStackCount + "x " + this.nextStackItem));

  }
  
  private void transitionToNextPlayer(PlayerEntity player, ItemStack stack) {
    this.isMyTurn = false;
    this.freeSelection = false;

    String nextTurnMessage = "nextTurn,"+stack.getCount() + "," + Item.getRawId(stack.getItem());

    this.sendMessageToPartner(nextTurnMessage);
  }

  public void transitionToFreeSelection() {
    ((ServerPlayerEntity)this.player).sendMessage(Text.of("It's your turn! Pick up whatever you want!"));
    this.freeSelection = true;
    
  }

 
  @Override
  public void registerEventHandlers() {
    PlayerEvents.ON_PLAYER_PICKUP.register((player, stack) -> {
      if(!isActive()) return;

      if(!this.isMyTurn) {
        this.onChallengeLost();
        return;
      }

      if(freeSelection) {
        this.transitionToNextPlayer(player, stack);
        return;
      }

      if(stack.getCount() != this.nextStackCount) {
        LOGGER.info("Stacks do not match: " + stack.getCount() + " != " + this.nextStackCount);
        this.onChallengeLost();
        return;
      }

      if(!stack.isOf(Item.byRawId(nextStackItem))) {
        LOGGER.info("Stacks do not match: " + stack.toString() + " != " + Item.byRawId(nextStackItem));
        this.onChallengeLost();
        return;
      }
      
      this.transitionToFreeSelection();
      
      return;
    });
  }
  
  @Override
  public String getName() {
    return name;
  }

  @Override
  public ItemStack getIndicatorItemStack() {
    if(this.isEnabled()) {
      return Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack();
    } else {
      return Items.APPLE.getDefaultStack();
    }
  }

  @Override
  protected void onPostStart() {
    this.configureFromEnvironment();
    try {
      HttpServer httpServer = HttpServer.create(new InetSocketAddress(this.serverPort), 0);
      httpServer.createContext("/status", new StatusHandler());
      httpServer.start();
    } catch (IOException exp) {
      return;
    }

    this.player = this.getServer().getPlayerManager().getPlayerList().getFirst();
    
    if(this.isMyTurn) {
      this.transitionToFreeSelection();
    } else {
    ((ServerPlayerEntity)this.player).sendMessage(Text.of("It's NOT your turn! Wait for your partner to pick an item!"));
    }
  }

  private class StatusHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
      try (InputStream is = exchange.getRequestBody()) {
          String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);

          if(body.startsWith("lost")) {
            player.kill(player.getServerWorld());
            player.changeGameMode(GameMode.SPECTATOR);
          } else if (body.startsWith("nextTurn")) {
            var messageParts = body.split(",");
            var stackCount = Integer.parseInt(messageParts[1]);
            var stackItem = Integer.parseInt(messageParts[2]);
            transitionFromNextPlayer(stackCount, stackItem);
          }
          
          
          // Send success response
          String response = "ok";
          exchange.getResponseHeaders().set("Content-Type", "application/json");
          exchange.sendResponseHeaders(200, response.length());
          try (OutputStream os = exchange.getResponseBody()) {
              os.write(response.getBytes(StandardCharsets.UTF_8));
          }
      } catch (Exception e) {
          LOGGER.error("Error handling challenge message", e);
          String errorResponse = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
          exchange.sendResponseHeaders(500, errorResponse.length());
          try (OutputStream os = exchange.getResponseBody()) {
              os.write(errorResponse.getBytes(StandardCharsets.UTF_8));
          }
      }
    }
    
  }

  private void sendMessageToPartner(String message) {
    URI uri = URI.create(this.partnerServerUrl + "/status");
      LOGGER.info("Sending message to "+ uri.toString());
    HttpRequest request = HttpRequest.newBuilder().uri(uri).POST(HttpRequest.BodyPublishers.ofString(message)).build();
    httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
    .thenAccept(response -> {
        if (response.statusCode() == 200) {
            LOGGER.info("Successfully sent message to partner");
        } else {
            LOGGER.error("Failed to send message: " + response.statusCode());
        }
    })
    .exceptionally(throwable -> {
        LOGGER.error("Error sending message", throwable);
        return null;
    });
    
  }

      private void configureFromEnvironment() {
        this.serverPort = Integer.parseInt(System.getenv("CHALLENGE_SERVER_PORT"));

        // Read partner server URL from environment variable
        String partnerUrl = System.getenv("CHALLENGE_PARTNER_URL");
        if (partnerUrl == null || partnerUrl.trim().isEmpty()) {
            partnerUrl = System.getProperty("challenge.partner.url"); // Fallback to system property
        }
        
        // Read initiator flag from environment variable
        String initiatorEnv = System.getenv("CHALLENGE_IS_INITIATOR");
        if (initiatorEnv == null || initiatorEnv.trim().isEmpty()) {
            initiatorEnv = System.getProperty("challenge.is.initiator", "false"); // Fallback to system property
        }
        
        boolean isInitiator = Boolean.parseBoolean(initiatorEnv);
        
        if (partnerUrl != null && !partnerUrl.trim().isEmpty()) {
            // Ensure URL has proper format
            if (!partnerUrl.startsWith("http://") && !partnerUrl.startsWith("https://")) {
                partnerUrl = "http://" + partnerUrl;
            }
            
            // Remove trailing slash if present
            if (partnerUrl.endsWith("/")) {
                partnerUrl = partnerUrl.substring(0, partnerUrl.length() - 1);
            }
            
            this.configurePartner(partnerUrl, isInitiator);
            LOGGER.info("Configured challenge partner from environment: URL=" + partnerUrl + ", Initiator=" + isInitiator);
        } else {
            LOGGER.warn("No partner URL found in environment variables. Challenge will need manual configuration.");
            LOGGER.info("Set CHALLENGE_PARTNER_URL environment variable to configure automatically.");
            LOGGER.info("Set CHALLENGE_IS_INITIATOR=true/false to specify if this server initiates the challenge.");
        }
    }

        // Configuration method - call this before starting the challenge
    public void configurePartner(String partnerServerUrl, boolean isInitiator) {
        this.partnerServerUrl = partnerServerUrl;
        this.isMyTurn = isInitiator; // Initiator goes first
    }
}
