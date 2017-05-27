class variavel{
    public int valorAntigo;
    public int valorAtual;
    public String nome;
    /*
        <transação, nome variavel, valor antigo, valor novo>
        l[0] = nº transação
        l[1] = nome variavel
        l[2] = valor antigo
        l[3] = valor novo
    */
    public variavel(String[] l){//vetor string que contem o split na ","
        this.nome = l[1];//posição contem o nome da variavel
        this.valorAntigo = Integer.parseInt(l[2]);//posição contem o valor antigo da variavel
        this.valorAtual = Integer.parseInt(l[3]);//posição contem o valor novo da variavel
    }
    public void getVariavel(){
        System.out.print("    --> v: " + this.nome);
        System.out.print(", vAntigo: " + this.valorAntigo);
        System.out.println(", vNovo: " + this.valorAtual);
    }
}
