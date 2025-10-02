package edu.cerp.checkin;

import edu.cerp.checkin.console.MainConsole;
import edu.cerp.checkin.logic.SesionService;

public class App {
    public static void main(String[] args) {
        // Lógica para detectar si se debe usar la GUI (con el argumento --gui)
        boolean usarGui = false;
        for (String a : args) if ("--gui".equalsIgnoreCase(a)) usarGui = true;

        // CORRECCIÓN: Se llama al constructor sin argumentos.
        SesionService service = new SesionService(); 
        
        service.cargarDatosDemo(); // Carga de datos de prueba

        if (usarGui) {
            // TODO: Se lanzará la GUI aquí en el futuro
            // edu.cerp.checkin.ui.CheckInGUI.show(service);
            System.out.println("⚠ GUI no implementada. Corre sin --gui para usar consola.");
        } else {
            // Lanza la aplicación de consola
            MainConsole.run(service);
        }
    }
}