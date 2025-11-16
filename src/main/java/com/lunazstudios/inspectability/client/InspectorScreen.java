package com.lunazstudios.inspectability.client;

import com.lunazstudios.inspectability.client.util.HeldItemTransformManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;
import org.lwjgl.input.Mouse;

import java.io.IOException;

public class InspectorScreen extends GuiScreen {
    private float rotationX = 0.0f;
    private float rotationY = 0.0f;
    private float rotationZ = 0.0f;
    private float offsetX = 0.0f;
    private float offsetY = 0.0f;
    private float offsetZ = -2.0f;
    private float scale = 2.0f;

    private double lastMouseX;
    private double lastMouseY;

    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 20.0f;

    private long screenOpenedTime;

    public InspectorScreen() {
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float delta) {
        if (Minecraft.getSystemTime() - screenOpenedTime < 10000) {
            this.drawString(this.fontRenderer, I18n.format("tooltip.inspectability.move"), 10, 10, 16777215);
            this.drawString(this.fontRenderer, I18n.format("tooltip.inspectability.reposition"), 10, 20, 16777215);
            this.drawString(this.fontRenderer, I18n.format("tooltip.inspectability.rotate"), 10, 30, 16777215);
            this.drawString(this.fontRenderer, I18n.format("tooltip.inspectability.zoom"), 10, 40, 16777215);
        }

        super.drawScreen(mouseX, mouseY, delta);

        HeldItemTransformManager.setTransformations(
                offsetX, offsetY, offsetZ,
                scale,
                rotationX, rotationY, rotationZ
        );
    }

    @Override
    public void initGui() {
        HeldItemTransformManager.setInspectorMode(true);
        HeldItemTransformManager.setTransformations(0.0f, 0.0f, -2.0f, 2.0f, 0.0f, 0.0f, 0.0f);

        lastMouseX = (double) (Mouse.getEventX() * this.width) / this.mc.displayWidth;
        lastMouseY = this.height - (double) (Mouse.getEventY() * this.height) / this.mc.displayHeight - 1;

        screenOpenedTime = Minecraft.getSystemTime();
    }

    @Override
    public void onGuiClosed() {
        HeldItemTransformManager.setInspectorMode(false);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        if (clickedMouseButton == 0) {
            if (lastMouseX != 0 || lastMouseY != 0) {
                rotationY += (float) ((mouseX - lastMouseX) * 0.5f);
                rotationX += (float) ((mouseY - lastMouseY) * 0.5f);
            }
        }

        if (clickedMouseButton == 1) {
            if (lastMouseX != 0 || lastMouseY != 0) {
                offsetX += (float) ((mouseX - lastMouseX) * 0.01f);
                offsetY -= (float) ((mouseY - lastMouseY) * 0.01f);
            }
        }

        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
        int scroll = Mouse.getEventDWheel();
        if (scroll == 0) return;
        scale += scroll > 0 ? 0.1f : -0.1f;
        scale = MathHelper.clamp(scale, MIN_SCALE, MAX_SCALE);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}