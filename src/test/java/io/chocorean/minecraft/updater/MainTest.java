package io.chocorean.minecraft.updater;

import javafx.stage.Stage;
import org.junit.After;
import org.junit.Test;
import org.testfx.framework.junit.ApplicationTest;

public class MainTest extends ApplicationTest {

    @Override
    public void start (Stage stage) throws Exception {
        /*Parent mainNode = FXMLLoader.load(Main.class.getResource("/fxml/app.fxml"));
        stage.setScene(new Scene(mainNode));
        stage.show();
        stage.toFront();*/
    }

    @After
    public void tearDown () throws Exception {
        /*FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
        closeCurrentWindow();*/
    }

    @Test
    public void testButtons () {
        //clickOn("#saveButton");
        //clickOn("#installForgeButton");
        //clickOn("#installModsButton");
    }

    /*@Test
    public void testChangeUsername () {
        clickOn("#username");
        write("tarba des bois");
        clickOn("#saveButton");
    }
*/
}
