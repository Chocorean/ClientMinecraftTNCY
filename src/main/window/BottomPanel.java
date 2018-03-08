package main.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class BottomPanel extends JPanel implements ActionListener {
    // Components
    private JTextField pathToMods;
    private JTextField pathToApp;

    BottomPanel() {
        super();
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel pathToModsLabel = new JLabel("Chemin du dossier des mods");
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10,10,0,10);
        c.gridx=0;
        c.gridy=0;
        this.add(pathToModsLabel,c);
        c.insets = new Insets(0,10,0,10);

        pathToMods = new JTextField(System.getProperty("user.home") + "/.minecraft/mods/");
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth=5;
        c.gridx=0;
        c.gridy=1;
        this.add(pathToMods,c);

        JButton lookForModsPath = new JButton("...");
        lookForModsPath.setActionCommand("mod_path");
        lookForModsPath.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.gridx=5;
        c.gridy=1;
        this.add(lookForModsPath,c);

        JLabel pathToAppLabel = new JLabel("Chemin de l'exécutable à lancer");
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10,10,0,10);
        c.gridx=0;
        c.gridy=2;
        this.add(pathToAppLabel,c);
        c.insets = new Insets(0, 10, 0, 10);

        pathToApp = new JTextField();
        pathToApp.setToolTipText("Laisser vide pour ne rien exécuter après la mise à jour.");
        c.fill = GridBagConstraints.BOTH;
        c.gridwidth = 5;
        c.gridx=0;
        c.gridy=3;
        this.add(pathToApp,c);

        JButton lookForExePath = new JButton("...");
        lookForExePath.setActionCommand("exe_path");
        lookForExePath.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.gridx=5;
        c.gridy=3;
        this.add(lookForExePath,c);

        JButton runButton = new JButton("Mettre à jour");
        runButton.setActionCommand("update");
        runButton.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(10,10,10,10);  //top padding
        c.gridwidth=3;
        c.gridx=1;
        c.gridy=4;
        this.add(runButton,c);
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        pathToApp.setEnabled(false);
        pathToMods.setEnabled(false);

        if (actionEvent.getActionCommand().equals("mod_path")) {
            // open browser
        } else if (actionEvent.getActionCommand().equals("exe_path")) {
            // open browser
        } else if (actionEvent.getActionCommand().equals("update")){
            // update mods

            // run exe
            if (pathToApp.getText().contains(".exe")) {
                this.execute(pathToApp.getText());
            } else if (pathToApp.getText().contains(".jar")) {
                this.execute("java -jar "+pathToApp.getText());
            } else {
                // Alert : cannot run specified file
            }
        }
        pathToApp.setEnabled(true);
        pathToMods.setEnabled(true);
    }

    private void execute(String cmd) {
        Runtime rt = Runtime.getRuntime();
        try {
            Process pr = rt.exec(cmd);
        } catch (IOException e) {
            // Alert
            e.printStackTrace();
        }
    }
}
