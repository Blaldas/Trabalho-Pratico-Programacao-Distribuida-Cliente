package conecao;

import bases.ChannelsAndUsersList;
import bases.DadosEstatisticosCanal;
import bases.ListaMensagensResposta;
import bases.Mensagem;
import typos.Typos;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;


//ela tem como responsabilidade receber mensagens e processar o que fazer com elas
//snedo que tanto pode responder como mostrar informação para o user
//tipo de mensagens que pode receber:
//-checks se user está online
//receber mensagens diretas
//receber mensagens pedidas do servidor
///...
public class RecebeMensagensThread extends Thread {

    private ObjectOutputStream oOS;                          //escreve para o servidor
    private ObjectInputStream oIS;                           //recebe do servidor

    private Conecao conecaoResposta;
    private List<RecebeMensagensThread> listaConecoes;

    public RecebeMensagensThread(ObjectOutputStream oOS, ObjectInputStream oIS, Conecao conecaoResposta, List<RecebeMensagensThread> listaConecoes) {
        this.oOS = oOS;
        this.oIS = oIS;
        this.conecaoResposta = conecaoResposta;
        this.listaConecoes = listaConecoes;
        this.start();
    }

    @Override
    public void run() {
          /*
                o objeto devolvido pelo sevidor é do tipo conecao
                Esse objeto inclui APENAS a String "msgCode"
                Esta classe deverá ler essa string e colocar no ecra (sout) o sucesso ou não da operação
                 */
        System.out.println(conecaoResposta.getMsgCode());
        if (conecaoResposta.getMsgCode().equals(Typos.MENSAGE_SEND_SUCCESS))   //Caso tenha enviado com sucesso
        {
            System.out.println("A mensagem foi enviada com sucesso");
        } else if (conecaoResposta.getMsgCode().equals(Typos.MENSAGE_SEND_FAIL)) {
            System.out.println("Não foi possivel enviar a mensagem");
        } else if (conecaoResposta.getMsgCode().equals(Typos.DIRECT_MESSAGE)) {
            System.out.println("Mensagem Recebida:");
            Mensagem mensagem = (Mensagem) conecaoResposta.getObj();
            System.out.println(mensagem.toString());

        } else if (conecaoResposta.getMsgCode().equals(Typos.CONNECTION_CHECK))        //caso seja uma conecao de checke
        {
            Conecao conecao = new Conecao(Typos.CONNECTION_CHECK, null);            //cria uma mensagem para indicar que ainda esta online
            EnviaConecao enviaConecao = new EnviaConecao(conecao, oOS);            //envia a mensagem de que ainda esta online

        } else if (conecaoResposta.getMsgCode().equals(Typos.LIST_DIRECT_MESSAGE_SUCESS)) {
            //caso não haja problemas, as devidas mensagens deverão ser bem listadas
            //obtem a lista com as mensagens:
            ListaMensagensResposta lMR = (ListaMensagensResposta) conecaoResposta.getObj();

            List<Mensagem> listaMensagens = lMR.getListaMensagens();

            //lista as mensagens
            //caso não haja mensagens trocadas com o utlizador
            if (listaMensagens.size() == 0) {
                System.out.println("Não foram encontradas nenhumas mensagens trocada com o utilizador pedido");
            } else {
                //caso haja mensagens
                // lista todas as mensagens
                for (Mensagem msg : listaMensagens) {
                    System.out.println(msg.toString() + "\n");
                }
                //indica numero de mensagens exposta, uma vez que não é necessáriamente o número pedido(pode não ter trocado tantas mensagens)
                System.out.println("\nForam listadas " + listaMensagens.size() + " mensagens");
            }
        } else if (conecaoResposta.getMsgCode().equals(Typos.LIST_DIRECT_MESSAGE_FAILURE)) {
            //caso tenha havido algum problema
            System.out.println("Não foi possivel listar as mensagens com o utilizador pretendido.\nTente Novamente mais tarde ou verifique se colocou o nome correto.");
        } else if (conecaoResposta.getMsgCode().equals(Typos.LIST_ALL_CHANNELS_AND_USERS_SUCESS)) {
            //caso o user tenha pedido para listar todos os canais e utilizadores conhecidos
            //transforma na classe que será usada para essa troca
            ChannelsAndUsersList channelsAndUsersList = (ChannelsAndUsersList) conecaoResposta.getObj();

            //lista os canais
            System.out.println("Lista de Canais:");
            for (String str : channelsAndUsersList.getListaCanais())
                System.out.println(str);

            //lista os utilizadores
            System.out.println("Lista de Utilizadores:");
            for (String str : channelsAndUsersList.getListaUtilizadores())
                System.out.println(str);

        } else if (conecaoResposta.getMsgCode().equals(Typos.LIST_ALL_CHANNELS_AND_USERS_FAILURE)) {
            System.out.println("Não foi possivel pedir para listar todos os canais e utilizadores!");
        } else if (conecaoResposta.getMsgCode().equals(Typos.CREATE_CHANNEL_FAIL)) {

            System.out.println("Não foi possivel criar o novo canal");
        } else if (conecaoResposta.getMsgCode().equals(Typos.CREATE_CHANNEL_SUCCESS)) {
            System.out.println("Canal criado com sucesso");
        } else if (conecaoResposta.getMsgCode().equals(Typos.MESSAGE_CHANNEL_SUCCESS)) {
            System.out.println("Mensagem enviada para canal com sucesso");
        } else if (conecaoResposta.getMsgCode().equals(Typos.MESSAGE_CHANNEL_FAIL)) {
            System.out.println("Não foi possivel enviar a mensagem para o canal");
        } else if (conecaoResposta.getMsgCode().equals(Typos.CHANNEL_LIST_MESSAGES_SUCCESS)) {
            //caso não haja problemas, as devidas mensagens deverão ser bem listadas
            //obtem a lista com as mensagens:
            ListaMensagensResposta lMR = (ListaMensagensResposta) conecaoResposta.getObj();

            List<Mensagem> listaMensagens = lMR.getListaMensagens();

            //lista as mensagens
            //caso não haja mensagens trocadas com o utlizador
            if (listaMensagens.size() == 0) {
                System.out.println("Não foram encontradas nenhumas mensagens trocada com o utilizador pedido");
            } else {
                //caso haja mensagens
                // lista todas as mensagens
                for (Mensagem msg : listaMensagens) {
                    System.out.println(msg.toString() + "\n");
                }
                //indica numero de mensagens exposta, uma vez que não é necessáriamente o número pedido(pode não ter trocado tantas mensagens)
                System.out.println("\nForam listadas " + listaMensagens.size() + " mensagens");
            }
        } else if (conecaoResposta.getMsgCode().equals(Typos.CHANNEL_LIST_MESSAGES_FAIL)) {
            System.out.println("Não foi possivel listar as mensagens do canal");

        } else if (conecaoResposta.getMsgCode().equals(Typos.DELETE_CHANNEL_FAIL)) {
            System.out.println("Não foi possivel eliminar o canal!");
        } else if (conecaoResposta.getMsgCode().equals(Typos.DELETE_CHANNEL_SUCCESS)) {
            System.out.println("O canal foi eliminado permanentemente!");

        } else if (conecaoResposta.getMsgCode().equals(Typos.CHANNEL_STATISTICS_FAIL)) {
            DadosEstatisticosCanal dadosEstatisticosCanal = (DadosEstatisticosCanal) conecaoResposta.getObj();
            System.out.println("Não foi possivel alistar as estatisticas do canal <" + dadosEstatisticosCanal.getNome() + ">");

        } else if (conecaoResposta.getMsgCode().equals(Typos.CHANNEL_STATISTICS_SUCCESS)) {
            DadosEstatisticosCanal dadosEstatisticosCanal = (DadosEstatisticosCanal) conecaoResposta.getObj();
            System.out.println(dadosEstatisticosCanal.toString());

        } else if (conecaoResposta.getMsgCode().equals(Typos.EDIT_CHANNEL_SUCCESS)) {
            System.out.println("Canal Editado com sucesso");
        } else if (conecaoResposta.getMsgCode().equals(Typos.EDIT_CHANNEL_FAIL)) {
            System.out.println("Não foi possivel editar o canal. Garanta que é o owner do mesmo e que colocou os dados corretos ou entao tente mais tarde");
        }


     /*
        else {
            System.out.println("OHHH diabo, que o tcp falhou.... EnviaConecao-> run()-> else");
        }

     */
        //WARNING: PODE DAR PROBLEMAS???
        listaConecoes.remove(this);         //retira-se da lista de conecoes feitas
        //WARNING: PODE DAR PROBLEMAS???
    }
}
