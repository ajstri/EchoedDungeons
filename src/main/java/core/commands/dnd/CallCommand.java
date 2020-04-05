package core.commands.dnd;

import core.commands.Command;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Arrays;
import java.util.List;

public class CallCommand extends Command {

    private final String STR_MOD = "Strength Modifier";
    private final String DEX_MOD = "Dexterity Modifier";
    private final String CON_MOD = "Constitution Modifier";
    private final String INT_MOD = "Intelligence Modifier";
    private final String WIS_MOD = "Wisdom Modifier";
    private final String CHA_MOD = "Charisma Modifier";
    private final String PROF_BONUS =" + Proficiency Bonus, if applicable";

    @Override
    protected void onCommand(MessageReceivedEvent mre, String[] args) {
        MessageChannel c = mre.getChannel();
        Member dm = mre.getMember();
        String skill = args[1];
        User target = mre.getMessage().getMentionedUsers().get(0);

        if (checkDMStatus(dm)) {
            // Check for initiative
            if (skill.toLowerCase().contains("init")) sendCallInitiative(c, target);

                // Check for attack
            else if (skill.toLowerCase().contains("attack")) sendCallAttack(c, dm.getAsMention(), target);

                // Check for STR skills
            else if (skill.toLowerCase().contains("athletics"))
                sendCallSkill(c, "Athletics", STR_MOD + PROF_BONUS, target);

                // Check for DEX skills
            else if (skill.toLowerCase().contains("acrobatics")) sendCallSkill(c, "Acrobatics", DEX_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("sleight")) sendCallSkill(c, "Sleight of Hand", DEX_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("stealth")) sendCallSkill(c, "Stealth", DEX_MOD + PROF_BONUS, target);

                // Check for INT skills
            else if (skill.toLowerCase().contains("arcana")) sendCallSkill(c, "Arcana", INT_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("history")) sendCallSkill(c, "History", INT_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("invest")) sendCallSkill(c, "Investigation", INT_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("nature")) sendCallSkill(c, "Nature", INT_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("religion")) sendCallSkill(c, "Religion", INT_MOD + PROF_BONUS, target);

                // Check for WIS skills
            else if (skill.toLowerCase().contains("animal")) sendCallSkill(c, "Animal Handling", WIS_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("ins")) sendCallSkill(c, "Insight", WIS_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("med")) sendCallSkill(c, "Medicine", WIS_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("perc")) sendCallSkill(c, "Perception", WIS_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("survival")) sendCallSkill(c, "Survival", WIS_MOD + PROF_BONUS, target);

                // Check for CHA skills
            else if (skill.toLowerCase().contains("decep")) sendCallSkill(c, "Deception", CHA_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("intim")) sendCallSkill(c, "Intimidation", CHA_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("perf")) sendCallSkill(c, "Performance", CHA_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("pers")) sendCallSkill(c, "Persuasion", CHA_MOD + PROF_BONUS, target);

                // Check for saving throws
            else if (skill.toLowerCase().contains("str")) sendCallSkill(c, "Strength Saving Throw", STR_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("dex")) sendCallSkill(c, "Dexterity Saving Throw", DEX_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("con")) sendCallSkill(c, "Constitution Saving Throw", CON_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("int")) sendCallSkill(c, "Intelligence Saving Throw", INT_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("wis")) sendCallSkill(c, "Wisdom Saving Throw", WIS_MOD + PROF_BONUS, target);
            else if (skill.toLowerCase().contains("cha")) sendCallSkill(c, "Charisma Saving Throw", CHA_MOD + PROF_BONUS, target);

            else c.sendMessage("Skill **`" + skill + "`** does not exist.").queue();

            mre.getMessage().delete().queue();
        }
        else {
            c.sendMessage(dm.getAsMention() + ", You do not have permission to call rolls!").queue();
        }
    }

    @Override
    protected List<String> getAliases() {
        return Arrays.asList("call", "c");
    }

    @Override
    protected boolean isDND() {
        return true;
    }

    @Override
    protected String getDescription() {
        return "Calls for players to roll for something specific.";
    }

    @Override
    protected String getName() {
        return "Call Command";
    }

    @Override
    protected List<String> getUsage() {
        return null;
    }

    @Override
    protected boolean getDefaultPermission() {
        return false;
    }

    private boolean checkDMStatus(Member dm) {
        List<Role> roles = dm.getRoles();

        for (Role r : roles) {
            if (r.getName().toLowerCase().contains("dm")) {
                return true;
            }
        }
        return false;
    }

    private void sendCallSkill(MessageChannel channel, String skill, String mod,  User target) {
        channel.sendMessage(target.getAsMention() + ", Please roll for: `" + skill + "`." + "\n" +
                            "If you forgot, this is how: " + "\n" +
                            "`d20 + " + mod + "`").queue();
    }

    private void sendCallAttack(MessageChannel channel, String dm, User target) {
        channel.sendMessage(target.getAsMention() + ", Roll `d20 + Attack Bonus` to see if you land your attack. " +
                "If " + dm + " says you do, roll for damage").queue();
    }

    private void sendCallInitiative(MessageChannel channel,  User target) {
        channel.sendMessage(target.getAsMention() + ", Please roll for: `Initiative`." + "\n" +
                "If you forgot, this is how: " + "\n" +
                "`d20 + " + DEX_MOD + "`").queue();
    }
}
