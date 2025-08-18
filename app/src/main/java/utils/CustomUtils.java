package utils;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class CustomUtils {
    public static void toastMessages(Context activity_page, String toastMessage){
        Toast.makeText( activity_page, toastMessage, Toast.LENGTH_LONG ).show();
    }

    public static void errorMessages(Context activity_page, String errorMessage, Exception e){
        toastMessages(activity_page, errorMessage + e.getMessage());
    }
}