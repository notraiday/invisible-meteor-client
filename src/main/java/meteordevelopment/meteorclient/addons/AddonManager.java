/*
 * This file is part of the Meteor Client distribution (https://github.com/MeteorDevelopment/meteor-client).
 * Copyright (c) Meteor Development.
 */

package meteordevelopment.meteorclient.addons;

import meteordevelopment.meteorclient.MeteorClient;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.EntrypointContainer;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.fabricmc.loader.api.metadata.Person;

import anticope.rejects.MeteorRejectsAddon;
import zgoly.meteorist.Meteorist;

import java.util.ArrayList;
import java.util.List;

public class AddonManager {
    public static final List<MeteorAddon> ADDONS = new ArrayList<>();
// TODO RAIDAY
    public static void init() {
        // Meteor pseudo addon
        {
            MeteorClient.ADDON = new MeteorAddon() {
                @Override
                public void onInitialize() {}

                @Override
                public String getPackage() {
                    return "meteordevelopment.meteorclient";
                }

                @Override
                public String getWebsite() {
                    return "https://modrinth.com/mod/mod-volume-options";
                }

                @Override
                public GithubRepo getRepo() {
                    return new GithubRepo("pitan76", "mvo76");
                }

                @Override
                public String getCommit() {
                    String commit = MeteorClient.MOD_META.getCustomValue(MeteorClient.MOD_ID + ":commit").getAsString();
                    return commit.isEmpty() ? null : commit;
                }
            };

            ModMetadata metadata = FabricLoader.getInstance().getModContainer(MeteorClient.MOD_ID).get().getMetadata();

            MeteorClient.ADDON.name = metadata.getName();
            MeteorClient.ADDON.authors = new String[metadata.getAuthors().size()];
            if (metadata.containsCustomValue(MeteorClient.MOD_ID + ":color")) {
                MeteorClient.ADDON.color.parse(metadata.getCustomValue(MeteorClient.MOD_ID + ":color").getAsString());
            }

            int i = 0;
            for (Person author : metadata.getAuthors()) {
                MeteorClient.ADDON.authors[i++] = author.getName();
            }

            ADDONS.add(MeteorClient.ADDON);
        }

        // Addons
        for (EntrypointContainer<MeteorAddon> entrypoint : FabricLoader.getInstance().getEntrypointContainers("meteor", MeteorAddon.class)) {
            ModMetadata metadata = entrypoint.getProvider().getMetadata();
            MeteorAddon addon;
            try {
                addon = entrypoint.getEntrypoint();
            } catch (Throwable throwable) {
                throw new RuntimeException("Exception during addon init \"%s\".".formatted(metadata.getName()), throwable);
            }

            addon.name = metadata.getName();

            if (metadata.getAuthors().isEmpty()) throw new RuntimeException("Addon \"%s\" requires at least 1 author to be defined in it's fabric.mod.json. See https://fabricmc.net/wiki/documentation:fabric_mod_json_spec".formatted(addon.name));
            addon.authors = new String[metadata.getAuthors().size()];

            if (metadata.containsCustomValue(MeteorClient.MOD_ID + ":color")) {
                addon.color.parse(metadata.getCustomValue(MeteorClient.MOD_ID + ":color").getAsString());
            }

            int i = 0;
            for (Person author : metadata.getAuthors()) {
                addon.authors[i++] = author.getName();
            }

            ADDONS.add(addon);
        }

        // fully simulate the rejects addon without actual mode
        MeteorRejectsAddon rejects = new MeteorRejectsAddon();
        rejects.name = "Meteor Rejects";
        rejects.authors = new String[]{"AntiCope"};
        ADDONS.add(rejects);

        // fully simulate the meteorist addon without actual mode
        Meteorist meteorist = new Meteorist();
        meteorist.name = "Meteorist";
        meteorist.authors = new String[]{"zgoly"};
        ADDONS.add(meteorist);


    }
}
