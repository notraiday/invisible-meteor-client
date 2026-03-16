/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.mixin;

import meteordevelopment.meteorclient.gui.GuiThemes;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.render.MultiplayerTabAddons;
import meteordevelopment.meteorclient.systems.modules.player.NameProtect;
import meteordevelopment.meteorclient.systems.proxies.Proxies;
import meteordevelopment.meteorclient.systems.proxies.Proxy;
import meteordevelopment.meteorclient.utils.render.color.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static meteordevelopment.meteorclient.MeteorClient.mc;

@Mixin(MultiplayerScreen.class)
public abstract class MultiplayerScreenMixin extends Screen {
    @Unique
    private int textColor1;
    @Unique
    private int textColor2;

    @Unique
    private String loggedInAs;
    @Unique
    private int loggedInAsLength;

    @Unique
    private ButtonWidget accounts;

    @Unique
    private ButtonWidget proxies;

    public MultiplayerScreenMixin(Text title) {
        super(title);
    }

    @Inject(method = "refreshWidgetPositions", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        if (!Modules.get().get(MultiplayerTabAddons.class).isActive()) {
            return;
        }
        textColor1 = Color.fromRGBA(255, 255, 255, 255);
        textColor2 = Color.fromRGBA(175, 175, 175, 255);

        loggedInAs = "Logged in as ";
        loggedInAsLength = textRenderer.getWidth(loggedInAs);

        if (accounts == null) {
            accounts = addDrawableChild(
                new ButtonWidget.Builder(Text.literal("Accounts"), button -> client.setScreen(GuiThemes.get().accountsScreen()))
                    .size(75, 20)
                    .build()
            );
        }
        accounts.setPosition(this.width - 75 - 3, 3);

        if (proxies == null) {
            proxies = addDrawableChild(
                    new ButtonWidget.Builder(Text.literal("Proxies"), button -> client.setScreen(GuiThemes.get().proxiesScreen()))
                        .size(75, 20)
                        .build()
                );
        }
        proxies.setPosition(this.width - 75 - 3 - 75 - 2, 3);
    }

    // No render inject here: MultiplayerScreen no longer has a stable render override target
    // across the updated mappings. Keep only widget-position hook to avoid startup crashes.
}
