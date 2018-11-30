package br.com.tbdc.controller;

import br.com.tbdc.model.voo.Voo;

import java.util.ArrayList;
import java.util.List;

public class ControladorVoo {

    private static ControladorVoo instance;
    private List<Voo> voos;

    private ControladorVoo() {
        voos = new ArrayList<>();
    }

    public static synchronized ControladorVoo getInstance() {
        if (instance == null) {
            instance = new ControladorVoo();
        }
        return instance;
    }
}
