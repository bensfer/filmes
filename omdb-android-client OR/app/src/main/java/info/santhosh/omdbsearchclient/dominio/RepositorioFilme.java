package info.santhosh.omdbsearchclient.dominio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.ArrayAdapter;

import info.santhosh.omdbsearchclient.dominio.entidades.Filme;

public class RepositorioFilme {

    private SQLiteDatabase conn;

    public RepositorioFilme(SQLiteDatabase conn) {
        this.conn = conn;
    }

    public void inserirFilme(Filme filme, String assistido) {

        ContentValues values = new ContentValues();

        values.put("TITULO", filme.getTitulo());
        values.put("POSTER", filme.getPoster());
        values.put("LANCAMENTO", filme.getLancamento());
        values.put("DURACAO", filme.getDuracao());
        values.put("DIRETOR", filme.getDiretor());
        values.put("GENERO", filme.getGenero());
        values.put("ESCRITORES", filme.getEscritores());
        values.put("ATORES", filme.getAtores());
        values.put("SINOPSE", filme.getSinopse());
        values.put("ASSISTIDO", assistido);

        conn.insertOrThrow("TB_FILMES", null, values);
    }

    public void excluirFilme(long id) {

        conn.delete("TB_FILMES", " _id = ? ", new String[]{ String.valueOf( id )});
    }

    public ArrayAdapter<Filme> buscaFilme(Context context, String assistido) {

        ArrayAdapter<Filme> adpFilme = new ArrayAdapter<Filme>(context, android.R.layout.simple_list_item_1);

        Cursor cursor = conn.query("TB_FILMES", null, "ASSISTIDO = ?", new String[]{assistido}, null, null, null);

        if (cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {

                Filme filme = new Filme();

                filme.setId(cursor.getLong( cursor.getColumnIndex(filme.ID) ));
                filme.setPoster(cursor.getBlob( cursor.getColumnIndex(filme.POSTER) ));
                filme.setTitulo(cursor.getString( cursor.getColumnIndex(filme.TITULO) ));
                filme.setLancamento(cursor.getString( cursor.getColumnIndex(filme.LANCAMENTO) ));
                filme.setDuracao(cursor.getString( cursor.getColumnIndex(filme.DURACAO) ));
                filme.setDiretor(cursor.getString( cursor.getColumnIndex(filme.DIRETOR) ));
                filme.setGenero(cursor.getString( cursor.getColumnIndex(filme.GENERO) ));
                filme.setEscritores(cursor.getString( cursor.getColumnIndex(filme.ESCRITORES) ));
                filme.setAtores(cursor.getString( cursor.getColumnIndex(filme.ATORES) ));
                filme.setSinopse(cursor.getString( cursor.getColumnIndex(filme.SINOPSE) ));

                adpFilme.add(filme);

            } while (cursor.moveToNext());
        } else {
            adpFilme = null;
        }

        return adpFilme;
    }

}
