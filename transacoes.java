import java.util.ArrayList;
import java.io.*;
class transacoes{
    public ArrayList<String> op = new ArrayList();// variavel das operações referente a transação
    public String transacao;
    //metodo construtor
    public transacoes(String t){
        this.transacao = t;
    }
    public void insertT(String o){
        this.op.add(o);
    }
    public void getTransacoes(){
        for(int g = 0; g < this.op.size(); g++)
            System.out.println("        " + this.op.get(g));
    }
}
