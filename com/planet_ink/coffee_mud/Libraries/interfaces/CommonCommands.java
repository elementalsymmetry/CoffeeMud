package com.planet_ink.coffee_mud.Libraries.interfaces;
import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.core.collections.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

import java.io.IOException;
import java.util.*;
/*
   Copyright 2005-2020 Bo Zimmerman

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

	   http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/
/**
 * Message handlers for the basic common commands, like
 * get, drop, put, etc.  Also handles postings for many
 * common activities that aren't always generated by
 * a user Command.  Can also generate traditional command
 * entries.
 *
 * This is the high-level functionality for all of these
 * types of commands, and should be used most of the time
 * when you want a mob to do these things, as it goes
 * through the normal command process.
 *
 * @author Bo Zimmerman
 *
 */
public interface CommonCommands extends CMLibrary
{
	/**
	 * Adds a new listener to global messages. See
	 * the monitor method for more information on what
	 * that means.
	 *
	 * @see com.planet_ink.coffee_mud.core.interfaces.MsgMonitor
	 * @see CommonCommands#monitorGlobalMessage(Room, CMMsg)
	 * @see CommonCommands#delGlobalMonitor(MsgMonitor)
	 *
	 * @param M the monitor object to call back
	 */
	public void addGlobalMonitor(MsgMonitor M);

	/**
	 * Removes a listener to global messages. See
	 * the monitor method for more information on what
	 * that means.
	 *
	 * @see com.planet_ink.coffee_mud.core.interfaces.MsgMonitor
	 * @see CommonCommands#monitorGlobalMessage(Room, CMMsg)
	 * @see CommonCommands#addGlobalMonitor(MsgMonitor)
	 *
	 * @param M the monitor object to call back
	 */
	public void delGlobalMonitor(MsgMonitor M);

	/**
	 * When a message is generated and sent anywhere in the mud, this
	 * method is called to allow any global followers to inspect the
	 * message and possibly react.  Messages reflect things like
	 * actions taken, the responses to those actions, and other
	 * substantive events.  Global listeners, which are extremely
	 * special cases and should be few in number are the monitors.
	 *
	 * @see CommonCommands#addGlobalMonitor(MsgMonitor)
	 * @see CommonCommands#delGlobalMonitor(MsgMonitor)
	 *
	 * @param room the room in which the event occurred
	 * @param msg the message sent to that room.
	 */
	public void monitorGlobalMessage(Room room, CMMsg msg);

	/**
	 * This method does a forced execution of a normal command using the
	 * normal channels used by players.
	 *
	 * @param mob the mob to execute the command
	 * @param command the command ID() to execute
	 * @param parms the arguments to the command, including the command word
	 * @return the boolean response from the command, which is mostly meaningless
	 */
	public boolean forceStandardCommand(MOB mob, String command, List<String> parms);

	/**
	 * This method does a forced internal execute of a specific command with the
	 * given parameters.  Please see each Command class for information about
	 * which commands support internal execution, and what their parameters are,
	 * if any.
	 *
	 * @see CommonCommands#unforcedInternalCommand(MOB, String, Object...)
	 * @see CommonCommands#forceStandardCommand(MOB, String, List)
	 *
	 * @param mob the mob to execute the command
	 * @param command the command class ID() to execute
	 * @param parms the parameters required by the commands internal execute method
	 * @return the response object from the internal execute command
	 */
	public Object forceInternalCommand(MOB mob, String command, Object... parms);

	/**
	 * This method does a normal internal execute of a specific command with the
	 * given parameters.  Please see each Command class for information about
	 * which commands support internal execution, and what their parameters are,
	 * if any.
	 *
	 * @see CommonCommands#forceInternalCommand(MOB, String, Object...)
	 * @see CommonCommands#forceStandardCommand(MOB, String, List)
	 *
	 * @param mob the mob to execute the command
	 * @param command the command class ID() to execute
	 * @param parms the parameters required by the commands internal execute method
	 * @return the response object from the internal execute command
	 */
	public Object unforcedInternalCommand(MOB mob, String command, Object... parms);

