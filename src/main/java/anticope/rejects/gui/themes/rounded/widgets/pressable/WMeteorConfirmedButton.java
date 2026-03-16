package anticope.rejects.gui.themes.rounded.widgets.pressable;

import anticope.rejects.gui.themes.rounded.MeteorRoundedGuiTheme;
import anticope.rejects.gui.themes.rounded.MeteorWidget;
import meteordevelopment.meteorclient.gui.renderer.GuiRenderer;
import meteordevelopment.meteorclient.gui.renderer.packer.GuiTexture;
import meteordevelopment.meteorclient.gui.widgets.pressable.WConfirmedButton;

public class WMeteorConfirmedButton extends WConfirmedButton implements MeteorWidget {
    public WMeteorConfirmedButton(String text, String confirmText, GuiTexture texture) {
        super(text, confirmText, texture);
    }

    @Override
    protected void onRender(GuiRenderer renderer, double mouseX, double mouseY, double delta) {
        MeteorRoundedGuiTheme theme = theme();
        double pad = pad();

        renderBackground(renderer, this, pressed, mouseOver);

        String currentText = getText();
        if (currentText != null) {
            renderer.text(currentText, x + width / 2 - textWidth / 2, y + pad, theme.textColor.get(), false);
        } else {
            double ts = theme.textHeight();
            renderer.quad(x + width / 2 - ts / 2, y + pad, ts, ts, texture, theme.textColor.get());
        }
    }
}
