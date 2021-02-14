package UDP;

import bases.IpAndPort;
import classesAjuda.ListaIpsandPorts;
import classesAjuda.SleepTime;
import conecao.Conecao;
import main.Main;
import typos.Typos;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;


//clase usada no incio do programa que obtem indicação de todos os servidores online e disponiveis para conecao
public class ConecaoInicial extends Thread {

    private int port;
    private List<IpAndPort> listaIpServidoresOnline;


    //NOTA: BASTA APENAS CHAMAR O CONSTRUTOR PAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    public ConecaoInicial(int port, List<IpAndPort> ListaIpServidoresOnline) {     // ||||
        this.port = port;                                                                //     ||||
        this.listaIpServidoresOnline = ListaIpServidoresOnline;                      //     ||||
        this.start();       //<<<<<<<<<<<<<<===================================================
    }


    /*
     * envia para o endresso e porto conhecido pedido de conecao
     * na resposta coloca os dados de outros servidores numa loista qaue guarda isso da main
     * Se sv aceitar: devolve
     * */
    @Override
    public void run() {
        try {
            DatagramSocket s = new DatagramSocket();
            s.setSoTimeout(5000);
            Conecao conecao = new Conecao(Typos.SEARCH_REQUEST, null);

            // Serialize to a byte array
            ByteArrayOutputStream bOUS = new ByteArrayOutputStream();
            ObjectOutput oO = new ObjectOutputStream(bOUS);
            oO.writeObject(conecao);
            oO.close();

            byte[] buffer = bOUS.toByteArray();

            //envia
            InetAddress adress = InetAddress.getByName(Main.ip);
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length, adress, port);    //envia a mensagem
            s.send(dp);



            //prepara para receber as coisas:
            byte[] r = new byte[4096];

            //recebe resposta
            DatagramPacket dpresposta = new DatagramPacket(r, r.length);   //coloca o dp a receber
            s.receive(dpresposta);
            //passa a resposta para um objeto
            ObjectInputStream iStream = new ObjectInputStream(new ByteArrayInputStream(r));
            Conecao conecaoResposta = (Conecao) iStream.readObject();
            iStream.close();

            //obtem lista de servidores da resposta
            ListaIpsandPorts lip= (ListaIpsandPorts) conecaoResposta.getObj();
            List<IpAndPort> listaServidoresRecebida = lip.getLista();
            listaIpServidoresOnline.clear();
            listaIpServidoresOnline.addAll(listaServidoresRecebida);

            //compara resposta recebida
            //se for igual não faz nada MAIN.ip já tem o ip correto
            //caso seja diferente
            if (conecaoResposta.getMsgCode().equals(Typos.SEARCH_ANSWER_DENNIED)) {
                int flag = 0;       //flag de coneção
                int i = 1;          //posição dos ips no array
                //testa mandar conecao para todos os ips ate um aceitar
                do {
                    //envia conecao
                    adress = listaIpServidoresOnline.get(i).getConctionData().getAddress();  //cria um InetAdress com um dos ips da lista
                    port = listaIpServidoresOnline.get(i).getConctionData().getPort();
                    dp = new DatagramPacket(buffer, buffer.length, adress, port);    //envia a mensagem
                    s.send(dp);

                    //obtem resposta
                    dpresposta = new DatagramPacket(r, r.length);   //coloca o dp a receber
                    s.receive(dpresposta);
                    //passa a resposta para um objeto
                    iStream = new ObjectInputStream(new ByteArrayInputStream(r));
                    conecaoResposta = (Conecao) iStream.readObject();
                    iStream.close();
                    //se não aceitar
                    if (conecaoResposta.getMsgCode().equals(Typos.SEARCH_ANSWER_DENNIED)) {
                        i++;
                    } else {//se aceitar
                        flag = 1;               //coloca a flag a 1
                        Main.ip = listaIpServidoresOnline.get(i).getConctionData().getHostString();    //coloca o ip certo no main
                        Main.port =listaIpServidoresOnline.get(i).getConctionData().getPort();
                        break;                      //para o loop
                    }
                } while (i < listaIpServidoresOnline.size());     //faz enquanto houver ips na lista

                //caso nenhum servidor tenha aceitado:
                if (flag == 0) {
                    System.out.println("Não foram encontrados servidores que aceitacem conecção!\nA sair\n");
                    System.exit(-1);
                } else {   //caso um servior tenha aceitado
                    System.out.println("Foi encontrado servidor para coneção");
                }

            }
            //se aceitar
            else if (conecaoResposta.getMsgCode().equals(Typos.SEARCH_ANSWER_ACCEPTED)) {
                System.out.println(listaIpServidoresOnline.get(0).getConctionData().getHostString()+ ":" + listaIpServidoresOnline.get(0).getConctionData().getPort());
                Main.ip = listaIpServidoresOnline.get(0).getConctionData().getHostString();    //coloca o ip certo no main
                Main.port =listaIpServidoresOnline.get(0).getConctionData().getPort();

                System.out.println("Foi encontrado servidor para coneção");
            }
        } catch (SocketTimeoutException e) {
            System.out.println("O servidor colocado não foi encontrado\nA sair do programa.");
            System.exit(-1);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