	/**
	 * Handler-like function that is called whenever the given mob
	 * enters a command that cannot be resolved to an actual mud
	 * command, social, or skill.
	 *
	 * @param mob the player who entered the bad command
	 * @param command the parsed command as entered.
	 * @return true if the command remained unknown, false if rejected
	 */
	public boolean handleUnknownCommand(MOB mob, List<String> command);

	/**
	 * Method called by the system when certain (but not all -- maybe someday) commands
	 * fail because of syntax or bad arguments.
	 *
	 * @param mob the mob who tried the command
	 * @param commands the command that was typed in
	 * @param msgStr the message explaining the failure, using <S-NAME>, <T-NAME>, etc..
	 * @return true if the command failure went through, and false if the command failure failed
	 */
	public boolean postCommandFail(final MOB mob, final List<String> commands, final String msgStr);

	/**
	 * Method called by the system when certain (but not all -- maybe someday) commands
	 * fail because of syntax or bad arguments.
	 *
	 * @param mob the mob who tried the command
	 * @param target the target of the command, if applicable and identified
	 * @param tools the tool used by the command, if applicable and identified
	 * @param command the command that was typed in
	 * @param msgStr the message explaining the failure, using <S-NAME>, <T-NAME>, etc..
	 * @return true if the command failure went through, and false if the command failure failed
	 */
	public boolean postCommandFail(final MOB mob, Environmental target, Environmental tools, final List<String> command, final String msgStr);

	/**
	 * Method called by the system when certain (but not all -- maybe someday) commands
	 * fail because of a message rejection specifically.  So, presumably the command was
	 * entered correctly, but something in the world stopped it from happening.
	 *
	 * @param mob the mob who did the command
	 * @param commands the command string entered
	 * @return true if the command completed being rejected, or false if the rejection was rejected
	 */
	public boolean postCommandRejection(final MOB mob, final List<String> commands);

	/**
	 * Method called when certain (but not all -- maybe someday) commands fail because of
	 * a message rejection specifically.  So, presumably the command was entered correctly,
	 * but something in the world stopped it from happening.
	 *
	 * @param mob the mob who did the command
	 * @param target the target of the command, if applicable
	 * @param tools a tool used by the command, if applicable
	 * @param command the command string entered
	 * @return true if the command completed being rejected, or false if the rejection was rejected
	 */
	public boolean postCommandRejection(final MOB mob, Environmental target, Environmental tools, final List<String> command);

	/**
	 * Method to post a chat message onto a channel from a specific mob.
	 *
	 * @param mob the mob to do the chatting
	 * @param channelName the channel to chat on
	 * @param message the message to send to the channel
	 * @param systemMsg true to format as a system message, false for a normal chat message
	 */
	public void postChannel(MOB mob, String channelName, String message, boolean systemMsg);

	/**
	 * Method to post an informational, system-generated message to a channel.
	 * Most channels are generic, but some are clan-only, meaning that a message
	 * sent by a member of clan A is only seen by other members of clan A, even
	 * on the same channel.  This is why the clanList is included, so that the
	 * channel knows which clans can read it, if that's applicable.
	 *
	 * @param channelName the name of the channel to post the message to
	 * @param clanList null normally, or a list of clans for possible focus
	 * @param message the message to send to the channel
	 * @param systemMsg true to format as a system message, false for a normal chat message
	 */
	public void postChannel(String channelName, Iterable<Pair<Clan,Integer>> clanList, String message, boolean systemMsg);

	/**
	 * Shortcut method to make a mob drop an item onto the room.
	 *
	 * @param mob the mob to do the dropping
	 * @param dropThis the item to drop
	 * @param quiet true to do so without a visible room message
	 * @param optimized true if this is part of a mass-drop (no stat recalc)
	 * @param intermediate true if this is only the first part of a GIVE
	 * @return true if the item was successfully dropped
	 */
	public boolean postDrop(MOB mob, Environmental dropThis, boolean quiet, boolean optimized, boolean intermediate);

	/**
	 * Shortcut method to make one mob given an item to another
	 *
	 * @param mob the mob to do the giving
	 * @param targetM the mob to do the receiving of the giving
	 * @param giveThis the item to give
	 * @param quiet true to do so without a visible room message
	 * @return true if the item was successfully given
	 */
	public boolean postGive(MOB mob, MOB targetM, Item giveThis, boolean quiet);

