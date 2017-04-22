package com.simpumind.e_tech_news.activities;

        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.content.res.Configuration;
        import android.content.res.Resources;
        import android.graphics.Color;
        import android.preference.PreferenceManager;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.util.Log;
        import android.view.Gravity;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.AdapterView;
        import android.widget.BaseAdapter;
        import android.widget.Spinner;
        import android.widget.SpinnerAdapter;
        import android.widget.Switch;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.simpumind.e_tech_news.R;
        import com.simpumind.e_tech_news.models.User;
        import com.simpumind.e_tech_news.utils.PrefManager;

        import java.util.ArrayList;
        import java.util.Locale;

        import static java.security.AccessController.getContext;

public class SettingsActivity extends AppCompatActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        getSupportActionBar().setHomeButtonEnabled(true);

        setTitle("General Settings");

        TextView username = (TextView) findViewById(R.id.username);
        TextView userEmail = (TextView) findViewById(R.id.user_email);
        TextView userPhone = (TextView) findViewById(R.id.user_phone);
        TextView textView = (TextView) findViewById(R.id.type);
        Switch socialSwitch = (Switch) findViewById(R.id.sign_out_of_social_switch);
        Switch unsubscribeAll = (Switch) findViewById(R.id.unsubscribe_switch);

        initCustomSpinner();

        User u = PrefManager.getStoredUser(getApplicationContext());

        if(!u.getUsername().equals("")){
            username.setText(u.getUsername());
            userEmail.setText(u.getEmail());
            userPhone.setText(u.getMsisdn());
            String typeMsg = u.getSigninType().equals("") ? "No Social Sign in yet!" : textView.getText() + u.getSigninType();
            textView.setText(typeMsg);

            boolean isSwitched = u.getSigninType().equals("") ? false : true;

            if(!isSwitched){
                socialSwitch.setEnabled(false);
            }else{
                //perform social account switch
            }

        }

    }

    @Override
    public boolean onSupportNavigateUp() {
        //This method is called when the up button is pressed. Just the pop back stack.
        Log.d(TAG,"--onSupportNavigateUp()--");
        getSupportFragmentManager().popBackStack();
        super.onBackPressed();
        return true;
    }

    private void initCustomSpinner() {

        Spinner spinnerCustom= (Spinner)findViewById(R.id.spinner);
        // Spinner Drop down elements
        ArrayList<String> languages = new ArrayList<>();
        languages.add("Select");
        languages.add("English");
        languages.add("French");
        CustomSpinnerAdapter customSpinnerAdapter=new CustomSpinnerAdapter(getApplicationContext(),languages);
        spinnerCustom.setAdapter(customSpinnerAdapter);
        spinnerCustom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String item = parent.getItemAtPosition(position).toString();
                if(position == 1) {
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().putString("lang", "en").commit();
                    Locale locale = new Locale("en");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "Locale in English !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }else if(position == 2){
                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    preferences.edit().putString("lang", "fr").commit();
                    Locale locale = new Locale("fr");
                    Locale.setDefault(locale);
                    Configuration config = new Configuration();
                    config.locale = locale;
                    getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());
                    Toast.makeText(getApplicationContext(), "Locale in French !", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(getApplicationContext(), NewsMainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }

                Toast.makeText(parent.getContext(),  item, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }



    public static class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

        private final Context activity;
        private ArrayList<String> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<String> asr) {
            this.asr = asr;
            activity = context;
        }



        public int getCount()
        {
            return asr.size();
        }

        public Object getItem(int i)
        {
            return asr.get(i);
        }

        public long getItemId(int i)
        {
            return (long)i;
        }



        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(18);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(16, 16, 16, 16);
            txt.setTextSize(16);
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow_drop_down_black_24dp, 0);
            txt.setText(asr.get(i));
            txt.setTextColor(Color.parseColor("#000000"));
            return  txt;
        }

    }

}

