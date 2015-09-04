package com.app.domesticapp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class AgentRegisterActivity extends ActionBarActivity implements OnClickListener
{

	EditText t1,t2,t3,t4,t5,t6;
	Spinner s_type;
	String selected_service;
	Button b1,b2;
	List<String> list_services;
	
	private static final String EMAIL_REGEX="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	
	 private static Pattern pattern;
	    //non-static Matcher object because it's created from the input String
	 private Matcher matcher;
	
	ProgressDialog pDialog;
    String URL_Response="";
	String URL_REGISTER = "http://domesticapp.3pixelart.com/register_agent.php";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agentregister);
		
		t1=(EditText) findViewById(R.id.fname);
		t2=(EditText) findViewById(R.id.lname);
		t3=(EditText) findViewById(R.id.m_no);
		t4=(EditText) findViewById(R.id.password);
		t5=(EditText) findViewById(R.id.cpassword);
		t6=(EditText) findViewById(R.id.org);
		s_type=(Spinner) findViewById(R.id.service_type);
		
		pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		
		b1=(Button)findViewById(R.id.button_reg);
		b1.setOnClickListener(AgentRegisterActivity.this);
		b2=(Button)findViewById(R.id.button_clear);
		b2.setOnClickListener(AgentRegisterActivity.this);
		
		
		list_services = new ArrayList<String>();
		list_services.add("--SELECT--");
		list_services.add("PLUMBER");
		list_services.add("ELECTRICIAN");
		list_services.add("CARPENTER");
		
		
		ArrayAdapter<String> adapter=new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,list_services);
		s_type.setAdapter(adapter);
		
		s_type.setOnItemSelectedListener(new OnItemSelectedListener() 
		 {
	  		  

				@Override
				public void onItemSelected(AdapterView<?> arg0, View arg1,int arg2, long arg3) {
					// TODO Auto-generated method stub
					if(arg2<0)
						arg2=0;
					
					selected_service=(String) s_type.getItemAtPosition(arg2);
					Toast.makeText(AgentRegisterActivity.this,"SERVICE SELECTED : "+selected_service,3000).show();	
					   
				    
				}

				@Override
				public void onNothingSelected(AdapterView<?> arg0) {
					// TODO Auto-generated method stub
					
				}

			});
		
		}
	
	
	public boolean is_email(String in_email)
	{
	    String email_parts[]=in_email.split("@");///email_parts[0]=yogesh,email_parts[1]=gmail.com
	    if(email_parts.length==2)
	    {
		     String further_parts[]=email_parts[1].split("\\.");
			 if(further_parts.length==2)
			 {
			    if(further_parts[1].equals("com")||further_parts[1].equals("in")||further_parts[1].equals("org"))
			    {
			    	matcher = pattern.matcher(in_email);
			    	return matcher.matches();
			    }
		        
				    
				else
			      return false;  
			 }
			 else
			  return false;
		}
		else
		 return false;
	
	}
	
	public boolean is_num(String in_number)
	{
	      try
		    {
			     double i=Double.parseDouble(in_number);
				 if(in_number.length()==10)
				   return true;
				 else
				  return false;
			}
		 catch(Exception e)
            {
                return false;
            }			
	
	}
	
	public static boolean validateFirstName( String firstName )
	   {
	      return firstName.matches( "[A-Z][a-zA-Z]*" );
	   } // end method validateFirstName

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v==b1)
		{
			if(t1.getText().toString().equals("")||t2.getText().toString().equals("")||t3.getText().toString().equals("")||t5.getText().toString().equals("")||t6.getText().toString().equals(""))
			{
				Toast.makeText(AgentRegisterActivity.this,"INVALID DETAILS PROVIDED",3000).show();
			}
			else
			{
				if(t4.getText().toString().equals(t5.getText().toString()))
				{
					if(validateFirstName(t1.getText().toString())&&validateFirstName(t2.getText().toString()))
					{
						if(is_num(t3.getText().toString()))
						{
					        new Regsiter_agent().execute();
						}
							else
							{
								Toast.makeText(AgentRegisterActivity.this,"INVALID MOBILE NUMBER",3000).show();
							}
					}
					else
						Toast.makeText(AgentRegisterActivity.this,"INVALID FIRSTNAME/LASTNAME",3000).show();
					
					
					
				}
				else
					Toast.makeText(AgentRegisterActivity.this,"PASSOWRD DONOT MATCH",3000).show();
				
			}
			
			
		}
		if(v==b2)
		{
			t1.setText("");
			t2.setText("");
			t3.setText("");
			t4.setText("");
			t5.setText("");
			t6.setText("");
			
		}	
	
	}
	
	
	private class Regsiter_agent extends AsyncTask<Void, Void, Void> {
		
		 @Override
		    protected void onPreExecute() {
		        super.onPreExecute();
		        pDialog = new ProgressDialog(AgentRegisterActivity.this);
		        pDialog.setMessage("Registering User..");
		        pDialog.setCancelable(false);
		        pDialog.show();
		 
		    }
		 
		    @Override
		    protected Void doInBackground(Void... arg0) {
		    	 HttpClient httpclient = new DefaultHttpClient();
				    HttpPost httppost = new HttpPost(URL_REGISTER);
				try {
						   
				
					// Add your data
						    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
						    nameValuePairs.add(new BasicNameValuePair("fname",t1.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("lname",t2.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("m_no",t3.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("pass",t4.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("org",t6.getText().toString()));
						    nameValuePairs.add(new BasicNameValuePair("service_type",selected_service));
						   
						    
					
						    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					
						    // Execute HTTP Post Request
					
						    ResponseHandler<String> responseHandler = new BasicResponseHandler();
						    String response = httpclient.execute(httppost, responseHandler);
					
						    //This is the response from a php application
						   // String reverseString = response;
						   // server_message_list=response;
						    //Toast.makeText(this, "response" + reverseString, Toast.LENGTH_LONG).show();
						    Log.d("DOMESTIC","URL RES IS : "+response);
						    if(response.equals("DONE"))
						    	URL_Response="DONE";
						    else if(response.equals("EXISTS"))
						    	URL_Response="EXISTS";
						    else
						    	URL_Response="INVALID";
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
		        if(URL_Response.equals("DONE"))
		        {
		        	Toast.makeText(AgentRegisterActivity.this, "REGISTRATION SUCCESSFULL", Toast.LENGTH_LONG).show();
		        	SQLiteDatabase db=openOrCreateDatabase("DOMESTIC", MODE_PRIVATE, null);
		    		db.execSQL("create table if not exists users (registered varchar(5));");
		    		String reg="YES";
		    		db.execSQL("insert into users values('"+reg+"');");
			        db.close();
		    		
		        	Intent in=new Intent(AgentRegisterActivity.this,MainActivity.class);
		        	startActivity(in);
		        }
		        else
		        {
		        	if(URL_Response.equals("EXISTS"))
		        		Toast.makeText(AgentRegisterActivity.this, "AGENT ALREADY REGISTERED", Toast.LENGTH_LONG).show();
		        	else
		        	 Toast.makeText(AgentRegisterActivity.this, "REGISTRATION ERROR", Toast.LENGTH_LONG).show();
		        }
		    }	
		
	}

}
