package org.example.patterns.strategy;

import org.example.model.Elemento;
import org.example.model.EstrategiaVisualizacion;

public class VisualizacionSimple implements EstrategiaVisualizacion {

    @Override
    public void visualizar(Elemento elemento) {
        System.out.println("  [Simple] Titulo: " + elemento.getTitulo()
                + " | Prioridad: " + elemento.getPrioridad()
                + " (" + elemento.getPrioridad().getColor() + ")");
    }
}