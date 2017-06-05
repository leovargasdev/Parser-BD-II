import java.util.*;
import java.io.*;
class parser{
    public static Map lendoEntrada() throws IOException{
        Map<String,Integer> valores = new HashMap<String,Integer>();
        BufferedReader entrada = new BufferedReader(new FileReader(new File("entrada.txt")));//carrega o arquivo
        String linha = entrada.readLine();
        while(linha != null){
            String[] info  = linha.replaceAll(" ", "").split("=");
            valores.put( info[0], Integer.parseInt(info[1]));
            linha = entrada.readLine();
        }
        entrada.close();
        return valores;
    }
    public static void main(String [] args) throws IOException{
        Map<String,Integer> arquivoEntrada = lendoEntrada();
        BufferedReader ler = new BufferedReader(new FileReader(new File("log.txt")));//carrega o arquivo
        ArrayList<transacoes> transacoes = new ArrayList(), redo = new ArrayList(), undo = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        ArrayList<String> linhas = new ArrayList(), listaCheckPoint = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        String l = ler.readLine(), start = "START", commit = "COMMIT", checkPoint = "CKPT";
        while(l != null){
            l = l.replaceAll("[< >()]", "").toUpperCase();// remove os caracteres especiais e espaços da linha
            linhas.add(l);
            if(l.startsWith(start)){// entra aqui quando eh uma nova transaçao, verificando se começa com "start"
                String linhaT = l.replaceAll(start, "");// remove a palavra "start" da linha
                if(linhaT.startsWith(checkPoint)){
                    String[] ckpt = linhaT.replaceAll(checkPoint, "").split(",");//transacoes que estão no parametro do CKPT
                    for (int a = 0; a < ckpt.length; a++)
                        for(int w = 0; w < transacoes.size(); w++ )
                            if(ckpt[a].equals(transacoes.get(w).transacao))
                                transacoes.get(w).setCkpt(); // seta a flag de check point da transação
                } else {
                    transacoes.add(new transacoes(linhaT));// cria uma nova transação e add essa nova transação no ArrayList de transações
                }
            } else if(l.startsWith(commit)){// entra aqui quando eh um commit, verificando se começa com "commit"
                l = l.replaceAll(commit, "");
                for(int w = 0; w < transacoes.size(); w++ )// percorre todas as transacoes já iniciada
                    if(l.startsWith(transacoes.get(w).transacao))
                        transacoes.get(w).commitTransacao();
            } else if(l.contains(checkPoint)){// verifica se existe CKPT na linha, se sim significa que é o fim do checkPoint
                for(int w = 0; w < transacoes.size(); w++)
                    if(!transacoes.get(w).ckpt)// transações que não estão no parametro do CKPT
                        if(transacoes.get(w).commit){// e que já foram comitadas
                            System.out.println("Garantida pelo Disco: " + transacoes.get(w).transacao);
                            transacoes.remove(w);// remove a transacoes pois ela esta garantida em disco
                        }
            } else {
                for(int w = 0; w < transacoes.size(); w++)// percorre todas as transacoes já iniciada
                    if(l.startsWith(transacoes.get(w).transacao))// verifica qual transacao pertence essa operação
                        transacoes.get(w).insertT(l);
                String[] vl = l.split(",");
                if(arquivoEntrada.containsKey(vl[1]))
                    arquivoEntrada.remove(vl[1]);
            }
            l = ler.readLine();
        }
        ler.close();
        for(int i = 0; i < transacoes.size(); i++){
            if (transacoes.get(i).commit)
                redo.add(transacoes.get(i));
            else
                undo.add(transacoes.get(i));
        }
        System.out.print("Aplicado REDO: ");
        for(int i = 0; i < redo.size(); i++)
            System.out.print(redo.get(i).transacao + " ");
        System.out.println();
        System.out.print("Aplicado UNDO: ");
        for(int i = 0; i < undo.size(); i++)
            System.out.print(undo.get(i).transacao + " ");
        System.out.println();
        System.out.println("Variaveis não usadas:");
        for (String key : arquivoEntrada.keySet()) {
            Integer value = arquivoEntrada.get(key);
            System.out.println(key + " = " + value);
        }
    }
}
