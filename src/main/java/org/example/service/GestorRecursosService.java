package org.example.service;
import org.example.model.RecursoTIC;

import java.util.List;

public interface GestorRecursosService {
    void agregarRecurso(RecursoTIC recurso);
    List<RecursoTIC> listarRecursos();
    boolean actualizarRecurso(String codigo, RecursoTIC nuevoRecurso);
    RecursoTIC buscarRecurso(String codigo);

}
