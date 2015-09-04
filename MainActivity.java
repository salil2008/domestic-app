package com.app.domesticapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;


import android.support.v7.app.ActionBarActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends ActionBarActivity implements OnClickListener 

{

	Button b1,b2,b3;
	EditText tuname,tpass;
	ProgressDialog pDialog;
	String logged_user="";
	String service_type="";
	
	String URL_Response="";
	String URL_LOGIN = "http://domesticapp.3pixelart.com/login_check.php";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		b1=(Button)findViewById(R.id.button1);
		b1.setOnClickListener(MainActivity.this);
		b2=(Button)findViewById(R.id.button2);
		b2.setOnClickListener(MainActivity.this);
		b3=(Button)findViewById(R.id.button3);
		b3.setOnClickListener(MainActivity.this);
		
		SQLiteDatabase db=openOrCreateDatabase("DOMESTIC", MODE_PRIVATE, null);
		db.execSQL("create table if not exists users (registered varchar(5));");
		Cursor c=db.rawQuery("select * from users", null);
		if(c.moveToFirst())
		{
			b3.setEnabled(false);
		}
		db.close();
		
		
		
		tuname=(EditText) findViewById(R.id.username);
		tpass=(EditText) findViewById(R.id.password);
		
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		 finish();
	}
	
	
	private class Authentication extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(MainActivity.this);
		        pDialog.setMessage("Authentication User..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_LOGIN);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("username",tuname.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("password",tpass.getText().toString()));
						    
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						    //This is the response from a php application
						   // String reverseString = response;
						   // server_message_list=response;
						    //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
						    Log.d("APMC","URL RES IS : "+response);
						    if(response.indexOf("VALID#USER")>=0)
						    {
						    	URL_Response="VALID";
						    	logged_user="USER";
						    	
						    	
						    }
						    else if(response.indexOf("VALID#AGENT")>=0)
						    {
						    	URL_Response="VALID";
						    	logged_user="AGENT";
						    	String[] data=response.split("#");
						    	service_type=data[2];
						    	
						    }
						    else
						    	URL_Response="INVALID";
				       } 
				    catch (ClientProtocolException e) 
				    {
					   
					    Log.d("APMC","CPE response " + e.toString());
					    // TODO Auto-generated catch block
					    URL_Response="INVALID";
				    } 
				    catch (IOException e) 
				    {
						    
						    Log.d("QRCODE","IOE response " + e.toString());
						    // TODO Aut return 0
						    URL_Response="INVALID";
				    }
		 
		        return null;
		    }
		 
		    @Override
		    protected void onPostExecute(Void result) {
		        super.onPostExecute(result);
		        if (pDialog.isShowing())
		            pDialog.dismiss();
		        Log.d("APMC","the URL VALU IS : "+URL_Response);
		        if(URL_Response.equals("VALID"))
		        {
		        	Toast.makeText(MainActivity.this, "LOGIN SUCCESSFULL", Toast.LENGTH_LONG).show();
		        	if(logged_user.equals("AGENT"))
		        	{
		        		Intent in=new Intent(MainActivity.this,AgentSectionActivity.class);
			        	in.putExtra("logged_user",tuname.getText().toString());
			        	in.putExtra("service_type",service_type);
			        	startActivity(in);
		        	}
		        	else if(logged_user.equals("USER"))
		        	{
		        		Intent in=new Intent(MainActivity.this,UserSectionActivity.class);
		        		in.putExtra("logged_user",tuname.getText().toString());
		        		
			        	startActivity(in);
		        	}
		        	
		        }
		        else
		        {
		        	Toast.makeText(MainActivity.this, "AUTENTICATION ERROR", Toast.LENGTH_LONG).show();
		        }
		    }	
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==b1)//LOGIN
		{
		    String uname,pass;
			uname=tuname.getText().toString();
			pass=tpass.getText().toString();
			if(uname.equals("")||pass.equals(""))
			{
			      Toast.makeText(MainActivity.this,"INVALID USERNAME/PASSWORD",3000).show();
			}
			else
			{
			   new Authentication().execute();
			}
		}
		if(v==b2)//CLEAR ALL
		{
		    tuname.setText("");
			tpass.setText("");
		}
		if(v==b3)//REGISTRATION
		{
		    Intent in=new Intent(MainActivity.this,CategoryActivity.class);
			in.putExtra("username","yogesh");
			in.putExtra("password","123123");
			startActivity(in);
		}
		//Toast.makeText(MainActivity.this,"A BUTTON IS CLICKED",3000).show();
	}
}
