        /**
 * Esta clase construye los nodos del árbol de expresión
 */
class NodeArbol {
    /**
     * El dato dentro del nodo
     */
    protected Object data;
    /**
     * El hijo izquierdo de un nodo
     */
    protected NodeArbol left;
    /**
     * El hijo derecho de un nodo
     */
    protected NodeArbol right;

    /**
     * Constructor de la clase NodeArbol
     * @param data El dato a insertar dentro del nodo
     */
    public NodeArbol(Object data) {
        this.data = data;
        left = right = null;
    }
}

/**
 * Esta clase construye y define los nodos de las pilas/stacks.
 */
class NodePila {

    /**
     * Define el dato de la pila
     */
    protected NodeArbol data;
    /**
     * Puntero de los nodos en la pila
     */
    protected NodePila next;

    /**
     * Constructor del nodo de la pila
     * @param x Dato del nodo
     */
    public NodePila(NodeArbol x) {
            this.data = x;
            next = null;
    }
}

/**
 *Clase que maneja las pilas de los datos del árbol.
 */
class PilaArbolExp {
    /**
     * Primer elemento de la pila.
     */
    private NodePila tope;

    /**
     * Constructor de la clase
     */
    public PilaArbolExp() {
        tope = null;
    }

    /**
     * El método añade elementos al tope de la pila
     * @param elemento Dato que se desea añadir a la pila.
     */
    public void insertar(NodeArbol elemento) {
        NodePila nuevo;
        nuevo = new NodePila(elemento);
        nuevo.next = tope;
        tope = nuevo;
    }

    /**
     * Método booleano que verifica si la pila no tiene elementos
     * @return Booleano de si la pila stá vacía
     */
    public boolean pilaVacia() {
        return tope == null;
    }

    /**
     * Método que encuentra el tope de la lista
     * @return El primer elemento de la lista
     */
    public NodeArbol topePila() {
            return tope.data;
    }

    /**
     * Elimina el tope de la pila
     * @return el elemento quitado de la lista
     */

    public NodeArbol quitar() {
        NodeArbol aux = null;
        if (!pilaVacia()) {
            aux = tope.data;
            tope = tope.next;
        }
        return aux;
    }
}

/**
 * Crea un árbol de expresión cuando es una operación aritmética,
 * asi como su solución.
 */

class ArbolBinarioExp {
    /**
     * La raíz del árbol de expresión
     */
    NodeArbol raiz;
    public ArbolBinarioExp() {
        raiz = null;
    }

    /**
     * Constructor de la clase. Define el valor de la raíz del árbol
     * @param cadena La operación recibida por parte del cliente
     */
    public ArbolBinarioExp(String cadena) {
            raiz = creaArbolBE(cadena);
    }

    /**
     * Método que crea un subarbol en el árbol.
     * @param dato2 Hijo derecho
     * @param dato1 Hijo izquierdo
     * @param operador Raíz del subárbol
     * @return El operador que sirve como raíz del subarbol
     */
    public NodeArbol creaSubArbol(NodeArbol dato2, NodeArbol dato1, NodeArbol operador) {
        operador.left = dato1;
        operador.right = dato2;
        return operador;
    }

    /**
     * Lee una cadena de texto en
     * @param subArbol El subarbol que se va a recorrer
     * @param c El string que se va a recorrer
     * @return La cadena recorrida
     */
    private String inOrden(NodeArbol subArbol, String c) {
        String cadena;
        cadena = "";
        if (subArbol != null) {
            cadena = c + inOrden(subArbol.left, c) + subArbol.data.toString() + "\n" +
                    inOrden(subArbol.right, c);
        }
        return cadena;
    }

    /**
     * Método devuelve una representacion textual del Objeto
     * @return La cadena de texto recorrida
     */
    public String toString() {
        String cadena = "";
        cadena = inOrden(raiz, cadena);
        return cadena;
    }

