package my.apps.todo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ToDoActivity extends ActionBarActivity {
	
	ArrayList<String> items;
	ArrayAdapter<String> itemsAdapter;
	ListView lvItems;
	
	Button btnA;
	Button btnE;
	Button btnC;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_todo);
		
		init();
	}
	
	private void init(){

		readItems();
		
		lvItems = (ListView)findViewById(R.id.lvItems);
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        lvItems.setAdapter(itemsAdapter);
        
        btnA = (Button)findViewById(R.id.btnAddItem);
        btnE = (Button)findViewById(R.id.btnEditItem);	
        btnC = (Button)findViewById(R.id.btnCancel);	
      
        setupListViewListener();
	}
	
	public void addTodoItem(View v){
		EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
		if(!etNewItem.getText().toString().isEmpty()){
			itemsAdapter.add(etNewItem.getText().toString());
			etNewItem.setText("");
			saveItems();
		}else{
			
			Toast.makeText(getApplicationContext(),
                    "Please enter text", Toast.LENGTH_SHORT)
                    .show();
			etNewItem.setText("");
		}
	}	
	
	public void canceTodoItem(View v){
		toggleAddToEditCancelButtons(false);
	}
	
	private void setupListViewListener(){
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> aView, View item,
					int pos, long id) {
				items.remove(pos);
				itemsAdapter.notifyDataSetInvalidated();
				saveItems();
				
				if(btnE.isShown()){
					toggleAddToEditCancelButtons(false);
				}
				return true;
			}		
		});
		
		lvItems.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> aView, View view, final int pos, long id) {
				
				toggleAddToEditCancelButtons(true);

		        EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
		        etNewItem.setText(items.get(pos));
		        
		        btnE.setOnClickListener(new OnClickListener() {
		           
					@Override
					public void onClick(View v) {
						EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
						String val = etNewItem.getText().toString();
			        	items.remove(pos);
			        	items.add(pos, val);					         
			        	itemsAdapter.notifyDataSetChanged();
			        	toggleAddToEditCancelButtons(false);
					}
		        });
			}			
		});
	}
	
	private void clearText(){
		EditText etNewItem = (EditText)findViewById(R.id.etNewItem);
		etNewItem.setText("");
	}
	
	private void toggleAddToEditCancelButtons(boolean toggle){
		clearText();
		
		btnE.setVisibility(toggle?View.VISIBLE:View.INVISIBLE);
		btnC.setVisibility(toggle?View.VISIBLE:View.INVISIBLE);
		btnA.setVisibility(toggle?View.INVISIBLE:View.VISIBLE);
	}
	
	private void readItems(){
		File fielsDir = getFilesDir();
		File todoFile = new File(fielsDir, "todo.txt");
		
		try {
			items = new ArrayList<String>(FileUtils.readLines(todoFile));
		} catch (IOException e) {
			items= new ArrayList<String>();
			e.printStackTrace();
		}		
	}
	
	private void saveItems(){
		File fielsDir = getFilesDir();
		File todoFile = new File(fielsDir, "todo.txt");
		
		try {
			FileUtils.writeLines(todoFile, items);
		} catch (IOException e) {			
			e.printStackTrace();
		}	
	}	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
