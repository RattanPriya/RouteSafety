package com.example.mysafety;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void bFunction(View view)
    {
        Intent routeIntent = new Intent(this, Route.class);
        EditText src = (EditText) findViewById(R.id.sourceTxtBx);
        EditText dest = (EditText) findViewById(R.id.destinationTxtBx);
        Bundle RouteBndl = new Bundle();
        RouteBndl.putString("source",(String.valueOf(src.getText())).replaceAll(" ","+"));
        RouteBndl.putString("destination",(String.valueOf(dest.getText())).replaceAll(" ","+"));
        routeIntent.putExtras(RouteBndl);
        startActivity(routeIntent);

    }
    
}
