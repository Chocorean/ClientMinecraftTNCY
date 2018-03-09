package io.chocorean.MinecraftUpdater.window;


import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class TopPanel extends JPanel {
    // Component list
    private JEditorPane webPage;
    private JScrollPane webContainer;

    TopPanel() {
        super();
        webPage = new JEditorPane();
        webPage.setEditable(false);
        webPage.addHyperlinkListener(new HyperlinkListener(){
            public void hyperlinkUpdate(HyperlinkEvent e){
                if (e.getEventType() == (HyperlinkEvent.EventType.ACTIVATED)) {
                    try{
                        Desktop.getDesktop().browse(e.getURL().toURI());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });



        try {
                // downloading files
            URL file = new URL("https://raw.githubusercontent.com/Chocorean/ClientMinecraftTNCY/master/src/resource/changelog/changelog.changelog");
            ReadableByteChannel rbc = Channels.newChannel(file.openStream());
            FileOutputStream fos = new FileOutputStream("changelog.changelog");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            file = new URL("https://raw.githubusercontent.com/Chocorean/ClientMinecraftTNCY/master/src/resource/changelog/styles.css");
            rbc = Channels.newChannel(file.openStream());
            fos = new FileOutputStream("stylesheet.css");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            webPage.setPage("file:changelog.changelog");

            // rm files
            new File("changelog.changelog").delete();
            new File("stylesheet.css").delete();
        }catch (IOException e) {
            webPage.setContentType("text/changelog");
            webPage.setText("<changelog><h1>Erreur</h1>Impossible d'ouvrir le fichier.</changelog>");
        }

        webContainer = new JScrollPane(webPage);
        webContainer.setPreferredSize(new Dimension(800,400));
        this.add(webContainer);
        this.add(new JLabel(" ")); // bottom space


    }
}
