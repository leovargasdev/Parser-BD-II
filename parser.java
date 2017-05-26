import java.util.ArrayList;
import java.io.*;
class parser{
    public static void main(String [] args) throws IOException{
        File log = new File("log.txt");
        BufferedReader ler = new BufferedReader(new FileReader(log));
        String l = ler.readLine();
        ArrayList<String> logLinhas = new ArrayList();
        while(l != null){
            logLinhas.add(l.replaceAll("[< > ]", ""));
            l = ler.readLine();
        }
        ler.close();
        for(int i = 0; i < logLinhas.size(); i++)
            System.out.println(logLinhas.get(i));
        /*String t = "<Meu nome eh Jansdrei!>";
        System.out.println(t);
        t = t.replaceAll("[<,>, ]", "");
        System.out.println(t);*/
    }
}
