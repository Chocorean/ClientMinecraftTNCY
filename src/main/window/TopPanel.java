package main.window;

import javax.swing.*;
import java.io.IOException;

public class TopPanel extends JPanel {
    // Component list
    private JEditorPane webPage;
    private JScrollPane webContainer;

    TopPanel() {
        super();
        webPage = new JEditorPane();
        webPage.setEditable(false);

        try {
            webPage.setPage("file:src/resource/html/changelog.html");
        }catch (IOException e) {
            webPage.setContentType("text/html");
            webPage.setText("<html><h1>Erreur</h1>Impossible d'ouvrir le fichier.</html>");
        }

        webContainer = new JScrollPane(webPage);
        this.add(webContainer);
    }
}
