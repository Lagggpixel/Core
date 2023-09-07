package me.lagggpixel.core.modules;

import javax.annotation.Nonnull;

public abstract class Module {

    public @Nonnull abstract String getId();

    public abstract void initialize();

    public abstract void registerCommands();

    public abstract void registerListeners();

}
