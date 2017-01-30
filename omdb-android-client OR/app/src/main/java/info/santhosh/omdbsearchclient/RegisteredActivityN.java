package info.santhosh.omdbsearchclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.santhosh.omdbsearchclient.database.DataBase;
import info.santhosh.omdbsearchclient.dominio.RepositorioFilme;
import info.santhosh.omdbsearchclient.dominio.entidades.Filme;

public class RegisteredActivityN extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private DataBase dataBase;
    private SQLiteDatabase conn;

    private ListView lst_filmes;
    private ArrayAdapter<Filme> adpFilmes;
    private RepositorioFilme repositorioFilme;

    private String assistido = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered);

        lst_filmes = (ListView)findViewById(R.id.lst_filmes);

        lst_filmes.setOnItemClickListener(this);

        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioFilme = new RepositorioFilme(conn);

            assistido = "NAO";
            adpFilmes = repositorioFilme.buscaFilme(this, assistido);

            if (adpFilmes != null) {

                lst_filmes.setAdapter(adpFilmes);

            } else {

                AlertDialog.Builder lista_vazia = new AlertDialog.Builder(this);
                lista_vazia.setMessage("Você ainda não registrou nenhum filme para assistir");
                lista_vazia.setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("EXIT", true);
                        startActivity(intent);
                    }
                });
                lista_vazia.show();

            }

        } catch (SQLException ex) {

            AlertDialog.Builder erro_create = new AlertDialog.Builder(this);
            erro_create.setMessage("Erro ao criar banco" + ex.getMessage());
            erro_create.setNeutralButton("OK", null);
            erro_create.show();

        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        try {

            Filme filme = adpFilmes.getItem(position);

            Intent it = new Intent(this, RegisteredDetail.class);

            it.putExtra("FILME", filme);

            startActivity(it);

        } catch (Exception ex) {

            AlertDialog.Builder erro_detalhe = new AlertDialog.Builder(this);
            erro_detalhe.setMessage("Erro ao detalhar filme registrado: " + ex.getMessage());
            erro_detalhe.setNeutralButton("OK", null);
            erro_detalhe.show();

        }

    }
}
