package com.challenge;

import java.util.LinkedList;
import java.util.List;

import com.challenge.challenges.BaseChallenge;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Unit;

public class ChallengeCollection extends SimpleInventory {
		private List<BaseChallenge> challenges = new LinkedList<>();
		private boolean running = false;

		public ChallengeCollection() {
		  super(27);
		}

		public void addChallenge(BaseChallenge challenge) {
		  this.challenges.add(challenge);
		  ItemStack newItemStack = getItemStackForChallenge(challenge, false);
		  this.addStack(newItemStack);
		}

		private ItemStack getItemStackForChallenge(BaseChallenge challenge, boolean enabled) {
			ItemStack newItemStack = challenge.getIndicatorItemStack();
			MutableText name = Text.literal(challenge.getName());
			if(enabled) {
				name.setStyle(Style.EMPTY.withColor(TextColor.parse("green").getOrThrow()).withBold(true));	
			} else {
				name.setStyle(Style.EMPTY.withColor(TextColor.parse("red").getOrThrow()).withBold(false));	
		  	}	
			newItemStack.set(DataComponentTypes.HIDE_ADDITIONAL_TOOLTIP, Unit.INSTANCE);
			newItemStack.set(DataComponentTypes.ITEM_NAME, name);
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
			this.setStack(slotIndex, newItemStack);
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
	}
