package com.pfa.backend.entity;

import jakarta.persistence.*;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
public class EntrepriseListener {

    @PreUpdate
    public void preUpdate(Entreprise entreprise) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(entreprise);
        for (String propertyName : entreprise.getOriginalValues().keySet()) {
            Object originalValue = entreprise.getOriginalValues().get(propertyName);
            Object newValue = beanWrapper.getPropertyValue(propertyName);

            if (originalValue != null && !originalValue.equals(newValue)) {
                HistoriqueDentreprise historique = new HistoriqueDentreprise();
                historique.setEntreprise(entreprise);
                historique.setAttributModifie(propertyName);
                historique.setAncienneValeur(originalValue.toString());
                historique.setNouvelleValeur(newValue != null ? newValue.toString() : null);
                historique.setDateModification(new Date());

                entreprise.getHistoriqueDentreprise().add(historique);
            }
        }
    }
}
