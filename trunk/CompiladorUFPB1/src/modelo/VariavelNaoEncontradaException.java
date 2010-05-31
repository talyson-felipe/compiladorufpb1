/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

/**
 *
 * @author clodbrasilino
 */
public class VariavelNaoEncontradaException extends ErroSemantico {

    private String identificador;
    private int linha;

    public VariavelNaoEncontradaException(String identificador, int linha) {
        this.identificador = identificador;
        this.linha = linha;
    }

    public String errosToString() {
        return "A variável \"" + identificador + "\" na linha " + linha + " não foi declarada.";
    }

}
