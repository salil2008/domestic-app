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

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("NewApi")
public class UserActivity extends ActionBarActivity implements OnClickListener 
{
     
	Button b1,b2;
	EditText t1,t2,t3,t4,t5,t6,t7;
	CheckBox c1,c2,c3;
	
	
	private static final String EMAIL_REGEX="^[\\w-\\+]+(\\.[\\w]+)*@[\\w-]+(\\.[\\w]+)*(\\.[a-z]{2,})$";
	
	 private static Pattern pattern;
	    //non-static Matcher object because it's created from the input String
	 private Matcher matcher;
	
	ProgressDialog pDialog;
    String URL_Response="";
	String URL_REGISTER = "http://domesticapp.3pixelart.com/register_user.php";
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user);
		
		pattern = Pattern.compile(EMAIL_REGEX, Pattern.CASE_INSENSITIVE);
		
		b1=(Button)findViewById(R.id.button_reg);
		b1.setOnClickListener(UserActivity.this);
		b2=(Button)findViewById(R.id.button_clear);
		b2.setOnClickListener(UserActivity.this);
		
		t1=(EditText) findViewById(R.id.fname);
		t2=(EditText) findViewById(R.id.lname);
		t3=(EditText) findViewById(R.id.m_no);
		t4=(EditText) findViewById(R.id.pass);
		t5=(EditText) findViewById(R.id.cpass);
		t6=(EditText) findViewById(R.id.email);
		t7=(EditText) findViewById(R.id.address);
		
		
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
				if(t1.getText().toString().equals("")||t2.getText().toString().equals("")||t3.getText().toString().equals("")||t5.getText().toString().equals(""))
				{
					Toast.makeText(UserActivity.this,"INVALID DETAILS PROVIDED",3000).show();
				}
				else
				{
					if(t4.getText().toString().equals(t5.getText().toString()))
					{
						
						if(validateFirstName(t1.getText().toString())&&validateFirstName(t2.getText().toString()))
						{
							if(is_num(t3.getText().toString()))
							{
								new Regsiter_user().execute();
							}
							else
								{
									Toast.makeText(UserActivity.this,"INVALID MOBILE NUMBER",3000).show();
								}
						}
						else
							Toast.makeText(UserActivity.this,"INVALID FIRSTNAME/LASTNAME",3000).show();
						
						
						
						
					}
					else
						Toast.makeText(UserActivity.this,"PASSOWRD DONOT MATCH",3000).show();
					
				}
				
				
			}
			if(v==b2)
			{
				t1.setText("");
				t2.setText("");
				t3.setText("");
				t4.setText("");
				t5.setText("");
				
			}
			
			
		}
		
		
		private class Regsiter_user extends AsyncTask<Void, Void, Void> {
			
			 @Override
			    protected void onPreExecute() {
			        super.onPreExecute();
			        pDialog = new ProgressDialog(UserActivity.this);
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
							    nameValuePairs.add(new BasicNameValuePair("email",t6.getText().toString()));
							    nameValuePairs.add(new BasicNameValuePair("address",t7.getText().toString()));
							   
							    
						
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
			        	Toast.makeText(UserActivity.this, "REGISTRATION SUCCESSFULL", Toast.LENGTH_LONG).show();
			        	SQLiteDatabase db=openOrCreateDatabase("DOMESTIC", MODE_PRIVATE, null);
			    		db.execSQL("create table if not exists users (registered varchar(5));");
			    		String reg="YES";
			    		db.execSQL("insert into users values('"+reg+"');");
				        db.close();
			    		
			        	Intent in=new Intent(UserActivity.this,MainActivity.class);
			        	startActivity(in);
			        }
			        else
			        {
			        	if(URL_Response.equals("EXISTS"))
			        		Toast.makeText(UserActivity.this, "USER ALREADY REGISTERED", Toast.LENGTH_LONG).show();
			        	else
			        	 Toast.makeText(UserActivity.this, "REGISTRATION ERROR", Toast.LENGTH_LONG).show();
			        }
			    }	
			
		}


}
