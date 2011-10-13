package analisador.core;

import java.util.Scanner;
import analisador.exceptions.DuplicatedException;
import analisador.exceptions.ExpectedException;
import analisador.exceptions.FatalException;
import analisador.exceptions.UndefinedException;

/**
 *
 * @author Erisvaldo
 */
public class IJVMCompiler extends Compiler {
//    private CompilerConstants token;
    private String emit;
    private String error;
    private String expected;
    private String fatal;
    private String generatedCode;
    private String inputCode;
    private char currentChar;
    private char token;
    private int currentIndex;
    private int labelCount; // indicates the current label

    private static final int KWLIST_SZ = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("KWLIST_SZ") );
    private static final int MAXNAME = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("MAXNAME") );
    private static final int MAXNUM = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("MAXNUM") );
    private static final int MAXOP = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("MAXOP") );
    private static final int MAXTOKEN = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("MAXTOKEN") );
    private static final int SYMTBL_SZ = Integer.parseInt( java.util.ResourceBundle.getBundle("analisador/resources/Compiler").getString("SYMTBL_SZ") );

    private static IJVMCompiler instance;
    private Scanner in;

    private char[] symtype;
    private char[] value;
    private char[] valueWhile;
    private String[] kwlist = {"if", "else", "then", "while", "do", "read", "write", "var", "end"};
    private String[] symtbl;

    private final String kwcode = "iltwdRWve";
    private int nsym;

    private enum CompilerConstants {
        KW_IF,
        KW_ELSE,
        KW_ENDIF,
        KW_END,
        TK_IDENT,
        TK_NUMBER,
        TK_OPERATOR
    }

    private IJVMCompiler() {
        generatedCode = inputCode = "";
        symtbl = new String[SYMTBL_SZ];
        symtype = new char[SYMTBL_SZ];
        value = new char[MAXTOKEN + 1];

        System.out.println("MAXNAME: " + MAXNAME);
        System.out.println("MAXNUM: " + MAXNUM);
    }

    public static IJVMCompiler getInstance() {
        if (instance == null)
            instance = new IJVMCompiler();

        return instance;
    }

    // verify if entry matches the expected character
    public void match(char character) throws ExpectedException {
        if (currentChar != character)
            throw new ExpectedException("'" + character + "'");

        nextChar();
        skipWhite();
    }
    
    // last version
    // translates a name (identifier or keyword)
    public void getName() throws ExpectedException {
	skipWhite();

	if ( !Character.isLetter( getCurrentChar() ))
            throw new ExpectedException("Identificador");

	int i;

        for (i = 0; Character.isLetterOrDigit( getCurrentChar() ) && i < MAXTOKEN; i++) {
                System.out.println("Verificar value:" + Character.toLowerCase( getCurrentChar() ));
		value[i] = Character.toLowerCase( getCurrentChar() );
		nextChar();
	}

	value[i] = '\0';
	token = 'x';        
    }

    public void getName(char[] name) throws ExpectedException {
        if ( !Character.isLetter( getCurrentChar() ) )
            throw new ExpectedException("Nome");

        int i;

        for (i = 0; Character.isLetterOrDigit( getCurrentChar() ) && i < MAXNAME; i++) {
            name[i] = Character.toLowerCase( getCurrentChar() );
            nextChar();
        }

        name[i] = '\0';
        skipWhite();
    }

    // last version
    // translates an integer
    public void getNum() throws ExpectedException {
        skipWhite();

        if ( !Character.isDigit( getCurrentChar() ) )
            throw new ExpectedException("N�mero");

        int i;

        for (i = 0; Character.isDigit( getCurrentChar() ) && i < MAXTOKEN; i++) {
            value[i] = getCurrentChar();
            nextChar();
        }

        value[i] = '\0';
        token = '#';
    }

    public void getNum(char[] num) throws ExpectedException {
        if ( !Character.isDigit( getCurrentChar() ) )
            throw new ExpectedException("Inteiro");

        int i;

        for (i = 0; Character.isDigit( getCurrentChar() ) && i < MAXNUM; i++) {
            num[i] = getCurrentChar();
            nextChar();
        }

        num[i] = '\0';
        skipWhite();
    }

    private void nextChar() {
//        System.out.println("entrou em nextChar()");

        if ( in.hasNext() ) {
            setCurrentChar( in.next().charAt(0) );
//            System.out.println("currentChar: " + getCurrentChar());
        }

//        System.out.println("chegou no fim de nextChar()");
    }

    // emit an instruction followed by a new line
    private void emit(String message) {
//        System.out.println("entrou em emit()");
        setEmit(message + '\n');

        System.out.print( getEmit() );
        generatedCode += getEmit();
//        System.out.println("chegou no fim de emit()");
    }

    private void expected(String message) {
        setExpected("Error: " + message + " expected!\n");

        System.out.print( getExpected() );
    }

    private void fatal(String message) {
        setFatal("Fatal Error: " + message);

        System.out.print( getFatal() );
    }

    // last version
    private void expression() throws UndefinedException, ExpectedException, FatalException {
        if ( isAddOp(token) )
            instructionClear();
        else
            term();

	while ( isAddOp(token) ) {
//            instructionPush();

            switch (token) {
                case '+':
                    add();
                    break;

                case '-':
                    subtract();
                    break;
            }
	}
    }

    // last version
    private void add() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        term();
        instructionPopAdd();
    }

    // last version
    private void subtract() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        term();
        instructionPopSub();
    }

    // last version
    // translates a multiply operation
    private void multiply() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        factor();
        instructionPopMul();
    }

    // last version
    // translates a divide operation
    private void divide() throws ExpectedException, FatalException, UndefinedException {
        nextToken();
        factor();
        instructionPopDiv();
    }

    // last version
    // translates a math term
    private void term() throws UndefinedException, ExpectedException, FatalException {
        factor();

        while ( isMulOp(token) ) {
//            instructionPush();

            switch (token) {
                case '*':
                    multiply();
                    break;
                case '/':
                    divide();
                    break;
            }
	}
    }

    // last version
    private void assignment() throws UndefinedException, ExpectedException, FatalException {
        System.out.println("entrou em assignment()");
        String name = getString(value);
        System.out.println("assignment() name: " + name);
        checkTable(name);
        nextToken();
        if(getString(value).equals(":"))
            nextToken();
        matchString("=");
        boolExpression();
        instructionStore(name);
        }
    
    // last version
    // translate a factor
    private void factor() throws UndefinedException, ExpectedException, FatalException {        
	if (token == '(') {
                nextToken();
		boolExpression();
		matchString(")");
	} else {
                if (token == 'x')
                    instructionLoadVar( getString(value) );
        	else if (token == '#')
                    instructionLoadConst( getString(value) );
        	else
                    throw new ExpectedException("Fator Matem�tico");

                nextToken();
        }
    }

    /* analisa e traduz um fator com sinal opcional */
    private void signedFactor() throws ExpectedException, FatalException, UndefinedException {
        if (getCurrentChar() == '+')
            nextChar();
    
        if (getCurrentChar() == '-') {
            nextChar();

            if ( Character.isDigit( getCurrentChar() ) ) {
                
            }
//                emit("MOV AX, -" + getNum());
            else {
                factor();
                emit("NEG AX");
            }
        } else
            factor();
    }

    public void setEmit(String emit) {
        this.emit = emit;
    }

    public void setInputCode(String javaCode) {
        this.inputCode = javaCode;
    }

    /**
     * @return current compiler emit
     */
    public String getEmit() {
        return emit;
    }

    /**
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    /**
     * @return the expected
     */
    public String getExpected() {
        return expected;
    }

    /**
     * @param expected the expected to set
     */
    public void setExpected(String expected) {
        this.expected = expected;
    }

    /**
     * @return the fatal
     */
    public String getFatal() {
        return fatal;
    }

    /**
     * @param fatal the fatal to set
     */
    public void setFatal(String fatal) {
        this.fatal = fatal;
    }

    /**
     * @return the inputCode
     */
    public String getInputCode() {
        return inputCode;
    }

    /**
     * @return the currentIndex
     */
    public int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * @param currentIndex the currentIndex to set
     */
    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

    private char getCurrentChar() {
        return currentChar;
    }

    private void setCurrentChar(char currentChar) {
        this.currentChar = currentChar;
    }

    /**
     * @return the IjvmCode
     */
    public String getCode(String inputCode) throws ExpectedException, FatalException, DuplicatedException, UndefinedException {
        setCurrentIndex(0);
        setLabelCount(0);
        setInputCode( inputCode.replace('\n', ' ').concat("END") );

        System.out.println("// Inicio do codigo java");
        System.out.println( getInputCode() );
        System.out.println("// Fim do codigo java");

        generatedCode = "";
        in = new Scanner( getInputCode() ).useDelimiter("");
        
        init();
        matchString("program");
        nextToken();
        semicolon();
        topdecls();        
        matchString("begin");
        instructionProlog();
        block();
        matchString("end");
        instructionEpilog();


        in = null;

        return generatedCode;
    }

    // recognizes a operator
    private boolean isOp(char character) {
        char[] operators = {'+', '-', '*', '/', '<', '>', ':', '='};

        for (int i = 0; i < operators.length; i++)
            if (operators[i] == character)
                return true;

        return false;
    }
    
    // recognizes addiction operation
    private boolean isAddOp(char character) {
        return (character == '+' || character == '-');
    }

    // recognizes multiply operation
    private boolean isMulOp(char character) {
        return (character == '*' || character == '/');
    }

    // recognizes OR operation
    private boolean isOrOp(char character) {
        return (character == '|' || character == '~');
    }

    // recognizes relational operators
    private boolean isRelOp(char character) {
        return (character == '=' || character == '#' || character == '<' || character == '>');
    }

    // recognizes a boolean literal
    private boolean isBoolean(char character) {
        return (character == 'T' || character == 'F');
    }

    // receives a boolean literal
    private boolean getBoolean() throws ExpectedException {
        boolean bool;

        if ( !isBoolean( getCurrentChar() ) )
            throw new ExpectedException("Literal Booleano");

        bool = (getCurrentChar() == 'T');

        nextChar();

        return bool;
    }

    // last version
    // translates an operator
    public void getOp() {
        skipWhite();
        token = getCurrentChar();
        value[0] = getCurrentChar();
        value[1] = '\0';
        nextChar();
    }

    public void nextToken() throws ExpectedException {
        skipWhite();
        
        if ( Character.isLetter( getCurrentChar() ) )
            getName();
        else if ( Character.isDigit( getCurrentChar() ) )
            getNum();
        else
            getOp();
    }

