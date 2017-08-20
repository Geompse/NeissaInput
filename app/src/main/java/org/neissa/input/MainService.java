package org.neissa.input;

import android.app.*;
import android.os.*;
import android.content.*;
import android.view.*;
import android.inputmethodservice.*;
import android.widget.*;
import android.view.View.*;
import android.view.inputmethod.*;

public class MainService extends InputMethodService
{
	public static MainService current;

	public void exec(KeyView item, boolean longPressed)
	{
		InputConnection ic = getCurrentInputConnection();
		
		if (item.attrSpecial != null)
		{
			int action = 0;
			int modifier = 0;
			switch (item.attrSpecial)
			{
				case "TAB":
					action = KeyEvent.KEYCODE_TAB;
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
				case "ESC":
					action = KeyEvent.KEYCODE_ESCAPE;
					break;
				
				case "DELETE":
					action = KeyEvent.KEYCODE_DEL;
					break;
				case "SUPPR":
					action = KeyEvent.KEYCODE_FORWARD_DEL;
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
				case "SPACE":
					action = longPressed ? KeyEvent.KEYCODE_ENTER : KeyEvent.KEYCODE_SPACE;
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
				ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, action, 0, modifier));
				ic.sendKeyEvent(new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_UP, action, 0, modifier));
			}
		}
		else
			ic.commitText(longPressed ? item.attrLong : item.attrShort, 1);
	}

    @Override
    public View onCreateInputView()
    {
		current = this;
        return init(getLayoutInflater());
	}
	public static View init(LayoutInflater parent)
	{
		LinearLayout view = (LinearLayout)parent.inflate(R.layout.main, null);
		return view;
    }
}
