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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class AgentAprrovedServiceList extends ActionBarActivity
{
	ListView service_list;
	String agent_service_type="";
	ProgressDialog pDialog;
    String URL_Response="";
	String URL_REQUESTS = "http://domesticapp.3pixelart.com/agent_approved_services.php";
	String logged_user="";
	
	final ArrayList<String> list1 = new ArrayList<String>();
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agentaprrovedservicelist);
		
		service_list=(ListView) findViewById(R.id.service_list);
		
		logged_user=this.getIntent().getStringExtra("logged_user");
		
		agent_service_type=this.getIntent().getStringExtra("service_type");
		
		Log.d("DOMESTIC","LOGGED USER AGENT APPROVED ACTIVITY  SERVICE LIST "+logged_user);
		
		new s_approved_lists().execute();
		
		
		 service_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

		     

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					
					 String selectedservice=list1.get(position);
					 Toast.makeText(getApplicationContext(), "Selected Service : "+selectedservice,   Toast.LENGTH_LONG).show();
					 Intent in=new Intent(AgentAprrovedServiceList.this,ApprovedServiceDetailsActivity.class);
					 
					 in.putExtra("sel_service", selectedservice);
					 in.putExtra("agent_service_type",agent_service_type);
					 in.putExtra("logged_user", logged_user);
					 
					 
					 		     
					 startActivity(in);
					 
					 
				}

		      });
			
	}
	
	private class s_approved_lists extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(AgentAprrovedServiceList.this);
		        pDialog.setMessage("Fetching all Approved Requests..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_REQUESTS);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("logged_user",logged_user));
						    
						    
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						    //This is the response from a php application
						   // String reverseString = response;
						   // server_message_list=response;
						    //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
						    Log.d("DOMESTIC","URL RES IS : "+response);
						    if(response.equals("NONE"))
						    	URL_Response="NONE";
						    else 
						    {
						    	URL_Response=response;
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
		        	Toast.makeText(AgentAprrovedServiceList.this, "NO ANY APPROVED REQUESTS", Toast.LENGTH_LONG).show();
		        	
		        }
		        else
		        {
		        	String services[]=URL_Response.split(",");
		        	for (int i = 0; i < services.length; ++i) {
			 		      list1.add(services[i]);
			 		    }
			 		    ArrayAdapter<String> adapter = new ArrayAdapter<String>(AgentAprrovedServiceList.this,android.R.layout.simple_list_item_1, list1);
			 		   service_list.setAdapter(adapter);
		        }
		    }	
		
	}

}
