package me.theeninja.islandroyale.entity.analyzers;

import com.badlogic.gdx.utils.IntArray;
import me.theeninja.islandroyale.MatchMap;

public class EntityAnalyzer {
    private IntArray xAnalysisLocations = new IntArray();
    private IntArray yAnalysisLocations = new IntArray();

    private final MatchMap matchMap;

    public EntityAnalyzer(MatchMap matchMap) {
        this.matchMap = matchMap;
    }

    public MatchMap getMatchMap() {
        return matchMap;
    }

    public void updateAnalysisLocations() {

    }
}
