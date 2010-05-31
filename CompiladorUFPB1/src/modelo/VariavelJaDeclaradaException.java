/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

/**
 *
 * @author clodbrasilino
 */

public class VariavelJaDeclaradaException extends ErroSemantico {

    private String identificador;
    private int linha;

    public VariavelJaDeclaradaException(String identificador, int linha) {
        this.identificador = identificador;
        this.linha = linha;
    }

    public String errosToString() {
        return "Variável \"" + identificador + "\" na linha " + linha + "Já foi declarada.";
    }

}
