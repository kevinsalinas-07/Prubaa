package org.example.patterns.strategy;

import org.example.model.Elemento;
import org.example.model.EstrategiaVisualizacion;
import org.example.model.Recordatorio;
import org.example.model.Tarea;

public class VisualizacionCompleta implements EstrategiaVisualizacion {

    @Override
    public void visualizar(Elemento elemento) {
        System.out.println("  [Completa] Titulo:      " + elemento.getTitulo());
        System.out.println("             Descripcion: " + elemento.getDescripcion());
        System.out.println("             Prioridad:   " + elemento.getPrioridad()
                + " (" + elemento.getPrioridad().getColor() + ")");
        if (elemento instanceof Tarea tarea) {
            System.out.println("             Estado:      " + tarea.getEstado());
        } else if (elemento instanceof Recordatorio rec) {
            System.out.println("             Fecha:       " + rec.getFecha());
        }
    }
}