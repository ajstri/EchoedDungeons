/*
Copyright 2020 EchoedAJ

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */
package core.commands.dnd;

import core.Main;
import core.commands.Command;
import dndinfo.other.DNDConstants;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *  CallCommand class of the EchoedDungeons project
 *
 *  All methods are explained in {@link Command}
 *
 * @author EchoedAJ
 * @since April 2020
 */
public class CallCommand extends Command {

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        MessageChannel c = mre.getChannel();
        Member dm = mre.getMember();
        String skill = args[1];
        User target = mre.getMessage().getMentionedUsers().get(0);

        if (dm != null) {

            if (checkDMStatus(dm)) {
                // Check for initiative
                if (skill.toLowerCase().contains("init")) sendCallInitiative(c, target);

                    // Check for attack
                else if (skill.toLowerCase().contains("attack")) {
                    sendCallAttack(c, dm.getAsMention(), target);
                }

                    // Check for STR skills
                else if (skill.toLowerCase().contains("athletics")) {
                    sendCallSkill(c, "Athletics", DNDConstants.STR_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for DEX skills
                else if (skill.toLowerCase().contains("acrobatics")) {
                    sendCallSkill(c, "Acrobatics", DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("sleight")) {
                    sendCallSkill(c, "Sleight of Hand", DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("stealth")) {
                    sendCallSkill(c, "Stealth", DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for INT skills
                else if (skill.toLowerCase().contains("arcana")) {
                    sendCallSkill(c, "Arcana", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("history")) {
                    sendCallSkill(c, "History", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("invest")) {
                    sendCallSkill(c, "Investigation", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("nature")) {
                    sendCallSkill(c, "Nature", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("religion")) {
                    sendCallSkill(c, "Religion", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for WIS skills
                else if (skill.toLowerCase().contains("animal")) {
                    sendCallSkill(c, "Animal Handling", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("ins")) {
                    sendCallSkill(c, "Insight", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("med")) {
                    sendCallSkill(c, "Medicine", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("perc")) {
                    sendCallSkill(c, "Perception", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("survival")) {
                    sendCallSkill(c, "Survival", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for CHA skills
                else if (skill.toLowerCase().contains("decep")) {
                    sendCallSkill(c, "Deception", DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("intim")) {
                    sendCallSkill(c, "Intimidation", DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("perf")) {
                    sendCallSkill(c, "Performance", DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("pers")) {
                    sendCallSkill(c, "Persuasion", DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for saving throws
                else if (skill.toLowerCase().contains("str")) {
                    sendCallSkill(c, "Strength Saving Throw", DNDConstants.STR_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("dex")) {
                    sendCallSkill(c, "Dexterity Saving Throw", DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("con")) {
                    sendCallSkill(c, "Constitution Saving Throw", DNDConstants.CON_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("int")) {
                    sendCallSkill(c, "Intelligence Saving Throw", DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("wis")) {
                    sendCallSkill(c, "Wisdom Saving Throw", DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("cha")) {
                    sendCallSkill(c, "Charisma Saving Throw", DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Skill does not exist
                else {
                    c.sendMessage("Skill **`" + skill + "`** does not exist.").queue();
                }

                // Delete the call
                mre.getMessage().delete().queue();
            }
            else {
                // Not DM
                c.sendMessage(dm.getAsMention() + ", You do not have permission to call rolls!").queue();
            }
        }
        else {
            // Author is null
            mre.getChannel().sendMessage("Uh oh. Something went wrong.").queue();
        }
    }

    @Override
    public List<String> getAliases() {
        return Arrays.asList("call", "c");
    }

    @Override
    public boolean isDND() {
        return true;
    }

    @Override
    public String getDescription() {
        return "Calls for players to roll for something specific.";
    }

    @Override
    public String getName() {
        return "Call Command";
    }

    @Override
    public List<String> getUsage() {
        return Collections.singletonList(
                "`" + Main.config.getPrefix() + "call [arg] @Target`\n" +
                "[arg] can be `initiative`, `attack`, or a `skill`.\n" +
                "@Target must be a tag in order to work correctly."
        );
    }

    @Override
    public boolean getDefaultPermission() {
        return false;
    }

    /**
     * Check if author has a role called DM.
     * @param dm Member to check
     * @return true if Member is a DM, false if not.
     */
    private boolean checkDMStatus(Member dm) {
        List<Role> roles = dm.getRoles();

        // for each Role, see if it has "dm"
        for (Role r : roles) {
            if (r.getName().toLowerCase().contains("dm")) {
                return true; // found
            }
        }
        return false;
    }

    /**
     * Calls a skill
     * @param channel Channel to send call to
     * @param skill Skill to call
     * @param mod Modifier for the skill
     * @param target Target to tag
     */
    private void sendCallSkill(MessageChannel channel, String skill, String mod,  User target) {
        channel.sendMessage(target.getAsMention() + ", Please roll for: `" + skill + "`." + "\n" +
                            "If you forgot, this is how: " + "\n" +
                            "`d20 + " + mod + "`").queue();
    }

    /**
     * Calls for attack
     * @param channel Channel to send call to
     * @param dm DM that called the attack
     * @param target Target to tag
     */
    private void sendCallAttack(MessageChannel channel, String dm, User target) {
        channel.sendMessage(target.getAsMention() + ", Roll `d20 + Attack Bonus` to see if you land your attack. " +
                "If " + dm + " says you do, roll for damage").queue();
    }

    /**
     * Calls for initiative
     * @param channel Channel to send call to
     * @param target Target to tag
     */
    private void sendCallInitiative(MessageChannel channel,  User target) {
        channel.sendMessage(target.getAsMention() + ", Please roll for: `Initiative`." + "\n" +
                "If you forgot, this is how: " + "\n" +
                "`d20 + " + DNDConstants.DEX_MOD + "`").queue();
    }
}
