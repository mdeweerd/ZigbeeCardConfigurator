/*
 * ZigBeeToolApp.java
 */

package zigbeetool;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;
import com.bulenkov.darcula.DarculaLaf;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;


/**
 * The main class of the application.
 */
public class ZigBeeToolApp extends SingleFrameApplication {
    ZigBeeToolView view=null;
    /**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
    try {
        UIManager.setLookAndFeel(new DarculaLaf());
	//SwingUtilities.updateComponentTreeUI(view.getComponent());
    } catch (UnsupportedLookAndFeelException ex) {
        Logger.getLogger(ZigBeeToolApp.class.getName()).log(Level.SEVERE, null, ex);
    }
        view = new ZigBeeToolView(this);
        show(view);
    }

    public ZigBeeToolView getView(){
        return view;
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(java.awt.Window root) {
    }

    /**
     * A convenient static getter for the application instance.
     * @return the instance of ZigBeeToolApp
     */
    public static ZigBeeToolApp getApplication() {
        return Application.getInstance(ZigBeeToolApp.class);
    }

    /**
     * Main method launching the application.
     */
    public static void main(String[] args) {
        launch(ZigBeeToolApp.class, args);
    }
}
