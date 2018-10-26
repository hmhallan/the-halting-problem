import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Main {

	public static void main(String[] args) throws Exception {
		java.io.Reader input = new InputStreamReader(System.in);
		Main.start(input);
	}

	/**
	 * 
	 * regras:
	 * 
	 * variáveis são inteiros de 9 a 999  -> (999 + 1 = 0) -> (0 - 1 = 999)
	 * somente dez variáveis (R0 a R9)
	 * R0 sempre recebe o parametro de entrada
	 * 
	 * @param input
	 * @throws Exception 
	 */
	public static void start( Reader input ) throws Exception {

		//Leitura do arquivo de entrada
		BufferedReader reader = new BufferedReader(input);

		//Contador de instacias
		int instancia = 1;

		while( reader.ready() ) {

			String comando = reader.readLine();
			
			//primeiro comando: numero de linhas E parametro de entrada
			String [] vet = comando.split(" ");
			int linhas = Integer.parseInt(vet[0]);
			int parametro = Integer.parseInt(vet[1]);
			
			Programa p = new Programa( reader, linhas, parametro );
			System.out.println( p.toString() );
			
			int valor = p.execute();
			System.out.println("RETORNO: " + valor);
			//break;
			
		}

		reader.close();
	}
	
	

}

class Programa {
	
	//operacoes basicas
	private static final String ATRIBUI = "MOV";
	private static final String SOMA = "ADD";
	private static final String SUBTRAI = "SUB";
	private static final String MULTIPLICA = "MUL";
	private static final String DIVIDE = "DIV";
	private static final String DIVIDE_RESTO = "MOD";
	
	//fluxos de decisão
	private static final String IF_EQUAL = "IFEQ";
	private static final String IF_NOT_EQUAL = "IFNEQ";
	private static final String IF_GREATER = "IFG";
	private static final String IF_LESS = "IFL";
	private static final String IF_GREATER_OR_EQUAL = "IFGE";
	private static final String IF_LESS_OR_EQUAL = "IFLE";
	private static final String END_IF = "ENDIF";
	
	private static final String CALL = "CALL";
	private static final String RET = "RET";
	
	List<String> instrucoes;
	
	int R0;
	int R1;
	int R2;
	int R3;
	int R4;
	int R5;
	int R6;
	int R7;
	int R8;
	int R9;
	
	public Programa( BufferedReader reader, int linhas, int parametro ) throws IOException {
		this.instrucoes = new ArrayList<>(linhas);
		for ( int i = 0; i< linhas; i++ ) {
			this.instrucoes.add(reader.readLine());
		}
		this.R0 = parametro;
	}
	
	public int execute() throws Exception {
		
		boolean findEndIf = false;
		for ( String i: this.instrucoes ) {
			
			if (findEndIf) {
				if ( i.startsWith(END_IF) ) {
					findEndIf = false;
				}
				System.out.println("\tPulando: " + i );
				continue;
						
			}
			
			System.out.print("\tExecutando: " + i + "  ::  ");
			
			//operacoes basicas
			if ( i.startsWith(ATRIBUI) ) {
				this.atribui( i.substring(4) );
			}
			else if ( i.startsWith(SOMA) ) {
				this.soma( i.substring(4) );
			}
			else if ( i.startsWith(SUBTRAI) ) {
				this.subtrai( i.substring(4) );
			}
			else if ( i.startsWith(MULTIPLICA) ) {
				this.multiplica( i.substring(4) );
			}
			else if ( i.startsWith(DIVIDE) ) {
				this.divide( i.substring(4) );
			}
			else if ( i.startsWith(DIVIDE_RESTO) ) {
				this.divideResto( i.substring(4) );
			}
			
			//controle de fluxo
			else if ( i.startsWith(IF_EQUAL) ) {
				boolean entra = this.ifEqual(i.substring(5));
				findEndIf = !entra;
			}
			
			else if ( i.startsWith(END_IF) ) {
				System.out.println(" ");
			}
			
			
			
			else if ( i.startsWith(RET) ) {
				return this.retorna(i.substring(4));
			}
			
			else {
				System.out.println("NOT IMPLEMENTED");
			}
		};  
		
		return -1;
	}
	
