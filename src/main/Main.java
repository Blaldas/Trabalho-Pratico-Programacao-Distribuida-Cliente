package main;

import bases.*;
import UDP.ConecaoInicial;
import conecao.Conecao;
import conecao.EnviaConecao;
import conecao.RecebeMensagens;
//import ficheiros.ThreadEnviaFicheiroParaCanal;
import typos.Typos;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/*
            // Serialize to a byte array

            Colocar o que se quer na classe Conecao

            ByteArrayOutputStream bOUS = new ByteArrayOutputStream();
            ObjectOutput oO = new ObjectOutputStream(bOUS);
            oO.writeObject(conecao);
            oO.close();
            byte buffer[] = bOUS.toByteArray();

            enviar o buffer (ou passar para string)


 */
public class Main {
    private static int estado = 0;                  //estado 0-> fazer login/regist ... Estado 1-> já esta logado no servidor
    public static int port;                         //porta de trabalho dos servidores
    public static String ip;                        //ip do servidor ao qual nos vamos conectar

    static Socket socket;                                   //é o socket da ligação com o servidor
    static ObjectOutputStream oOS;                          //escreve para o servidor
    static ObjectInputStream oIS;                           //recebe do servidor

    static Scanner sc;
    static Utilizador eu;       //dados do utilizador
    static String Canal;        //nome do canal

    static ArrayList<IpAndPort> listaIpServidoresOnline;    //lista que guarda ips de servidores disponiveis

    static RecebeMensagens recebeMensagens;            //thread que passa a receber mensagens do sevidor

    private static boolean exit = false;                         //flag para sair do programa


