/*
            --> REDO: transação com commit, valor novo
            --> UNDO: transação sem commit, valor antigo
*/
import java.util.*;
import java.io.*;
class parser{
    public static Map lendoEntrada() throws IOException{
        Map<String,Integer> valores = new HashMap<String,Integer>();
        BufferedReader entrada = new BufferedReader(new FileReader(new File("entrada.txt")));//carrega o arquivo
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
        Map<String,Integer> valoresEntrada = lendoEntrada();// valores do arquivo entrada.txt
        FileWriter t =new FileWriter("saida.txt", false);// apaga tudo oq tem no arquivo
        BufferedReader ler = new BufferedReader(new FileReader(new File("log.txt")));
        ArrayList<transacoes>   transacoes = new ArrayList(),
                                redo = new ArrayList(),
                                undo = new ArrayList();
        String  l = ler.readLine(),
                start = "START",
                commit = "COMMIT",
                checkPoint = "CKPT";
        Boolean startCkpt = false;
        while(l != null){
            if(!l.isEmpty()){
                l = l.replaceAll("[< >()]", "").toUpperCase();
                if(l.startsWith(start)){                                                //Uma Nova transação ou Start CKPT
                    l = l.replaceAll(start, "");
                    if(l.startsWith(checkPoint)){                                       //<Start CKPT()>
                        startCkpt = true;                                               //Para as transações criadas dentro do CKPT
                        String[] ckpt = l.replaceAll(checkPoint, "").split(",");        //Transações no parametro do CKPT
                        for (int a = 0; a < ckpt.length; a++)
                            for(int w = 0; w < transacoes.size(); w++)
                                if(ckpt[a].equals(transacoes.get(w).transacao))         //Confere as Transações que estão no parametro
                                    transacoes.get(w).setCkpt(true);
                    } else {                                                             //Nova transação
                        transacoes.add(new transacoes(l, startCkpt));                   //Passa a flag startCkpt, para o caso da transação iniciar dentro do CKPT
                    }
                }
                else if(l.startsWith(commit)){                                          //Um Commit
                    l = l.replaceAll(commit, "");
                    for(int w = 0; w < transacoes.size(); w++ )
                        if(l.startsWith(transacoes.get(w).transacao))                  //Verifica de qual transação o commit pertence
                            transacoes.get(w).setCommit();
                }
                else if(l.contains(checkPoint)){                                        //Fim do CKPT, no caso um <end CKPT>
                    startCkpt = false;                                                  //Desativa a flag
                    for(int w = 0; w < transacoes.size(); w++){
                        if(!transacoes.get(w).ckpt && transacoes.get(w).commit)         //Para transacoes que não estavam no parametro, não foram iniciadas dentro do CKPT e que já foram comitadas
                            transacoes.get(w).setEmDisco();                             //Transação esta garantida em disco
                        transacoes.get(w).setCkpt(startCkpt);                           //Retira a flag Ckpt das transacoes, caso tenha um novo CKPT
                    }
                }
                else {                                                                  //Operação de uma transação
                    for(int w = 0; w < transacoes.size(); w++)
                        if(l.startsWith(transacoes.get(w).transacao))                   //Verifica de qual transação a operação pertence
                            transacoes.get(w).insertT(l);
                }
            }
            l = ler.readLine();
        }
        ler.close();
        t.close();
        BufferedWriter escrever = new BufferedWriter(new FileWriter("saida.txt", true));
        for(int i = 0; i < transacoes.size(); i++){
            if(transacoes.get(i).emDisco){
                System.out.println("Em Disco:   " + transacoes.get(i).transacao);
            } else {
                Boolean valor = false; //significa valor antigo
                if (transacoes.get(i).commit){
                    System.out.println("REDO:       " + transacoes.get(i).transacao);
                    //redo.add(transacoes.get(i));
                    valor = true;
                } else {
                    System.out.println("UNDO:       " + transacoes.get(i).transacao);
                    //transacoes.get(i));
                }
                for(int u = 0; u < transacoes.get(i).op.size(); u++){
                    if(valoresEntrada.containsKey(transacoes.get(i).op.get(u).nome)) // posição contem o nome da variavel
                        valoresEntrada.remove(transacoes.get(i).op.get(u).nome);
                    String linha = transacoes.get(i).op.get(u).nome + " = ";
                    if(valor) linha += Integer.toString(transacoes.get(i).op.get(u).valorAtual);
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
