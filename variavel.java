class variavel{
    private int valorAntigo;
    private int valorNovo;
    private String nome;
    /*
        <transação, nome variavel, valor antigo, valor novo>
        l[0] = nº transação
        l[1] = nome variavel
        l[2] = valor antigo
        l[3] = valor novo
    */
    public variavel(String[] l){//vetor string que contem o split na ","
        this.nome = l[1];
        this.valorAntigo = Integer.parseInt(l[2]);
        this.valorNovo = Integer.parseInt(l[3]);
    }
    public void getVariavel(){
        System.out.print("    --> v: " + this.nome);
        System.out.print(", vAntigo: " + this.valorAntigo);
        System.out.println(", vNovo: " + this.valorNovo);
    }
    public String getNome(){
        return this.nome;
    }
    public String getValorAntigo(){
        return Integer.toString(this.valorAntigo);
    }
    public String getValorNovo(){
        return Integer.toString(this.valorNovo);
    }
}
