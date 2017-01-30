package info.santhosh.omdbsearchclient;

import android.content.DialogInterface;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;

import java.io.ByteArrayOutputStream;

import info.santhosh.omdbsearchclient.database.DataBase;
import info.santhosh.omdbsearchclient.dominio.RepositorioFilme;
import info.santhosh.omdbsearchclient.dominio.entidades.Filme;
import info.santhosh.omdbsearchclient.omdbApiRetrofitService.searchService;

public class DetailActivity extends AppCompatActivity implements View.OnClickListener{

    public static final String MOVIE_DETAIL = "movie_detail";
    public static final String IMAGE_URL = "image_url";

    private DataBase dataBase;
    private SQLiteDatabase conn;
    private RepositorioFilme repositorioFilme;
    private Filme filme;

    private Button btn_add_assist;
    private Button btn_add_nassist;

    private ImageView plot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final searchService.Detail detail = getIntent().getParcelableExtra(MOVIE_DETAIL);

        final String imageUrl =  getIntent().getStringExtra(IMAGE_URL);

        Glide.with(this).load(imageUrl).into( (ImageView) findViewById(R.id.main_backdrop));

        // set title for the appbar
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.main_collapsing);
        collapsingToolbarLayout.setTitle(detail.Title);

        ((TextView) findViewById(R.id.grid_title)).setText(detail.Title);
        ((TextView) findViewById(R.id.grid_writers)).setText(detail.Writer);
        ((TextView) findViewById(R.id.grid_actors)).setText(detail.Actors);
        ((TextView) findViewById(R.id.grid_director)).setText(detail.Director);
        ((TextView) findViewById(R.id.grid_genre)).setText(detail.Genre);
        ((TextView) findViewById(R.id.grid_released)).setText(detail.Released);
        ((TextView) findViewById(R.id.grid_plot)).setText(detail.Plot);
        ((TextView) findViewById(R.id.grid_runtime)).setText(detail.Runtime);

        try {

            dataBase = new DataBase(this);
            conn = dataBase.getWritableDatabase();

            repositorioFilme = new RepositorioFilme(conn);


        } catch (SQLException ex) {

            AlertDialog.Builder erro_banco = new AlertDialog.Builder(this);
            erro_banco.setMessage("Erro ao criar banco" + ex.getMessage());
            erro_banco.setNeutralButton("OK", null);
            erro_banco.show();

        }

        btn_add_assist = (Button)findViewById(R.id.btn_add_assist);
        btn_add_assist.setOnClickListener(this);

        btn_add_nassist = (Button)findViewById(R.id.btn_add_nassist);
        btn_add_nassist.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        String assistido = "";

        switch (v.getId()) {
            case R.id.btn_add_assist:
                assistido = ("SIM");
                break;
            case R.id.btn_add_nassist:
                assistido = ("NAO");
                break;
        }
        Log.e("assistido", "1" + assistido);
        Boolean existe = buscaIgual(assistido);

        if (!existe) {
            inserir(assistido);
        } else {
            AlertDialog.Builder erro_jaexiste = new AlertDialog.Builder(this);
            if (assistido.equals("SIM")) {
                erro_jaexiste.setMessage("Este filme já está registrado como 'assistido'");
            } else if (assistido.equals("NAO")) {
                erro_jaexiste.setMessage("Este filme já está registrado para assistir");
            }
            erro_jaexiste.setNeutralButton("OK", null);
            erro_jaexiste.show();
        }

    }

    private Boolean buscaIgual(String assistido) {

        Cursor busca = null;
        Boolean existe = null;

        try {
            busca = conn.rawQuery("SELECT * FROM TB_FILMES WHERE SINOPSE = ? AND ASSISTIDO = ? AND LANCAMENTO = ? AND DURACAO = ?" ,
                    new String[]{(((TextView) findViewById(R.id.grid_plot)).getText().toString()) , assistido,
                            (((TextView) findViewById(R.id.grid_released)).getText().toString()),
                            (((TextView) findViewById(R.id.grid_runtime)).getText().toString())});
            if (busca != null) {
                busca.moveToFirst();
            }

            if(busca.getCount() > 0) {
                existe = true;
            } else {
                existe = false;
            }

        } catch (Exception e) {
            AlertDialog.Builder erro_cursor = new AlertDialog.Builder(this);
            erro_cursor.setMessage("erro no cursor: " + e.getMessage());
            erro_cursor.setNeutralButton("OK", null);
            erro_cursor.show();
        }

        return existe;

    }

    private void inserir(final String assistido) {

            // ImageView
            plot = (ImageView)findViewById(R.id.main_backdrop);

            // prevent nullpointer caused by ImageView isn't fully loaded
            if (plot.getDrawable() == null) {

                AlertDialog.Builder imagem_null = new AlertDialog.Builder(this);
                imagem_null.setMessage("O poster do filme ainda não foi totalmente carregado. O que você deseja fazer?");

                imagem_null.setPositiveButton("INSERIR SEM POSTER", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.e("assistido", "2" + assistido);
                        filme = new Filme();
                        inserirComum(assistido);

                    }
                });
                imagem_null.setNegativeButton("AGUARDAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                imagem_null.show();

            } else {

                // convert glideBitmap to bitmap
                Bitmap bitmap = ((GlideBitmapDrawable) plot.getDrawable().getCurrent()).getBitmap();

                // convert Bitmap to byte
                ByteArrayOutputStream saida = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, saida);
                byte[] img = saida.toByteArray();

                filme = new Filme();

                filme.setPoster(img);

                inserirComum(assistido);

            }

    }

    private void inserirComum(final String assistido) {

        try {

            filme.setTitulo(((TextView) findViewById(R.id.grid_title)).getText().toString());
            filme.setLancamento(((TextView) findViewById(R.id.grid_released)).getText().toString());
            filme.setDuracao(((TextView) findViewById(R.id.grid_runtime)).getText().toString());
            filme.setDiretor(((TextView) findViewById(R.id.grid_director)).getText().toString());
            filme.setGenero(((TextView) findViewById(R.id.grid_genre)).getText().toString());
            filme.setEscritores(((TextView) findViewById(R.id.grid_writers)).getText().toString());
            filme.setAtores(((TextView) findViewById(R.id.grid_actors)).getText().toString());
            filme.setSinopse(((TextView) findViewById(R.id.grid_plot)).getText().toString());
            Log.e("assistido", "3" + assistido);
            AlertDialog.Builder confirm_add = new AlertDialog.Builder(this);
            confirm_add.setTitle("Confirmação");
            if (assistido.equals("SIM")) {
                confirm_add.setMessage("Você realmente deseja registrar este filme como 'já asisistido'?");
            } else if (assistido.equals("NAO")) {
                confirm_add.setMessage("Você realmente deseja registrar este filme como 'para assistir'?");
            }
            confirm_add.setPositiveButton("SIM", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    repositorioFilme.inserirFilme(filme, assistido);
                }
            });
            confirm_add.setNegativeButton("NÃO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            confirm_add.show();


        } catch (Exception ex) {

            AlertDialog.Builder erro_registro = new AlertDialog.Builder(this);
            erro_registro.setMessage("Erro ao registrar filme " + ex.getMessage());
            erro_registro.setNeutralButton("OK", null);
            erro_registro.show();
        }
    }

}











