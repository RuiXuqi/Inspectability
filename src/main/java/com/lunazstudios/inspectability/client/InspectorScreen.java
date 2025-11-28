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

    private int lastMouseX;
    private int lastMouseY;

    private static final float MIN_SCALE = 0.1f;
    private static final float MAX_SCALE = 20.0f;

    private static final float ROTATION_SENSITIVITY = 0.5f;
    private static final float MOVE_SENSITIVITY = 0.01f;

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

        screenOpenedTime = Minecraft.getSystemTime();
    }

    @Override
    public void onGuiClosed() {
        HeldItemTransformManager.setInspectorMode(false);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        lastMouseX = mouseX;
        lastMouseY = mouseY;
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        int dx = mouseX - lastMouseX;
        int dy = mouseY - lastMouseY;

        if (clickedMouseButton == 0) {
            rotationY += dx * ROTATION_SENSITIVITY;
            rotationX += dy * ROTATION_SENSITIVITY;
        } else if (clickedMouseButton == 1) {
            offsetX += dx * MOVE_SENSITIVITY;
            offsetY -= dy * MOVE_SENSITIVITY;
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