package com.app.domesticapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class CategoryActivity extends ActionBarActivity implements OnClickListener 
{

    ImageView iv1,iv2;
   @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_category);
		
		iv1=(ImageView) findViewById(R.id.imageView1);
		iv1.setOnClickListener(CategoryActivity.this);
		iv2=(ImageView) findViewById(R.id.imageView2);
		iv2.setOnClickListener(CategoryActivity.this);
		
		}

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v==iv1)
				{
				    Intent in=new Intent(CategoryActivity.this,UserActivity.class);
					startActivity(in);
				}
				if(v==iv2)
				{
					Intent in=new Intent(CategoryActivity.this,AgentRegisterActivity.class);
					startActivity(in);
				}
				
				
			}

}
