package org.example.controller;

import org.example.model.RecursoTIC;
import org.example.service.impl.GestorRecursosServiceImpl;

import java.util.List;

public class RecursoTICController {

  // GestorRecursosServiceImpl servicio = new GestorRecursosServiceImpl();

    private GestorRecursosServiceImpl servicio;

    // Constructor que obtiene la instancia del Singleton
    public RecursoTICController() {
        this.servicio = GestorRecursosServiceImpl.getInstance();
    }

    public void agregarRecurso(RecursoTIC recurso) {
        servicio.agregarRecurso(recurso);
    }

    public List<RecursoTIC> listarRecursos() {
        return servicio.listarRecursos();
    }

    public boolean actualizarRecurso(String codigo, RecursoTIC nuevoRecurso) {
        return servicio.actualizarRecurso(codigo, nuevoRecurso);
    }

    public RecursoTIC buscarRecurso(String codigo) {
        return servicio.buscarRecurso(codigo);
    }

    // Método para crear un nuevo recurso usando el Builder
    public RecursoTIC crearRecurso(String codigo, String nombre, String tipo, String estado) {
        return new RecursoTIC.Builder(codigo) // 1. Se inicializa el Builder pasando el código.
                .setNombre(nombre)  // // 2. Se establece el valor del nombre
                .setTipo(tipo)     // 3. Se establece el valor del tipo.
                .setEstado(estado)   // 4. Se establece el valor del estado.
                .build(); //   5. Finalmente, se construye y devuelve el objeto RecursoTIC.
    }
}
