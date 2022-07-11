package com.navtech.conomy.fragments;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Bundle;

import androidx.annotation.NonNull;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.navtech.conomy.R;
import com.navtech.conomy.classes.Transaction;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class Fragment_servicios extends Fragment {

    private FirebaseRecyclerAdapter<Transaction, Fragment_servicios.TransactionViewHolder> transactions_service_adapter;
    ItemTouchHelper.SimpleCallback callback;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragment_view = inflater.inflate(R.layout.fragment_servicios, container, false);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        RecyclerView rv_transaction_servicio = fragment_view.findViewById(R.id.rv_transaction_servicio);

        DatabaseReference finanzas = database.getReference().child("finanzas");
        Query finanzasQuery = finanzas.orderByChild("type").equalTo(1);

        FirebaseRecyclerOptions OptF = new FirebaseRecyclerOptions.Builder<Transaction>().setQuery(finanzasQuery,Transaction.class).build();

        transactions_service_adapter = new FirebaseRecyclerAdapter<Transaction, TransactionViewHolder>(OptF) {
            @Override
            protected void onBindViewHolder(@NonNull TransactionViewHolder transactionViewHolder, int i, @NonNull Transaction transaction) {
                transactionViewHolder.setNombre(fragment_view.getContext(),transaction.getName());
                transactionViewHolder.setAmount(fragment_view.getContext(),transaction.getAmount());
                transactionViewHolder.setIcon(fragment_view.getContext(),transaction.getType());
                transactionViewHolder.setKey(fragment_view.getContext(),transaction.getKey());
            }

            @NonNull
            @Override
            public Fragment_servicios.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(fragment_view.getContext())
                        .inflate(R.layout.transaction_layout,parent,false);
                return new Fragment_servicios.TransactionViewHolder(view);
            }
        };



        LinearLayoutManager layoutManagerVertical
                = new LinearLayoutManager(fragment_view.getContext(), LinearLayoutManager.VERTICAL, false);

        callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                TextView key_tv = viewHolder.itemView.findViewById(R.id.transaction_key_item);
                switch (direction) {
                    case ItemTouchHelper.LEFT:
                        finanzas.child(key_tv.getText().toString()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment_view.getContext(), "Se elimino correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;

                    case ItemTouchHelper.RIGHT:
                        finanzas.child(key_tv.getText().toString()).child("done").setValue(true).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(fragment_view.getContext(), "Se completo correctamente", Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                }
            }

            @Override
            public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
                new RecyclerViewSwipeDecorator.Builder(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
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
        itemTouchHelper.attachToRecyclerView(rv_transaction_servicio);
        rv_transaction_servicio.setLayoutManager(layoutManagerVertical);
        rv_transaction_servicio.setAdapter(transactions_service_adapter);
        transactions_service_adapter.startListening();

        return fragment_view;
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
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