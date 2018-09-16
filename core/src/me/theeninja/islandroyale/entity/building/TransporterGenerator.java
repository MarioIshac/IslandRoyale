package me.theeninja.islandroyale.entity.building;

import me.theeninja.islandroyale.ai.Player;
import me.theeninja.islandroyale.entity.controllable.Transporter;
import me.theeninja.islandroyale.entity.controllable.TransporterType;
import me.theeninja.islandroyale.gui.screens.Match;

public class TransporterGenerator extends OffenseBuilding<TransporterGenerator, TransporterGeneratorType, Transporter, TransporterType> {
    public TransporterGenerator(TransporterGeneratorType entityType, Player owner, float x, float y, Match match) {
        super(entityType, owner, x, y, match);
    }

    @Override
    protected TransporterGenerator getReference() {
        return this;
    }

    @Override
    Transporter newGenericSpecificEntity(TransporterType entityType, Player owner, float x, float y, Match match) {
        return new Transporter(entityType, owner, x, y, match);
    }
}
