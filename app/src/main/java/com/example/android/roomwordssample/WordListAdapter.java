package com.example.android.roomwordssample;

/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;


public class WordListAdapter extends RecyclerView.Adapter<WordListAdapter.WordViewHolder> {

    public static final int REQUEST_CODE_CAMERA = 101;
    public static final int CAMERA_RESQUEST_CODE = 102;
    private static final int TAKE_PICTURE = 1111;
    private final WordListAdapter adapter;
    private WordViewModel mWordViewModel;
    private ViewGroup _parent;
    ImageView selectedImgView;



    class WordViewHolder extends RecyclerView.ViewHolder {
        private final TextView wordItemView;

        private WordViewHolder(View itemView) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.textView);
            mWordViewModel = ViewModelProviders.of((FragmentActivity) mInflater.getContext()).get(WordViewModel.class);
        }
    }

    private final LayoutInflater mInflater;
    private List<Word> mWords = Collections.emptyList(); // Cached copy of words

    WordListAdapter(Context context, WordViewModel wordViewModel) {
        mWordViewModel = wordViewModel;
        mInflater = LayoutInflater.from(context);
        adapter = this;
    }

    @NonNull
    @Override
    public WordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        _parent = parent;
        return new WordViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, @SuppressLint("RecyclerView") int position) {
        Word current = mWords.get(position);
        holder.wordItemView.setText(current.getWord());
        /*
        The on click event is defined when a word from the list is touched one click in the work list
         */
        holder.wordItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View _inflater = mInflater.inflate(R.layout.edit_word, _parent, false);


                final EditText editText;
                final Button camaraButton;
                final Button galeryButton;

                editText = (EditText) _inflater.findViewById(R.id.word_edit_value);
                camaraButton = (Button) _inflater.findViewById(R.id.camaraButton);
                galeryButton = (Button) _inflater.findViewById(R.id.galeryButton);
                editText.setText(current.getWord());

                // Use of an alert type window to ask the user and confirm the action he wants to perform
                AlertDialog.Builder message = new AlertDialog.Builder(mInflater.getContext());
                message.setTitle("Do you need edit the word ?");
                message.setMessage("Original word: "+current.getWord());
                message.setView(_inflater);
                //message.setView(_inflaterImg);
                message.setCancelable(false);

                camaraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"Hola boton camara", Toast.LENGTH_SHORT).show();
                        makeCamaraPermisions(v);
                    }

                    private void makeCamaraPermisions(View v ) {
                        if (ContextCompat.checkSelfPermission(v.getContext(), Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED){
                            ActivityCompat.requestPermissions((Activity) v.getContext(), new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
                        }else{
                            openDeviceCamera(v);
                        }
                    }
                });

                galeryButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(v.getContext(),"Hola boton galery", Toast.LENGTH_SHORT).show();
                    }
                });

                message.setPositiveButton("Save edit", new DialogInterface.OnClickListener() {
                    // If you click the save edit button, the word is edit from in the list.
                    public void onClick(DialogInterface message, int idWork) {
                        current.setmWord(editText.getText().toString());
                        mWordViewModel.update(current);
                        adapter.notifyItemChanged(position);
                    }
                });
                // If you click cancel, the delete action is aborted.
                message.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface message, int idWork) {
                        message.dismiss();
                    }
                });
                message.show();
            }

            private void openDeviceCamera(View v) {
                Toast.makeText(v.getContext(), R.string.Camera_permission_granted, Toast.LENGTH_LONG).show();
                Intent cameraItent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Activity activityCamera = (Activity) v.getContext();
                activityCamera.startActivityForResult(cameraItent, CAMERA_RESQUEST_CODE);


            }

        });

        /*
        The on Long click event is defined when a word from the list is touched for few seconds in the work list
         */
        holder.wordItemView.setOnLongClickListener((View.OnLongClickListener) view -> {

            // Use of an alert type window to ask the user and confirm the action he wants to perform
            AlertDialog.Builder message = new AlertDialog.Builder(mInflater.getContext());
            message.setTitle(" Delete the selected word ?");
            message.setMessage("Select word: "+current.getWord());
            message.setCancelable(false);
            message.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                // If you click the delete button, the word is removed from the list.
                public void onClick(DialogInterface message, int idWork) {
                    mWordViewModel.delete(current);
                }
            });
            // If you click cancel, the delete action is aborted.
            message.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface message, int idWork) {
                    message.dismiss();
                }
            });
            message.show();
            return false;
        });

    }


    @SuppressLint("NotifyDataSetChanged")
    void setWords(List<Word> words) {
        mWords = words;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mWords.size();
    }
}


