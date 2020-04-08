package dndinfo.races;

import java.util.List;

public abstract class Race {

    // TODO: Implement Race Framework
    public abstract String getName();
    public abstract String getWikiLink();

    public abstract String getAge();
    public abstract String getSize();
    public abstract String getAlignment();
    public abstract int getSpeed();

    public abstract List<String> getSkillProficiencies();
    public abstract List<String> getToolProficiencies();
    public abstract List<String> getLanguages();

    public abstract String getAbilityScoreImprovement();
    public abstract List<String> getSubRaces();

}
