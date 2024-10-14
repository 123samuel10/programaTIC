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
        return new RecursoTIC.Builder(codigo)
                .setNombre(nombre)
                .setTipo(tipo)
                .setEstado(estado)
                .build();
    }
}
