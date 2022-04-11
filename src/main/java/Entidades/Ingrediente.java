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
public class Ingrediente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JoinColumn(insertable = true, updatable = true, name = "idtipoingrediente", nullable = true)
    @ManyToOne
    private TipoIngrediente tipoingrediente;

    @Column(name = "nombre", nullable = false)
    private String nombre;

    @Column(name = "marca", nullable = false)
    private String marca;

    @Column(name = "precioactual", nullable = true)
    private float precioactual;

    @Column(name = "escala", nullable = true)
    private String escala;

    public Ingrediente() {
    }

    public Ingrediente(String nombre, String marca) {
        this.nombre = nombre;
        this.marca = marca;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public TipoIngrediente getTipoingrediente() {
        return tipoingrediente;
    }

    public void setTipoingrediente(TipoIngrediente tipoingrediente) {
        this.tipoingrediente = tipoingrediente;
    }

    public float getPrecioactual() {
        return precioactual;
    }

    public void setPrecioactual(float precioactual) {
        this.precioactual = precioactual;
    }

    public String getEscala() {
        return escala;
    }

    public void setEscala(String escala) {
        this.escala = escala;
    }
    
    public Ingrediente getIngrediente() {
        return this;
    }

}
