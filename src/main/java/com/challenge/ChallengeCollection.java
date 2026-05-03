package com.challenge;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.challenge.challenges.BaseChallenge;


import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class ChallengeCollection implements Container {
		public static final Logger LOGGER = LoggerFactory.getLogger(ChallengeMod.MOD_ID);

		private List<BaseChallenge> challenges = new LinkedList<>();
		private List<ItemStack> slots = new LinkedList<>();

		private boolean running = false;

		public int getContainerSize() {
			return 36;
		}

		public boolean isEmpty() {
			return false;
		}

		public ItemStack getItem(int slot) {	
			if(slot >= this.slots.size()) {
				return ItemStack.EMPTY;
			}

			return this.slots.get(slot);
		}

		public ItemStack removeItem(int slot, int count) {
			this.toggleChallenge(slot);
			return ItemStack.EMPTY;
		}

		public ItemStack removeItemNoUpdate(int slot) {
			this.toggleChallenge(slot);
			return ItemStack.EMPTY;
		}

		public void setItem(int slot, ItemStack itemStack) {
		}

		public ChallengeCollection() {
		}

		public void clearContent() {
			return;
		}


		public void addChallenge(BaseChallenge challenge) {
			LOGGER.info("Adding challenge: " + challenge.getName());
			this.challenges.add(challenge);
			ItemStack newItemStack = getItemStackForChallenge(challenge, false);
			this.slots.add(newItemStack);
		}

		private ItemStack getItemStackForChallenge(BaseChallenge challenge, boolean enabled) {
			ItemStack newItemStack = challenge.getIndicatorItemStack();
			MutableComponent name = Component.literal(challenge.getName());
			if(enabled) {
				name.setStyle(Style.EMPTY.withColor(TextColor.parseColor("green").getOrThrow()).withBold(true));
			} else {
				name.setStyle(Style.EMPTY.withColor(TextColor.parseColor("red").getOrThrow()).withBold(false));	
		  	}	
			// newItemStack.set(DataComponentTypes.TOOLTIP_DISPLAY, Unit.INSTANCE);
			newItemStack.set(DataComponents.CUSTOM_NAME, name);
			return newItemStack;
		}

		public void toggleChallenge(int slotIndex) {
			BaseChallenge challenge = this.challenges.get(slotIndex);
			if (challenge.isEnabled()) {
				challenge.disable();
			} else {
				challenge.enable();
				if(running) challenge.start();
			}
			ItemStack newItemStack = getItemStackForChallenge(this.challenges.get(slotIndex), challenge.isEnabled());
			this.slots.set(slotIndex, newItemStack);
		}
		
		public List<BaseChallenge> getEnabledChallenges() {
			List<BaseChallenge> enabledChallenges = new LinkedList<>();
			for(BaseChallenge challenge : this.challenges) {
				if(challenge.isEnabled()) {
					enabledChallenges.add(challenge);
				}
			}
			return enabledChallenges;
		}

		public void start() {
			for(BaseChallenge challenge : this.getEnabledChallenges()) {
				challenge.start();
			}
			this.running = true;
		}

		public void stop() {
			for(BaseChallenge challenge : this.getEnabledChallenges()) {
				challenge.stop();
			}
			this.running = false;
		}
		
		public void setChanged() {
			return;
		}

		public boolean stillValid(Player player) {
			return true;
		}

		public int size() {
			return this.challenges.size();
		}

		public BaseChallenge getChallenge(int idx) {
			return this.challenges.get(idx);
		}
	}
