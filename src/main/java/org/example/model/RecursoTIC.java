package org.example.model;

public class RecursoTIC {
    private String codigo;
    private String nombre;
    private String tipo;
    private String estado;

    // Constructor privado que acepta un objeto de tipo Builder
    private RecursoTIC(Builder builder) {
        this.codigo = builder.codigo;
        this.nombre = builder.nombre;
        this.tipo = builder.tipo;
        this.estado = builder.estado;
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }

    public String getEstado() {
        return estado;
    }

    // Método toString para mostrar información del recurso
    @Override
    public String toString() {
        return "Código: " + codigo + ", Nombre: " + nombre + ", Tipo: " + tipo + ", Estado: " + estado;
    }

    // Clase estática Builder
    public static class Builder {
        private String codigo;
        private String nombre;
        private String tipo;
        private String estado;

        public Builder(String codigo) {  // Requerimos que el código sea obligatorio
            this.codigo = codigo;
        }

        public Builder setNombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder setTipo(String tipo) {
            this.tipo = tipo;
            return this;
        }

        public Builder setEstado(String estado) {
            this.estado = estado;
            return this;
        }

        public RecursoTIC build() {
            // Aquí this hace referencia a la instancia de Builder
            return new RecursoTIC(this);
        }
    }
}

