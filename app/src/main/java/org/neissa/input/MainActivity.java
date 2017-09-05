package org.neissa.input;
import android.app.*;
import android.os.*;
import android.content.res.*;
import android.view.*;
import android.widget.*;
import android.graphics.*;
import android.content.*;
import android.text.*;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		LinearLayout mainView = new LinearLayout(this);
		mainView.setOrientation(LinearLayout.VERTICAL);
		
		mainView.addView(MainService.init(getLayoutInflater(),R.layout.schemafr));
		mainView.addView(MainService.init(getLayoutInflater(),R.layout.schemabr));
		for(int i=0; i<2; i++)
			((LinearLayout)((LinearLayout)mainView.getChildAt(i)).getChildAt(0)).getLayoutParams().height = 500;
		
		for(int i=1; i<=10; i++)
		{
			EditText editText = new EditText(this){
					protected void onDraw(Canvas canvas)
					{
						super.onDraw(canvas);
					}
			};
			editText.setHint(""+(i==10?0:i));
			final SharedPreferences sharedPref = getSharedPreferences("org.neissa.input",Context.MODE_PRIVATE);
			String text = sharedPref.getString("key_"+editText.getHint(), "");
			editText.setText(text);
			editText.addTextChangedListener(new TextWatcher(){

					@Override
					public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
					{
					}

					@Override
					public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
					{
						SharedPreferences.Editor editor = sharedPref.edit();
						editor.putString("key_"+((TextView)getCurrentFocus()).getHint(), p1.toString());
						editor.commit();
					}

					@Override
					public void afterTextChanged(Editable p1)
					{
					}
				});
			
			mainView.addView(editText);
		}
		
		ScrollView scrollView = new ScrollView(this);
		scrollView.addView(mainView);
		setContentView(scrollView);
	}
}
