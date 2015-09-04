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
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ApprovedServiceDetailsActivity extends ActionBarActivity implements OnClickListener
{
	String service_id="";
	
	ProgressDialog pDialog;
	String logged_user="";
	String agent_service_type;
    String URL_Response="";
	String URL_SERVICE_DETAILS = "http://domesticapp.3pixelart.com/get_service_details.php";
	String URL_USER_CANCEL = "http://domesticapp.3pixelart.com/agent_cancel_request.php";
	TextView user_number,date_time,service_uid;
	EditText service_desc,address;
	Button btn_cancel;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_approvedservicedetails);
		
		service_id=this.getIntent().getStringExtra("sel_service");
		int i=service_id.indexOf("(");
		int j=service_id.indexOf(")");
		service_id=service_id.substring(i+1, j);
		
		logged_user=this.getIntent().getStringExtra("logged_user");
		agent_service_type=this.getIntent().getStringExtra("agent_service_type");
		
        service_uid=(TextView)findViewById(R.id.service_id);
		
		user_number=(TextView) findViewById(R.id.user_no);
		user_number.setOnClickListener(this);
		
		date_time=(TextView) findViewById(R.id.date_time);
		
		service_desc=(EditText) findViewById(R.id.description);
		address=(EditText) findViewById(R.id.address);
		
		btn_cancel=(Button) findViewById(R.id.btn_cancel);
		btn_cancel.setOnClickListener(ApprovedServiceDetailsActivity.this);
		
		service_desc.setFocusable(false);
		service_desc.setClickable(true);
		
		address.setFocusable(false);
		address.setClickable(true);
		
		new get_service_details().execute();
		
	}
	
	
	private class get_service_details extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(ApprovedServiceDetailsActivity.this);
		        pDialog.setMessage("Fetching Service Details..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_SERVICE_DETAILS);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("sel_service",service_id));
						    
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						    //This is the response from a php application
						   // String reverseString = response;
						   // server_message_list=response;
						    //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
						    Log.d("DOMESTIC","URL RES IS : "+response);
						    if(response.equals("NOTAV"))
						    	URL_Response="NOTAV";
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
		        if(URL_Response.equals("NOTAV"))
		        {
		        	Toast.makeText(ApprovedServiceDetailsActivity.this, "SORRY REQUEST IS ALREADY OCCUPIED", Toast.LENGTH_LONG).show();
                   Intent in=new Intent(ApprovedServiceDetailsActivity.this,AgentSectionActivity.class);
                   in.putExtra("logged_user", logged_user);
					startActivity(in);
		        }
		        else
		        {
		        	String services[]=URL_Response.split("#");
		        	service_uid.setText(services[0]);
		        	user_number.setText(services[1]);
		        	date_time.setText(services[2]);
		        	service_desc.setText(services[3]);
		        	address.setText(services[4]);
		        }
		    }	
		
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==user_number)
		{
			Intent callIntent = new Intent(Intent.ACTION_CALL);
			callIntent.setData(Uri.parse("tel:+"+user_number.getText().toString().trim()));
			startActivity(callIntent );
		}
		if(v==btn_cancel)
		{
			new cancel_request().execute();
		}
	}
	
	private class cancel_request extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(ApprovedServiceDetailsActivity.this);
		        pDialog.setMessage("Cancelling the request..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_USER_CANCEL);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("sel_service",service_id));
						    
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						    //This is the response from a php application
						   // String reverseString = response;
						   // server_message_list=response;
						    //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
						    Log.d("DOMESTIC","URL RES IS : "+response);
						    if(response.equals("NOTAV"))
						    	URL_Response="NOTAV";
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
		        if(URL_Response.equals("INVALID"))
		        {
		        	Toast.makeText(ApprovedServiceDetailsActivity.this, "ERROR IN CANCELATION", Toast.LENGTH_LONG).show();
                  Intent in=new Intent(ApprovedServiceDetailsActivity.this,AgentSectionActivity.class);
                  in.putExtra("logged_user", logged_user);
					startActivity(in);
		        }
		        else
		        {
		        	   Toast.makeText(ApprovedServiceDetailsActivity.this, "REQUEST CANCELLED SUCCESSFULLY", Toast.LENGTH_LONG).show();
	                   Intent in=new Intent(ApprovedServiceDetailsActivity.this,AgentSectionActivity.class);
	                   in.putExtra("logged_user", logged_user);
	                   in.putExtra("service_type",agent_service_type);
					   startActivity(in);
		        }
		    }	
		
	}
}
