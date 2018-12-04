package ws;

/**
 * Retorna o status http da requisição
 *
 */
public class WSStatus {

    private Integer codigo;
    private String descricao;

    public WSStatus() {
        this.codigo = null;
        this.descricao = "";
    }

    public WSStatus(Integer codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public void setCodigo(Integer codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}
