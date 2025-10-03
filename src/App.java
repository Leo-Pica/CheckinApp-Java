package edu.cerp.checkin;

import edu.cerp.checkin.console.MainConsole;
import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.ui.CheckInGUI;

public class App {
    public static void main(String[] args) {
        boolean usarGui = false;
        for (String a : args) {
            if ("--gui".equalsIgnoreCase(a)) {
                usarGui = true;
                break;
            }
        }

        SesionService service = new SesionService();
        service.cargarDatosDemo();

        if (usarGui) {
            CheckInGUI.show(service);
        } else {
            MainConsole.run(service);
        }
    }
}