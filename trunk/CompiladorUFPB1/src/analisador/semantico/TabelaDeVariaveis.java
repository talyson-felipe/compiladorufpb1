/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package analisador.semantico;

import java.util.ArrayList;
import java.util.Stack;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import modelo.ErroSemantico;
import modelo.Token;
import modelo.Variavel;
import modelo.tipos.TipoVariavel;

/**
 *
 * @author clodbrasilino
 */
public class TabelaDeVariaveis {

    private Map<String, Variavel> variaveis;

    public TabelaDeVariaveis(){
        super();
        variaveis = new HashMap<String, Variavel>();
    }

    public boolean areDeclarada(Stack<Object> s){
        for (Object o: s){

            if (o instanceof Token){
                Token t = (Token) o;
                if(!isDeclarada(t.getToken())){
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isDeclarada(String chave){
        return variaveis.keySet().contains(chave);
    }

    public Variavel get(String chave){
        return variaveis.get(chave);
    }

    public void createAll(Stack<Object> tokens) throws ErroSemantico {
        try {
            TipoVariavel t = (TipoVariavel) tokens.pop();
            List<Token> identificadores = new ArrayList<Token>();
            while(!tokens.isEmpty()) {
                identificadores.add((Token) tokens.pop());
            }

            for(Token identificador: identificadores) {
                create(identificador.getToken(), t);
            }
        } catch (NullPointerException ex) {
            throw new ErroSemantico() {

                public String errosToString() {
                    return "Erro interno.";
                }
            };
        }

    }

    public void create(String chave, TipoVariavel tipo){
        switch(tipo){
            case BOOLEAN:
                variaveis.put(chave, new Variavel(tipo, new Boolean(false)));
                break;
            case INTEGER:
                variaveis.put(chave, new Variavel(tipo, new Integer(0)));
                break;
            case REAL:
                variaveis.put(chave, new Variavel(tipo, new Float(0)));
                break;
        }
    }

    public void create(String chave, TipoVariavel tipo, Object valor){
        variaveis.put(chave, new Variavel(tipo, valor));
    }

}
