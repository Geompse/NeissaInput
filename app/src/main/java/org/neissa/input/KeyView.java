package org.neissa.input;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.widget.Gallery.*;
import android.view.*;
import android.os.*;

public class KeyView extends TextView
{
	public AttributeSet attributes;
	public String attrShort;
	public String attrLong;
	public String attrSpecial;
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
	public void init()
	{
		attrShort = attributes.getAttributeValue("http://neissa.org","short");
		attrLong = attributes.getAttributeValue("http://neissa.org","long");
		attrSpecial = attributes.getAttributeValue("http://neissa.org","special");
		
		setTextSize(20.0f);
		
		setOnTouchListener(new OnTouchListener() {
				public boolean touchDone = false;
				public boolean firstDone = false;
				public KeyView touchItem;
				
				Runnable runnable = new Runnable() {
					public void run() {
						touchDone = true;
						if (MainService.current != null)
							MainService.current.exec(touchItem, true);
						if(touchItem.attrSpecial != null)
							handler.postDelayed(this,firstDone?150:500);
						firstDone = true;
					}
				};
				Handler handler = new android.os.Handler();
				
				@Override
				public boolean onTouch(View item, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						touchDone = false;
						firstDone = false;
						touchItem = (KeyView)item;
						item.setBackgroundColor(0xFFFF8800);
						handler.postDelayed(runnable,100);
					}
					else if (event.getAction() == MotionEvent.ACTION_UP)
					{
						handler.removeCallbacks(runnable);
						if (MainService.current != null && !touchDone)
							MainService.current.exec(touchItem, false);
						touchItem.setBackgroundColor(0);
					}
					return true;
				}
			});
	}
}
