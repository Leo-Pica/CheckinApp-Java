package edu.cerp.checkin.ui;

import edu.cerp.checkin.logic.SesionService;
import edu.cerp.checkin.model.Inscripcion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class CheckInGUI {

    public static void show(SesionService service) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Check-in Aula (GUI)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(500, 500);
            frame.setLayout(new BorderLayout());

            // Panel de registro
            JPanel registroPanel = new JPanel(new GridLayout(4, 2, 5, 5));
            JTextField nombreField = new JTextField();
            JTextField documentoField = new JTextField();
            JTextField cursoField = new JTextField();

            registroPanel.add(new JLabel("Nombre:"));
            registroPanel.add(nombreField);
            registroPanel.add(new JLabel("Documento:"));
            registroPanel.add(documentoField);
            registroPanel.add(new JLabel("Curso:"));
            registroPanel.add(cursoField);

            JButton registrarBtn = new JButton("Registrar");

            // Panel de búsqueda
            JPanel buscarPanel = new JPanel(new BorderLayout());
            JTextField buscarField = new JTextField();
            JButton buscarBtn = new JButton("Buscar");

            buscarPanel.add(new JLabel("Buscar por nombre, documento o curso:"), BorderLayout.NORTH);
            buscarPanel.add(buscarField, BorderLayout.CENTER);
            buscarPanel.add(buscarBtn, BorderLayout.EAST);

            // Botón de listar
            JButton listarBtn = new JButton("Listar todos");

            // Área de resultados
            JTextArea resultadoArea = new JTextArea();
            resultadoArea.setEditable(false);
            JScrollPane scroll = new JScrollPane(resultadoArea);

            // Acciones
            registrarBtn.addActionListener((ActionEvent e) -> {
                String nombre = nombreField.getText().trim();
                String documento = documentoField.getText().trim();
                String curso = cursoField.getText().trim();
                if (!nombre.isEmpty() && !documento.isEmpty() && !curso.isEmpty()) {
                    service.registrar(nombre, documento, curso);
                    resultadoArea.setText("✔ Registrado correctamente\n\n" + service.resumen());
                    nombreField.setText("");
                    documentoField.setText("");
                    cursoField.setText("");
                } else {
                    resultadoArea.setText("⚠ Por favor, completa todos los campos.");
                }
            });

            listarBtn.addActionListener((ActionEvent e) -> {
                List<Inscripcion> lista = service.listar();
                resultadoArea.setText("📋 Inscripciones:\n");
                for (Inscripcion i : lista) {
                    resultadoArea.append(i.getNombre() + " | " + i.getDocumento() + " | " +
                                         i.getCurso() + " | " + i.getFechaHora() + "\n");
                }
            });

            buscarBtn.addActionListener((ActionEvent e) -> {
                String query = buscarField.getText().trim();
                if (!query.isEmpty()) {
                    List<Inscripcion> resultados = service.buscar(query);
                    resultadoArea.setText("🔍 Resultados de búsqueda:\n");
                    for (Inscripcion i : resultados) {
                        resultadoArea.append(i.getNombre() + " | " + i.getDocumento() + " | " +
                                             i.getCurso() + " | " + i.getFechaHora() + "\n");
                    }
                    if (resultados.isEmpty()) {
                        resultadoArea.append("⚠ No se encontraron coincidencias.");
                    }
                } else {
                    resultadoArea.setText("⚠ Ingresa un término para buscar.");
                }
            });

            // Organización visual
            JPanel botonesPanel = new JPanel(new FlowLayout());
            botonesPanel.add(registrarBtn);
            botonesPanel.add(listarBtn);

            JPanel superiorPanel = new JPanel(new BorderLayout());
            superiorPanel.add(registroPanel, BorderLayout.NORTH);
            superiorPanel.add(botonesPanel, BorderLayout.CENTER);
            superiorPanel.add(buscarPanel, BorderLayout.SOUTH);

            frame.add(superiorPanel, BorderLayout.NORTH);
            frame.add(scroll, BorderLayout.CENTER);
            frame.setVisible(true);
        });
    }
}