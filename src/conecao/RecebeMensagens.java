package conecao;


import main.Main;
import typos.Typos;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//esta classe é lançada numa thread
//fica à espera de conecoes e lanca uma thread RECEBEMENSAGENSTHREAD quando é conectada
public class RecebeMensagens extends Thread {


    private ObjectOutputStream oOS;                          //escreve para o servidor
    private ObjectInputStream oIS;                           //recebe do servidor

    private Conecao conecaoRecebida;
    private List<RecebeMensagensThread> listaConecoes;

    public RecebeMensagens(ObjectOutputStream oOS, ObjectInputStream oIS) {
        this.oIS = oIS;
        this.oOS = oOS;
        listaConecoes = new ArrayList<RecebeMensagensThread>();
        this.start();
    }


    //espera por receber coneções.
    //lanca thread que processa a conecao quando recebe
    @Override
    public void run() {
        while (true) {
            try {


                conecaoRecebida = (Conecao) oIS.readObject();   //recebe resposta do servidor
                listaConecoes.add(new RecebeMensagensThread(oOS, oIS, conecaoRecebida, listaConecoes));    //chama a thread que processa

            }catch (ClassNotFoundException e){
                e.printStackTrace();

            }catch (IOException e){ //ativa esta excepção quando
                if(Main.getExit())
                    return;
                else
                {
                    System.out.println("Servidor desconectou-se da rede\nA sair do programa");
                    System.exit(0);
                }

                return;
            }
        }
    }
}
