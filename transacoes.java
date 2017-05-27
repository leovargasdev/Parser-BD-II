import java.util.*;
import java.io.*;
class transacoes{
    public ArrayList<variavel> op = new ArrayList();// variavel das operações referente a transação
    public String transacao;
    //metodo construtor
    public transacoes(String t){
        this.transacao = t;
    }
    public void insertT(String l){
        variavel v = new variavel(l.split(","));
        this.op.add(v);
    }
    public void getTransacoes(){
        for(int g = 0; g < this.op.size(); g++)
            this.op.get(g).getVariavel();
    }
}
