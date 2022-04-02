package com.example.android_final_blackjack.ui.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.android_final_blackjack.R;
import com.example.android_final_blackjack.databinding.FragmentUserBinding;

public class UserFragment extends Fragment {
    private FragmentUserBinding binding;

    //setting up variables to be used later
    EditText nameIn;
    Button saveBttn;
    SharedPreferences shrPref;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //collecting data from the UI
        nameIn = root.findViewById(R.id.User_NameIn);
        saveBttn = root.findViewById(R.id.bttn_NmEntr);

        //connecting to the shared preference
        shrPref = getActivity().getSharedPreferences("AppShrPrefs", Context.MODE_PRIVATE);

        //setting the text of the EditText name field
        nameIn.setText(shrPref.getString("name", ""));

        //adding a click listener to the save button
        saveBttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //collecting the string from the edittext
                String nameOut = nameIn.getText().toString();

                //testing of the name in is empty or not
                if(nameOut.isEmpty()) {
                    //telling the user that the name entered is empty
                    Toast.makeText(getActivity(), "No Name Entered", Toast.LENGTH_SHORT).show();

                    //setting the shared preference to black for the name field
                    SharedPreferences.Editor prefEdit = shrPref.edit();
                    prefEdit.putString("name", "");
                    prefEdit.commit();
                } else {
                    //telling the user that their name ahs been changed to what they input
                    Toast.makeText(getActivity(), "Name Changed To " + nameOut, Toast.LENGTH_SHORT).show();

                    //setting the shared preference to the inputted name
                    SharedPreferences.Editor prefEdit = shrPref.edit();
                    prefEdit.putString("name", nameOut);
                    prefEdit.commit();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}