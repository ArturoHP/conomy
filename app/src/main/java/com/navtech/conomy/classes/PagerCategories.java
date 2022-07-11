package com.navtech.conomy.classes;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.navtech.conomy.fragments.Fragment_alimentacion;
import com.navtech.conomy.fragments.Fragment_all_categories;
import com.navtech.conomy.fragments.Fragment_alquiler;
import com.navtech.conomy.fragments.Fragment_pagos;
import com.navtech.conomy.fragments.Fragment_salud;
import com.navtech.conomy.fragments.Fragment_servicios;
import com.navtech.conomy.fragments.Fragment_transporte;
import com.navtech.conomy.fragments.Fragment_vestimenta;
import com.navtech.conomy.fragments.Fragment_viajes;

public class PagerCategories extends FragmentStatePagerAdapter {
    int tabCount;

    //Constructor de la clase
    public PagerCategories(FragmentManager fm, int tabCount) {
        super(fm);
        //se inicializa el tabcount
        this.tabCount= tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                Fragment_all_categories all_categories_tab = new Fragment_all_categories();
                return all_categories_tab;
            case 1:
                Fragment_servicios servicios_tab = new Fragment_servicios();
                return servicios_tab;
            case 2:
                Fragment_alimentacion alimentacion_tab = new Fragment_alimentacion();
                return alimentacion_tab;
            case 3:
                Fragment_pagos pagos_tab = new Fragment_pagos();
                return pagos_tab;
            case 4:
                Fragment_transporte transporte = new Fragment_transporte();
                return transporte;
            case 5:
                Fragment_salud salud_tab = new Fragment_salud();
                return salud_tab;
            case 6:
                Fragment_vestimenta vestimenta_tab = new Fragment_vestimenta();
                return vestimenta_tab;
            case 7:
                Fragment_viajes viajes_tab = new Fragment_viajes();
                return viajes_tab;
            case 8:
                Fragment_alquiler alquiler_tab = new Fragment_alquiler();
                return alquiler_tab;

                default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
