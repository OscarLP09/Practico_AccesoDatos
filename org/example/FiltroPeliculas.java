package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FiltroPeliculas {

    private String archivoEntrada; // Archivo CSV original

    // Constructor para inicializar el archivo de entrada
    public FiltroPeliculas(String archivoEntrada) {
        this.archivoEntrada = archivoEntrada;
    }

    // Método para filtrar las películas por género
    public void filtrarPorGenero(String genero) {
        String archivoSalida = genero + ".csv";

        try (BufferedReader br = new BufferedReader(new FileReader(archivoEntrada));
             BufferedWriter bw = new BufferedWriter(new FileWriter(archivoSalida))) {

            String linea;
            boolean esPrimeraLinea = true;
            boolean hayPeliculas = false;

            // Leer archivo de entrada línea por línea
            while ((linea = br.readLine()) != null) {
                // Limpiar espacios en blanco
                linea = linea.trim();

                // Verificar que la línea no esté vacía
                if (linea.isEmpty()) {
                    continue; // Saltar líneas vacías
                }

                System.out.println("Leyendo línea: " + linea);
                String[] campos = separarCampos(linea);
                System.out.println("Número de campos: " + campos.length);

                // Asegurarnos de que la línea tenga al menos 5 columnas antes de acceder
                if (campos.length < 5) {
                    System.err.println("Línea con formato incorrecto o incompleta: " + linea);
                    continue; // Saltar a la siguiente línea si no tiene las columnas necesarias
                }

                // Escribir el encabezado en el nuevo archivo
                if (esPrimeraLinea) {
                    bw.write(linea); // Escribir el encabezado tal cual
                    bw.newLine();
                    esPrimeraLinea = false;
                } else {
                    String generoPelicula = campos[4].trim(); // Se asume que el género está en la columna 5
                    System.out.println("Género de la película: " + generoPelicula + " | Género buscado: " + genero);

                    // Si el género coincide, escribir la línea en el archivo de salida
                    if (generoPelicula.equalsIgnoreCase(genero)) {
                        bw.write(linea);
                        bw.newLine();
                        hayPeliculas = true;
                    }
                }
            }

            // Si no se encontraron películas con ese género, vaciar el archivo
            if (!hayPeliculas) {
                System.out.println("No se encontraron películas del género: " + genero);
                new FileWriter(archivoSalida, false).close(); // Elimina el contenido del archivo
            } else {
                System.out.println("Archivo creado con películas del género: " + genero);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para separar campos de una línea CSV, respetando comas dentro de comillas
    private String[] separarCampos(String linea) {
        return linea.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Regex que respeta comas dentro de comillas
    }

    public static void main(String[] args) {
        FiltroPeliculas filtro = new FiltroPeliculas("peliculas.csv");
        String genero = "Ciencia ficción"; // Puedes cambiarlo por cualquier género
        filtro.filtrarPorGenero(genero);
    }
}