    public static void main(String[] argv) {

        //port = Integer.parseInt(argv[2]);
        listaIpServidoresOnline = new ArrayList<IpAndPort>();
        sc = new Scanner(System.in);
        System.out.println("porta do servidor\n");
        port = sc.nextInt();
        System.out.println("endereço do servidor\n");
        ip = sc.next();


        listaIpServidoresOnline.add(new IpAndPort(ip, port));
        //ip = argv[1];
        //listaIpServidoresOnline.add(argv[1]);

        System.out.println("À procura de servidores online...");
        //manda mensagem broadcast para todos os servidores
        ConecaoInicial bCI = new ConecaoInicial(port, listaIpServidoresOnline);
        try {                                       //espera pela thread, para se poder ligar ao servidor
            bCI.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        /////////////////////////////////////LIGAÇÃO TCP
        try {
            socket = new Socket(ip, port);
            oOS = new ObjectOutputStream(socket.getOutputStream());
            oIS = new ObjectInputStream(socket.getInputStream());

        } catch (IOException e) {
            e.printStackTrace();
        }
            /*
            PARA ENVIAR CENAS:
            oOS.writeObject(string objeto a enviar);
			oOS.flush();


			PARA LER CENAS:
			String tempStr = (String) oIS.readObject();
			E TRANSFORMAR NA CLASSE PRETENDIDA
             */

        estado = 0;         //estado-> 0- fazer login/regist; 1-> mandar mensagem direta/entrar canal

        //menu de cenas a fazer
        while (true) {

            if (estado == 0) {          //estado de login e regist
                //login
                //regist
                System.out.println("0- sair");
                System.out.println("1- login");
                System.out.println("2- registar");
                int opt = sc.nextInt();

                switch (opt) {
                    case 0:
                        exit = true;
                        sair();
                        break;
                    case 1:                     //login
                        login();
                        break;
                    case 2:                     //registar
                        regist();
                        break;
                }
            } else if (estado == 1) {      //estado de aceder às devidas funcionalidades do servidor
                //outras funções e envio de mensagem direta

                System.out.println("0- sair do programa");
                System.out.println("1- Enviar mensagem direta");
                System.out.println("2- Enviar mensagem para canal");  //provavelmenete é necessário criar um novo estyado para o canal
                System.out.println("3- Criar um canal");    //provavelmenete é necessário criar um novo estyado para o canal
                System.out.println("4- Listar N mensagem diretas com utilizador X");
                System.out.println("5- Listar todos utilizadores e canais existentes");
                System.out.println("6- Listar N mensagens de um canal");
                System.out.println("7- Eliminar canal");
                System.out.println("8- Apresentar dados estatisticos de um canal");
                System.out.println("9- Editar canal");
                System.out.println("10- Enviar ficheiro para canal");
                int opt = sc.nextInt();

                switch (opt) {
                    case 0:
                        exit = true;
                        sair();
                        break;
                    case 1:                     //Enviar mensagem direta
                        enviarMensagemDireta();
                        break;
                    case 2:
                        enviarMensagemParaCanal();
                        break;
                    case 3:
                        criarUmCanal();
                        break;
                    case 4:
                        listarNMensagensDiretas();
                        break;
                    case 5:
                        listarTodosCanaisEUtilizadores();
                        break;
                    case 6:
                        listarMensagensDeCanal();
                        break;
                    case 7:
                        eliminarCanal();
                        break;
                    case 8:
                        apresentarDadosEstatisticosCanal();
                        break;
                    case 9:
                        editarCanal();
                        break;
                    case 10:
                        enviarFicheiroParaCanal();
                        break;

                }


            }

            if (exit)
                break;

        }

    }

    private static void enviarFicheiroParaCanal() {
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        sc.nextLine();
        System.out.println("password do canal:\n");
        String password = sc.next();
        sc.nextLine();
        System.out.println("localização do ficheiro");
        String foto = sc.next();
        sc.nextLine();
        System.out.println("não implementado");
        //ThreadEnviaFicheiroParaCanal threadEnviaFaicheiroParaCanal= new ThreadEnviaFicheiroParaCanal(ip, port, nome, password, foto);
    }

    private static void editarCanal() {
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        sc.nextLine();
        System.out.println("password do canal:\n");
        String password = sc.next();
        sc.nextLine();

        //descricao:
        System.out.println("Mudar descricao do canal? (y/n)");
        String confirmacao = sc.next();
        sc.nextLine();
        String novaDescricao = null;
        if (confirmacao.equals("y")) {
            System.out.println("indique a nova descricao:");
            novaDescricao = sc.next();
            novaDescricao += sc.nextLine();         //garante que lê a linha
        }

        //palavrapass
        System.out.println("Mudar palavra-passe do canal? (y/n)");
        String novaPassFalg = sc.next();
        sc.nextLine();

        String novaPass = null;
        if (novaPassFalg.equals("y")) {
            System.out.println("indique a nova palavra-passe do canal:");
            novaPass = sc.next();
            sc.nextLine();
        }

        EditarCanal mudarCanal = new EditarCanal(nome, password, novaDescricao, novaPass);
        Conecao conecao = new Conecao(Typos.EDIT_CHANNEL, mudarCanal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);

    }

    private static void apresentarDadosEstatisticosCanal() {
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        sc.nextLine();
        System.out.println("password do canal:\n");
        String password = sc.next();
        sc.nextLine();

        DadosEstatisticosCanal dadosEstatisticosCanal = new DadosEstatisticosCanal(nome, password);
        Conecao conecao = new Conecao(Typos.CHANNEL_STATISTICS, dadosEstatisticosCanal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);
    }

    private static void eliminarCanal() {
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        sc.nextLine();
        System.out.println("password do canal:\n");
        String password = sc.next();
        sc.nextLine();
        System.out.println("Tem a certeza? \nEste procedimento fará com que todas as mesnagens e ficheiros trocados naquele canal sejam permanentemente eliminados\n(y/n)");
        String confirmacao = sc.next();
        sc.nextLine();

        if (!confirmacao.equals("y")) {
            System.out.println("a operação de eliminar o canal <" + nome + "> foi cancelada por sua decisão");
            return;
        }

        Canal canal = new Canal(nome, eu.getNome(), "", password);
        Conecao conecao = new Conecao(Typos.DELETE_CHANNEL, canal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);


    }

    private static void listarMensagensDeCanal() {
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        System.out.println("password:\n");
        String password = sc.next();
        System.out.println("Indique o numero de mensagens a listar");
        int nListar = sc.nextInt();

        PedidoListaMensagensCanal pedidoListaMensagensCanal = new PedidoListaMensagensCanal(nome, password, nListar);
        Conecao conecao = new Conecao(Typos.CHANNEL_LIST_MESSAGES, pedidoListaMensagensCanal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);

    }

    private static void criarUmCanal() {
        //obtem nome e pass
        System.out.println("nome do canal:\n");
        String nome = sc.next();
        System.out.println("password:\n");
        String password = sc.next();
        System.out.println("descricao do canal:\n");
        String descricao = sc.next();
        descricao += sc.nextLine();         //garante que lê a linha

        String owner = eu.getNome();


        Canal canal = new Canal(nome, owner, descricao, password);
        Conecao conecao = new Conecao(Typos.CREATE_CHANNEL, canal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);
    }

    private static void enviarMensagemParaCanal() {
        //obtem nome e pass
        System.out.println("nome do Canal:\n");
        String nomeCanal = sc.next();
        System.out.println("password:\n");
        String password = sc.next();
        System.out.println("mensagem:");
        String mensagem = sc.next();
        mensagem += sc.nextLine();         //garante que lê a linha

        MensagemCanal mensagemCanal = new MensagemCanal(eu.getNome(), nomeCanal, password, mensagem);
        Conecao conecao = new Conecao(Typos.MESSAGE_CHANNEL, mensagemCanal);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);


    }