/* OLD VERSION FOR ONLY ONE CHARACTER PER TIME */
    // translates a identifier
    private void ident() throws ExpectedException {
//        char name = getName();
        getName();

    }

    // last version
    private void init() throws ExpectedException {
        nsym = 0;

        nextChar();
        nextToken();
    }

    private void skipWhite() {
//        while ( isSpace( getCurrentChar() ) )
        while ( Character.isSpaceChar( getCurrentChar() ) )
            nextChar();
    }

    private void other() throws ExpectedException {
//        emit("# " + getName());
    }

    // Translates a complete program
    private void program() throws ExpectedException, FatalException, UndefinedException {
        block();

        if ( in.hasNext() )
            throw new ExpectedException("Fim");

        emit("END");
    }

    // last version
    private void block() throws UndefinedException, ExpectedException, FatalException {
	boolean follow = false;

	do {
            scan();
            
            switch (token) {
                case 'i':
                    doIf();
                    break;
                case 'w':
                    doWhile();
                    break;
                case 'R':
                    doRead();
                    break;
                case 'W':
                    doWrite();
                    break;
                case 'x':
                    assignment();
                    break;
                case 'e':
                case 'l':
                    follow = true;
                    break;
            }

            if (!follow) {
                semicolon();
                System.out.println("Case");
            }
	} while (!follow);
    }
    // generates a new unique label.
    private int newLabel() {
        return labelCount++;
    }

    // emit a label.
    private void postLabel(int label) {
        emit("L" + label + ":");
    }

    // last version
    private void doIf() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        boolExpression();

        int l1 = newLabel();
        int l2 = l1;