	/**
	 * Shortcut method to make a mob try to open something
	 *
	 * @param mob the mob to do the opening
	 * @param openThis the item or exit to open
	 * @param quiet true to do so without a visible room message
	 * @return true if the thing was successfully opened
	 */
	public boolean postOpen(MOB mob, Environmental openThis, boolean quiet);

	/**
	 * Shortcut method for making a mob get something from the room, or
	 * from a container.
	 *
	 * @param mob the mob to do the getting
	 * @param container the items current container, or null
	 * @param getThis the item to get
	 * @param quiet true to do so without a visible message
	 * @return true if the item was successfully gotten
	 */
	public boolean postGet(MOB mob, Item container, Item getThis, boolean quiet);

	/**
	 * Shortcut method for making a mob put an item into a container.
	 *
	 * @param mob the mob to do the putting
	 * @param container the target container to put something into
	 * @param getThis the item to put into the container
	 * @param quiet true to do so without a visible room message
	 * @return true if the item was successfully put into the container
	 */
	public boolean postPut(MOB mob, Item container, Item getThis, boolean quiet);

	/**
	 * Shortcut method to make a mob remove an item and put it
	 * into their inventory.
	 *
	 * @param mob the mob to do the removing
	 * @param item the item to be removed
	 * @param quiet true to do so without a visible message
	 * @return true if the item was removed
	 */
	public boolean postRemove(MOB mob, Item item, boolean quiet);

	/**
	 * Shortcut method to make a mob attempt to wear an item.
	 *
	 * @param mob the mob to do the wearing
	 * @param item the item to be worn
	 * @param quiet true to attempt without a visible message
	 * @return true if the item was successfully worn
	 */
	public boolean postWear(MOB mob, Item item, boolean quiet);

	/**
	 * Shortcut method for making a mob look around the room
	 *
	 * @param mob the mob to do the looking
	 * @param quiet true to do so without a visible message
	 */
	public void postLook(MOB mob, boolean quiet);

	/**
	 * Shortcut method for making a mob read something.
	 *
	 * @param mob the mob to do the reading
	 * @param target the item to read
	 * @param readOff read arguments, such as page or chapter number, or ""
	 * @param quiet true to do so without a room-visible message
	 */
	public void postRead(MOB mob, Physical target, String readOff, boolean quiet);

	/**
	 * Shortcut method for making a mob panic and flee the room.
	 *
	 * @param mob the mob to flee
	 * @param whereTo NOWHERE or a direction in which to flee, or "" to random choose
	 */
	public void postFlee(MOB mob, String whereTo);

	/**
	 * Shortcut method for making a mob sheath a weapon into
	 * an available sheath.
	 *
	 * @param mob the mob to sheath a weapon
	 * @param ifPossible true to suppress errors or lack of weapon/sheath
	 */
	public void postSheath(MOB mob, boolean ifPossible);

	/**
	 * Shortcut method for making a mob draw their first available weapon
	 * from a sheath.
	 *
	 * @param mob the mob to draw the weapon
	 * @param doHold true to off-hand the weapon, false for main-hand
	 * @param ifNecessary if true, only do so if not currently wielding
	 */
	public void postDraw(MOB mob, boolean doHold, boolean ifNecessary);

	/**
	 * Shortcut method for making a mob stand up.
	 *
	 * @param mob the mob to stand
	 * @param ifNecessary true if they should attempt only if not standing
	 * @param quietly true to do so without a visible message
	 */
	public void postStand(MOB mob, boolean ifNecessary, boolean quietly);

	/**
	 * Shortcut method for making a mob lay down and go to sleep
	 *
	 * @param mob the mob to sleep
	 */
	public void postSleep(MOB mob);

	/**
	 * Shortcut method for making one mob attempt to follow another.
	 *
	 * @param follower the mob to do the following
	 * @param leader the mob to be followed
	 * @param quiet true to do so without a visible message
	 */
	public void postFollow(MOB follower, MOB leader, boolean quiet);

