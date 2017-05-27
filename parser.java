import java.util.*;
import java.io.*;
class parser{
    public static void main(String [] args) throws IOException{
        BufferedReader ler = new BufferedReader(new FileReader(new File("log.txt")));//carrega o arquivo
        ArrayList<transacoes> transacoes = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        String l = ler.readLine(), start = "start", commit = "Commit";
        while(l != null){
            l = l.replaceAll("[< >]", "");// remove os caracteres especiais e espaços da linha
            if(l.startsWith(start)){// entra aqui quando eh uma nova transaçao, verificando se começa com "start"
                String linhaT = l.replaceAll(start, "");// remove a palavra "start" da linha
                transacoes.add(new transacoes(linhaT));// cria uma nova transação e add essa nova transação no ArrayList de transações
            } else if(l.startsWith(commit)){// entra aqui quando eh um commit, verificando se começa com "commit"
                //System.out.println("Commit: " + l);
            } else {
                for(int w = 0; w < transacoes.size(); w++ )// percorre todas as transacoes já iniciada
                    if(l.startsWith(transacoes.get(w).transacao))// verifica qual transacao pertence essa operação
                        transacoes.get(w).insertT(l);
            }
            l = ler.readLine();
        }
        ler.close();
        System.out.println("transacoes: ");
        for(int i = 0; i < transacoes.size(); i++){
            System.out.println(transacoes.get(i).transacao + ": ");
            transacoes.get(i).getTransacoes();
        }
    }
}
