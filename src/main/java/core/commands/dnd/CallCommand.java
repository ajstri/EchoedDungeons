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
import dndinfo.other.proficiencylists.Proficiencies;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import utils.Constants;
import utils.Logger;

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
        Logger.info("CALL (called by " + mre.getAuthor().getAsTag() + ")");

        MessageChannel c = mre.getChannel();
        Member dm = mre.getMember();
        String skill = args[1];

        // Define the target user
        String target;
        if (mre.getMessage().mentionsEveryone()) target = "Everyone";
        else target = mre.getMessage().getMentionedUsers().get(0).getAsMention();

        if (dm != null) {

            if (checkDMStatus(dm)) {
                // Check for initiative
                if (skill.toLowerCase().contains("init")) sendCallInitiative(c, target);

                    // Check for attack
                else if (skill.toLowerCase().contains("attack")) {
                    sendCallAttack(c, dm.getAsMention(), target);
                }

                    // Check for STR skills
                else if (skill.toLowerCase().contains("athletic")) {
                    sendCallSkill(c, Proficiencies.Skills.athletics, DNDConstants.STR_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for DEX skills
                else if (skill.toLowerCase().contains("acrobatics")) {
                    sendCallSkill(c, Proficiencies.Skills.acrobatics, DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("sleight")) {
                    sendCallSkill(c, Proficiencies.Skills.sleight, DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("stealth")) {
                    sendCallSkill(c, Proficiencies.Skills.stealth, DNDConstants.DEX_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for INT skills
                else if (skill.toLowerCase().contains("arcana")) {
                    sendCallSkill(c, Proficiencies.Skills.arcana, DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("history")) {
                    sendCallSkill(c, Proficiencies.Skills.history, DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("invest")) {
                    sendCallSkill(c, Proficiencies.Skills.investigation, DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("nature")) {
                    sendCallSkill(c, Proficiencies.Skills.nature, DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("religion")) {
                    sendCallSkill(c, Proficiencies.Skills.religion, DNDConstants.INT_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for WIS skills
                else if (skill.toLowerCase().contains("animal")) {
                    sendCallSkill(c, Proficiencies.Skills.animal_handling, DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("ins")) {
                    sendCallSkill(c, Proficiencies.Skills.insight, DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("med")) {
                    sendCallSkill(c, Proficiencies.Skills.medicine, DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("perc")) {
                    sendCallSkill(c, Proficiencies.Skills.perception, DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("survival")) {
                    sendCallSkill(c, Proficiencies.Skills.survival, DNDConstants.WIS_MOD + DNDConstants.PROF_BONUS, target);
                }

                    // Check for CHA skills
                else if (skill.toLowerCase().contains("decep")) {
                    sendCallSkill(c, Proficiencies.Skills.deception, DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("intim")) {
                    sendCallSkill(c, Proficiencies.Skills.intimidation, DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("perf")) {
                    sendCallSkill(c, Proficiencies.Skills.performance, DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
                }
                else if (skill.toLowerCase().contains("pers")) {
                    sendCallSkill(c, Proficiencies.Skills.persuasion, DNDConstants.CHA_MOD + DNDConstants.PROF_BONUS, target);
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
    public String getModule() {
        return Constants.DND;
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
    private void sendCallSkill(MessageChannel channel, String skill, String mod,  String target) {
        channel.sendMessage(target + ", Please roll for: `" + skill + "`." + "\n" +
                            "If you forgot, this is how: " + "\n" +
                            "`d20 + " + mod + "`").queue();
    }

    /**
     * Calls for attack
     * @param channel Channel to send call to
     * @param dm DM that called the attack
     * @param target Target to tag
     */
    private void sendCallAttack(MessageChannel channel, String dm, String target) {
        channel.sendMessage(target + ", Roll `1d20+[Attack Bonus]` to see if you land your attack. " +
                "If " + dm + " says you do, roll for damage").queue();
    }

    /**
     * Calls for initiative
     * @param channel Channel to send call to
     * @param target Target to tag
     */
    private void sendCallInitiative(MessageChannel channel,  String target) {
        channel.sendMessage(target + ", Please roll for: `Initiative`." + "\n" +
                "If you forgot, this is how: " + "\n" +
                "`d20 + " + DNDConstants.DEX_MOD + "`").queue();
    }
}
