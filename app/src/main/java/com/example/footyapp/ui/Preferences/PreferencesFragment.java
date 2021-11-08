package com.example.footyapp.ui.Preferences;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.footyapp.MainActivity;
import com.example.footyapp.R;
import com.example.footyapp.TriviaActivity;
import com.example.footyapp.ui.matches.MatchesFragment;

public class PreferencesFragment extends Fragment {

    private PreferencesViewModel preferencesViewModel;
    private View view;

    AppCompatCheckBox cb = null;
    AppCompatCheckBox checkBox_darkMode = null;

    Button trivia = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_preferences,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;

        if(trivia == null)
            trivia = getView().findViewById(R.id.button_did_you_know);

        if(cb == null)
            cb = getView().findViewById(R.id.checkBox_favorites);

        if(checkBox_darkMode == null)
            checkBox_darkMode = getView().findViewById(R.id.checkBox_darkMode);

        cb.setChecked(false);
        checkBox_darkMode.setChecked(false);

        trivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TriviaActivity.class);
                startActivity(intent);
            }
        });

        checkBox_darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                MainActivity.isModeNotChanged = false;
                MainActivity.changeMode = !MainActivity.changeMode;

                Intent intent = new Intent(getActivity(),MainActivity.class);
                startActivity(intent);
            }
        });

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked)
                    return;

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.MyDialog);//AlertDialogCustom);
                String message = "Are you sure about that?";
                String title = "Change Favorites";

                builder.setMessage(message).setTitle(title);

                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        TriviaActivity.isUserChangedFavorite = true;
                        MainActivity.isModeNotChanged = true;
                        MatchesFragment.isFavoriteChanged = true;
                        Intent intent = new Intent(getActivity(),MainActivity.class);
                        startActivity(intent);
                        cb.setChecked(false);
                    }
                });

                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        cb.setChecked(false);
                    }
                });

                AlertDialog dialog = builder.create();

                dialog.setIcon(R.drawable.qmark_img);

                dialog.show();

                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorGreen));
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTypeface(dialog.getButton(DialogInterface.BUTTON_POSITIVE).getTypeface(), Typeface.BOLD);
                dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextSize(20);

                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorGreen));
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTypeface(dialog.getButton(DialogInterface.BUTTON_NEGATIVE).getTypeface(), Typeface.BOLD);
                dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextSize(20);

                TextView textViewMessage = (TextView) dialog.findViewById(android.R.id.message);
                textViewMessage.setTextSize(17);
                textViewMessage.setTypeface(null, Typeface.BOLD);
                textViewMessage.setTextColor(Color.rgb(0,0,255));

                Window window = dialog.getWindow();
                window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                window.setGravity(Gravity.CENTER);

                if(MainActivity.changeMode)
                    window.setBackgroundDrawableResource(R.color.greyyy);
            }
        });
    }

}
