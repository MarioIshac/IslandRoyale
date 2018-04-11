package me.theeninja.islandroyale;

import me.theeninja.islandroyale.gui.DrawableTexture;

public abstract class IslandPerson implements DrawableTexture {
    private int screenXPos;
    private int screenYPos;

    IslandPerson() {

    }

    public int getScreenXPos() {
        return screenXPos;
    }

    public void setScreenXPos(int screenXPos) {
        this.screenXPos = screenXPos;
    }

    public int getScreenYPos() {
        return screenYPos;
    }

    public void setScreenYPos(int screenYPos) {
        this.screenYPos = screenYPos;
    }
}
