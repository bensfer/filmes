package info.santhosh.omdbsearchclient;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import info.santhosh.omdbsearchclient.database.DataBase;
import info.santhosh.omdbsearchclient.dominio.RepositorioFilme;
import info.santhosh.omdbsearchclient.dominio.entidades.Filme;

public class RegisteredDetail extends AppCompatActivity implements View.OnClickListener {

    private Filme filme;

    private Button btn_del;

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioFilme repositorioFilme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registered_detail);

        Bundle bundle = getIntent().getExtras();

        if ((bundle != null) && (bundle.containsKey("FILME"))) {

            filme = (Filme)bundle.getSerializable("FILME");
            preencheDados();

        } else {
            filme = new Filme();
        }

        btn_del = (Button)findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);

    }

    private void preencheDados () {

        try {

            Bitmap raw = null;

            // get DB image and convert to bitmap
            byte[] fotoArray = filme.getPoster();

            if (fotoArray != null) {
                raw = BitmapFactory.decodeByteArray(fotoArray, 0, fotoArray.length);
            }

            ((TextView) findViewById(R.id.registered_title)).setText(filme.getTitulo());
            ((ImageView) findViewById(R.id.registered_backdrop)).setImageBitmap(raw);
            ((TextView) findViewById(R.id.registered_released)).setText(filme.getLancamento());
            ((TextView) findViewById(R.id.registered_runtime)).setText(filme.getDuracao());
            ((TextView) findViewById(R.id.registered_director)).setText(filme.getDiretor());
            ((TextView) findViewById(R.id.registered_genre)).setText(filme.getGenero());
            ((TextView) findViewById(R.id.registered_writers)).setText(filme.getEscritores());
            ((TextView) findViewById(R.id.registered_actors)).setText(filme.getAtores());
            ((TextView) findViewById(R.id.registered_plot)).setText(filme.getSinopse());

        } catch (Exception ex) {

            AlertDialog.Builder erro_preencher = new AlertDialog.Builder(this);
            erro_preencher.setMessage("Erro ao preencher dados de filme registrado: " + ex.getMessage());
            erro_preencher.setNeutralButton("OK", null);
            erro_preencher.show();

        }

    }

    @Override
    public void onClick(View v) {

         excluir();

    }

    private void excluir() {

        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioFilme = new RepositorioFilme(conn);

            AlertDialog.Builder confirm_del = new AlertDialog.Builder(this);
            confirm_del.setTitle("Confirmação");
            confirm_del.setMessage("Você realmente deseja excluir este filme?");
            confirm_del.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    repositorioFilme.excluirFilme( filme.getId() );

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("EXIT", true);
                    startActivity(intent);
                }
            });
            confirm_del.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            confirm_del.show();

        } catch (Exception ex) {

            AlertDialog.Builder erro_del = new AlertDialog.Builder(this);
            erro_del.setMessage("Erro ao excluir filme "+ ex.getMessage() );
            erro_del.setNeutralButton("OK", null);
            erro_del.show();

        }

    }

}
