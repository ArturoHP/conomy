package com.navtech.conomy.fragments;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.navtech.conomy.MainActivity;
import com.navtech.conomy.R;
import com.navtech.conomy.classes.Transaction;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Fragment_all_categories extends Fragment{

    private FirebaseRecyclerAdapter<Transaction, Fragment_all_categories.TransactionViewHolder> transactionAdapter;
    TextView total_amount;
    ItemTouchHelper.SimpleCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view = inflater.inflate(R.layout.fragment_all_categories, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RecyclerView rv_transacciones = fragment_view.findViewById(R.id.rv_transacciones);

        DatabaseReference type_categories = database.getReference().child("type_services");//Esto se modificara para despues redirigirl
        DatabaseReference finanzas = database.getReference().child("finanzas");

        //LinearLayout categories_ll = findViewById(R.id.categories_ll);

        total_amount = fragment_view.findViewById(R.id.total_amount);

        //transacciones aun no pagadas
        Query finanzasQuery = finanzas.orderByChild("done").equalTo(false);

        FirebaseRecyclerOptions OptF = new FirebaseRecyclerOptions.Builder<Transaction>().setQuery(finanzasQuery,Transaction.class).build();

        transactionAdapter = new FirebaseRecyclerAdapter<Transaction, Fragment_all_categories.TransactionViewHolder>(OptF) {

            @Override
            protected void onBindViewHolder(@NonNull Fragment_all_categories.TransactionViewHolder transactionViewHolder, int i, @NonNull Transaction transaction) {

                transactionViewHolder.setNombre(fragment_view.getContext(),transaction.getName());
                transactionViewHolder.setAmount(fragment_view.getContext(),transaction.getAmount());
                transactionViewHolder.setIcon(fragment_view.getContext(),transaction.getType());
                transactionViewHolder.setKey(fragment_view.getContext(),transaction.getKey());

            }

            @NonNull
            @Override
            public Fragment_all_categories.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(fragment_view.getContext())
                        .inflate(R.layout.transaction_layout,parent,false);
                return new Fragment_all_categories.TransactionViewHolder(view);
            }
        };

        LinearLayoutManager layoutManagerVertical
                = new LinearLayoutManager(fragment_view.getContext(), LinearLayoutManager.VERTICAL, false);


        callback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                TextView key_tv = viewHolder.itemView.findViewById(R.id.transaction_key_item);
                switch (direction){
                    case ItemTouchHelper.LEFT:
                        finanzas.child(key_tv.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment_view.getContext(),"Se elimino correctamente",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case ItemTouchHelper.RIGHT:
                        finanzas.child(key_tv.getText().toString()).child("done").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment_view.getContext(),"Se completo correctamente",Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX,dY, actionState, isCurrentlyActive)
                        .addSwipeLeftLabel("Borrar")
                        .addSwipeLeftActionIcon(R.drawable.ic_trash_black)
                        .setSwipeLeftLabelColor(R.color.DarkOliveGreen)
                        .addSwipeRightLabel("Pagar")
                        .setSwipeRightLabelColor(R.color.IndianRed)
                        .addSwipeRightActionIcon(R.drawable.ic_done_black)
                        .create()
                        .decorate();

                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            }
        };

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(rv_transacciones);
        rv_transacciones.setLayoutManager(layoutManagerVertical);
        rv_transacciones.setAdapter(transactionAdapter);
        transactionAdapter.startListening();


        finanzas.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sum_TotalAmounts();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                sum_TotalAmounts();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                if(rv_transacciones.getChildCount()-1 == 0){
                    total_amount.setText(String.valueOf(0));
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        type_categories.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                View customCategoryLayout = inflater.inflate(R.layout.category_layout, null, false);
                TextView name_category = (TextView) customCategoryLayout.findViewById(R.id.name_category);
                name_category.setText(snapshot.getValue().toString());

                //categories_ll.addView(customCategoryLayout);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return fragment_view;
    }

    private void sum_TotalAmounts() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference().child("finanzas");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                double totalsum = 0;
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (dataSnapshot.exists() && dataSnapshot.getValue() != null){
                        if(!Boolean.parseBoolean(String.valueOf(ds.child("done").getValue()))){
                            Double count = Objects.requireNonNull(ds.child("amount").getValue(Double.class));
                            totalsum += count;
                            total_amount.setText(String.valueOf(totalsum));
                        }
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }


    public class TransactionViewHolder extends RecyclerView.ViewHolder{
        View mView;
        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;
        }
        public void setNombre(Context ctx, String nombre){
            TextView transaction_name_item = mView.findViewById(R.id.transaction_name_item);
            transaction_name_item.setText(nombre);
        }

        public void setAmount(Context ctx,double amount){
            TextView transaction_amount_item = mView.findViewById(R.id.transaction_amount_item);
            transaction_amount_item.setText("Monto: " + String.valueOf(amount));
        }

        public void setKey(Context ctx,String key){
            TextView transaction_key_item = mView.findViewById(R.id.transaction_key_item);
            transaction_key_item.setText(key);
        }


        public void setIcon(Context ctx,int type){
            ImageView transaction_icon_item = mView.findViewById(R.id.transaction_icon_item);
            switch (type){
                case 1:
                    transaction_icon_item.setImageResource(R.drawable.ic_servicios_black);
                    break;
                case 2:
                    transaction_icon_item.setImageResource(R.drawable.ic_alimentacion_black);
                    break;
                case 3:
                    transaction_icon_item.setImageResource(R.drawable.ic_pagos_black);
                    break;
                case 4:
                    transaction_icon_item.setImageResource(R.drawable.ic_transporte_black);
                    break;
                case 5:
                    transaction_icon_item.setImageResource(R.drawable.ic_salud_black);
                    break;
                case 6:
                    transaction_icon_item.setImageResource(R.drawable.ic_vestimenta_black);
                    break;
                case 7:
                    transaction_icon_item.setImageResource(R.drawable.ic_viajes_black);
                    break;
                case 8:
                    transaction_icon_item.setImageResource(R.drawable.ic_alquiler_black);
                    break;
                default:

                    break;
            }
        }
    }
}