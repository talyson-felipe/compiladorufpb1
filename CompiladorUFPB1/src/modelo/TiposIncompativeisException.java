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
public class TiposIncompativeisException extends ErroSemantico {

    TipoVariavel t1, t2;

    public TiposIncompativeisException(TipoVariavel t1, TipoVariavel t2) {
        this.t1 = t1;
        this.t2 = t2;
    }

    public String errosToString() {
        return "Impossivel realizar operação entre " + t2 + " e " + t1 + ".";
    }


}
