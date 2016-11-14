package by.knick.valera.ui;

import java.util.ResourceBundle;

/**
 * Created by kirill on 14.11.2015.
 */
public class UiStrings {
	private static final UiStrings INSTANCE = new UiStrings();
	
	private ResourceBundle STRINGS_BUNDLE = ResourceBundle.getBundle("strings");	
    
    public ResourceBundle getBundle(){
    	return STRINGS_BUNDLE;
    }
    
    public static UiStrings getInstance(){
    	return INSTANCE;
    }
}
