package classesAjuda;

public class SleepTime extends Thread{

    int continua;       //flag
    int time;           //tempo a adormecer


    //esta classe adormece por um tempo e depois ativa uma flag
    //NOTA: BASTA APENAS CHAMAR O CONSTRUTOR PAHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH
    public SleepTime(int time, int continua){                   //          ||||
        this.continua = continua;                               //          ||||
        this.time = time;                                       //          ||||
        start();    //      <<<<<<<<--------------------------------------------
    }


    @Override
    public void run() {
        try {
            sleep(time);            //adormece

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        continua = 0;               //ativa a flag colocando-a a zero
    }
}
