package io.github.vampirestudios.gadget.programs.system;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.model.ModelVillager;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import io.github.vampirestudios.gadget.api.app.Layout;
import io.github.vampirestudios.gadget.api.app.component.Button;
import io.github.vampirestudios.gadget.api.app.component.Label;
import io.github.vampirestudios.gadget.api.app.component.Text;
import io.github.vampirestudios.gadget.api.app.component.TextField;
import io.github.vampirestudios.gadget.api.app.emojie_packs.Icons;
import io.github.vampirestudios.gadget.api.task.Callback;
import io.github.vampirestudios.gadget.api.task.TaskManager;
import io.github.vampirestudios.gadget.api.utils.BankUtil;
import io.github.vampirestudios.gadget.api.utils.RenderUtil;
import io.github.vampirestudios.gadget.programs.system.task.TaskDeposit;
import io.github.vampirestudios.gadget.programs.system.task.TaskWithdraw;
import io.github.vampirestudios.gadget.util.InventoryUtil;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Objects;

import static io.github.vampirestudios.gadget.Reference.MOD_ID;

public class ApplicationBank extends SystemApplication {

    private static final ItemStack EMERALD = new ItemStack(Items.EMERALD);
    private static final ResourceLocation BANK_ASSETS = new ResourceLocation(MOD_ID, "textures/gui/bank.png");
    private static final ResourceLocation villagerTextures = new ResourceLocation("textures/entity/villager/villager.png");
    private static final ModelVillager villagerModel = new ModelVillager(0.0F);

    private Layout layoutStart;
    private Label labelTeller;
    private Text textWelcome;
    private Button btnDepositWithdraw;
    private Button btnTransfer;

    private Layout layoutMain;
    private Label labelBalance;
    private Label labelAmount;
    private TextField amountField;
    private Button btnZero;
    private Button btnClear;
    private Button buttonDeposit;
    private Button buttonWithdraw;
    private Label labelEmeraldAmount;
    private Label labelInventory;

    private int rotation;

    public ApplicationBank() {

    }

    @Override
    public void onTick() {
        super.onTick();
        rotation++;
        if (rotation >= 100) {
            rotation = 0;
        }
    }

    @Override
    public void init(@Nullable NBTTagCompound intent) {

        layoutStart = new Layout();
        layoutStart.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
        {
            GlStateManager.pushMatrix(); {
                GlStateManager.enableDepth();
                GlStateManager.translate(x + 25, y + 33, 15);
                GlStateManager.scale((float) -2.5, (float) -2.5, (float) -2.5);
                GlStateManager.rotate(-10F, 1, 0, 0);
                GlStateManager.rotate(180F, 0, 0, 1);
                GlStateManager.rotate(-20F, 0, 1, 0);
                float scaleX = (mouseX - x - 25) / (float) width;
                float scaleY = (mouseY - y - 20) / (float) height;
                mc.getTextureManager().bindTexture(villagerTextures);
                villagerModel.render(null, 0F, 0F, 0F, -70F * scaleX + 20F, 30F * scaleY, 1F);
                GlStateManager.disableDepth();
            }
            GlStateManager.popMatrix();

            mc.getTextureManager().bindTexture(BANK_ASSETS);
            RenderUtil.drawRectWithTexture(x + 46, y + 19, 0, 0, 146, 52, 146, 52);
        });

