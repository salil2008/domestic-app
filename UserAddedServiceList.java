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



import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class UserAddedServiceList extends ActionBarActivity implements OnClickListener 
{

ProgressDialog pDialog;
	
	String selected_service;
	
	
	String URL_Response="";
	String URL_USER_LIST = "http://domesticapp.3pixelart.com/user_service_list.php";
	String logged_user="";
	
	ListView user_list;
	String service_list[];
	final ArrayList<String> list1 = new ArrayList<String>();
	
	String selectedservice="";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_useraddedservicelist);
		
		user_list=(ListView) findViewById(R.id.userservicelist);
		
		logged_user=this.getIntent().getStringExtra("logged_user");   
		
		
		new User_service_list().execute();	

		
		user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				 selectedservice=list1.get(position);
				 Toast.makeText(getApplicationContext(), "Selected Service : "+selectedservice,   Toast.LENGTH_LONG).show();
				 Intent in=new Intent(UserAddedServiceList.this,UserServiceDetails.class);
				 in.putExtra("logged_user", logged_user);
				 in.putExtra("sel_service",selectedservice);
				 		     
				 startActivity(in);
				 
				 
			}

	      });
		
	}
	
	private class User_service_list extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(UserAddedServiceList.this);
		        pDialog.setMessage("Getting User Added Service Lists..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_USER_LIST);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("sel_service",selectedservice));
						    nameValuePairs.add(new BasicNameValuePair("logged_user",logged_user));
						    
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						   
						    Log.d("DOMESTIC","URL RES IS : "+response);
						    if(response.equals("NONE"))
						    	URL_Response="NONE";
						    else 
						    {
						    	service_list=response.split(",");
						    	URL_Response="SUCCESS";
						    }
						    
				       } 
				    catch (ClientProtocolException e) 
				    {
					   
					    Log.d("DOMESTIC","CPE response " + e.toString());
					    // TODO Auto-generated catch block
					    URL_Response="INVALID";
				    } 
				    catch (IOException e) 
				    {
						    
						    Log.d("DOMESTIC","IOE response " + e.toString());
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
		        Log.d("DOMESTIC","the URL VALU IS : "+URL_Response);
		        if(URL_Response.equals("NONE"))
		        {
		        	Toast.makeText(UserAddedServiceList.this, "NO ANY SERVICE REQUEST ADDED BY USER", Toast.LENGTH_LONG).show();
		        	
		        }
		        else
		        {
		        	
		 		    for (int i = 0; i < service_list.length; ++i) {
		 		      list1.add(service_list[i]);
		 		    }
		 		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(UserAddedServiceList.this,android.R.layout.simple_list_item_1, list1);
		 		   user_list.setAdapter(adapter);
		        }
		    }	
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	

}
