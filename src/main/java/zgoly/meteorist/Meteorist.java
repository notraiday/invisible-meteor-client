package zgoly.meteorist;

import meteordevelopment.meteorclient.addons.GithubRepo;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.hud.HudGroup;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zgoly.meteorist.commands.DataCommand;
import zgoly.meteorist.commands.InstructionsCommand;
import zgoly.meteorist.commands.InteractCommand;
import zgoly.meteorist.commands.PlayersInfoCommand;
import zgoly.meteorist.hud.TextPresets;
import zgoly.meteorist.modules.*;
import zgoly.meteorist.modules.autocrafter.AutoCrafter;
import zgoly.meteorist.modules.autologin.AutoLogin;
import zgoly.meteorist.modules.autotrade.AutoTrade;
import zgoly.meteorist.modules.instructions.Instructions;
import zgoly.meteorist.modules.placer.Placer;
import zgoly.meteorist.modules.rangeactions.RangeActions;
import zgoly.meteorist.modules.slotclick.SlotClick;
import zgoly.meteorist.settings.StringPairSetting;
import zgoly.meteorist.utils.misc.MeteoristStarscript;

public class Meteorist extends MeteorAddon {
    public static final Logger LOG = LoggerFactory.getLogger("NOP");
    public static final Category CATEGORY = new Category("Meteorist", Items.FIRE_CHARGE.getDefaultStack());
    public static final HudGroup HUD_GROUP = new HudGroup("Meteorist");
    public static String MOD_ID = "meteorist";
    public static GuiTexture ARROW_UP;
    public static GuiTexture ARROW_DOWN;
    public static GuiTexture COPY;
    public static GuiTexture EYE;

    public static Identifier identifier(String path) {
        return Identifier.of(MOD_ID, path);
    }

    @Override
    public void onInitialize() {
        // Factories
        StringPairSetting.register();

        // Modules
        Modules.get().add(new AutoCrafter());
        Modules.get().add(new AutoFeed());
        Modules.get().add(new AutoFix());
        Modules.get().add(new AutoHeal());
        Modules.get().add(new AutoInteract());
        Modules.get().add(new AutoLeave());
        Modules.get().add(new AutoLogin());
        Modules.get().add(new AutoMud());
        Modules.get().add(new AutoSleep());
        Modules.get().add(new AutoSneak());
        Modules.get().add(new AutoTrade());
        Modules.get().add(new BoatControl());
        Modules.get().add(new DisconnectSound());
        Modules.get().add(new DmSpam());
        Modules.get().add(new DoubleDoorsInteract());
        Modules.get().add(new EntityInteract());
        Modules.get().add(new Grid());
        Modules.get().add(new Instructions());
        Modules.get().add(new ItemSucker());
        Modules.get().add(new JumpFlight());
        Modules.get().add(new JumpJump());
        Modules.get().add(new Placer());
        Modules.get().add(new SlotClick());
        Modules.get().add(new RangeActions());
        Modules.get().add(new ZAimbot());
        Modules.get().add(new ZKillaura());
        Modules.get().add(new ZoomPlus());

        // Commands
        Commands.add(new DataCommand());
        Commands.add(new InstructionsCommand());
        Commands.add(new InteractCommand());
        Commands.add(new PlayersInfoCommand());

        // HUD text presets
        MeteoristStarscript.init();
        Hud.get().register(TextPresets.INFO);

        // Icons
        ARROW_UP = GuiRenderer.addTexture(identifier("textures/icons/gui/arrow_up.png"));
        ARROW_DOWN = GuiRenderer.addTexture(identifier("textures/icons/gui/arrow_down.png"));
        COPY = GuiRenderer.addTexture(identifier("textures/icons/gui/copy.png"));
        EYE = GuiRenderer.addTexture(identifier("textures/icons/gui/eye.png"));
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getWebsite() {
        return "https://github.com/Zgoly/Meteorist";
    }

    @Override
    public String getPackage() {
        return getRepo().owner() + "." + getRepo().name();
    }

    @Override
    public GithubRepo getRepo() {
        return new GithubRepo("zgoly", "meteorist");
    }

    @Override
    public String getCommit() {
        // Embedded builds may not ship as standalone "meteorist" mod metadata.
        ModContainer container = FabricLoader.getInstance().getModContainer(MOD_ID).orElse(null);
        if (container == null) return null;

        String commit = container.getMetadata().getCustomValue(MOD_ID + ":commit").getAsString();
        return commit.isEmpty() ? null : commit;
    }
}
