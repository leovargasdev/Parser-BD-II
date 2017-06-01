import java.util.*;
import java.io.*;
class transacoes{
    public ArrayList<variavel> op = new ArrayList();// variavel das operações referente a transação
    public String transacao;
    public Boolean commit; // flag para saber se foi dado um commit antes de um checkPoint ou depois
    public Boolean ckpt; // flag para saber se uma transação esta no parametro do checkPoint
    //metodo construtor
    public transacoes(String t){
        this.transacao = t;
        this.commit = false;
        this.ckpt =  false;
    }
    public void insertT(String l){
        variavel v = new variavel(l.split(","));
        this.op.add(v);
    }
    public void getTransacoes(){
        for(int g = 0; g < this.op.size(); g++)
            this.op.get(g).getVariavel();
    }
    public void commitTransacao(){
        this.commit = true;
    }
    public void setCkpt(){
        this.ckpt = true;
    }
}