    /**
     * Define la prioridad de los operadores, siguiendo la regla PEMDAS
     * @param c El operador evaluado
     * @return La prioridad de ese operador
     */
    private int prioridad(char c) {
        int p = 100;
        switch (c) {
            case '^':
                p = 30;
                break;
            case '%':
                p = 25;
                break;
            case '*':
            case '/':
                p = 20;
                break;
            case '+':
            case '-':
                p = 10;
                break;
            default:
                p = 0;

        }
        return p;
    }

    /**
     * Define si el caracter evaluado es un operador
     * @param c El caracter evaluado
     * @return Booleano que determina si es operador
     */
    private boolean esOperador(char c) {
        boolean resultado;
        switch (c) {
            case '(':
            case ')':
            case '^': //potencia
            case '*':
            case '/':
            case '+':
            case '-':
            case '%':
                resultado = true;
                break;
            default:
                resultado = false;
        }
        return resultado;
    }

    /**
     * Crea el Arbol de expresión aritmético
     * @param cadena La operación aritmética recibida por el cliente
     * @return El árbol de expresión
     */
    private NodeArbol creaArbolBE(String cadena) {

        PilaArbolExp pilaOperadores;
        PilaArbolExp pilaExpresiones;
        NodeArbol token,token1;
        NodeArbol op1;
        NodeArbol op2;
        NodeArbol op;

        pilaOperadores = new PilaArbolExp();
        pilaExpresiones = new PilaArbolExp();
        char caracterEvaluado;
        char caracteranterior;
        String valor = "";

        for (int i = 0; i < cadena.length(); i++) {
            caracterEvaluado = cadena.charAt(i);
            caracteranterior = 0;
            token = new NodeArbol(caracterEvaluado);
            token1 = new NodeArbol(valor);

            if (i>=1){
                caracteranterior =cadena.charAt(i-1);
            }

            if (Character.isDigit(caracterEvaluado) || caracterEvaluado == '.') {
                StringBuilder numero = new StringBuilder();
                boolean decimal = false;
                while (i < cadena.length() && (Character.isDigit(cadena.charAt(i))|| cadena.charAt(i) == '.')){
                    char c = cadena.charAt(i);
                    if (c=='.' && decimal){
                        break;
                    }
                    if (c=='.'){
                        decimal = true;
                    }
                    numero.append(cadena.charAt(i));
                    i++;
                }
                i--;
                NodeArbol numeroNodo = new NodeArbol(numero);
                pilaExpresiones.insertar(numeroNodo);
            } else if (!esOperador(caracterEvaluado)) {
                valor += caracterEvaluado;

            } else { /*es un operador*/
                switch (caracterEvaluado) {
                    case '(':
                        pilaOperadores.insertar(token);
                        break;
                    case ')':
                        while (!pilaOperadores.pilaVacia() &&
                                !pilaOperadores.topePila().data.equals('(')) {
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        /*
                         * Lo quitamos de la pila de operadores, porque
                         * el parentesis no forma parte de nuestra expresión
                         */
                        pilaOperadores.quitar();
                        break;
                    default:
                        if (caracterEvaluado == '+'){
                        }
                        if (caracterEvaluado == '-'){
                            if (!(caracteranterior == ')')){
                                if (valor.equals("")){
                                    token1 = new NodeArbol(0);
                                    pilaExpresiones.insertar(token1);
                                }
                            }
                        }
                        while (!pilaOperadores.pilaVacia() && prioridad(caracterEvaluado)
                                <= prioridad(pilaOperadores.topePila().data.toString().charAt(0))) {
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        pilaOperadores.insertar(token);

                }
            }
        }
        /*
         * En el caso que la pila de operadores no este vacia
         */
        while (!pilaOperadores.pilaVacia()) {
            op2 = pilaExpresiones.quitar();
            op1 = pilaExpresiones.quitar();
            op = pilaOperadores.quitar();
            op = creaSubArbol(op2, op1, op);
            pilaExpresiones.insertar(op);
        }
        /*
         * Al final se retorna el arbol completo de expresiones
         */
        op = pilaExpresiones.quitar();
        return op;
    }

    /**
     * Método que recoge el resultado
     * @return Solución de la operación
     */
    public double evaluaAritmetica() {
            return evalua(raiz);
    }

    /**
     * Resuelve la operación matemática
     * @param subarbol El subarbol que comenzará la evaluación y solución de la operación
     * @return El resultado de la operación
     */
    private double evalua(NodeArbol subarbol) {
        double acum = 0;
        if (!esOperador(subarbol.data.toString().charAt(0))) {
                return Double.parseDouble(subarbol.data.toString());
        }
        else {
            switch (subarbol.data.toString().charAt(0)) {
                case '^':
                    acum = Math.pow(evalua(subarbol.left), evalua(subarbol.right));
                    break;
                case '*':
                    acum = evalua(subarbol.left) * evalua(subarbol.right);
                    break;
                case '/':
                    acum = evalua(subarbol.left) / evalua(subarbol.right);
                    break;
                case '+':
                    acum = evalua(subarbol.left) + evalua(subarbol.right);
                    break;
                case '-':
                    acum = evalua(subarbol.left) - evalua(subarbol.right);
                    break;
                case '%':
                    acum =  ((evalua(subarbol.left)) * evalua(subarbol.right)) / 100;
                    break;
                }
        }
        return acum;
    }
}

/**
 * Crea un árbol de expresión cuando es una operación lógica,
 * asi como su solución.
 */
class ArbolLogico {
    /**
     * La raíz del árbol de expresión
     */
    NodeArbol raizlogica;

    public ArbolLogico() {
        this.raizlogica = null;
    }

    /**
     * Constructor de la clase. Define el valor de la raíz del árbol
     * @param cadena La operación recibida por parte del cliente
     */

    public ArbolLogico(String cadena) {
        raizlogica = creaArbolLogicoBE(cadena);
    }

    /**
     * Método que crea un subarbol en el árbol.
     * @param dato2 Hijo derecho
     * @param dato1 Hijo izquierdo
     * @param operador Raíz del subárbol
     * @return El operador que sirve como raíz del subarbol
     */
    public NodeArbol creaSubArbol(NodeArbol dato2, NodeArbol dato1, NodeArbol operador) {
        operador.left = dato1;
        operador.right = dato2;
        return operador;
    }

    /**
     * Lee una cadena de texto en
     * @param subArbol El subarbol que se va a recorrer
     * @param c El string que se va a recorrer
     * @return La cadena recorrida
     */
    private String inOrden(NodeArbol subArbol, String c) {
        String cadena;
        cadena = "";
        if (subArbol != null) {
            cadena = c + inOrden(subArbol.left, c) + subArbol.data.toString() + "\n" +
                    inOrden(subArbol.right, c);
        }
        return cadena;
    }

    /**
     * Método devuelve una representacion textual del Objeto
     * @return La cadena de texto recorrida
     */
    public String toString() {
        String cadena = "";
        cadena = inOrden(raizlogica, cadena);
        return cadena;
    }

    /**
     * Define la prioridad de los operadores lógicos
     * @param c El operador evaluado
     * @return La prioridad de ese operador
     */
    private int prioridad(char c) {
        int p = 100;
        switch (c) {
            case '&':
            case '|':
            case '~':
            case '?':
                p = 10;
                break;
            default:
                p = 0;
        }
        return p;
    }

    /**
     * Define si el caracter evaluado es un operador lógico
     * @param c El caracter evaluado
     * @return Booleano que determina si es operador lógico
     */
    private boolean esOperador(char c) {
        boolean resultado;
        switch (c) {
            case '&': //operador logico AND
            case '|': //operador logico OR
            case '~': //operador logico NOT
            case '?': //operador logico XOR
            case '%':
                resultado = true;
                break;
            default:
                resultado = false;
        }
            return resultado;
    }

    /**
     * Crea el Arbol de expresión lógico
     * @param cadena La operación lógico recibida por el cliente
     * @return El árbol de expresión
     */
    private NodeArbol creaArbolLogicoBE(String cadena) {
        PilaArbolExp pilaOperadores;
        PilaArbolExp pilaExpresiones;
        NodeArbol token;
        NodeArbol op1;
        NodeArbol op2;
        NodeArbol op;

        pilaOperadores = new PilaArbolExp();
        pilaExpresiones = new PilaArbolExp();
        char caracterEvaluado;
        for (int i = 0; i < cadena.length(); i++) {
            caracterEvaluado = cadena.charAt(i);
            token = new NodeArbol(caracterEvaluado);
            if (Character.isLetter(caracterEvaluado)) {
                StringBuilder logico = new StringBuilder();
                while (i < cadena.length() && Character.isLetter(cadena.charAt(i))) {
                    logico.append(cadena.charAt(i));
                    i++;
                }
                i--;
                NodeArbol logicoNodo = new NodeArbol(logico);
                pilaExpresiones.insertar(logicoNodo);
            } else if (!esOperador(caracterEvaluado)) {

            } else { /**es un operador*/
                switch (caracterEvaluado) {
                    case '(':
                            pilaOperadores.insertar(token);
                            break;
                    case ')':
                        while (!pilaOperadores.pilaVacia() &&
                                !pilaOperadores.topePila().data.equals('(')) {
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        /**
                         * Lo quitamos de la pila de operadores, porque
                         * el parentesis no forma parte de nuestra expresión
                         */
                        pilaOperadores.quitar();
                        break;
                    default:
                        while (!pilaOperadores.pilaVacia() && prioridad(caracterEvaluado)
                                <= prioridad(pilaOperadores.topePila().data.toString().charAt(0))) {
                            op2 = pilaExpresiones.quitar();
                            op1 = pilaExpresiones.quitar();
                            op = pilaOperadores.quitar();
                            op = creaSubArbol(op2, op1, op);
                            pilaExpresiones.insertar(op);
                        }
                        pilaOperadores.insertar(token);

                }
            }

        }
        /*
         * En el caso que la pila de operadores no este vacia
         */
        while (!pilaOperadores.pilaVacia()) {
            op2 = pilaExpresiones.quitar();
            op1 = pilaExpresiones.quitar();
            op = pilaOperadores.quitar();
            op = creaSubArbol(op2, op1, op);
            pilaExpresiones.insertar(op);
        }
        /*
         * Al final se retorna el arbol completo de expresiones
         */
        op = pilaExpresiones.quitar();
        return op;
    }

    /**
     * Método que recoge el resultado
     * @return Solución de la operación
     */
    public boolean evaluaLogico() {
        return evalualogico(raizlogica);
    }

    /**
     * Resuelve la operación lógica
     * @param subarbol El subarbol que comenzará la evaluación y solución de la operación
     * @return El resultado de la operación
     */
    private boolean evalualogico(NodeArbol subarbol) {
        boolean acum = false;
        if (subarbol == null) {
        // Manejar el caso en el que subarbol es nulo
        return false; // O cualquier otro valor predeterminado
        }
        char caracter = subarbol.data.toString().charAt(0);
        if (!esOperador(subarbol.data.toString().charAt(0)) && Character.isLetter(caracter)) {
            return Boolean.parseBoolean(subarbol.data.toString());
        } else {
            switch (subarbol.data.toString().charAt(0)) {
                case '&':
                    acum = evalualogico(subarbol.left) && evalualogico(subarbol.right);
                    break;
                case '|':
                    acum = evalualogico(subarbol.left) || evalualogico(subarbol.right);
                    break;
                case '~':
                    acum = !evalualogico(subarbol.right);
                    break;
                case '?':
                    acum = evalualogico(subarbol.left) ^ evalualogico(subarbol.right);
                    break;

            }
        }
        return acum;
    }
}