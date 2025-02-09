/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */
package meteordevelopment.meteorclient.systems.modules.render;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
public class MultiplayerTabAddons extends Module {
    // private final SettingGroup sgGeneral = settings.getDefaultGroup();
    // private final Setting<Boolean> smooth = sgGeneral.add(new
    // BoolSetting.Builder()
    // .name("proxies")
    // .description("Smooth transition.")
    // .defaultValue(true)
    // .build()
    // );
    public MultiplayerTabAddons() {
        super(Categories.Render, "multiplayerAddons", "Proxies and Accounts buttons in Multiplayer tab");
    }
}
