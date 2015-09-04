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

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public class UserSectionActivity extends ActionBarActivity implements OnClickListener
{

	Location current_location;
	LocationManager manager;
	String Provider_name="";
	boolean isGpsEnabled,isNetworkEnabled;
	Double latitude,longitude;
	
	Button b1;
	
	ImageView iv1,iv2;
	String logged_user="";
	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usersection);
		
		 if (android.os.Build.VERSION.SDK_INT > 9) {
			    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			    StrictMode.setThreadPolicy(policy);
			}
		 
		 b1=(Button) findViewById(R.id.button1);
		 b1.setOnClickListener(UserSectionActivity.this);
		
		logged_user=this.getIntent().getStringExtra("logged_user");
		
		iv1=(ImageView) findViewById(R.id.imageView1);
		iv1.setOnClickListener(UserSectionActivity.this);
		iv2=(ImageView) findViewById(R.id.imageView2);
		iv2.setOnClickListener(UserSectionActivity.this);
		
		manager=(LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
		isGpsEnabled=manager.isProviderEnabled(LocationManager.GPS_PROVIDER); 
        isNetworkEnabled=manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if(isGpsEnabled||isNetworkEnabled)
        {
            if(isGpsEnabled)
			{
			   Provider_name=LocationManager.GPS_PROVIDER;
			}
			if(isNetworkEnabled)
			{
			    Provider_name=LocationManager.NETWORK_PROVIDER;
			}
        }
        else
        {
           Toast.makeText(UserSectionActivity.this,"NO LOCATION DATA AVAILABLE",4000).show();
        }	
        
        Log.d("DOMESTIC","THE PROVIDER NAME IS : "+Provider_name);
		current_location=manager.getLastKnownLocation(Provider_name);
		LocationListener listener=new UserLocationListener();
		manager.requestLocationUpdates(Provider_name, 120000, 0, listener);
		
		Log.d("DOMESTIC","THE CURRENT LOC IS : "+current_location);
		if(current_location!=null)
		{
				
				latitude=current_location.getLatitude();
				longitude=current_location.getLongitude();
				Toast.makeText(UserSectionActivity.this,"LATITUDE IS : "+latitude+"\n LONGITUDE IS :"+longitude+"",4000).show();
				senddata(logged_user,Double.toString(latitude),Double.toString(longitude));
		}
		else
		{
		   Toast.makeText(UserSectionActivity.this,"NO STORED LOCATION AVAILABLE",4000).show();
		}
		
		
	}
	
	
	public int senddata(String username,String lat,String lon){
		 
		 HttpClient httpclient = new DefaultHttpClient();
		    HttpPost httppost = new HttpPost("http://domesticapp.3pixelart.com/update_user_data.php");
		try {
				    // Add your data
				    List <NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
				    nameValuePairs.add(new BasicNameValuePair("username", username));
				    nameValuePairs.add(new BasicNameValuePair("lat", lat));
				    nameValuePairs.add(new BasicNameValuePair("lon", lon));
				    			   
				    
			
				    httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			
				    // Execute HTTP Post Request
			
				    ResponseHandler<String> responseHandler = new BasicResponseHandler();
				    String response = httpclient.execute(httppost, responseHandler);
			
				    //This is the response from a php application
				    
				   // server_mesg_list=response;
				   // Toast.makeText(this, "response" + server_mesg_list, Toast.LENGTH_LONG).show();
				    return 1;
	
		       } 
		    catch (ClientProtocolException e) 
		    {
			    Toast.makeText(this, "CPE response " + e.toString(), Toast.LENGTH_LONG).show();
			    // TODO Auto-generated catch block
			    return 0;
		    } 
		    catch (IOException e) 
		    {
				    Toast.makeText(this, "IOE response " + e.toString(), Toast.LENGTH_LONG).show();
				    // TODO Auto-generated catch block
				    return 0;
		    }
		
		
	}
	
	public class UserLocationListener implements LocationListener
	{

		@Override
		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub
			current_location=location;
			latitude=current_location.getLatitude();
			longitude=current_location.getLongitude();
			Toast.makeText(UserSectionActivity.this,"LATITUDE IS : "+latitude+"\n LONGITUDE IS :"+longitude+"",4000).show();
			senddata(logged_user,Double.toString(latitude),Double.toString(longitude));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
		
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		if(arg0==iv1)
		{
			    Intent in=new Intent(UserSectionActivity.this,ServiceRequestActivity.class);
			    in.putExtra("logged_user",logged_user);
			    in.putExtra("lat", Double.toString(latitude));
			    in.putExtra("lon", Double.toString(longitude));
				startActivity(in);
		}
		if(arg0==iv2)
		{
			Intent in=new Intent(UserSectionActivity.this,UserAddedServiceList.class);
		    in.putExtra("logged_user",logged_user);
		    startActivity(in);
		}
		
		if(arg0==b1)
		{
			Intent in=new Intent(UserSectionActivity.this,MainActivity.class);
		    startActivity(in);
		   
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		Intent in=new Intent(getApplicationContext(),MainActivity.class);
		startActivity(in);
	}

}
