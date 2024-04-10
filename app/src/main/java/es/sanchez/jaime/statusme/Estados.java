package es.sanchez.jaime.statusme;

import java.util.ArrayList;
import java.util.List;

public class Estados {
    private List<String> estados_de_animo;

    public Estados(List<String> estados_de_animo) {
        this.estados_de_animo = estados_de_animo;
    }

    public List<String> getEstados_de_animo() {
        return estados_de_animo;
    }

    public void setEstados_de_animo(List<String> estados_de_animo) {
        this.estados_de_animo = estados_de_animo;
    }

    @Override
    public String toString() {
        return "Estados{" +
                "estados_de_animo=" + estados_de_animo +
                '}';
    }
}
