package org.neissa.input;
import android.app.*;
import android.os.*;
import android.content.res.*;
import android.view.*;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(MainService.init(getLayoutInflater()));
	}
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		//getWindow().setGravity(64);
		//getWindow().setLayout(-1,500);
	}

}
