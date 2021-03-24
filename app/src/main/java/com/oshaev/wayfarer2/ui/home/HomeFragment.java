package com.oshaev.wayfarer2.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.oshaev.wayfarer2.Article;
import com.oshaev.wayfarer2.Country;
import com.oshaev.wayfarer2.LoginActivity;
import com.oshaev.wayfarer2.R;
import com.oshaev.wayfarer2.ShowArticleActivity;
import com.oshaev.wayfarer2.User;
import com.oshaev.wayfarer2.UserProfileActivity;
import com.oshaev.wayfarer2.ui.SquareImageView;

import java.util.ArrayList;

public class HomeFragment extends Fragment {


    User userHome;

    private HomeViewModel homeViewModel;
    private RecyclerView countriesRecyclerView;
    private RecyclerView allCountriesRecyclerView;
    private RecyclerView articlesRecyclerView;
    private ShapeableImageView userAvatarImage;
    private LinearLayoutManager countriesManager;
    private LinearLayoutManager allCountriesManager;
    private LinearLayoutManager articlesManager;
    private CountriesAdapter countriesAdapter;
    private CountriesAdapter allCountriesAdapter;
    private ArticlesAdapter articlesAdapter;

    private SearchView searchView;
    private SearchView allCountriesSearchView;

    private TextView userNameTextView;
    private SquareImageView interestingImageView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             final ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        countriesRecyclerView = root.findViewById(R.id.countriesRecyclerView);
        allCountriesRecyclerView = root.findViewById(R.id.allCountriesRecyclerView);
        articlesRecyclerView = root.findViewById(R.id.articlesRecyclerView);
        userAvatarImage = root.findViewById(R.id.userAvatarImage);
        searchView = root.findViewById(R.id.searchView);
        allCountriesSearchView= root.findViewById(R.id.allCountriesSearchView);
        userNameTextView = root.findViewById(R.id.userNameTextView);
        interestingImageView = root.findViewById(R.id.interestingImageView);

        interestingImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShowArticleActivity.class);
                intent.putExtra("title", "30 советов для туриста");
                intent.putExtra("paper", R.string.interesting_paper );
                intent.putExtra("imgUrl", "https://firebasestorage.googleapis.com/v0/b/wayfarer-social-network.appspot.com/o/adventure_img.jpg?alt=media&token=ead9540e-6e5a-4558-8a56-75ad8cf33b0b");
                startActivity(intent);
            }
        });



        homeViewModel.getUser(FirebaseAuth.getInstance().getUid(), true).observe(getViewLifecycleOwner(),
                new Observer<User>() {
                    @Override
                    public void onChanged(User user) {
                        userNameTextView.setText("Привет, "
                                +" "+user.getName()+"!");
                        userHome = user;
                        //Toast.makeText(getContext(), userHome.getUserCountries().get(0).getName() , Toast.LENGTH_SHORT).show();

                    }
                });

        countriesManager = new LinearLayoutManager(getContext());
        countriesManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        countriesAdapter = new CountriesAdapter(homeViewModel.countries);
        countriesRecyclerView.setLayoutManager(countriesManager);
        countriesRecyclerView.setAdapter(countriesAdapter);
        countriesAdapter.notifyDataSetChanged();

        allCountriesManager = new LinearLayoutManager(getContext());
        allCountriesManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        allCountriesAdapter = new CountriesAdapter(homeViewModel.allCountries);
        allCountriesRecyclerView.setLayoutManager(allCountriesManager);
        allCountriesRecyclerView.setAdapter(allCountriesAdapter);
        allCountriesAdapter.notifyDataSetChanged();

        articlesManager = new LinearLayoutManager(getContext());
        articlesManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        articlesAdapter = new ArticlesAdapter(homeViewModel.articles);
        articlesRecyclerView.setLayoutManager(articlesManager);
        articlesRecyclerView.setAdapter(articlesAdapter);
        articlesAdapter.notifyDataSetChanged();

        userAvatarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(FirebaseAuth.getInstance().getCurrentUser() != null)
                {
                    Intent intent = new Intent(getActivity(), UserProfileActivity.class);
                    startActivity(intent);
                } else
                {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
            }
        });

        homeViewModel.getCountriesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Country>>() {
            @Override
            public void onChanged(ArrayList<Country> countries) {
                countriesAdapter.countries = countries;
                // ↓↓↓↓↓ часть поиска ↓↓↓↓↓// ↓↓↓↓↓ часть поиска ↓↓↓↓↓
                countriesAdapter.setCountriesCopy(homeViewModel.countries);
                allCountriesAdapter.setCountriesCopy(homeViewModel.allCountries);
                countriesAdapter.notifyDataSetChanged();
                allCountriesAdapter.notifyDataSetChanged();

            }
        });

        homeViewModel.getArticlesMutableLiveData().observe(getViewLifecycleOwner(), new Observer<ArrayList<Article>>() {
            @Override
            public void onChanged(ArrayList<Article> articles) {
                articlesAdapter.articles = articles;
                articlesAdapter.notifyDataSetChanged();
            }
        });



        articlesAdapter.onArticleClickListener(new ArticlesAdapter.OnArticleClickListener() {
            @Override
            public void onArticleClick(int position) {
                Intent intent = new Intent(getActivity(), ShowArticleActivity.class);
                intent.putExtra("imgUrl", homeViewModel.articles.get(position).getImgUrl());
                intent.putExtra("paper", homeViewModel.articles.get(position).getPaper());
                intent.putExtra("title", homeViewModel.articles.get(position).getTitle());
                startActivity(intent);
            }
        });





        // ↓↓↓↓↓ часть поиска ↓↓↓↓↓// ↓↓↓↓↓ часть поиска ↓↓↓↓↓// ↓↓↓↓↓ часть поиска ↓↓↓↓↓
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Country> newList = new ArrayList<>();
                for (Country channel: homeViewModel.countries){
                    String channelName = channel.getName().toLowerCase();
                    if (channelName.contains(newText)){
                        newList.add(channel);
                    }
                }
                countriesAdapter.setCountriesCopy(newList);
                return true;
            }
        });


        allCountriesSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener()
        {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                newText = newText.toLowerCase();
                ArrayList<Country> newList = new ArrayList<>();
                for (Country channel: homeViewModel.allCountries){
                    String channelName = channel.getName().toLowerCase();
                    if (channelName.contains(newText)){
                        newList.add(channel);
                    }
                }
                allCountriesAdapter.setCountriesCopy(newList);
                return true;
            }
        });

        homeViewModel.getUserInModel(FirebaseAuth.getInstance().getUid());

        allCountriesAdapter.setOnCountryClickListener(new CountriesAdapter.OnCountryClickListener()
        {
            @Override
            public void OnCountryClick(int position) {
               // DatabaseReference userCountryRef = FirebaseDatabase.getInstance()
                     //   .getReference("users").child(userHome)
                User currentUser = new User();
                if(!userHome.getUserCountries().contains(homeViewModel.allCountries.get(position).getName())) {
                    userHome.getUserCountries().add(homeViewModel.allCountries.get(position));
                }
                /*
                boolean flag = true;
                for(int i = 0; i<homeViewModel.allCountries.size(); i++)
                {
                    for(int j = 0; j<userHome.getUserCountries().size(); j++)
                    {
                        if(userHome.getUserCountries().get(j).getName()
                                .equals(homeViewModel.allCountries.get(i).getName()))
                        {
                            flag = false;
                        }
                    }
                }

                if(flag)
                {
                    userHome.getUserCountries().add(homeViewModel.allCountries.get(position));

                }

                 */

                currentUser = homeViewModel.getUser(FirebaseAuth.getInstance().getUid());
                //homeViewModel.currentUser.getUserCountries().add(homeViewModel.allCountries.get(position));
                homeViewModel.updateUserCountries(currentUser.getUserKey(), userHome.getUserCountries());
            }
        });


        return root;
    }
}
