package com.ttl.helper;

import java.util.Arrays;
import java.util.List;

import android.widget.AutoCompleteTextView;

public class Validator implements AutoCompleteTextView.Validator {
	String[] valid ; 
	public Validator(List<String> values)
	{
		
		valid = new String[values.size()];
			valid = values.toArray(valid);
	}
    @Override
    public boolean isValid(CharSequence text) {
      
    	
        Arrays.sort(valid);
        if (Arrays.binarySearch(valid, text.toString()) > 0) {
            return true;
        }

        return false;
    }

    @Override
    public CharSequence fixText(CharSequence invalidText) {
              return "";
    }
}