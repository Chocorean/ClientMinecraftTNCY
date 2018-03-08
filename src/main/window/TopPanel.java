package main.window;

import javax.swing.*;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import java.awt.*;
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
            URL file = new URL("https://raw.githubusercontent.com/Chocorean/ClientMinecraftTNCY/master/src/resource/html/changelog.html");
            ReadableByteChannel rbc = Channels.newChannel(file.openStream());
            FileOutputStream fos = new FileOutputStream("changelog.html");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
            file = new URL("https://raw.githubusercontent.com/Chocorean/ClientMinecraftTNCY/master/src/resource/html/stylesheet.css");
            rbc = Channels.newChannel(file.openStream());
            fos = new FileOutputStream("stylesheet.css");
            fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);

            webPage.setPage("file:changelog.html");

            // rm files
            BottomPanel.execute("rm changelog.html");
            BottomPanel.execute("rm stylesheet.css");
        }catch (IOException e) {
            webPage.setContentType("text/html");
            webPage.setText("<html><h1>Erreur</h1>Impossible d'ouvrir le fichier.</html>");
        }

        webContainer = new JScrollPane(webPage);
        webContainer.setPreferredSize(new Dimension(800,400));
        this.add(webContainer);
        this.add(new JLabel(" ")); // bottom space


    }
}
