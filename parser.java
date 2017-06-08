/*
    * Academico: Leonardo Luis de Vargas
    * Matricula: 1411100047
    * Trabalho: Simular um sistema de log REDO/UNDO
*/

import java.util.*;
import java.io.*;
class parser{
    public static Map lendoEntrada() throws IOException{
        Map<String,Integer> valores = new HashMap<String,Integer>();
        BufferedReader entrada = new BufferedReader(new FileReader(new File("txt/entrada.txt")));
        String linha = entrada.readLine();
        while(linha != null){
            String[] info  = linha.replaceAll(" ", "").toUpperCase().split("=");
            valores.put( info[0], Integer.parseInt(info[1]));
            linha = entrada.readLine();
        }
        entrada.close();
        return valores;
    }
    public static void main(String [] args) throws IOException{
        Map<String,Integer> valoresEntrada = lendoEntrada();                            //Valores do arquivo entrada.txt
        FileWriter arqSaida =new FileWriter("txt/saida.txt", false);                        //Limpa o arquivo de saida.txt
        BufferedReader ler = new BufferedReader(new FileReader(new File("txt/log.txt")));
        ArrayList<transacoes>   transacoes = new ArrayList();
        String  start = "START",
                commit = "COMMIT",
                checkPoint = "CKPT";
        Boolean startCkpt = false;
        for(String l = ler.readLine(); l != null; l = ler.readLine()){
            if(!l.isEmpty()){
                l = l.replaceAll("[< >()]", "").toUpperCase();
                if(l.startsWith(start)){                                                //Uma Nova transação ou Start CKPT
                    l = l.replaceAll(start, "");
                    if(l.startsWith(checkPoint)){                                       //<Start CKPT()>
                        startCkpt = true;                                               //Para as transações criadas dentro do CKPT
                        String[] ckpt = l.replaceAll(checkPoint, "").split(",");        //Transações no parametro do CKPT
                        for (int a = 0; a < ckpt.length; a++)
                            for(int w = 0; w < transacoes.size(); w++)
                                if(ckpt[a].equals(transacoes.get(w).getNome()))         //Confere as Transações que estão no parametro
                                    transacoes.get(w).setCkpt(true);
                    } else {                                                            //Nova transação
                        transacoes.add(new transacoes(l, startCkpt));                   //Passa a flag startCkpt, para o caso da transação iniciar dentro do CKPT
                    }
                } else if(l.startsWith(commit)){                                        //Um Commit
                    l = l.replaceAll(commit, "");
                    for(int w = 0; w < transacoes.size(); w++ )
                        if(l.startsWith(transacoes.get(w).getNome()))                   //Verifica de qual transação o commit pertence
                            transacoes.get(w).setCommit();
                } else if(l.contains(checkPoint)){                                      //Fim do CKPT, no caso um <end CKPT>
                    startCkpt = false;                                                  //Desativa a flag
                    for(int w = 0; w < transacoes.size(); w++){
                        if(!transacoes.get(w).getCkpt() && transacoes.get(w).getCommit())         //Para transacoes que não estavam no parametro, não foram iniciadas dentro do CKPT e que já foram comitadas
                            transacoes.get(w).setEmDisco();                             //Transação esta garantida em disco
                        transacoes.get(w).setCkpt(startCkpt);                           //Retira a flag Ckpt das transacoes, caso tenha um novo CKPT
                    }
                } else {                                                                //Operação de uma transação
                    for(int w = 0; w < transacoes.size(); w++)
                        if(l.startsWith(transacoes.get(w).getNome()))                   //Verifica de qual transação a operação pertence
                            transacoes.get(w).insertVar(l);
                }
            }
        }
        ler.close();
        arqSaida.close();
        BufferedWriter escrever = new BufferedWriter(new FileWriter("txt/saida.txt", true));
        for(int i = 0; i < transacoes.size(); i++){
            if(transacoes.get(i).getEmDisco()){
                System.out.println("Em Disco:   " + transacoes.get(i).getNome());
            } else {
                Boolean valor = false;                                                  //False = UNDO e True = REDO
                if (transacoes.get(i).getCommit()){                                          //Se teve commit é REDO
                    System.out.println("REDO:       " + transacoes.get(i).getNome());
                    valor = true;
                } else {                                                                //Sem commit UNDO
                    System.out.println("UNDO:       " + transacoes.get(i).getNome());
                }
                for(int u = 0; u < transacoes.get(i).vars.size(); u++){
                    if(valoresEntrada.containsKey(transacoes.get(i).vars.get(u).getNome()))    //Busca no hashmap das variaveis da entrada
                        valoresEntrada.remove(transacoes.get(i).vars.get(u).getNome());        //Remove as variaveis que foram modificadas pelas transações
                    String linha = transacoes.get(i).vars.get(u).getNome() + " = ";
                    if(valor) linha += transacoes.get(i).vars.get(u).getValorNovo();
                    else linha += transacoes.get(i).vars.get(u).getValorAntigo();
                    escrever.write(linha);
                    escrever.newLine();
                }
            }
        }
        for (String key : valoresEntrada.keySet()) {
            String linha = key + " = " + Integer.toString(valoresEntrada.get(key));
            escrever.write(linha);
            escrever.newLine();
        }
        escrever.close();
    }
}
