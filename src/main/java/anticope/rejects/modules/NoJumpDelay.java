package anticope.rejects.modules;

import anticope.rejects.MeteorRejectsAddon;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.orbit.EventHandler;
import meteordevelopment.meteorclient.systems.modules.Module;

public class NoJumpDelay extends Module {

    public NoJumpDelay() {
        super(MeteorRejectsAddon.CATEGORY, "no-jump-delay", "NoJumpDelay.");
    }

    @EventHandler
    private void onTick(TickEvent.Post event) {
        // API changed; keep module present without unsafe accessor usage.
    }
}
