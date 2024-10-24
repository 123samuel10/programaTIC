package org.example;

import org.example.controller.RecursoTICController;
import org.example.model.RecursoTIC;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final RecursoTICController controlador = new RecursoTICController();

    public static void main(String[] args) {
        int opcion;
        do {
            mostrarMenu();
            opcion = scanner.nextInt();
            scanner.nextLine(); // Limpiar el buffer

            switch (opcion) {
                case 1:
                    agregarRecursoTIC();
                    break;
                case 2:
                    listarRecursosTIC();
                    break;
                case 3:
                    actualizarRecursoTIC();
                    break;
                case 4:
                    buscarRecursoTIC();
                    break;
                case 5:
                    System.out.println("Saliendo...");
                    break;
                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 5);
        scanner.close();
    }

    // Método para mostrar el menú
    private static void mostrarMenu() {
        System.out.println("\nGestión de Recursos TIC");
        System.out.println("1. Agregar Recurso TIC");
        System.out.println("2. Listar Recursos TIC");
        System.out.println("3. Actualizar Recurso TIC");
        System.out.println("4. Buscar Recurso TIC por código");
        System.out.println("5. Salir");
        System.out.print("Selecciona una opción: ");
    }

    // Método para agregar un nuevo recurso TIC
    private static void agregarRecursoTIC() {
        System.out.print("Código: ");
        String codigo = scanner.nextLine();
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();
        System.out.print("Estado: ");
        String estado = scanner.nextLine();

        RecursoTIC nuevoRecurso = controlador.crearRecurso(codigo, nombre, tipo, estado);
        controlador.agregarRecurso(nuevoRecurso);
        System.out.println("Recurso agregado correctamente.");
    }

    // Método para listar todos los recursos TIC
    private static void listarRecursosTIC() {
        System.out.println("\nLista de Recursos TIC:");
        for (RecursoTIC recurso : controlador.listarRecursos()) {
            System.out.println(recurso);
        }
    }

    // Método para actualizar un recurso TIC existente
    private static void actualizarRecursoTIC() {
        System.out.print("Código del recurso a actualizar: ");
        String codigoActualizar = scanner.nextLine();
        System.out.print("Nuevo nombre: ");
        String nuevoNombre = scanner.nextLine();
        System.out.print("Nuevo tipo: ");
        String nuevoTipo = scanner.nextLine();
        System.out.print("Nuevo estado: ");
        String nuevoEstado = scanner.nextLine();

        RecursoTIC recursoActualizado = controlador.crearRecurso(codigoActualizar, nuevoNombre, nuevoTipo, nuevoEstado);
        boolean actualizado = controlador.actualizarRecurso(codigoActualizar, recursoActualizado);
        if (actualizado) {
            System.out.println("Recurso actualizado correctamente.");
        } else {
            System.out.println("Recurso no encontrado.");
        }
    }

    // Método para buscar un recurso TIC por código
    private static void buscarRecursoTIC() {
        System.out.print("Código del recurso a buscar: ");
        String codigoBuscar = scanner.nextLine();
        RecursoTIC recursoEncontrado = controlador.buscarRecurso(codigoBuscar);
        if (recursoEncontrado != null) {
            System.out.println("Recurso encontrado: " + recursoEncontrado);
        } else {
            System.out.println("Recurso no encontrado.");
        }
    }
}