	/**
	 * Shortcut method for forcing the given mob to do the command:
	 * tell [target] [text] ... with special options
	 *
	 * @param mob the speaker
	 * @param target the target of the tell
	 * @param text the message to say
	 * @param isPrivate true to TELL or private SAYTO, false to public SAYTO
	 * @param tellFlag true to TELL, false to private SAYTO
	 */
	public void postSay(MOB mob, MOB target, String text, boolean isPrivate, boolean tellFlag);

	/**
	 * Shortcut method for forcing the given mob to do the command:
	 * sayto [target] [text]
	 *
	 * @param mob the mob to do the command
	 * @param target the target being spoken to
	 * @param text the text to say
	 */
	public void postSay(MOB mob, MOB target,String text);

	/**
	 * Shortcut method for forcing the given mob to do the command:
	 * say [text]
	 *
	 * @param mob the mob to do the command
	 * @param text the text to say
	 */
	public void postSay(MOB mob, String text);

	/**
	 * Constructs a full table of the exits for the given room
	 * and sends them as a tell message to the given mob.
	 *
	 * @see CommonCommands#lookAtExitsShort(Room, MOB)
	 *
	 * @param room the room to find exits in
	 * @param mob the mob to tell about those exits
	 */
	public void lookAtExits(Room room, MOB mob);

	/**
	 * Constructs an abbreviated list of the exits for the given room
	 * and sends them as a tell message to the given mob.
	 *
	 * @see CommonCommands#lookAtExits(Room, MOB)
	 *
	 * @param room the room to find exits in
	 * @param mob the mob to tell about those exits
	 */
	public void lookAtExitsShort(Room room, MOB mob);

	/**
	 * Parse variable display and description texts to generate
	 * the dynamic text seen in some rooms.
	 * 
	 * @param mob the mob who is looking
	 * @param area the area containing the mob
	 * @param room the room containing the mob
	 * @param text the raw variable text
	 * @return the dynamic text
	 */
	public String parseVaries(final MOB mob, final Area area, final Room room, final String text);
	
	/**
	 * Handler function for when a mob looks at something, or
	 * examines it closely.  Things like items, mobs, rooms, etc.
	 *
	 * @param msg the looking or examining message
	 */
	public void handleBeingLookedAt(CMMsg msg);

	/**
	 * Handler function for when a mob reads something
	 *
	 * @param msg the reading message
	 */
	public void handleBeingRead(CMMsg msg);

	/**
	 * Handler function for when a mob recalls back to their beacon room
	 *
	 * @param msg the recall message
	 */
	public void handleRecall(CMMsg msg);

	/**
	 * Handler function for when a mob sits down
	 *
	 * @param msg the sitting message
	 */
	public void handleSit(CMMsg msg);

	/**
	 * Handler function for when a mob stands up
	 *
	 * @param msg the standing message
	 */
	public void handleStand(CMMsg msg);

	/**
	 * Handler function for when a mob goes to sleep
	 *
	 * @param msg the sleeping message
	 */
	public void handleSleep(CMMsg msg);

	/**
	 * Handler function for when a mob sniffs an item or
	 * mob or room or something.
	 *
	 * @param msg the item sniffing message
	 */
	public void handleBeingSniffed(CMMsg msg);

	/**
	 * Handler function for when a mob is given an item
	 * by another mob.
	 *
	 * @param msg the item giving message
	 */
	public void handleBeingGivenTo(CMMsg msg);

	/**
	 * Handler function for when a mob gets an item
	 *
	 * @param msg the item getting message
	 */
	public void handleBeingGetted(CMMsg msg);

	/**
	 * Handler function for when a mob drops an item
	 *
	 * @param msg the item removing message
	 */
	public void handleBeingDropped(CMMsg msg);

	/**
	 * Handler function for when a mob removes an item
	 *
	 * @param msg the item removing message
	 */
	public void handleBeingRemoved(CMMsg msg);

	/**
	 * Handler function for when a mob wears an item
	 *
	 * @param msg the item wearing message
	 */
	public void handleBeingWorn(CMMsg msg);

