package com.oshaev.wayfarer2.ui.home;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oshaev.wayfarer2.Country;
import com.oshaev.wayfarer2.R;

import java.util.ArrayList;

public class CountriesAdapter extends RecyclerView.Adapter<CountriesAdapter.CountryViewHolder> {

    public ArrayList<Country> countries;
    ArrayList<Country> countriesCopy;
    OnCountryClickListener listener;

    public CountriesAdapter(ArrayList<Country> countries) {
        this.countries = countries;
    }

    // ↓↓↓↓↓ часть поиска ↓↓↓↓↓ // ↓↓↓↓↓ часть поиска ↓↓↓↓↓
    public void setCountriesCopy(ArrayList<Country> copy) {
        countriesCopy = new ArrayList<>();
        countries = copy;
        notifyDataSetChanged();
    }

    public interface OnCountryClickListener{
        void OnCountryClick(int position);
    }

    public void setOnCountryClickListener(OnCountryClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view;
       view = LayoutInflater.from(parent.getContext())
               .inflate(R.layout.card_country, parent, false);
       CountryViewHolder holder = new CountryViewHolder(view);
       return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.countryName.setText(country.getName());
        //if(country.getImgUrl()!=null)
        Glide.with(holder.countryFlag.getContext()).
                load(country.getImgUrl()).into(holder.countryFlag);
    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    public class CountryViewHolder extends RecyclerView.ViewHolder{

        ImageView countryFlag;
        TextView countryName;

        public CountryViewHolder(@NonNull final View itemView) {
            super(itemView);
            countryFlag = itemView.findViewById(R.id.countryFlag);
            countryName = itemView.findViewById(R.id.countryName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(listener!=null)
                    {
                        if(getAdapterPosition()!=RecyclerView.NO_POSITION)
                        {
                            listener.OnCountryClick(getAdapterPosition());
                        }
                    }

                }
            });
        }
    }



}