    //faz o login
    public static void login() {

        //obtem nome e pass
        System.out.println("username:\n");
        String nome = sc.next();
        System.out.println("password:\n");
        String password = sc.next();

        //obtem user
        try {
            eu = new Utilizador(nome, password, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("username: " + eu.getNome());
        System.out.println("password: " + eu.getPassword());
        //cria classe para ligação
        Conecao conecao = new Conecao(Typos.LOGIN, eu);

        //faz ligação
        System.out.println("envia pedido para login");
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);
        System.out.println(" pedido para login enviado ");
        //obtem resposta
        try {
            conecao = (Conecao) oIS.readObject();
            if (conecao.getMsgCode().equals(Typos.LOGIN_ACCEPTED))           //aceitou login
            {
                estado = 1;     //passa para estado 1
                recebeMensagens = new RecebeMensagens(oOS, oIS);             //ativa thread quefica à espera de mensagens
                System.out.println("Login feito");
            } else
                System.out.println("Não foi possivel fazer login");

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //registo
    public static void regist() {
        //obtem nome, pass e diretorio da fotografia
        System.out.println("username:\n");
        String nome = sc.next();
        System.out.println("password:\n");
        String password = sc.next();
        System.out.println("localização da foto");
        String foto = sc.next();

        //obtem user
        try {
            eu = new Utilizador(nome, password, foto);
        } catch (FileNotFoundException e) {
            System.out.println("A localização indicada não foi encontrada!");
            return;
        } catch (IOException e) {
            System.out.println("Erro a ler imagem");
            e.printStackTrace();
            return;
        }
        //cria classe para ligação
        Conecao conecao = new Conecao(Typos.REGISTER, eu);
        //faz ligação
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);

        //obtem resposta
        try {
            conecao = (Conecao) oIS.readObject();
            if (conecao.getMsgCode().equals(Typos.REGIST_ACCEPTED))           //aceitou login
            {
                estado = 1;     //passa para estado 1
                recebeMensagens = new RecebeMensagens(oOS, oIS);             //ativa thread quefica à espera de mensagens
                System.out.println("Registo feito");
            } else
                System.out.println("Não foi possivel fazer registo.\nTente mais tarde ou use outro username");
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }


    public static void enviarMensagemDireta() {
        System.out.println("Indique nome do utilizador que vai receber a mensagem:\n");
        String userNameReceptor = sc.next();
        System.out.println("Indique a mensagem a enviar:\n");
        String message = sc.next();
        message += sc.nextLine();

        Mensagem mensagem = new Mensagem(eu.getNome(), userNameReceptor, message);
        Conecao conecao = new Conecao(Typos.DIRECT_MESSAGE, mensagem);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);
        //A RESPOSTA DO SERVIDOR SERÁ "APANHADA" PELA THREAD QUE JÁ EXISTE E QUE PROCESSA O RECEBER DE MENSAGENS

    }


    public static void listarNMensagensDiretas() {
        System.out.println("Indique nome do utilizador com o qual quer listar as mensagens:\n");
        String userNameReceptor = sc.next();
        sc.nextLine();
        System.out.println("Indique número de mensagens a listar:\n");
        int n = sc.nextInt();

        //cria classe que trata de guardar username e numero de mensagens a listar
        PedidoListaMensagensDiretas listMensagens = new PedidoListaMensagensDiretas(userNameReceptor, n);
        //cria classe conecao que será enviada
        Conecao conecao = new Conecao(Typos.LIST_DIRECT_MESSAGE, listMensagens);
        //cria classe que enviará os dados em thread
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);

    }


    public static void listarTodosCanaisEUtilizadores() {
        Conecao conecao = new Conecao(Typos.LIST_ALL_CHANNELS_AND_USERS, null);
        EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);
    }

    public static void sair() {      //fecha tudo
        try {
            oIS.close();
            oOS.close();
            socket.close();
            if (recebeMensagens != null)
                recebeMensagens.join();     //this is some kind of joke, i know, but still, it is better than nothing


        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return;
    }


    public static int getEstado() {
        return estado;
    }

    public static void setEstado(int estado) {
        Main.estado = estado;
    }

    public static boolean getExit() {
        return exit;
    }


}
