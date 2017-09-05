package org.neissa.input;
import android.widget.*;
import android.content.*;
import android.util.*;
import android.graphics.*;
import android.widget.Gallery.*;
import android.view.*;
import android.os.*;
import android.content.res.*;
import android.content.pm.*;
import java.util.*;

public class KeyView extends TextView
{
	public AttributeSet attributes;
	public String attrShort;
	public String attrLong;
	public String attrSpecial;
	public String attrHalf;
	public String uid;

	public boolean touchDone = false;
	public boolean firstDone = false;
	public KeyView touchItem;

	public static HashMap<String,myRunnable> runnables = new HashMap<String,myRunnable>();
	public static Handler handler = new android.os.Handler();
	class myRunnable implements Runnable {
		public KeyView touchItem;
		public void run()
		{
			touchItem.touchDone = true;
			if (MainService.current != null)
				MainService.current.exec(touchItem, true);
			if (touchItem.attrSpecial != null)
				handler.postDelayed(this, touchItem.firstDone ?150: 500);
			touchItem.firstDone = true;
		}
	}
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
		if ("LANG".equals(attrSpecial))
		{
			ViewParent parent = this.getParent();
			while(parent != null && parent.getParent() != null && !(parent instanceof SchemaView))
				parent = parent.getParent();
			if(parent != null)
				setText("BR".equals(((SchemaView)parent).attrText)?"🇧🇷":"🇫🇷");
		}
	}

	@Override
	protected void onConfigurationChanged(Configuration newConfig)
	{
		super.onConfigurationChanged(newConfig);
		handler.removeCallbacks(runnables.get(uid));
	}

	public void init()
	{
		attrShort = attributes.getAttributeValue("http://neissa.org", "short");
		attrLong = attributes.getAttributeValue("http://neissa.org", "long");
		attrSpecial = attributes.getAttributeValue("http://neissa.org", "special");
		attrHalf = attributes.getAttributeValue("http://neissa.org", "half");
		uid = attrShort+"#"+attrLong+"#"+attrSpecial+"#"+attrHalf;
		if (!runnables.containsKey(uid))
			runnables.put(uid, new myRunnable());
		runnables.get(uid).touchItem = this;
		setTextColor(attrSpecial != null && attrSpecial.indexOf("SELECT") == 0 ? 0xFF00CCFF : 0xFFFFFFFF);
		setTextSize(MainService.current != null ? 20.0f : 10.0f);
		if ("LANG".equals(attrSpecial))
			setBackgroundColor(0x00000000);
		else if (attrHalf == null)
			setBackgroundResource(R.drawable.key);
		else if (attrHalf.equals("1"))
			setBackgroundResource(R.drawable.halfkey1);
		else if (attrHalf.equals("2"))
			setBackgroundResource(R.drawable.halfkey2);
		setOnTouchListener(new OnTouchListener() {

				@Override
				public boolean onTouch(View item, MotionEvent event)
				{
					if (event.getAction() == MotionEvent.ACTION_DOWN)
					{
						handler.removeCallbacks(runnables.get(uid));
						touchItem = (KeyView)item;
						touchItem.touchDone = false;
						touchItem.firstDone = false;
						if (touchItem.attrHalf == null)
							touchItem.setBackgroundColor(0xFFFF8800);
						else
						{
							runnables.get(touchItem.attrShort+"#"+touchItem.attrLong+"#"+touchItem.attrSpecial+"#1").touchItem.setBackgroundColor(0xFFFF8800);
							runnables.get(touchItem.attrShort+"#"+touchItem.attrLong+"#"+touchItem.attrSpecial+"#2").touchItem.setBackgroundColor(0xFFFF8800);
						}
						if(!"LANG".equals(touchItem.attrSpecial))
							handler.postDelayed(runnables.get(uid), 150);
					}
					else if (event.getAction() == MotionEvent.ACTION_UP)
					{
						handler.removeCallbacks(runnables.get(uid));
						if (MainService.current != null && !touchItem.touchDone)
							MainService.current.exec(touchItem, false);
						if ("LANG".equals(attrSpecial))
							setBackgroundColor(0x00000000);
						else if (touchItem.attrHalf == null)
							setBackgroundResource(R.drawable.key);
						else
						{
							runnables.get(touchItem.attrShort+"#"+touchItem.attrLong+"#"+touchItem.attrSpecial+"#1").touchItem.setBackgroundResource(R.drawable.halfkey1);
							runnables.get(touchItem.attrShort+"#"+touchItem.attrLong+"#"+touchItem.attrSpecial+"#2").touchItem.setBackgroundResource(R.drawable.halfkey2);
						}
					}
					return true;
				}
			});
	}
}
