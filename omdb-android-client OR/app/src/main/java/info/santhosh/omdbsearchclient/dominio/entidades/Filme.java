package info.santhosh.omdbsearchclient.dominio.entidades;

import android.util.Log;

import java.io.Serializable;

public class Filme implements Serializable{

    public static String ID = "_id";
    public static String POSTER = "POSTER";
    public static String TITULO = "TITULO";
    public static String LANCAMENTO = "LANCAMENTO";
    public static String DURACAO = "DURACAO";
    public static String DIRETOR = "DIRETOR";
    public static String GENERO = "GENERO";
    public static String ESCRITORES = "ESCRITORES";
    public static String ATORES = "ATORES";
    public static String SINOPSE = "SINOPSE";

    private long id;
    private byte[] poster;
    private String titulo;
    private String lancamento;
    private String duracao;
    private String diretor;
    private String genero;
    private String escritores;
    private String atores;
    private String sinopse;

    public Filme() {

    }

    public byte[] getPoster() {
        return poster;
    }

    public void setPoster(byte[] poster) {
        this.poster = poster;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLancamento() {
        return lancamento;
    }

    public void setLancamento(String lancamento) {
        this.lancamento = lancamento;
    }

    public String getDuracao() {
        return duracao;
    }

    public void setDuracao(String duracao) {
        this.duracao = duracao;
    }

    public String getDiretor() {
        return diretor;
    }

    public void setDiretor(String diretor) {
        this.diretor = diretor;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getEscritores() {
        return escritores;
    }

    public void setEscritores(String escritores) {
        this.escritores = escritores;
    }

    public String getAtores() {
        return atores;
    }

    public void setAtores(String atores) {
        this.atores = atores;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    @Override
    public String toString() {

        if (lancamento.equals("N/A")) {
            return ("(N/A) " + titulo);
        } else {
            return ("(" + lancamento.substring(7, 11) + ") " + titulo);
        }

    }

}
