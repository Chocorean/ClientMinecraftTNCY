package main.window;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class BottomPanel extends JPanel implements ActionListener {
    // Component
    private JTextField pathToMods;

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

        String path="";
        if (System.getProperty("os.name").contains("Windows")) {
            path = "/AppData/Roaming";
        }
        pathToMods = new JTextField(System.getProperty("user.home") + path +"/.minecraft/mods/");

        c.fill = GridBagConstraints.BOTH;
        c.gridwidth=5;
        c.gridx=0;
        c.gridy=1;
        this.add(pathToMods,c);

        JButton lookForModsPath = new JButton("...");
        lookForModsPath.setActionCommand("mod_path");
        lookForModsPath.addActionListener(this);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0,0,0,10);
        c.gridx=5;
        c.gridy=1;
        this.add(lookForModsPath,c);

        JLabel warningLabel = new JLabel("Attention! Le contenu du dossier sera supprimé.");
    warningLabel.setForeground(Color.red);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0,10,0,10);
        c.gridx=0;
        c.gridy=2;
        this.add(warningLabel,c);
        c.insets = new Insets(0,10,0,10);

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
            // locks fields while processing
        pathToMods.setEnabled(false);

        if (actionEvent.getActionCommand().equals("mod_path")) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
                pathToMods.setText(fc.getSelectedFile().getAbsolutePath());
        }
        else if (actionEvent.getActionCommand().equals("update")) {
                // update mods
            String move;
            if (System.getProperty("os.name").contains("Windows")) {
                move="move ";
            } else { move="mv "; }
            String path = pathToMods.getText();
            try {
                // downloading mod list
                URL mod_file = new URL("https://raw.githubusercontent.com/Chocorean/ClientMinecraftTNCY/master/src/resource/mods.txt");
                ReadableByteChannel mod_rbc = Channels.newChannel(mod_file.openStream());
                FileOutputStream mod_fos = new FileOutputStream("mods.txt");
                mod_fos.getChannel().transferFrom(mod_rbc, 0, Long.MAX_VALUE);

                // reading file
                BufferedReader br = new BufferedReader(new FileReader("mods.txt"));
                String line = br.readLine();
                while (line != null) {
                    // extract mod name
                    String name = line.split("/")[line.split("/").length-1];
                    // download mod
                    URL file = new URL(line);
                    ReadableByteChannel rbc = Channels.newChannel(file.openStream());
                    FileOutputStream fos = new FileOutputStream(name);
                    fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
                    this.execute(move + name + " " + pathToMods.getText() + "/");
                    // next mod
                    line = br.readLine();
                }
                // rm mod list
                this.execute("rm mods.txt");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // unlocks
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
