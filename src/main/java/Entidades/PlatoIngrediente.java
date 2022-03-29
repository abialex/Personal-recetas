/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Entidades;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 *
 * @author USER
 */
@Entity
public class PlatoIngrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(insertable = true, updatable = true, name = "idplato", nullable = false)
    @ManyToOne
    private Plato plato;
    
    @JoinColumn(insertable = true, updatable = true, name = "idingrediente", nullable = false)
    @ManyToOne
    private Ingrediente ingrediente;
    
    @Column(name = "cantidad", nullable = false)
    private float cantidad;
    
    @Column(name = "und", nullable = true)
    private String und;

    public PlatoIngrediente() {
    }

    public PlatoIngrediente(Plato plato, Ingrediente ingrediente, float cantidad, String und) {
        this.plato = plato;
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
        this.und = und;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Plato getPlato() {
        return plato;
    }

    public void setPlato(Plato plato) {
        this.plato = plato;
    }

    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public float getCantidad() {
        return cantidad;
    }

    public void setCantidad(float cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnd() {
        return und;
    }

    public void setUnd(String und) {
        this.und = und;
    }
    
    

}
