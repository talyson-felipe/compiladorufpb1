/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.lexico;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author clodbrasilino
 */
public class PalavrasReservadas {

    private List<String> palavrasReservadas;

    private static PalavrasReservadas instance;

    private PalavrasReservadas(){
        palavrasReservadas = new ArrayList<String>(13);
        palavrasReservadas.add("program");
        palavrasReservadas.add("var");
        palavrasReservadas.add("integer");
        palavrasReservadas.add("real");
        palavrasReservadas.add("boolean");
        palavrasReservadas.add("procedure");
        palavrasReservadas.add("begin");
        palavrasReservadas.add("end");
        palavrasReservadas.add("if");
        palavrasReservadas.add("then");
        palavrasReservadas.add("else");
        palavrasReservadas.add("while");
        palavrasReservadas.add("do");
    }

    public static PalavrasReservadas getInstance() {
        if (instance == null) {
            instance = new PalavrasReservadas();
        }
        return instance;
    }

    public boolean isPalavraReservada(String token) {
        return palavrasReservadas.contains(token);
    }
}