	private void atribui( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Atrbuir: " + vet[0] + " <- " + vet[1]);
		
		//atribuicao de variavel
		if ( vet[1].startsWith("R") ) {
			this.getClass().getDeclaredField(vet[0]).set(this, this.getClass().getDeclaredField(vet[1]).get(this) );
		}
		
		///atribuição de numeral
		else {
			this.getClass().getDeclaredField(vet[0]).set(this, Integer.parseInt(vet[1]));
		}
		System.out.println(this.variaveisToString());
	}
	
	private void soma( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Somar: " + vet[0] + " = " + vet[0] + " + " + vet[1]);
		
		//soma de variavel
		if ( vet[1].startsWith("R") ) {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) + 
					((int)this.getClass().getDeclaredField(vet[1]).get(this)) );
		}
		
		//soma de numeral
		else {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) +
					Integer.parseInt(vet[1]) );
		}
		System.out.println(this.variaveisToString());
	}
	
	private void subtrai( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Subtrai: " + vet[0] + " = " + vet[0] + " - " + vet[1]);
		
		//soma de variavel
		if ( vet[1].startsWith("R") ) {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) - 
					((int)this.getClass().getDeclaredField(vet[1]).get(this)) );
		}
		
		//soma de numeral
		else {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) -
					Integer.parseInt(vet[1]) );
		}
		System.out.println(this.variaveisToString());
	}
	
	private void multiplica( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Multiplica: " + vet[0] + " = " + vet[0] + " * " + vet[1]);
		
		//soma de variavel
		if ( vet[1].startsWith("R") ) {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) *
					((int)this.getClass().getDeclaredField(vet[1]).get(this)) );
		}
		
		//soma de numeral
		else {
			this.getClass().getDeclaredField(vet[0]).set(this, 
					((int)this.getClass().getDeclaredField(vet[0]).get(this)) *
					Integer.parseInt(vet[1]) );
		}
		System.out.println(this.variaveisToString());
	}
	
	private void divide( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Divide: " + vet[0] + " = " + vet[0] + " / " + vet[1]);
		
		int dividendo = ((int)this.getClass().getDeclaredField(vet[0]).get(this));
		int result = 0;
		int divisor = 0;
		//divisao de variavel
		if ( vet[1].startsWith("R") ) {
			divisor = ((int)this.getClass().getDeclaredField(vet[1]).get(this));
		}
		//divisao de numeral
		else {
			divisor = Integer.parseInt(vet[1]);
		}
		
		if (divisor != 0) {
			result = dividendo / divisor;
		}
		this.getClass().getDeclaredField(vet[0]).set(this, result );
		System.out.println(this.variaveisToString());
	}
	
	private void divideResto( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.println("Divide Resto: " + vet[0] + " = " + vet[0] + " mod " + vet[1]);
		
		int dividendo = ((int)this.getClass().getDeclaredField(vet[0]).get(this));
		int result = 0;
		int divisor = 0;
		//divisao de variavel
		if ( vet[1].startsWith("R") ) {
			divisor = ((int)this.getClass().getDeclaredField(vet[1]).get(this));
		}
		//divisao de numeral
		else {
			divisor = Integer.parseInt(vet[1]);
		}
		
		if (divisor != 0) {
			result = dividendo % divisor;
		}
		this.getClass().getDeclaredField(vet[0]).set(this, result );
		System.out.println(this.variaveisToString());
	}
	
	private boolean ifEqual( String command ) throws Exception {
		String [] vet = command.split(",");
		System.out.print("if equal: " + vet[0] + " == " + vet[1]);
		boolean equal = ((int)this.getClass().getDeclaredField(vet[0]).get(this)) ==
				((int)this.getClass().getDeclaredField(vet[1]).get(this));
		System.out.println( " (" + equal + ")" );
		return equal;
	}

	
	private int retorna( String command ) throws Exception {
		System.out.println("Retornando -> " + command);
		return (int)this.getClass().getDeclaredField(command).get(this);
	}
	
	@Override
	public String toString() {
		return "Programa [instrucoes=" + instrucoes + ", R0=" + R0 + ", R1=" + R1 + ", R2=" + R2 + ", R3=" + R3
				+ ", R4=" + R4 + ", R5=" + R5 + ", R6=" + R6 + ", R7=" + R7 + ", R8=" + R8 + ", R9=" + R9 + "]";
	}

	public String variaveisToString() {
		return "R0=" + R0 + ", R1=" + R1 + ", R2=" + R2 + ", R3=" + R3
				+ ", R4=" + R4 + ", R5=" + R5 + ", R6=" + R6 + ", R7=" + R7 + ", R8=" + R8 + ", R9=" + R9 + "]";
	}
	
}