        Button buttonPrevious = new Button(5, 5, Icons.ARROW_LEFT);
        buttonPrevious.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (mouseButton == 0) {
                setCurrentLayout(layoutStart);
            }
        });

        Layout layoutAccount = new Layout(200, 140);
        layoutAccount.addComponent(buttonPrevious);

        Label labelMoney = new Label("You have: " + InventoryUtil.getItemAmount(Minecraft.getMinecraft().player, Items.EMERALD) + " emeralds", 25, 10);
        layoutAccount.addComponent(labelMoney);

        labelTeller = new Label(TextFormatting.YELLOW + "Casey The Teller", 60, 7);
        layoutStart.addComponent(labelTeller);

        textWelcome = new Text(TextFormatting.BLACK + "Hello " + Minecraft.getMinecraft().player.getName() + ", welcome to The Emerald Bank! How can I help you?", 62, 25, 125);
        layoutStart.addComponent(textWelcome);

        btnDepositWithdraw = new Button(54, 74, "View Account");
        btnDepositWithdraw.setSize(76, 20);
        btnDepositWithdraw.setClickListener((mouseX, mouseY, mouseButton) -> {
            if (mouseButton == 0) {
                setCurrentLayout(layoutAccount);
            }
        });
        btnDepositWithdraw.setToolTip("View Account", "Shows your balance");
        layoutStart.addComponent(btnDepositWithdraw);

        btnTransfer = new Button(133, 74, "Transfer");
        btnTransfer.setSize(58, 20);
        btnTransfer.setToolTip("Transfer", "Withdraw and deposit emeralds");
        btnTransfer.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                setCurrentLayout(layoutMain);
            }
        });
        layoutStart.addComponent(btnTransfer);

        setCurrentLayout(layoutStart);

        layoutMain = new Layout(120, 143) {
            @Override
            public void handleTick() {
                super.handleTick();
                int amount = InventoryUtil.getItemAmount(Minecraft.getMinecraft().player, EMERALD.getItem());
                labelEmeraldAmount.setText("x " + Integer.toString(amount));
            }
        };
        layoutMain.setBackground((gui, mc, x, y, width, height, mouseX, mouseY, windowActive) ->
        {
            Gui.drawRect(x, y, x + width, y + 40, Color.GRAY.getRGB());
            Gui.drawRect(x, y + 39, x + width, y + 40, Color.DARK_GRAY.getRGB());
            Gui.drawRect(x + 62, y + 103, x + 115, y + 138, Color.BLACK.getRGB());
            Gui.drawRect(x + 63, y + 104, x + 114, y + 113, Color.DARK_GRAY.getRGB());
            Gui.drawRect(x + 63, y + 114, x + 114, y + 137, Color.GRAY.getRGB());
            RenderUtil.renderItem(x + 65, y + 118, EMERALD, false);
        });

        labelBalance = new Label("Balance", 60, 5);
        labelBalance.setAlignment(Label.ALIGN_CENTER);
        labelBalance.setShadow(false);
        layoutMain.addComponent(labelBalance);

        labelAmount = new Label("Loading balance...", 60, 18);
        labelAmount.setAlignment(Label.ALIGN_CENTER);
        labelAmount.setScale(2);
        layoutMain.addComponent(labelAmount);

        amountField = new TextField(5, 45, 110);
        amountField.setText("0");
        amountField.setEditable(false);
        layoutMain.addComponent(amountField);

        for (int i = 0; i < 9; i++) {
            int posX = 5 + (i % 3) * 19;
            int posY = 65 + (i / 3) * 19;
            Button button = new Button(posX, posY, Integer.toString(i + 1));
            button.setSize(16, 16);
            addNumberClickListener(button, amountField, i + 1);
            layoutMain.addComponent(button);
        }

        btnZero = new Button(5, 122, "0");
        btnZero.setSize(16, 16);
        addNumberClickListener(btnZero, amountField, 0);
        layoutMain.addComponent(btnZero);

        btnClear = new Button(24, 122, "Clr");
        btnClear.setSize(35, 16);
        btnClear.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                amountField.setText("0");
            }
        });
        layoutMain.addComponent(btnClear);

        buttonDeposit = new Button(62, 65, "Deposit");
        buttonDeposit.setSize(53, 16);
        buttonDeposit.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (amountField.getText().equals("0")) {
                    return;
                }

                final int amount = Integer.parseInt(amountField.getText());
                deposit(amount, (nbt, success) ->
                {
                    if (success) {
                        int balance = Objects.requireNonNull(nbt).getInteger("balance");
                        labelAmount.setText("$" + balance);
                        amountField.setText("0");
                    }
                });
            }
        });
        layoutMain.addComponent(buttonDeposit);

        buttonWithdraw = new Button(62, 84, "Withdraw");
        buttonWithdraw.setSize(53, 16);
        buttonWithdraw.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (amountField.getText().equals("0")) {
                    return;
                }

                withdraw(Integer.parseInt(amountField.getText()), (nbt, success) ->
                {
                    if (success) {
                        int balance = Objects.requireNonNull(nbt).getInteger("balance");
                        labelAmount.setText("$" + balance);
                        amountField.setText("0");
                    }
                });
            }
        });
        layoutMain.addComponent(buttonWithdraw);

        labelEmeraldAmount = new Label("x 0", 83, 123);
        layoutMain.addComponent(labelEmeraldAmount);

        labelInventory = new Label("Wallet", 74, 105);
        labelInventory.setShadow(false);
        layoutMain.addComponent(labelInventory);

        BankUtil.getBalance((nbt, success) ->
        {
            if (success) {
                int balance = Objects.requireNonNull(nbt).getInteger("balance");
                labelAmount.setText("$" + balance);
            }
        });
    }

    private void addNumberClickListener(Button btn, final TextField field, final int number) {
        btn.setClickListener((mouseX, mouseY, mouseButton) ->
        {
            if (mouseButton == 0) {
                if (!(field.getText().equals("0") && number == 0)) {
                    if (field.getText().equals("0"))
                        field.clear();
                    field.writeText(Integer.toString(number));
                }
            }
        });
    }

    private void deposit(int amount, Callback<NBTTagCompound> callback) {
        TaskManager.sendTask(new TaskDeposit(amount).setCallback(callback));
    }

    private void withdraw(int amount, Callback<NBTTagCompound> callback) {
        TaskManager.sendTask(new TaskWithdraw(amount).setCallback(callback));
    }

    @Override
    public void load(NBTTagCompound tagCompound) {

    }

    @Override
    public void save(NBTTagCompound tagCompound) {

    }

}