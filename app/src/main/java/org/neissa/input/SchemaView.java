package org.neissa.input;
import android.widget.*;
import android.graphics.*;
import android.util.*;
import android.content.*;
import android.content.res.*;

public class SchemaView extends LinearLayout
{
	public AttributeSet attributes;
	public String attrText;
	
	public SchemaView(Context context)
	{
		super(context);
		init();
	}
	public SchemaView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		attributes = attrs;
		init();
	}
	public SchemaView(Context context, AttributeSet attrs, int defStyle)
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
		attrText = attributes.getAttributeValue("http://neissa.org", "text");
		if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
			setPadding(6,2,6,2);
		else
			setPadding(2,2,2,6);
	}
}
