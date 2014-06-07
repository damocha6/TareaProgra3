
//Tarea Programada 3
//Daniel Mora - Gerardo Calderon - Edgar Solorzano

//Importacion de clases necesarias para la ejecucion del programa
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.io.*;
import java.util.*;
import java.lang.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;


//Definicion de la clase ambiente y sus constructores necesarios 
public class Ambiente {

Ambiente () {}

//Variables globales para almacenar los datos

private static String[] Variable = new String[50];
private static String[] Tipo = new String[50];
private static String[] Valor = new String[50];
private static int Indice = 0;
private static int Cantidad = 0;
private static int Indice2 = 0;
private static int Indice3 = 0;
private static int Indice4 = 0;
private static int Indice5 = 0;
private static String Valor2;

//___________________________________________________________________________________________________________//

//Funcion para leer el archivo de SML

public void LecturaArchivo(String nombre){
	try {
		File f;
		FileReader lector;
		f = new File(nombre);
		lector = new FileReader(f);
		BufferedReader br = new BufferedReader(lector);
		String expresion = "";
		String aux = "";
		while(true) {
			aux = br.readLine();
			if(aux != null) {
				expresion=expresion+aux;
				Separar(expresion);
				expresion = "";
				Cantidad = 0;
			}
			else
				break;
			    }
		br.close();
		lector.close();
	}
	catch(IOException e) {
	System.out.println("Error: " + e.getMessage());
	}
}

//___________________________________________________________________________________________________________//

//Funcion que sepera por espacios cada la linea y lo guarda en un arreglo cada palabra

public void Separar (String expresion) {
	String[] E1 = new String[100];
	int cont = 0;
	StringTokenizer st = new StringTokenizer(expresion, " \n");
	while (st.hasMoreTokens()){
		E1[cont] = st.nextToken().toString();
		cont++;
	}
	Cantidad = cont;
	Validar(E1);
}

//___________________________________________________________________________________________________________//

//Funcion que analiza y valida las lineas del archivo para obtener los valores y tipos

public void Validar (String[] Arreglo) {
	int contador = 0;
	while (contador < Cantidad) {
		if (Arreglo[contador].equals("let")) {
			Expresion_Let(Arreglo, contador + 2);
			if (Tipo[Indice] != null)
				break;
		}
		else {
			if (Arreglo[contador].contains("val")) {
				contador++;
				Variable[Indice] = Arreglo[contador];
				Determinar_Valor(Arreglo,contador + 2);
				if (Tipo[Indice] != null)
					break;
			}
		}
		contador++;
	}
}



//___________________________________________________________________________________________________________________________//

//Funcion que evalua las expresiones let

public void Expresion_Let (String[] Arreglo, int exp) {
	String[] VariableTemporal = new String [30];
	String[] ValorTemporal = new String [30];
	int IndiceTemporal = 0;
	VariableTemporal[IndiceTemporal] = Arreglo[exp];
	exp = exp + 2;
	ValorTemporal[IndiceTemporal] = Arreglo[exp];
	exp++;
	if (Arreglo[exp].equals("val")) {
		exp++;
		IndiceTemporal++;
		VariableTemporal[IndiceTemporal] = Arreglo[exp];
		exp = exp + 2;
		ValorTemporal[IndiceTemporal] = Arreglo[exp];		
	}
	else {
		exp++;
		if (Arreglo[exp].equals("val")) {
			exp++;
			Variable[Indice]=Arreglo[exp];
			exp = exp + 2;
			String Ev ="";
			int i2 = exp;
			while (!Arreglo[i2].equals("val") || !Arreglo[i2].equals("end")) {
				if (Arreglo[i2].equals("end") || Arreglo[i2].equals("val")) {
					Indice5 = i2;
					break;
				}
				if (Arreglo[i2].equals(VariableTemporal[0]))
					Ev = Ev + ValorTemporal[0];
				else if (Arreglo[i2].equals(VariableTemporal[1]))
					Ev = Ev + ValorTemporal[1];
				else 
					Ev = Ev + Arreglo[i2];
				i2++;
			}
			Tipo[Indice]=Eval_Exp(Ev);
			Indice5 = i2;
		}
		else {
			if (IsVariableiable(Arreglo[exp]) == true) {
				Variable[Indice]=Arreglo[exp];
				exp = exp + 2;
				String Ev ="";
				int i2 = exp;
				while (!Arreglo[i2].equals("val") || !Arreglo[i2].equals("end")) {
					if (Arreglo[i2].equals("end") || Arreglo[i2].equals("val")) {
						Indice5 = i2;
						break;
					}
					if (Arreglo[i2].equals(VariableTemporal[0]))
						Ev = Ev + ValorTemporal[0];
					else if (Arreglo[i2].equals(VariableTemporal[1]))
						Ev = Ev + ValorTemporal[1];
					else 
						Ev = Ev + Arreglo[i2];
					i2++;
				}
				Tipo[Indice]=Eval_Exp(Ev);
				Indice5 = i2;
			}
			else {
				JOptionPane.showMessageDialog(null,"Variableiable no existente o no declarada","Warning",JOptionPane.WARNING_MESSAGE);
				System.exit(0);
			}
			Cantidad = Indice5;
		}
	}
}


//_______________________________________________________________________________________________________________________//

//Funcion encargada de determinar el valor y el tipo de las expresiones

public void Determinar_Valor(String[] Arreglo, int exp) {
	while (exp < Cantidad) {
		if (IsNumber(Arreglo[exp]) == true){ //Verifica si la expresion es un integer
			Tipo[Indice] = "int"; //agrega el tipo 'int'
			Valor[Indice] = Arreglo[exp]; //agrega el valor del integer
			Indice++;
		}
		else if (Arreglo[exp].startsWith("'")) { //Verifica si la expresion es un string
			Tipo[Indice] = "str"; //agrega el tipo 'str'
			Valor[Indice] = Arreglo[exp]; //agrega el valor del string
			Indice++;
		}
		else if (Arreglo[exp].equals("true") || Arreglo[exp].equals("false")) { //Verifica si la expresion es un booleano
			Tipo[Indice] = "bool"; //Agrega el tipo 'bool'
			Valor[Indice] = Arreglo[exp]; //Agrega true o false dependiendo de la expresion
			Indice++;
		}
		else if (Arreglo[exp].startsWith("(")) { //Verifica si la expresion es una tupla
			Tipo[Indice] = Valores_Tupla(Arreglo[exp]);
			Valor[Indice] = Arreglo[exp];
			Indice++;
		}
		else if (Arreglo[exp].startsWith("[")) { //Verifica si la expresion es una lista
			Tipo[Indice] = Valores_Lista(Arreglo[exp]);
			Valor[Indice] = Arreglo[exp];
			Indice++;
		}
		else if (Arreglo[exp].startsWith("if")) { //Verifica si la expresion es una condicion if
			Tipo[Indice]=If_Then_Else(Arreglo,exp+1);
			exp = Indice4;
		}
		else {
			Tipo[Indice] = Eval_Exp(Arreglo[exp]);
			Valor[Indice] = Valor2;
			Indice++;
		}
	exp++;
	}
}

//____________________________________________________________________________________________________________________________//

//Funcion que determina el valor de la variable por medio del if-then-else y devuelve el tipo de variable

public String If_Then_Else (String[] condiciones, int Ind) {
	String Resultado = "";
	String valor1 = "";
	String valor2 = "";
	String operador = "";
	String Booleano = "";
	for (int elem = Ind; elem <= Cantidad; elem++) {
		if (IsVariableiable(condiciones[elem]) == true) {
			valor1 = Valor[Indice3];
		}
		else if (IsNumber(condiciones[elem]) == true)
			valor1 = condiciones[elem];
		elem++;
		operador = condiciones[elem];
		elem++;
		if (IsVariableiable(condiciones[elem]) == true) {
			valor2 = Valor[Indice3];
		}
		else if (IsNumber(condiciones[elem]) == true)
			valor2 = condiciones[elem];
		Booleano = ResultadoExpresion(valor1,valor2,operador);
		if (Booleano.equals("true")) {
			elem++;
			Resultado = Go_To_Then(condiciones, elem);
			elem = Indice4;
		}
		else {
			while (!condiciones[elem].equals("else")) {
				elem++;
			}
			Resultado = Expresion_Then(condiciones, elem);
			elem = Indice4;
		}
		elem = Indice4;
	}
	return Resultado;
}

//____________________________________________________________________________________________________________________________//

//Funcion que hace las operaciones a partir del then, en relación a la secuencia if-then-else

public String Go_To_Then(String[] operacion, int Ind) {
	String Res = "";
	if (!operacion[Ind].equals("then")) {
		JOptionPane.showMessageDialog(null,"No existe Then: Expresion Invalida","Warning",JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}
	else {
		Ind ++;
		String Ev = "";
		while (!operacion[Ind].equals("else")) {
			Ev = Ev + operacion[Ind];
			Ind++;
		}
		Res = Eval_Exp(Ev);
		while (operacion[Ind] != null) {
			Ind++;
		}
		Indice4 = Ind;
	}
	return Res;
}

//_____________________________________________________________________________________________________________________________//

//Funcion que hace las operaciones a partir del else, en relación a la secuencia if-then-else

public String Expresion_Then(String[] operacion, int Ind) {
	String Resultado = "";
	if (!operacion[Ind].equals("else")) {
		JOptionPane.showMessageDialog(null,"No existe Else: Expresion Invalida","Warning",JOptionPane.WARNING_MESSAGE);
		System.exit(0);
	}
	else {
		Ind++;
		String Evaluado = "";
		while (operacion[Ind] != null) {
			Evaluado = Evaluado + operacion[Ind];
			Ind++;
		}
		Resultado = Eval_Exp(Evaluado);
		Indice4 = Ind;
	}
	return Resultado;
}

//__________________________________________________________________________________________________________________________//

//Funcion que evalua las expresiones que no son ni "if-then-else" ni expresiones let

public String Eval_Exp(String expresion) {
	String Tipo = "";
	String valor1 = "";
	String valor2 = "";
	String operador = "";
	String[] num1 = new String[1];
	String[] resulta = expresion.split("(?<=[-+*/<>])|(?=[-+*/<>])");
	for (int t = 0; t < resulta.length; t++) {
		t = 0;
		if (IsNumber(resulta[t]) && resulta[t+1] == null) {
			Valor[Indice] = resulta[t];
			break;
		}
		if (IsVariableiable(resulta[t]) == true) {
			valor1 = Valor[Indice3];
		}
		else if (IsNumber(resulta[t]) == true) {
			valor1 = resulta[t];
		}
		t++;
		operador = resulta[t];
		t++;
		if (IsVariableiable(resulta[t]) == true) {
			valor2 = Valor[Indice3];
		}
		else if (IsNumber(resulta[t]) == true)
			valor2 = resulta[t];
		num1[0] = ResultadoExpresion(valor1,valor2,operador);
		resulta = Eliminar(resulta);
		if (num1[0].equals("true") || num1[0].equals("false"))
			Tipo = "bool";
		else 
			Tipo = "int";
		resulta = Concatenar(resulta,num1);
	}
	return Tipo;
}

//_______________________________________________________________________________________________________________________//

//Funcion que elimina los 3 primeros elementos del arreglo para su posterior uso

public String[] Eliminar(String[] Entra) {
	String[] Sale = new String [20];
	int el = 3;
	int o = 0;
	int q = 0;
	while (q < Entra.length && Entra[q] != null) {
		if (q >= el) {
			Sale[o] = Entra[q];
			o++;
			q++;
		}
		q++;
	}
	return Sale;
}

//_______________________________________________________________________________________________________________________________//

//Funcion que imprime los contenidos del arreglo enviado

public void Imprimir(String[] result) {
	for (int b = 0; b < result.length; b++) {
		System.out.println(result[b]);
	}
}

//_______________________________________________________________________________________________________________________________//

//Funcion que recibe 2 valores y un operador para posteriormente ser resueltos

public String ResultadoExpresion (String Valor1, String Valor2, String Operador) {
	int resultado = 0; //Almacena el resultado de la operacion
	String ResultadoString = ""; //Almacena el resultado en forma de string
	if (Operador.equals("+")) { // Si el operador es un +
		resultado = Integer.parseInt(Valor1) + Integer.parseInt(Valor2); //operacion de suma
		ResultadoString = Integer.toString(resultado);
		Valor2 = ResultadoString;
	}
	if (Operador.equals("-")) { // Si el operador es un -
		resultado = Integer.parseInt(Valor1) - Integer.parseInt(Valor2); //operacion de resta
		ResultadoString = Integer.toString(resultado);
		Valor2 = ResultadoString;
	}
	if (Operador.equals("*")) { // Si el operador es un *
		resultado = Integer.parseInt(Valor1) * Integer.parseInt(Valor2); //operacion de la multiplicación
		ResultadoString = Integer.toString(resultado);
		Valor2 = ResultadoString;
	}
	if (Operador.equals("/")) {// Si el operador es un /
		resultado = Integer.parseInt(Valor1) / Integer.parseInt(Valor2); //operacion de division entera
		ResultadoString = Integer.toString(resultado);
		Valor2 = ResultadoString;
	}
	if (Operador.equals("%")) { // Si el operador es un % 
		resultado = Integer.parseInt(Valor1) % Integer.parseInt(Valor2); //operacion de division modulo
		ResultadoString = Integer.toString(resultado);
		Valor2 = ResultadoString;
	}
	if (Operador.equals("<")) { // Si el operador es un <
		ResultadoString = Comparar(Valor1,Valor2,Operador); //operacion menor que
		Valor2 = ResultadoString;
	}
	if (Operador.equals(">")) { // Si el operador es un >
		ResultadoString = Comparar(Valor1,Valor2,Operador); //operacion de mayor que
		Valor2 = ResultadoString;
	}
	return ResultadoString;
}

//Funcion que concatena 2 arreglos o concatena un elemento al inicio del arreglo
public String[] Concatenar(String[] result, String[] a) {
	List list = new ArrayList(Arrays.asList(a));
    list.addAll(Arrays.asList(result));
    Object[] c = list.toArray();
    String[] stringArray = Arrays.copyOf(c, c.length, String[].class);
    return stringArray;
}

//_______________________________________________________________________________________________________________________________// 

//Funcion que compara dos valores y devuelve verdadero o falso

public String Comparar (String Valor1, String Valor2, String Operador) {
	String A = "";
	if (Operador .equals("<")) {
		if (Integer.parseInt(Valor1) < Integer.parseInt(Valor2))
			A = "true";
		else
			A = "false";
	}
	else if (Operador.equals(">")) {
		if (Integer.parseInt(Valor1) > Integer.parseInt(Valor2))
			A ="true";
		else
			A = "false";
	}
	return A;
}

//_______________________________________________________________________________________________________________________________//

//Funcion que determina los valores dentro de la tupla y devuelve el tipo de la tupla

public String Valores_Tupla (String Tupla) {
	if (Tupla.startsWith("(")) {
		Tupla = Tupla.substring(1, Tupla.length());
		if (Tupla.endsWith(")")) {
			Tupla = Tupla.substring(0, Tupla.length()-1);
		}
	}
	String[] elementos = Tupla.split(",");
	String Valor_Tupla = "";
	for (int i = 0; i < elementos.length; i++){
		if (IsNumber(elementos[i]) == true){ //Verifica si el elemento es un numero
			Valor_Tupla = Valor_Tupla + "int"; //Si es numero devuelve el tipo int
			if ( i != elementos.length - 1)
				Valor_Tupla = Valor_Tupla + "*";
		}
		else if (elementos[i].startsWith("'")) {
			Valor_Tupla = Valor_Tupla + "str"; //Si es string devuelve str
			if ( i != elementos.length - 1)
				Valor_Tupla = Valor_Tupla + "*";
		}
		else if (elementos[i].equals("true") || elementos[i].equals("false")) {
			Valor_Tupla = Valor_Tupla + "bool";
			if ( i != elementos.length - 1)
				Valor_Tupla = Valor_Tupla + "*";
		}
		else if (elementos[i].startsWith("(")) {
			  Valor_Tupla = Valor_Tupla + "(";
			  int j=0;
			  if (elementos[i].endsWith(")")) {
				  Valor_Tupla = Valor_Tupla + Valores_Tupla(elementos[i]);
				  Valor_Tupla = Valor_Tupla + ")";
			  }
			  else {
				  for (int k = i+1; k<elementos.length; k++) {
					  	if (!elementos[k].endsWith(")")) {
					  			elementos[i] = elementos[i] + ",";
					  			elementos[i] = elementos[i] + elementos[k];
					  	}
					  	else {
					  		elementos[i] = elementos[i] + ",";
					  		elementos[i] = elementos[i] + elementos[k];
					  	}
					  	j=k;
				  }
				  Valor_Tupla = Valor_Tupla + Valores_Tupla(elementos[i]);
				  i=j;
				  Valor_Tupla = Valor_Tupla + ")";
			  }
			  if ( i != elementos.length - 1 )
				  Valor_Tupla = Valor_Tupla + "*";
		}
		else if (elementos[i].startsWith("[")) {
			if (elementos[i].equals("[]")) {
				Valor_Tupla = Valor_Tupla + "'a list";
			}
			else {
				Valor_Tupla = Valor_Tupla + Valores_Lista(elementos[i]);
				if ( i != elementos.length - 1)
					Valor_Tupla = Valor_Tupla + "*";
			}
		}
	}
	if (Valor_Tupla.endsWith("*"))
		Valor_Tupla = Valor_Tupla.substring(0, Valor_Tupla.length()-1);
	return Valor_Tupla;
}

//_______________________________________________________________________________________________________________________________//

//Funcion que determina los valores internos de la lista y devuelve el tipo de la lista

public String Valores_Lista (String Lista) {
	String Valor_Lista = "";
	if (Lista.startsWith("[")) {
		Lista = Lista.substring(1, Lista.length());
		if (Lista.endsWith("]")) {
			Lista = Lista.substring(0, Lista.length()-1);
		}
	}
	String[] elementos = Lista.split(",");
	String tempo = "";;
	int j = 0;
	if (IsNumber(elementos[0]) == true)
		Valor_Lista = Valor_Lista + "int";
	else if (elementos[0].startsWith("'"))
		Valor_Lista = Valor_Lista + "str";
	else if (elementos[0].equals("true") || elementos[0].equals("false"))
		Valor_Lista = Valor_Lista + "bool";
	else if (elementos[0].startsWith("(")) {
		Valor_Lista = Valor_Lista + "(";
		if (elementos[0].endsWith(")")) {
			Valor_Lista = Valor_Lista + Valores_Tupla(elementos[0]);
		}
		else {
			for (int k = 0; k<elementos.length; k++) {
				if (!elementos[k].endsWith(")")) {
		  				tempo = elementos[0] + ",";
		  				tempo = elementos[0] + elementos[k];
				}
				else {
					elementos[0] = elementos[0] + ",";
					elementos[0] = elementos[0] + elementos[k];
				}
				j=k;
			}
		}
		tempo = "(" + Valores_Tupla(elementos[0]);
		Valor_Lista = tempo;
		Valor_Lista = Valor_Lista + ")";
	}
	if (elementos.length == 1) {
		Valor_Lista = Valor_Lista + " list";
	}
	else {
		for (int elem2 = 1; elem2 < elementos.length; elem2++) {
			if (Valor_Lista.equals("int")) {
				if (IsNumber(elementos[elem2]) == true) {
					elem2++;
					if (elementos.length == 2) {
						if (IsNumber(elementos[1]) == false){
							JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
							System.exit(0);
						}
						elem2 = 1;
						Valor_Lista = Valor_Lista + " list";
					}
					else {
						for (int elem3 = elem2; elem3 < elementos.length; elem3++){
							if (IsNumber(elementos[elem3]) == false){
								JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
							elem2 = elem3;
						}
						Valor_Lista = Valor_Lista + " list";
					}
				}
			}
			else if (Valor_Lista.equals("str")) {
				if (elementos[elem2].startsWith("'")) {
					elem2++;
					if (elementos.length == 2) {
							if (!elementos[1].startsWith("'")) {
								JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
							elem2 = 1;
							Valor_Lista = Valor_Lista + " list";
					}
					else {
						for (int elem3 = elem2; elem3 < elementos.length; elem3++) {
							if (!elementos[elem3].startsWith("'")){
								JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
							elem2 = elem3;
						}
						Valor_Lista = Valor_Lista + " list";
					}
				}
			}
			else if (Valor_Lista.equals("bool")) {
				if (elementos[elem2].equals("true") || elementos[elem2].equals("false")) {
					elem2++;
					if (elementos.length == 2) {
						if (!elementos[1].equals("true") && !elementos[1].equals("false")){
							JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
							System.exit(0);
						}
						elem2 = 1;
						Valor_Lista = Valor_Lista + " list";
					}
					else {
						for (int elem3 = elem2; elem3 < elementos.length; elem3++) {
							if (!elementos[elem3].equals("true") && !elementos[elem3].equals("false")){
								JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
							elem2 = elem3;
						}
						Valor_Lista = Valor_Lista + " list";
					}
				}
			}
			else if (Valor_Lista.contains("*") || Valor_Lista.contains("int") || Valor_Lista.contains("bool") || Valor_Lista.contains("str") || Valor_Lista.startsWith("(")) {
				elem2 = j;
				String tempo2="";
				if (elementos[elem2].startsWith("(")) {
					if (elementos[elem2].endsWith(")")) {
						tempo2 = Valores_Tupla(elementos[elem2]);
						Valor_Lista = Valor_Lista + " list";
					}
					else {
						for (int k = 0; k<elementos.length; k++) {
							if (!elementos[k].endsWith(")")) {
					  				tempo2 = elementos[0] + ",";
					  				tempo2 = elementos[0] + elementos[k];
							}
							else {
								tempo2 = elementos[0] + ",";
								tempo2 = elementos[0] + elementos[k];
							}
							j=k;
						}
					}
					String temp = Valores_Tupla(tempo2);
					elem2++;
					if (elem2 >= elementos.length) {
						if (!Valor_Lista.equals(temp)){
							JOptionPane.showMessageDialog(null,"Sintaxis InvÃ¡lida de Lista: Tipos HeterogÃ©neos","Warning",JOptionPane.WARNING_MESSAGE);
							System.exit(0);
						}
						Valor_Lista = Valor_Lista + " list";
					}
					else {
						for (int elem3 = elem2; elem3 < elementos.length; elem3++) {
							String Nueva = CrearTupla (elementos,"",elem2);
							if (!Valor_Lista.equals(Nueva)) {
								JOptionPane.showMessageDialog(null,"Sintaxis Invalida de Lista: Tipos Heterogeneos","Warning",JOptionPane.WARNING_MESSAGE);
								System.exit(0);
							}
							else {
								elem2 = Indice2;
							}
						}
					}
				}
			}
			else if (elementos[elem2].startsWith("[")) {
				String LN = "";
				for (int i = elem2; i < elementos.length; i++) {
					LN = LN + elementos[i];
					if (i >= elementos.length -1 )
						break;
					else 
						LN = LN + ",";
				}
				Valor_Lista = Valor_Lista + Valores_Lista(LN);
			}
			Valor_Lista = Valor_Lista + " list";
		}
	}
		return Valor_Lista;
}

//_______________________________________________________________________________________________________________________________//

//Funcion que crea una tupla en base a los parametros enviados y devuelve la tupla creada para su analisis

public String CrearTupla(String[] Linea, String Tupla, int contador) {
	String u = "";
	if (Linea[contador].startsWith("(")) {
		if (Linea[contador].endsWith(")")) {
			Tupla = Valores_Tupla(Linea[contador]);
		}
		else {
			for (int k = contador; k < Linea.length; k++) {
				if (!Linea[k].endsWith(")")) {
		  				Tupla = Linea[contador] + ",";
		  				Tupla = Linea[contador] + Linea[k];
				}
				else {
					Tupla = Linea[contador] + ",";
					Tupla = Linea[contador] + Linea[k];
				}
			}
		}
		u = Valores_Tupla(Tupla);
		Indice2 = contador;
	}
	return u;
}


//Este mÃ©todo booleano es para determinar si, el string enviado, es un nÃºmero
public boolean IsNumber (String Text) {
	boolean Resultado = true;
	try {
		Integer.parseInt(Text);
	}
	catch (NumberFormatException nfe){
		Resultado = false;
	}
	return Resultado;
}

//_______________________________________________________________________________________________________________________________//

//Funcion que determina si el string enviado, es una variable igual a la del arreglo "Variable"

public boolean IsVariableiable (String Text) {
	boolean Resultado = true;
	for (int a = 0; a < Variable.length; a++) {
		if (Text.equals(Variable[a])) {
			Resultado = true;
			Indice3=a;
			break;
		}
		else {
			Resultado = false;
		}
	}
	return Resultado;
}

//______________________________________________________________________________________________________________________________//

//Funcion que llama a las funciones para ejecutar el programa

public static void main (String[] args) {
	
	//Llamada a funciones
	Ambiente Static_Dinamic = new Ambiente();
	javax.swing.JFileChooser j= new javax.swing.JFileChooser();
	j.showOpenDialog(j);
	String archivo = j.getSelectedFile().getAbsolutePath();
	Static_Dinamic.LecturaArchivo(archivo);

	
	//Llamada a la ventana de interfaz gráfica y la tabla de despliegue de información.
	GridBagLayout gbl4 = new GridBagLayout();
    final JPanel panel4 = new JPanel();
    panel4.setLayout(gbl4);
	final JFrame Ambiente = new JFrame ("Ambiente Estatico/Dinamico");
	DefaultTableModel dtm= new DefaultTableModel();
	JTable table = new JTable(dtm);
	dtm.addColumn("Variable");
	dtm.addColumn("Valor en el Ambiente Estatico");
	dtm.addColumn("Valor en el Ambiente Dinamico");
	Object[] data = new Object[50];
	int a = 0;
	int b = 0;
	while (Variable[b]!=null) {
		data[a]=Variable[b];
		data[a+1]=Tipo[b];
		data[a+2]=Valor[b];
		dtm.addRow(data);
		b++;
	}
	Ambiente.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	table.setPreferredScrollableViewportSize(new Dimension(500, 200));
	JScrollPane scrollPane = new JScrollPane(table);
	Ambiente.getContentPane().add(scrollPane);
	Ambiente.setSize(500, 200);
	Ambiente.setVisible(true);
	
	
	}
}