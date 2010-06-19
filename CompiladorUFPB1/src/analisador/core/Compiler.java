
package analisador.core;

import analisador.exceptions.DuplicatedException;
import analisador.exceptions.ExpectedException;
import analisador.exceptions.FatalException;
import analisador.exceptions.UndefinedException;

/**
 *
 * @author Erisvaldo
 */
public abstract class Compiler {

    public abstract String getCode(String inputCode) throws DuplicatedException, ExpectedException, FatalException, UndefinedException;

    // CODE GENERATION ROUTINES

    // primary register = zero
    protected abstract void instructionClear();
    // primare register = negative
    protected abstract void instructionNegative();
    // primary register = numeric constant
    protected abstract void instructionLoadConst(String val);
    // primary register = variable
    protected abstract void instructionLoadVar(String name) throws UndefinedException;
    // put primary register at the stack
    protected abstract void instructionPush();
    // add primary register to top of the stack
    protected abstract void instructionPopAdd();
    // subtract primary register from top of the stack
    protected abstract void instructionPopSub();
    // multiply primary register by top of the stack
    protected abstract void instructionPopMul();
    // divide top of the stack by primary register
    protected abstract void instructionPopDiv();
    // stores primary register in a variable
    protected abstract void instructionStore(String name);
    // invert primary register
    protected abstract void instructionNot();
    // AND from top of the stack with primary register
    protected abstract void instructionPopAnd();
    // OR from top of the stack with primary register
    protected abstract void instructionPopOr();
    // "OR-exclusive" from top of stack with primary register
    protected abstract void instructionPopXor();
    // compares top of stack with primary register
    protected abstract void instructionPopCompare();
    // changes primary register and flags according to comparison
    protected abstract void instructionRelOp(char op);
    // jump
    protected abstract void instructionJmp(int label);
    // jump if false (0)
    protected abstract void instructionJmpFalse(int label);
    // read a value to the primary register and stores in a variable
    protected abstract void instructionRead();
    // shows primary register value
    protected abstract void instructionWrite();
    // Prolog of Main routine
    protected abstract void instructionProlog();
    // Epilog of Main routine
    protected abstract void instructionEpilog();
}
