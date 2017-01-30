package info.santhosh.omdbsearchclient.database;

public class ScriptSQL {

    public static String getCreateFilme() {

        StringBuilder sqlBuilder = new StringBuilder();
        sqlBuilder.append("CREATE TABLE IF NOT EXISTS TB_FILMES ( ");
        sqlBuilder.append("_id                INTEGER       NOT NULL ");
        sqlBuilder.append("PRIMARY KEY AUTOINCREMENT, ");
        sqlBuilder.append("POSTER             BLOB , ");
        sqlBuilder.append("TITULO             VARCHAR (50), ");
        sqlBuilder.append("LANCAMENTO         VARCHAR (15), ");
        sqlBuilder.append("DURACAO            VARCHAR (10), ");
        sqlBuilder.append("DIRETOR            VARCHAR (50), ");
        sqlBuilder.append("GENERO             VARCHAR (50), ");
        sqlBuilder.append("ESCRITORES         VARCHAR (100), ");
        sqlBuilder.append("ATORES             VARCHAR (50), ");
        sqlBuilder.append("IMDBID             VARCHAR (20), ");
        sqlBuilder.append("SINOPSE            VARCHAR (700), ");
        sqlBuilder.append("ASSISTIDO          VARCHAR (3) ");
        sqlBuilder.append(");");

        return sqlBuilder.toString();
    }
}