	/**
	 * Handler function for when a mob wields a weapon.
	 *
	 * @param msg the item wielding message
	 */
	public void handleBeingWielded(CMMsg msg);

	/**
	 * Handler function for when a mob holds an item.
	 *
	 * @param msg the item holding message
	 */
	public void handleBeingHeld(CMMsg msg);

	/**
	 * Handler function called when a mob does something that might affect their
	 * hygiene, typically in a cleanly way.  The observer that calls this is
	 * typically something that can recognize that the event affect hygiene.
	 *
	 * @param msg the message that triggered the hygiene efect
	 * @param minHygiene the smallest hygiene amount to consider the mob clean
	 * @param adjHygiene the amount by which to adjust hygiene, usually +
	 */
	public void handleHygienicMessage(final CMMsg msg, final int minHygiene, final long adjHygiene);

	/**
	 * Handler function called when a mob is spoken to by another mob in
	 * the same room, and the introduction system is enabled.  This is to
	 * check to see if the speaker is introducing themselves.
	 *
	 * @param speaker the mob speaking
	 * @param me the mob being spoken to
	 * @param said what the speaker said
	 */
	public void handleIntroductions(MOB speaker, MOB me, String said);

	/**
	 * Handler function called when a mob is spoken to by another mob
	 * with targeted speech.
	 *
	 * @param speaker the speaker mob
	 * @param me the mob being spoken to
	 * @param msg the message
	 */
	public void handleBeingSpokenTo(MOB speaker, MOB me, String msg);

	/**
	 * Handler function that is called when a mob enters the
	 * game, either by logging in, or respawning, or remorting.
	 *
	 * @param mob the player or npc coming to life
	 * @param msg the "come to life" message
	 */
	public void handleComeToLife(MOB mob, CMMsg msg);

	/**
	 * Handler function that is called when a mob observes another mob enter the
	 * game, either by logging in, or respawning, or remorting.
	 *
	 * @param observer the player or npc observing the life happening
	 * @param lifer the player or npc coming to life
	 * @param msg the "come to life" message
	 */
	public void handleObserveComesToLife(MOB observer, MOB lifer, CMMsg msg);

	/**
	 * Builds the standard SCORE command response.  Moved out of the command
	 * since it is used by skills and several other things.
	 *
	 * @param mob the mob whose score to get
	 * @return the score stats response
	 */
	public StringBuilder getScore(MOB mob);

	/**
	 * Builds a string list of the things the given mob is
	 * wearing.
	 *
	 * @param viewer the viewer of the worn stuff
	 * @param mob the mob wearing the stuff
	 * @return the list of stuff being worn
	 */
	public StringBuilder getEquipment(MOB viewer, MOB mob);

	/**
	 * Builds a string list of the things the given viewer can
	 * see in the given mobs inventory.
	 *
	 * @param viewer the viewer of the inventory
	 * @param mob the mob whose inventory needs viewing
	 * @return the list of stuff
	 */
	public StringBuilder getInventory(MOB viewer, MOB mob);

	/**
	 * This method builds a string of the things a mob knows about
	 * an item from taking a good hard longlook at it.
	 *
	 * @param mob the mob doing the looking
	 * @param item the item being looked at
	 * @return the message of things the mob learns about it
	 */
	public String getExamineItemString(MOB mob, Item item);

	/**
	 * Returns true if the given message reflects something that would improve
	 * someone's hygiene, such as washing.
	 *
	 * @param msg the message to examine
	 * @return true if it is hygienic
	 */
	public boolean isHygienicMessage(final CMMsg msg);

	/**
	 * Adjusts the mobs age based on ellapsed time,
	 * and checks for age related diseases and
	 * wish someone a happy birthday.
	 *
	 * @param mob the mob to tick
	 * @param millisSinceLast the ellapsed time
	 */
	public void tickAging(MOB mob, long millisSinceLast);

	/**
	 * An optional system that consumes mana based on
	 * effects cast by the player.
	 *
	 * @param mob the mob to consume mana of.
	 * @param manaConsumeCounter internally manipulated counter
	 * @return remaining manaConsumeCounter
	 */
	public int tickManaConsumption(MOB mob, int manaConsumeCounter);
}
