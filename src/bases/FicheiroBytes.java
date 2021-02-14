package bases;

import conecao.Conteudo;

import java.io.Serializable;

public class FicheiroBytes extends Conteudo implements Serializable {
    static final long serialVersionUID = 42L;

    private byte[] file;


    public FicheiroBytes(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }
}
