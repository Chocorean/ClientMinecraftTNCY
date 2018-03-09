package io.chocorean.MinecraftUpdater.window;

import javax.swing.*;

public class MyWindow extends JFrame {
    // Component list
    private TopPanel topFrame;
    private JSeparator sep;
    private BottomPanel bottomFrame;

    public MyWindow() {
        super();
        this.setTitle("Updater Client Minecraft TNCY");
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.buildWindow();
        this.pack();
        this.setResizable(false);
        this.setVisible(true);
    }

    private void buildWindow() {
        topFrame = new TopPanel();
        sep = new JSeparator();
        sep.setOrientation(JSeparator.HORIZONTAL);
        bottomFrame = new BottomPanel();

        this.add(topFrame);
        this.add(sep);
        this.add(bottomFrame);
    }
}
