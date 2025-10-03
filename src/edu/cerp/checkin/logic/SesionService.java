package edu.cerp.checkin.logic;

import edu.cerp.checkin.model.Inscripcion;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class SesionService {

    private final Map<String, Inscripcion> inscripciones;

    public SesionService() {
        this.inscripciones = new LinkedHashMap<>();
    }

    public void cargarDatosDemo() {
        registrar("Ana Pérez", "11234567", "Programación 1");
        registrar("Luis Gómez", "22345678", "Redes de Datos");
        registrar("María Sol", "33456789", "Bases de Datos");
        registrar("Juan Díaz", "44567890", "Programación 1");
        registrar("Emi Fernández", "55678901", "Bases de Datos");
    }

    public void registrar(String nombre, String documento, String curso) {
        Inscripcion nuevaInscripcion = new Inscripcion(nombre, documento, curso, LocalDateTime.now());
        inscripciones.put(documento, nuevaInscripcion);
    }

    public List<Inscripcion> listar() {
        return inscripciones.values().stream()
                .sorted(Comparator.comparing(Inscripcion::getFechaHora))
                .collect(Collectors.toList());
    }

    public List<Inscripcion> buscar(String query) {
        String lowerCaseQuery = query.toLowerCase();
        return inscripciones.values().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(lowerCaseQuery) ||
                             i.getDocumento().contains(lowerCaseQuery) ||
                             i.getCurso().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());
    }

    public String resumen() {
        Map<String, Long> resumenCursos = inscripciones.values().stream()
                .collect(Collectors.groupingBy(Inscripcion::getCurso, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        resumenCursos.forEach((curso, count) -> {
            sb.append(curso).append(": ").append(count).append(" inscriptos\n");
        });

        return sb.toString();
    }
}