//        instructionJmpFalse(l1);
        block();

        if (token == 'l') {
            nextToken();
            l2 = newLabel();
            instructionJmp(l2 - 1);
            emit("L" + (l1 - 1) + ":");
            block();
        }

        emit("L" + (l2 - 1) + ":");
        //matchString("ENDIF");
    }

    // last version
    private void doWhile() throws ExpectedException, FatalException, UndefinedException {
	nextToken();
        System.out.println("Entrei no WHILE");
	int l1 = newLabel();
	int l2 = l1 + 1;
	emit("L"+l1+":");
	boolExpression();
//	instructionJmpFalse(l2);
	block();
	//matchString("ENDWHILE");
	instructionJmp(l1);
	emit("L"+ (l2 + 1) +":");
    }

  

    // reads one variable
    void readVar() throws UndefinedException, ExpectedException {
        checkIdent();
        checkTable( getString(value) );
        instructionRead();
        nextToken();
    }

    // translates a READ command
    void doRead() throws UndefinedException, ExpectedException {
        nextToken();
        matchString("(");
        for (;;) {
            readVar();
            if (token != ',') {
                break;
            }
            nextToken();
        }
        matchString(")");
    }

    // translates a WRITE command
    void doWrite() throws UndefinedException, ExpectedException, FatalException {
        nextToken();
        matchString("(");
        for (;;) {
            expression();
            instructionWrite();
            if (token != ',') {
                break;
            }
            nextToken();
        }
        matchString(")");
    }

    public int getLabelCount() {
        return labelCount;
    }

    /**
     * @param labelCount the labelCount to set
     */
    public void setLabelCount(int labelCount) {
        this.labelCount = labelCount;
    }

    // last version
    private void boolExpression() throws ExpectedException, UndefinedException, FatalException {
        boolTerm();

        while ( isOrOp(token) ) {
//            instructionPush();

            switch (token) {
                case '|':
                    boolOr();
                    break;
                case '~':
                    boolXor();
                    break;
            }
        }
    }


    // last version
    private void boolOr() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        boolTerm();
        instructionPopOr();
    }

    // last version
    private void boolXor() throws ExpectedException, UndefinedException, FatalException {
        nextToken();
        boolTerm();
        instructionPopXor();
    }

    // last version
    private void boolTerm() throws ExpectedException, UndefinedException, FatalException {
        notFactor();

        while (token == '&') {
//            instructionPush();
            nextToken();
            notFactor();
            instructionPopAnd();
        }
    }

    // Translates a boolean factor.
    private void boolFactor() throws ExpectedException, FatalException, UndefinedException {
        if ( isBoolean( getCurrentChar() ) ) {
            if ( getBoolean() )
                emit("MOV AX, -1");
            else
                emit("MOV AX, 0");
        } else
            relation();
    }


    private void notFactor() throws ExpectedException, UndefinedException, FatalException {
        if (token == '!') {
            nextToken();
            relation();
            instructionNot();
        } else
            relation();
    }


    // last version
    private void relation() throws UndefinedException, ExpectedException, FatalException {
	char op;

	expression();

	if ( isRelOp(token) ) {
            op = token;
	
            nextToken(); // removes operator of the way
		
            if (op == '<') {
                if (token == '>') { /* <> */
                    nextToken();
                    op = '#';
		} else if (token == '=') {
                    nextToken();
                    op = 'L';
		}
            } else if (op == '>' && token == '=') {
                nextToken();
		op = 'G';
            }

//            instructionPush();
            expression();
//            instructionPopCompare();
            instructionRelOp(op);
	}
    }

    // translates a equals operator.
    private void equals() throws ExpectedException, FatalException, UndefinedException {
        match('=');

        int firstLabel = newLabel();
        int secondLabel = newLabel();

        expression();

        emit("POP BX");
        emit("CMP BX, AX");
        emit("JE L" + firstLabel);
        emit("MOV AX, 0");
        emit("JMP L" + secondLabel);
        postLabel(firstLabel);
        emit("MOV AX, -1");
        postLabel(secondLabel);
    }

    // translates a NOT equals operator.
    private void notEquals() throws ExpectedException, FatalException, UndefinedException {
        match('#');

        int firstLabel = newLabel();
        int secondLabel = newLabel();

        expression();
        
        emit("POP BX");
        emit("CMP BX, AX");
        emit("JNE L" + firstLabel);
        emit("MOV AX, 0");
        emit("JMP L" + secondLabel);
        postLabel(firstLabel);
        emit("MOV AX, -1");
        postLabel(secondLabel);
    }

    // translates a GREATER THAN operator.
    private void greater() throws ExpectedException, FatalException, UndefinedException {
        match('>');

        int firstLabel = newLabel();
        int secondLabel = newLabel();

        expression();

        emit("POP BX");
        emit("CMP BX, AX");
        emit("JG L" + firstLabel);
        emit("MOV AX, 0");
        emit("JMP L" + secondLabel);
        postLabel(firstLabel);
        emit("MOV AX, -1");
        postLabel(secondLabel);
    }

    // translates a LESS THAN operator.
    private void less() throws ExpectedException, FatalException, UndefinedException {
        match('<');

        int firstLabel = newLabel();
        int secondLabel = newLabel();

        expression();
        
        emit("POP BX");
        emit("CMP BX, AX");
        emit("JL L" + firstLabel);
        emit("MOV AX, 0");
        emit("JMP L" + secondLabel);
        postLabel(firstLabel);
        emit("MOV AX, -1");
        postLabel(secondLabel);
    }

    // new version
    private void newLine() {
        while (getCurrentChar() != '\n') {
            nextChar();
            skipWhite();
        }
    }

    // last version
    // get next token from input
    public void scan() {
        
        if (token == 'x') {
            System.out.println("felipe");
            System.out.println("value: " + getString(value));
            int kw = lookup(getString(value), kwlist, KWLIST_SZ);

            System.out.println("kw:" + kw);
            if (kw >= 0)
                token = kwcode.charAt(kw);
        }        
    }

    private void skipComma() {
        skipWhite();

        if (getCurrentChar() == ',') {
            nextChar();
            skipWhite();
        }
    }

    // Returns the address of the identifier in the Symbols' Table
    private boolean inTable(String name) {
        return !(lookup(name, symtbl, nsym) < 0);
    }

    // Returns the address of the identifier in the Symbols' Table
    private int locate(String name) {
        return lookup(name, symtbl, nsym);
    }

    // Search for a String in table. Used for looking for keywords and symbols
    private int lookup(String s, String[] list, int size) {
        for (int i = 0; i < size; i++) {
            if (list[i].compareTo(s) == 0) {
                return i;
            }
        }

        return -1;
    }

    // compares a String with the current token
    private void matchString(String string) throws ExpectedException {
        
        String valueString = getString(value);
        valueString = valueString.toLowerCase();

        System.out.println("valueString: " + valueString);

        System.out.println("Esse: " + string);
        if (string.compareTo(valueString) != 0) {
            
            throw new ExpectedException(string);
        }

        nextToken();
        
    }
    
    private String getString(char[] stringCharArray) {
        String aux = new String(stringCharArray);
        String string;

        if (aux.indexOf('\0') > -1)
            string = aux.substring(0, aux.indexOf('\0') );
        else if (aux.indexOf(' ') > -1)
            string = aux.substring(0, aux.indexOf(' ') );
        else
            string = aux;

        return string;
    }

    // Translates declarations
    private void topdecls() throws ExpectedException, DuplicatedException, FatalException {
        scan();
        System.out.println("token: " + token);
        while (token == 'v') {            
            do {
                decl();                
            }
            while (token == ',');

            semitoken();
            semicolon();
        }
        System.out.println("AKI");
    }

    // Variable declarations
    private void decl() throws ExpectedException, DuplicatedException, FatalException {
        nextToken();
        System.out.println("Token: " + token);
        if (token != 'x')
            throw new ExpectedException("Nome de Vari�vel");

        String valueString = getString(value);

        checkdup(valueString);
        addSymbol(valueString, 'v');
        allocVar(valueString, 0);
        nextToken();
    }

    // Reports an error if identifier already exists at Symbols Table
    private void checkdup(String name) throws DuplicatedException {
        if ( inTable(name) )
            throw new DuplicatedException(name);
    }

    // Reports an error if token is NOT a identifier
    private void checkIdent() throws ExpectedException {
        if (token != 'x')
            throw new ExpectedException("Identificador");
    }

    private void checkTable(String name) throws UndefinedException {
        if ( !inTable(name) ) {
            throw new UndefinedException(name);
        }
    }

    // adds a new identifier to Symbols Table
    private void addSymbol(String name, char type) throws DuplicatedException, FatalException {
        checkdup(name);

        if (nsym >= SYMTBL_SZ)
            throw new FatalException("Tabela de S�mbolos cheia!");

        String newsym = new String(name);  

        symtbl[nsym] = newsym;
        symtype[nsym] = type;
        nsym++;
    }

    // Allocate memory to declare a variable (+initializer)
    private void allocVar(String name, int value) {
//	emit(name + ":\tdw " + value);
    }

    // CODE GENERATION ROUTINES

    // primary register = zero
    protected void instructionClear() {
        throw new UnsupportedOperationException("clear");
//        emit("xor ax, ax");
    }

    // primare register = negative
    protected void instructionNegative() {
        throw new UnsupportedOperationException("negative");
//        emit("neg ax");
    }

    // primary register = numeric constant
    protected void instructionLoadConst(String val) {
        emit("bipush " + val);
//        emit("mov ax, " + val);
    }

    // primary register = variable
    protected void instructionLoadVar(String name) throws UndefinedException {
	if ( !inTable(name) )
            throw new UndefinedException(name);

        emit("iload " + name);
//        emit("mov ax, word ptr " + name);
    }

    // put primary register at the stack
    protected void instructionPush() {
        throw new UnsupportedOperationException("push");
//	emit("push ax");
    }

    // add primary register to top of the stack
    protected void instructionPopAdd() {
        emit("iadd");
//	emit("pop bx");
//	emit("add ax, bx");
    }

    // subtract primary register from top of the stack
    protected void instructionPopSub() {
        emit("isub");
//	emit("pop bx");
//	emit("sub ax, bx");
//	emit("neg ax");
    }

    // multiply primary register by top of the stack
    protected void instructionPopMul() {
        emit("imul");
//	emit("pop bx");
//	emit("mul bx");
    }

    // divide top of the stack by primary register
    protected void instructionPopDiv() {
        emit("idiv");
//	emit("pop bx");
//	emit("xchg ax, bx");
//	emit("cwd");
//	emit("div bx");
    }

    // stores primary register in a variable
    protected void instructionStore(String name) {
        emit("istore " + name);
//	emit("mov word ptr " + name + ", ax");
    }

    // invert primary register
    protected void instructionNot() {
        throw new UnsupportedOperationException("not");
//	emit("not ax");
    }

    // AND from top of the stack with primary register
    protected void instructionPopAnd() {
        emit("iand");
//	emit("pop bx");
//	emit("and ax, bx");
    }

    // OR from top of the stack with primary register
    protected void instructionPopOr() {
        emit("ior");
//	emit("pop bx");
//	emit("or ax, bx");
    }

    // "OR-exclusive" from top of stack with primary register
    protected void instructionPopXor() {
        throw new UnsupportedOperationException("xor");
//	emit("pop bx");
//	emit("xor ax, bx");
    }

    // compares top of stack with primary register
    protected void instructionPopCompare() {
        throw new UnsupportedOperationException("compare");
//	emit("pop bx");
//	emit("cmp bx, ax");
    }

    // changes primary register and flags according to comparison
    protected void instructionRelOp(char op) {
	String jump = "";

        System.out.println("token = " + token);
        System.out.println("while");
        System.out.println("value = " + getString(value));

	int l1 = newLabel();
        int l2 = newLabel();

	switch (op) {
	  case '=': jump = "if_icmpeq"; break;
	  case '#': jump = "if_icmpneq"; break; //TODO procurar instrução not para fazer esse comando
	  case '<': jump = "isub\niflt"; break;
	  case '>': jump = "swap\nisub\niflt"; break;
	}

        if (jump == null) {
            System.out.println("Entrei aki");
            throw new UnsupportedOperationException("comparison");            
        }

        if(getString(value).equals("do") || getString(value).equals("then")) {
	emit(jump + " L"+ l1);
//	emit("xor ax, ax");
	emit("goto L" + l2);
//	emit("L" + l1 + ":");
//	emit("bipush -1");
	emit("L" + l1 + ":");
        }
}

    // jump
    protected void instructionJmp(int label) {
        emit("goto L" + label);
//	emit("jmp L" + label);
    }

    // jump if false (0)
    protected void instructionJmpFalse(int label) {
        throw new UnsupportedOperationException("jmpfalse");
//	emit("jz L" + label);
    }

    // read a value to the primary register and stores in a variable
    protected void instructionRead() {
        throw new UnsupportedOperationException("read");
//	emit("call READ");
//	instructionStore( getString(value) );
    }

    // shows primary register value
    protected void instructionWrite() {
        throw new UnsupportedOperationException("write");
//	emit("call WRITE");
    }

    private void semicolon() throws ExpectedException {
        System.out.println("VARRRR: " + getString(value));
        if(getString(value).equals("integer") || getString(value).equals("real") || getString(value).equals("boolean") || getString(value).equals("do") || getString(value).equals("then"))
            nextToken();
        if (token == ';')
            nextToken();

    }

    private void semitoken() throws ExpectedException {
        System.out.println("semiToken: " + token);
        if (token == ':')
            nextToken();
    }

    // Prolog of Main routine
    protected void instructionProlog() {
//	emit("MAIN:");
//	emit("\tmov ax, PROG");
//	emit("\tmov ds, ax");
//	emit("\tmov es, ax");
    }

    // Epilog of Main routine
    protected void instructionEpilog() {
//	emit("\tmov ax,4C00h");
//	emit("\tint 21h");
//	emit("PROG ends");
//	emit("\tend MAIN");
    }
}
