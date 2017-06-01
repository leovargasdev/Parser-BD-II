/*
    * REDO --> transações comitadas (valor novo)
    * UNDO --> sem CKPT e commit (valor antigo)
*/

import java.util.*;
import java.io.*;
class parser{
    public static void main(String [] args) throws IOException{
        BufferedReader ler = new BufferedReader(new FileReader(new File("log.txt")));//carrega o arquivo
        ArrayList<transacoes> transacoes = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        ArrayList<String> linhas = new ArrayList(), listaCheckPoint = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        Boolean ckptFlag = false;
        String l = ler.readLine(), start = "START", commit = "COMMIT", checkPoint = "CKPT";
        while(l != null){
            l = l.replaceAll("[< >()]", "").toUpperCase();// remove os caracteres especiais e espaços da linha
            linhas.add(l);
            if(l.startsWith(start)){// entra aqui quando eh uma nova transaçao, verificando se começa com "start"
                String linhaT = l.replaceAll(start, "");// remove a palavra "start" da linha
                if(linhaT.startsWith(checkPoint)){
                    String[] ckpt = linhaT.replaceAll(checkPoint, "").split(",");
                    for (int a = 0; a < ckpt.length; a++)
                        for(int w = 0; w < transacoes.size(); w++ )
                            if(ckpt[a].equals(transacoes.get(w).transacao))
                                transacoes.get(w).setCkpt(); // seta a flag de check point da transação
                    ckptFlag = true;
                    //System.out.println("inicio ckpt: " + l);
                } else {
                    transacoes.add(new transacoes(linhaT));// cria uma nova transação e add essa nova transação no ArrayList de transações
                }
            } else if(l.startsWith(commit) && !ckptFlag){// entra aqui quando eh um commit, verificando se começa com "commit"
                l = l.replaceAll(commit, "");
                for(int w = 0; w < transacoes.size(); w++ )// percorre todas as transacoes já iniciada
                    if(l.startsWith(transacoes.get(w).transacao))
                        transacoes.get(w).commitTransacao();
            } else if(l.contains(checkPoint)){
                ckptFlag = false;
                //System.out.println("fim ckpt: " + l);
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
            if(!transacoes.get(i).commit || transacoes.get(i).ckpt){
                System.out.println(transacoes.get(i).transacao + ": ");
                transacoes.get(i).getTransacoes();
            }
        }
        /*for(int i = 0; i < linhas.size(); i++)
            System.out.println(linhas.get(i)+ ": ");*/
    }
}
