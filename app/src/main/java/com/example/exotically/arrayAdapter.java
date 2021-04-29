package com.example.exotically;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<cards> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<cards> items){
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        cards card_item = getItem(position);

        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);

        TextView name = (TextView) convertView.findViewById((R.id.name));
        TextView petName = (TextView) convertView.findViewById((R.id.petName));
        TextView bio = (TextView) convertView.findViewById((R.id.bio));
        TextView distance = (TextView) convertView.findViewById((R.id.distance));
        ImageView petImage = (ImageView) convertView.findViewById(R.id.petImage);
        ImageView ownerImage = (ImageView) convertView.findViewById(R.id.ownerImage);

        Chip mating = (Chip) convertView.findViewById(R.id.chipMating);
        Chip socializing = (Chip) convertView.findViewById(R.id.chipSocializing);
        Chip male = (Chip) convertView.findViewById(R.id.chipMale);
        Chip female = (Chip) convertView.findViewById(R.id.chipFemale);

        ownerImage.setImageResource(R.mipmap.ic_launcher);
        name.setText(card_item.getName());
        String distanceText = card_item.getDistance() + " mi away";
        distance.setText(distanceText);
        petImage.setImageResource(R.mipmap.ic_launcher);
        bio.setText(card_item.getBio());
        petName.setText(card_item.getPetName());

        if(!card_item.getMating()) {mating.setVisibility(Chip.GONE); }
        if(!card_item.getSocializing()) {socializing.setVisibility(Chip.GONE); }
        if(card_item.getGender()) {male.setVisibility(Chip.GONE); }
        if(!card_item.getGender()) {female.setVisibility(Chip.GONE); }
        /*
        switch(card_item.getProfileImageUrl()){
            case "default":
                Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
            default:
                //Glide.clear(image);
                Glide.with(getContext()).load(card_item.getProfileImageUrl()).into(image);
                break;
        }
        Glide.with(convertView.getContext()).load(card_item.getProfileImageUrl()).into(image); */

        return convertView;


    }
}
