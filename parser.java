import java.util.ArrayList;
import java.io.*;
class parser{
    public static void main(String [] args) throws IOException{
        File log = new File("log.txt");
        BufferedReader ler = new BufferedReader(new FileReader(log));
        String l = ler.readLine();
        ArrayList<transacoes> transacoes = new ArrayList(); // ArrayList com os objetos da classe transacoes.java
        String start = "start";
        //l = l.replaceFirst(start, "");
        //if(l.startsWith(start))// entra aqui quando eh uma nova transaçao
            //System.out.println(l);
        //replaceFirst(
        transacoes t;
        while(l != null){
            l = l.replaceAll("[< >]", "");// remove os caracteres especiais e espaços da linha
            if(l.startsWith(start)){ // entra aqui quando eh uma nova transaçao, verificando se começa com "start"
                String linhaT = l.replaceAll(start, "");// remove a palavra "start" da linha
                t = new transacoes(linhaT);// cria uma nova transação
                transacoes.add(t);// add a nova transação no ArrayList de transações
            } else{
                for(int w = 0; w < transacoes.size(); w++ ){
                    String transacao = transacoes.get(w).transacao; //nome da transação
                    if(l.startsWith(transacao)){
                        String opTransacao = l.replaceAll(transacao, "");
                        transacoes.get(w).insertT(opTransacao);
                    }
                }
            }
            l = ler.readLine();
        }
        ler.close();
        System.out.println("transacoes: ");
        for(int i = 0; i < transacoes.size(); i++){
            System.out.println("[" + i + "]:" + transacoes.get(i).transacao);
            transacoes.get(i).getTransacoes();
        }
        /*for(int i = 0; i < logLinhas.size(); i++)
            System.out.println(logLinhas.get(i));
        String t = "<Meu nome eh Jansdrei!>";
        System.out.println(t);
        t = t.replaceAll("[<,>, ]", "");
        System.out.println(t);*/
    }
}
