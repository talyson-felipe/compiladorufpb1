/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.semantico;

import modelo.TiposIncompativeisException;
import java.util.Stack;
import modelo.AtribuicaoIncompativelException;
import modelo.ErroSemantico;
import modelo.tipos.TipoVariavel;

/**
 *
 * @author clodbrasilino
 */
public class ChecagemDeTipos {

    private Stack<TipoVariavel> pilhaDeTipos; // TOPO sempre pega mais a direita

    public ChecagemDeTipos(){
        this.pilhaDeTipos = new Stack<TipoVariavel>();
    }

    public void push(TipoVariavel t){
        pilhaDeTipos.push(t);
    }

    public TipoVariavel avaliarAtribuicao() throws ErroSemantico{
        try {
            TipoVariavel t1 = null, t2 = null;
            if (pilhaDeTipos.peek() != null) {
                t1 = pilhaDeTipos.pop();
            }
            while(!pilhaDeTipos.isEmpty() && t1 != null){
                t2 = pilhaDeTipos.pop();
                switch(t1){
                    case BOOLEAN:
                        if (t2 == TipoVariavel.REAL){
                            t1 = TipoVariavel.REAL;
                            throw new AtribuicaoIncompativelException(t1, t2);
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            throw new AtribuicaoIncompativelException(t1, t2);
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            continue;
                        }
                        break;

                    case INTEGER:
                        if (t2 == TipoVariavel.REAL){
                            t1 = TipoVariavel.REAL;
                            continue;
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            continue;
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            throw new AtribuicaoIncompativelException(t1, t2);
                        }
                        break;

                    case REAL:
                        if (t2 == TipoVariavel.REAL){
                            continue;
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            throw new AtribuicaoIncompativelException(t1, t2);
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            throw new AtribuicaoIncompativelException(t1, t2);
                        }
                        break;
                }
            }
            return t1;
        } catch(NullPointerException ex) {
            throw new modelo.ErroSemantico() {

                public String errosToString() {
                    return "NullPointerException no metodo ChecagemDeTipos.avaliar()";
                }
            };
        } catch(ArrayIndexOutOfBoundsException ex) {
            throw new ErroSemantico() {

                public String errosToString() {
                    return "ArrayIndexOutOfBoundsException no metodo ChecagemDeTipos.avaliar()";
                }
            };
        }
    }

    public TipoVariavel avaliar() throws ErroSemantico{
        try {
            TipoVariavel t1 = null, t2 = null;
            if (pilhaDeTipos.peek() != null) {
                t1 = pilhaDeTipos.pop();
            }
            while(!pilhaDeTipos.isEmpty() && t1 != null){
                t2 = pilhaDeTipos.pop();
                switch(t1){
                    case BOOLEAN:
                        if (t2 == TipoVariavel.REAL){
                            throw new TiposIncompativeisException(t1, t2);
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            throw new TiposIncompativeisException(t1, t2);
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            continue;
                        }
                        break;

                    case INTEGER:
                        if (t2 == TipoVariavel.REAL){
                            t1 = TipoVariavel.REAL;
                            continue;
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            continue;
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            throw new TiposIncompativeisException(t1, t2);
                        }
                        break;

                    case REAL:
                        if (t2 == TipoVariavel.REAL){
                            continue;
                        }
                        if (t2 == TipoVariavel.INTEGER){
                            continue;
                        }
                        if (t2 == TipoVariavel.BOOLEAN){
                            throw new TiposIncompativeisException(t1, t2);
                        }
                        break;
                }
            }
            return t1;
        } catch(NullPointerException ex) {
            throw new modelo.ErroSemantico() {

                public String errosToString() {
                    return "NullPointerException no metodo ChecagemDeTipos.avaliar()";
                }
            };
        } catch(ArrayIndexOutOfBoundsException ex) {
            throw new ErroSemantico() {

                public String errosToString() {
                    return "ArrayIndexOutOfBoundsException no metodo ChecagemDeTipos.avaliar()";
                }
            };
        }


    }
}
