/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package modelo;

import modelo.tipos.TipoVariavel;

/**
 *
 * @author clodbrasilino
 */
public class AtribuicaoIncompativelException extends ErroSemantico{

    private TipoVariavel t1 = null, t2 = null;

    public AtribuicaoIncompativelException(TipoVariavel t1,TipoVariavel t2){
        this.t1 = t1;
        this.t2 = t2;
    }

    public String errosToString() {
        return "Impossível atribuir " + t1 + " à " + t2 + ".";
    }

}
