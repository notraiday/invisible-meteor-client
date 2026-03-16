package anticope.rejects.modules;

import anticope.rejects.MeteorRejectsAddon;
import meteordevelopment.meteorclient.settings.BoolSetting;
import meteordevelopment.meteorclient.settings.ColorSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;

public class SkeletonESP extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<SettingColor> skeletonColorSetting = sgGeneral.add(new ColorSetting.Builder()
        .name("players-color")
        .description("The other player's color.")
        .defaultValue(new SettingColor(255, 255, 255))
        .build()
    );

    public final Setting<Boolean> distance = sgGeneral.add(new BoolSetting.Builder()
        .name("distance-colors")
        .description("Changes the color of skeletons depending on distance.")
        .defaultValue(false)
        .build()
    );

    public SkeletonESP() {
        super(MeteorRejectsAddon.CATEGORY, "skeleton-esp", "Skeleton ESP renderer (compatibility port in progress).");
    }
}