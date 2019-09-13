package estg.ipvc.listatarefas;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private EditText textoTarefa;
    private Button botaoAdicionar;
    private ListView listaTarefa;
    private SQLiteDatabase baseDados;

    private ArrayAdapter<String> tarefasAdaptor;
    private ArrayList<String> itens;
    private ArrayList<Integer> ids;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {

            textoTarefa = (EditText) findViewById(R.id.nometarefa);
            botaoAdicionar = (Button) findViewById(R.id.inserirtarefa);

            listaTarefa = (ListView) findViewById(R.id.listatarefas);


            //Base de Dados
            baseDados = openOrCreateDatabase("apptarefas", MODE_PRIVATE, null);

            //Tabelas
            baseDados.execSQL("CREATE TABLE IF NOT EXISTS tarefas (id INTEGER PRIMARY KEY AUTOINCREMENT, tarefa VARCHAR )");

            botaoAdicionar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String texto = textoTarefa.getText().toString();
                    salvarTarefa(texto);

                }
            });

            // Clicar num dos elementos da lista para remover tarefa

            listaTarefa.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    removeTarefa(ids.get(i));

                }
            });


            //Chamar metodo listar Tarefas
            recuperaTarefas();

        }catch (Exception e){

            e.printStackTrace();
        }
    }


    private void salvarTarefa(String texto){


        try {

            if(texto.equals("")){
                Toast.makeText(MainActivity.this, "Erro!! Tentou introduzir uma tarefa vazia", Toast.LENGTH_SHORT).show();
            }

            else {
                baseDados.execSQL("INSERT INTO tarefas (tarefa) VALUES('" + texto + "')");
                Toast.makeText(MainActivity.this, "Tarefa criada comm sucesso!", Toast.LENGTH_SHORT).show();
                recuperaTarefas();
                textoTarefa.setText("");

            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }


    private void recuperaTarefas(){

        try{

// Recupera Tarefas

            Cursor cursor = baseDados.rawQuery("SELECT * FROM tarefas ORDER BY id DESC", null);

            //Recuperar ids colubas

            int indiceColunaID = cursor.getColumnIndex("id");
            int indiceColunaTarefa = cursor.getColumnIndex("tarefa");

            // criar Adapter

            itens = new ArrayList<String>();
            ids = new ArrayList<Integer>();
            tarefasAdaptor = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_2, android.R.id.text2,itens);


            listaTarefa.setAdapter(tarefasAdaptor);

            //Listar

            cursor.moveToFirst();
            while (cursor != null)
            {
                itens.add(cursor.getString(indiceColunaTarefa));
                ids.add( Integer.parseInt(cursor.getString(indiceColunaID)) );

                cursor.moveToNext();
            }


        }catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void removeTarefa(Integer id){

        try{

            baseDados.execSQL("DELETE FROM tarefas WHERE id="+ id);
            recuperaTarefas();
            Toast.makeText(MainActivity.this, "A tarefa foi removida com sucesso!!", Toast.LENGTH_SHORT).show();



        }catch (Exception e) {

            e.printStackTrace();

        }


    }
}
