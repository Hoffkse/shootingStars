
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Main {

    public static void main(String[] args) {
        ShipGUI win = new ShipGUI();

        win.addWindowListener(
                new WindowAdapter()
                {
                    public void windowClosing(WindowEvent e)
                    { System.exit(0); }
                }
        );
    }
}
