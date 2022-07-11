
package com.navtech.conomy;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joaquimley.faboptions.FabOptions;
import com.navtech.conomy.classes.PagerCategories;
import com.navtech.conomy.classes.Transaction;
import com.navtech.conomy.classes.Type_service;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FabOptions fabOptions = findViewById(R.id.fab_options);
        fabOptions.setButtonsMenu(R.menu.menu_categories);

        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name_transaction;
                int transaction_id = 0;
                Calendar cal = Calendar.getInstance();
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference finanzas = database.getReference().child("finanzas");

                switch (view.getId()) {
                    case R.id.fab_options_servicios:
                        transaction_id = 1;
                        name_transaction = "Servicios";
                        Toast.makeText(getApplicationContext(), "Servicios", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_alimentacion:
                        transaction_id = 2;
                        name_transaction = "Alimentos";
                        Toast.makeText(getApplicationContext(), "Alimentos", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_pagos:
                        transaction_id = 3;
                        name_transaction = "Pagos";
                        Toast.makeText(getApplicationContext(), "Pagos", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_transporte:
                        transaction_id = 4;
                        name_transaction = "Transporte";
                        Toast.makeText(getApplicationContext(), "Transporte", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_alquiler:
                        transaction_id = 5;
                        name_transaction = "Alquiler";
                        Toast.makeText(getApplicationContext(), "Alquiler", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_vestimenta:
                        transaction_id = 6;
                        name_transaction = "Vestimenta";
                        Toast.makeText(getApplicationContext(), "Vestimenta", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_viajes:
                        transaction_id = 7;
                        name_transaction = "Viajes";
                        Toast.makeText(getApplicationContext(), "Viajes", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.fab_options_salud:
                        transaction_id = 8;
                        name_transaction = "Salud";
                        Toast.makeText(getApplicationContext(), "Salud", Toast.LENGTH_SHORT).show();
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + view.getId());
                }


                Dialog dialogAddTransaction = new Dialog(MainActivity.this);
                dialogAddTransaction.setContentView(R.layout.dialog_add_new_transaction);
                dialogAddTransaction.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

                TextView name_transaction_dialog = dialogAddTransaction.findViewById(R.id.name_transaction_dialog);
                TextView name_dialog_transaction = dialogAddTransaction.findViewById(R.id.name_transaction);
                TextView amount_transaction = dialogAddTransaction.findViewById(R.id.amount_transaction);
                ImageView icon_transaction = dialogAddTransaction.findViewById(R.id.icon_transaction);
                CardView close_dialog_new_transaction = dialogAddTransaction.findViewById(R.id.close_dialog_new_transaction);
                CardView accept_dialog_new_transaction = dialogAddTransaction.findViewById(R.id.accept_dialog_new_transaction);

                name_transaction_dialog.setText(name_transaction);

                close_dialog_new_transaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogAddTransaction.cancel();
                    }
                });

                switch (transaction_id){
                    case 1:
                        icon_transaction.setImageResource(R.drawable.ic_servicios_black);
                        break;
                    case 2:
                        icon_transaction.setImageResource(R.drawable.ic_alimentacion_black);
                        break;
                    case 3:
                        icon_transaction.setImageResource(R.drawable.ic_pagos_black);
                        break;
                    case 4:
                        icon_transaction.setImageResource(R.drawable.ic_transporte_black);
                        break;
                    case 5:
                        icon_transaction.setImageResource(R.drawable.ic_salud_black);
                        break;
                    case 6:
                        icon_transaction.setImageResource(R.drawable.ic_vestimenta_black);
                        break;
                    case 7:
                        icon_transaction.setImageResource(R.drawable.ic_viajes_black);
                        break;
                    case 8:
                        icon_transaction.setImageResource(R.drawable.ic_alquiler_black);
                        break;
                    default:
                        break;
                }

                int finalTransaction_id = transaction_id;

                accept_dialog_new_transaction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(!amount_transaction.getText().toString().isEmpty() && !name_dialog_transaction.getText().toString().isEmpty()){
                            String random = getSaltString();
                            Map<Object,Object> map = new HashMap<>();

                            map.put("amount",Double.parseDouble(amount_transaction.getText().toString()));
                            map.put("date",String.valueOf(android.text.format.DateFormat.format("yyyy/MM/dd", new java.util.Date())));
                            map.put("type", finalTransaction_id);
                            map.put("month",cal.MONTH);
                            map.put("week_month_number",cal.get(Calendar.WEEK_OF_MONTH));
                            map.put("week_year_number",cal.get(Calendar.WEEK_OF_YEAR));
                            map.put("name",name_dialog_transaction.getText().toString());
                            map.put("done",false);
                            map.put("key",random);


                            finanzas.child(random).setValue(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(getApplicationContext(),"Se añadio la transacción correctamente",Toast.LENGTH_SHORT).show();
                                    dialogAddTransaction.dismiss();
                                }
                            });
                        }else{
                            Toast.makeText(getApplicationContext(),"Favor de llenar ambos campos",Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                dialogAddTransaction.show();
            }
        };

        fabOptions.setOnClickListener(clickListener);

        TabLayout tabLayout = findViewById(R.id.tab_layout_categories);
        ViewPager viewPager = findViewById(R.id.pager_categories);
        ImageButton menu_cv, settings_menu;
        menu_cv = findViewById(R.id.menu_imagebtn);
        settings_menu = findViewById(R.id.settings_imagebtn);

        tabLayout.addTab(tabLayout.newTab().setText("Todos"));

        tabLayout.addTab(tabLayout.newTab().setText("Servicios"));
        tabLayout.addTab(tabLayout.newTab().setText("Alimentación"));
        tabLayout.addTab(tabLayout.newTab().setText("Pagos"));
        tabLayout.addTab(tabLayout.newTab().setText("Transporte"));
        tabLayout.addTab(tabLayout.newTab().setText("Salud"));
        tabLayout.addTab(tabLayout.newTab().setText("Vestimenta"));
        tabLayout.addTab(tabLayout.newTab().setText("Viajes"));
        tabLayout.addTab(tabLayout.newTab().setText("Alquiler"));

        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tabLayout.setTabIndicatorAnimationMode(TabLayout.INDICATOR_ANIMATION_MODE_ELASTIC);

        PagerCategories adapter = new PagerCategories(this.getSupportFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(adapter);


        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (viewPager.getCurrentItem() == 0){
                    tabLayout.selectTab(tabLayout.getTabAt(0));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 1) {
                    tabLayout.selectTab(tabLayout.getTabAt(1));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 2) {
                    tabLayout.selectTab(tabLayout.getTabAt(2));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 3) {
                    tabLayout.selectTab(tabLayout.getTabAt(3));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 4){
                    tabLayout.selectTab(tabLayout.getTabAt(4));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 5) {
                    tabLayout.selectTab(tabLayout.getTabAt(5));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 6) {
                    tabLayout.selectTab(tabLayout.getTabAt(6));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 7) {
                    tabLayout.selectTab(tabLayout.getTabAt(7));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }
                if (viewPager.getCurrentItem() == 8) {
                    tabLayout.selectTab(tabLayout.getTabAt(8));
                    tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.almostWhite));
                }

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        menu_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        settings_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });




    }


    protected String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

}