/*
package ficheiros;

import classesAjuda.ClassToByteArray;
import conecao.Conecao;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class ThreadEnviaFicheiroParaCanal extends  Thread{
    //conecta ao servidor
    //envia ficheiro pra o servidor
    private String ip;
    private int port;
    private String nome;
    private String password;
    private String path;

    public ThreadEnviaFicheiroParaCanal(String ip, int port, String nome, String password, String path) {
        this.ip = ip;
        this.port = port;
        this.nome = nome;
        this.password = password;
        this.path = path;

        this.start();

    }

    @Override
    public void run() {
        //connecta ao servidor
        Socket socket = null;
        try {
            socket = new Socket(InetAddress.getByName(ip), port);
        } catch (IOException e) {
            System.out.println("Não foi possivel conectar ao servidor");
            return;
        }

        //Specify the file
        File file = new File(path);
        FileInputStream fis = null;
        try {
            fis = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            System.out.println("O caminho para o ficheiro indicado não foi encontrado");
            return;
        }
        BufferedInputStream bis = new BufferedInputStream(fis);

        ObjectOutputStream Oos = null;

        try {
            Oos = new ObjectOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }

        //serializa objeto
        try {
            byte[] contents = bis.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Conecao conecao = new Conecao()


    }
}
*/