/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.utils.misc.input;

import com.mojang.blaze3d.platform.InputConstants;
import meteordevelopment.meteorclient.MeteorClient;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.input.KeyEvent;

public class KeyBinds {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(MeteorClient.identifier("meteor-client"));

    public static KeyMapping OPEN_GUI = new KeyMapping("key.meteor-client.open-gui", InputConstants.Type.KEYBOARD, InputConstants.KEY_INSERT, CATEGORY);
    public static KeyMapping OPEN_COMMANDS = new KeyMapping("key.meteor-client.open-commands", InputConstants.Type.KEYBOARD, InputConstants.KEY_PERIOD, CATEGORY);

    private KeyBinds() {
    }

    // The GUI opens on Ctrl + Shift + Insert, KeyMapping has no modifiers so they are checked here
    public static boolean matchesOpenGui(KeyEvent input) {
        return OPEN_GUI.matches(input) && Input.modsMatch(input.modifiers(), InputConstants.MOD_CONTROL, InputConstants.MOD_SHIFT);
    }

    public static KeyMapping[] apply(KeyMapping[] binds) {
        // Add key binding
        KeyMapping[] newBinds = new KeyMapping[binds.length + 2];

        System.arraycopy(binds, 0, newBinds, 0, binds.length);
        newBinds[binds.length] = OPEN_GUI;
        newBinds[binds.length + 1] = OPEN_COMMANDS;

        // return newBinds;
        return binds;
    }
}
