package org.neissa.input;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.widget.Gallery.*;
import android.view.*;
import android.os.*;
import android.content.res.*;

public class KeyView extends TextView
{
	public AttributeSet attributes;
	public String attrShort;
	public String attrLong;
	public String attrSpecial;
	public String attrHalf;
	
	public boolean touchDone = false;
	public boolean firstDone = false;
	public static KeyView touchItem;
	public static Runnable runnable = new Runnable() {
		public void run() {
			touchItem.touchDone = true;
			if (MainService.current != null)
				MainService.current.exec(touchItem, true);
			if(touchItem.attrSpecial != null)
				handler.postDelayed(this,touchItem.firstDone?150:500);
			touchItem.firstDone = true;
		}
	};
	public static Handler handler = new android.os.Handler();
	
	public KeyView(Context context)
	{
		super(context);
		init();
	}
	public KeyView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		attributes = attrs;
		init();
	}
	public KeyView(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
		attributes = attrs;
		init();
	}
	protected void onDraw(Canvas canvas)
	{
        super.onDraw(canvas);
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		handler.removeCallbacks(runnable);
	}
	
	public void init()
	{
		attrShort = attributes.getAttributeValue("http://neissa.org","short");
		attrLong = attributes.getAttributeValue("http://neissa.org","long");
		attrSpecial = attributes.getAttributeValue("http://neissa.org","special");
		attrHalf = attributes.getAttributeValue("http://neissa.org","half");
		
		setTextColor(0xFFFFFFFF);
		setTextSize(20.0f);
		if(attrHalf == null)
			setBackgroundResource(R.drawable.key);
		else if(attrHalf.equals("1"))
			setBackgroundResource(R.drawable.halfkey1);
		else if(attrHalf.equals("2"))
			setBackgroundResource(R.drawable.halfkey2);
			
		setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View item, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						handler.removeCallbacks(runnable);
						touchItem = (KeyView)item;
						touchItem.touchDone = false;
						touchItem.firstDone = false;
						if(!"DELETE".equals(touchItem.attrSpecial) && !"SUPPR".equals(touchItem.attrSpecial))
							touchItem.setBackgroundColor(0xFFFF8800);
						handler.postDelayed(runnable,150);
					}
					else if (event.getAction() == MotionEvent.ACTION_UP)
					{
						handler.removeCallbacks(runnable);
						if (MainService.current != null && !touchItem.touchDone)
							MainService.current.exec(touchItem, false);
						if(attrHalf == null)
							setBackgroundResource(R.drawable.key);
						else if(attrHalf.equals("1"))
							setBackgroundResource(R.drawable.halfkey1);
						else if(attrHalf.equals("2"))
							setBackgroundResource(R.drawable.halfkey2);
					}
					return true;
				}
			});
	}
}
