import java.util.*;
import java.io.*;
class transacoes{
    public ArrayList<variavel> vars = new ArrayList();//Variaveis referente a transação
    private String nome;                            //Nome da transação
    private Boolean commit;
    private Boolean ckpt;                           //ckpt = true, quando uma transação esta no parametro do checkPoint ou iniciou dps do "start CKPT"
    private Boolean emDisco;                        //emDisco = true, quando uma transação finalizou antes do CKPT, e o CKPT finalizou com sucesso
    public transacoes(String t, Boolean checkPoint){
        this.nome = t;
        this.commit = false;
        this.ckpt =  checkPoint;
        this.emDisco =  false;
    }
    public void insertVar(String l){
        this.vars.add(new variavel(l.split(",")));
    }
    public void setCommit(){
        this.commit = true;
    }
    public void setEmDisco(){
        this.emDisco = true;
    }
    public void setCkpt(Boolean checkPoint){
        this.ckpt = checkPoint;
    }
    public void getTransacoes(){
        for(int g = 0; g < this.vars.size(); g++)
            this.vars.get(g).getVariavel();
    }
    public String getNome(){
        return this.nome;
    }
    public Boolean getCommit(){
        return this.commit;
    }
    public Boolean getCkpt(){
        return this.ckpt;
    }                            //ckpt = true, quando uma transação esta no parametro do checkPoint ou iniciou dps do "start CKPT"
    public Boolean getEmDisco(){
        return this.emDisco;
    }
}
