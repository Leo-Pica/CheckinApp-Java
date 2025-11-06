package edu.cerp.checkin.logic;

import edu.cerp.checkin.model.Inscripcion;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class SesionService {

    // Mapa que guarda las inscripciones en memoria (clave: documento)
    private final Map<String, Inscripcion> inscripciones;

    // Ruta del archivo CSV donde se guardan las inscripciones
    private final Path archivo = Paths.get("inscripciones.csv");

    // Formato de fecha para guardar y leer del archivo
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Constructor: inicializa el mapa y carga inscripciones desde el archivo
    public SesionService() {
        this.inscripciones = new LinkedHashMap<>();
        cargarDesdeArchivo(); // Carga persistente al iniciar
    }

    // Registra una nueva inscripción y la guarda en el archivo
    public void registrar(String nombre, String documento, String curso) {
        Inscripcion nueva = new Inscripcion(nombre, documento, curso, LocalDateTime.now());
        inscripciones.put(documento, nueva);
        guardarEnArchivo(nueva); // Persistencia inmediata
    }

    // Devuelve la lista ordenada de inscripciones por fecha
    public List<Inscripcion> listar() {
        return inscripciones.values().stream()
                .sorted(Comparator.comparing(Inscripcion::getFechaHora))
                .collect(Collectors.toList());
    }

    // Busca inscripciones que coincidan con el texto ingresado
    public List<Inscripcion> buscar(String query) {
        String q = query.toLowerCase();
        return inscripciones.values().stream()
                .filter(i -> i.getNombre().toLowerCase().contains(q) ||
                             i.getDocumento().contains(q) ||
                             i.getCurso().toLowerCase().contains(q))
                .collect(Collectors.toList());
    }

    // Devuelve un resumen por curso con cantidad de inscriptos
    public String resumen() {
        Map<String, Long> resumenCursos = inscripciones.values().stream()
                .collect(Collectors.groupingBy(Inscripcion::getCurso, Collectors.counting()));

        StringBuilder sb = new StringBuilder();
        resumenCursos.forEach((curso, count) -> {
            sb.append(curso).append(": ").append(count).append(" inscriptos\n");
        });
        return sb.toString();
    }

    // Guarda una inscripción en el archivo CSV (una línea por inscripción)
    private void guardarEnArchivo(Inscripcion i) {
        try (BufferedWriter writer = Files.newBufferedWriter(archivo, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
           System.out.println("📂 Guardando en: " + archivo.toAbsolutePath());
            writer.write(i.getNombre() + "," + i.getDocumento() + "," + i.getCurso() + "," + i.getFechaHora().format(formatter));
            writer.newLine();
        } catch (IOException e) {
            System.err.println("⚠ Error al guardar inscripción: " + e.getMessage());
        }
    }

    // Carga todas las inscripciones desde el archivo CSV al iniciar
    private void cargarDesdeArchivo() {
        if (!Files.exists(archivo)) return; // Si no existe el archivo, no se carga nada
        try (BufferedReader reader = Files.newBufferedReader(archivo)) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                String[] partes = linea.split(",", 4); // Se espera: nombre, documento, curso, fecha
                if (partes.length == 4) {
                    String nombre = partes[0];
                    String documento = partes[1];
                    String curso = partes[2];
                    LocalDateTime fecha = LocalDateTime.parse(partes[3], formatter);
                    Inscripcion i = new Inscripcion(nombre, documento, curso, fecha);
                    inscripciones.put(documento, i);
                }
            }
        } catch (IOException e) {
            System.err.println("⚠ Error al cargar inscripciones: " + e.getMessage());
        }
    }

    // Método opcional para pruebas (puede comentarse si ya hay persistencia)
    public void cargarDatosDemo() {
        registrar("Ana Pérez", "11234567", "Programación 1");
        registrar("Luis Gómez", "22345678", "Redes de Datos");
        registrar("María Sol", "33456789", "Bases de Datos");
        registrar("Juan Díaz", "44567890", "Programación 1");
        registrar("Emi Fernández", "55678901", "Bases de Datos");
    }
}
