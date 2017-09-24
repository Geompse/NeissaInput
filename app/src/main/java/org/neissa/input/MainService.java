package org.neissa.input;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.inputmethodservice.*;
import android.widget.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.content.res.*;
import android.util.*;
import android.content.pm.PackageManager.*;
import android.content.pm.*;

public class MainService extends InputMethodService
{
	public static MainService current;

	public static String[][] schemas = {
		{"fr","🇫🇷"},
		{"br","🇧🇷"},
		{"en","🇬🇧"},
		{"jp","🇯🇵"},
	};
	public void exec(KeyView item, boolean longPressed)
	{
		InputConnection ic = getCurrentInputConnection();

		if (item.attrSpecial != null)
		{
			int action = 0;
			int modifier = 0;
			int repeat = 1;
			switch (item.attrSpecial)
			{
				case "LANG":
					SharedPreferences sharedPref = getSharedPreferences("org.neissa.input", Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = sharedPref.edit();
					String currentSchemaname = sharedPref.getString("schemaname", "schemafr");
					String schemaname = "schema"+schemas[0][0];
					boolean next = false;
					for(String[] schema : schemas)
					{
						if(("schema"+schema[0]).equals(currentSchemaname))
							next = true;
						else if(next)
						{
							schemaname = "schema"+schema[0];
							next = false;
						}
					}
					editor.putString("schemaname", schemaname);
					editor.commit();
					setInputView(init(getLayoutInflater(), getCurrentSchema()));
					return;
				case "SELECT_LEFT":
					action = KeyEvent.KEYCODE_DPAD_LEFT;
					modifier = KeyEvent.META_SHIFT_ON;
					repeat = longPressed ? 5 : 1;
					break;
				case "SELECT_RIGHT":
					action = KeyEvent.KEYCODE_DPAD_RIGHT;
					modifier = KeyEvent.META_SHIFT_ON;
					repeat = longPressed ? 5 : 1;
					break;
				case "SELECT_UP":
					action = KeyEvent.KEYCODE_DPAD_UP;
					modifier = KeyEvent.META_SHIFT_ON;
					repeat = longPressed ? 5 : 1;
					break;
				case "SELECT_DOWN":
					action = KeyEvent.KEYCODE_DPAD_DOWN;
					modifier = KeyEvent.META_SHIFT_ON;
					repeat = longPressed ? 5 : 1;
					break;

				case "TAB":
					action = KeyEvent.KEYCODE_TAB;
					break;
				case "SEARCH":
					action = KeyEvent.KEYCODE_F;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "SELECT":
					action = KeyEvent.KEYCODE_A;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "CUT":
					action = KeyEvent.KEYCODE_X;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "COPY":
					action = KeyEvent.KEYCODE_C;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "PASTE":
					action = KeyEvent.KEYCODE_V;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "UNDO":
					action = KeyEvent.KEYCODE_Z;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "REDO":
					action = KeyEvent.KEYCODE_Y;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "BOLD":
					action = KeyEvent.KEYCODE_B;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "ITALIC":
					action = KeyEvent.KEYCODE_I;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "UNDERLINE":
					action = KeyEvent.KEYCODE_U;
					modifier = KeyEvent.META_CTRL_ON;
					break;
				case "QUIT":
					action = KeyEvent.KEYCODE_F4;
					modifier = KeyEvent.META_ALT_ON;
					break;
				case "ESC":
					action = KeyEvent.KEYCODE_ESCAPE;
					break;

				case "DELETE":
					action = KeyEvent.KEYCODE_DEL;
					break;
				case "SUPPR":
					action = KeyEvent.KEYCODE_FORWARD_DEL;
					break;
					
				case "SPACE":
					action = KeyEvent.KEYCODE_SPACE;
					break;
				case "ENTER":
					action = KeyEvent.KEYCODE_ENTER;
					break;
				case "ARROW_LEFT":
					action = KeyEvent.KEYCODE_DPAD_LEFT;
					if (longPressed)
						modifier = KeyEvent.META_CTRL_ON;
					break;
				case "ARROW_RIGHT":
					action = KeyEvent.KEYCODE_DPAD_RIGHT;
					if (longPressed)
						modifier = KeyEvent.META_CTRL_ON;
					break;
				case "ARROW_UP":
					action = longPressed ? KeyEvent.KEYCODE_PAGE_UP : KeyEvent.KEYCODE_DPAD_UP;
					break;
				case "ARROW_DOWN":
					action = longPressed ? KeyEvent.KEYCODE_PAGE_DOWN : KeyEvent.KEYCODE_DPAD_DOWN;
					break;
			}
			if (action != 0)
			{
				for(int i = 0; i<repeat; i++)
				{
					ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, action, 0, modifier));
					ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, action, 0, modifier));
				}
			}
		}
		else
		{
			SharedPreferences sharedPref = getSharedPreferences("org.neissa.input", Context.MODE_PRIVATE);
			String text = longPressed ? sharedPref.getString("key_" + item.attrShort, "") : item.attrShort;
			if (text.length() == 0)
				text = longPressed ? item.attrLong : item.attrShort;
			ic.commitText(text, 1);
		}
	}

    @Override
    public View onCreateInputView()
    {
		current = this;
		return init(getLayoutInflater(),getCurrentSchema());
	}

	/*@Override
	public void onStartInputView(EditorInfo info, boolean restarting)
	{
		// colorControlActivated
		try
		{
			Log.e("inputneissa", "" + getPackageManager().getPackageInfo(info.packageName, PackageManager.GET_ACTIVITIES|PackageManager.GET_META_DATA).activities[0].getThemeResource());
		}
		catch (PackageManager.NameNotFoundException e)
		{}
		// TODO: Implement this method
		super.onStartInputView(info, restarting);
	}*/

	@Override
	public boolean onEvaluateFullscreenMode()
	{
		return false;
	}
	
	public int getCurrentSchema()
	{
		SharedPreferences sharedPref = getSharedPreferences("org.neissa.input", Context.MODE_PRIVATE);
        String schemaname = sharedPref.getString("schemaname", "schemafr");
		boolean found = false;
		for(String[] schema : schemas)
			if(("schema"+schema[0]).equals(schemaname))
				found = true;
		if(!found)
			schemaname = "schema" + schemas[0][0];
		return getResources().getIdentifier(schemaname + (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE?"_large":""), "layout", getPackageName());
	}
	public static View init(LayoutInflater parent, int schema)
	{
		LinearLayout view = (LinearLayout)parent.inflate(schema, null);
		return view;
    }
}
