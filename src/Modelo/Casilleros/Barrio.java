package Modelo.Casilleros;

import Modelo.Jugador;
import Modelo.Tablero;
import Modelo.Edificacion;

import java.util.List;

public class Barrio implements Casillero, Propiedades {

    public Jugador propietario;
    public String nombre;
    public int costo;
    public List<Edificacion> listaCasas;
    public int alquilerActual;
    public int alquilerOriginal;
    public int casasTotales;
    public int valor_mercado;



    public Barrio(String unNombre, int valor_propiedad, int elAlquilerOriginal, List edificacionesCasas){
        nombre = unNombre;
        propietario  = null;
        costo = valor_propiedad;
        listaCasas = edificacionesCasas;
        alquilerActual = elAlquilerOriginal;
        alquilerOriginal = elAlquilerOriginal;
        casasTotales = 0;
        valor_mercado = costo;
    }

    public boolean permiteSalida(Jugador jugador){
        return true;
    }

    public boolean vender(Jugador jugador){
        if(!jugador.solicitar_dinero(valor_mercado)) return false;
        jugador.agregar_propiedad(this);
        cambiarPropietario(jugador);
        valor_mercado = costo * 85 /100;
        return true;
        }

    public void cambiarPropietario(Jugador jugador){
        propietario = jugador;
        destruirCasasConstruidas();
        alquilerActual = alquilerOriginal;
    }

    public void accionAlCaer(Jugador jugador, int numDado, Tablero tablero) {
        if (propietario == null){
            this.vender(jugador);
        }
        else if (propietario != jugador){
            jugador.solicitar_dinero(alquilerActual);
            propietario.cobrar_ingreso(alquilerActual);
        }
    }

    public Jugador duenio (){
        return propietario;
    }

    public int valorMercado(){
        return valor_mercado;
    }

    public boolean todasLasCasasFueronConstruidas(){
        for (int i = 0; i < listaCasas.size(); i++){
            if (!listaCasas.get(i).estaEdificado()){
                return false;
            }
        }
        return true;
    }

    public void destruirCasasConstruidas(){
        for (int i = 0; i < listaCasas.size(); i++){
            listaCasas.get(i).cambiarEstadoDeEdificacionAFalse();
        }
    }

    public boolean puedeEdificarCasa(Jugador jugador){
        return (jugador.capital() >= listaCasas.get(0).getPrecio() && !todasLasCasasFueronConstruidas());
    }

    public boolean edificarCasa(Jugador jugador) {
        if (puedeEdificarCasa(jugador)){
            jugador.solicitar_dinero(listaCasas.get(casasTotales).getPrecio());
            alquilerActual = listaCasas.get(casasTotales).getAlquiler();
            listaCasas.get(casasTotales).cambiarEstadoDeEdificacionATrue();
            casasTotales += 1;
            return true;
        }
        return false;
    }

    public int resetear(){
        propietario = null;
        return costo*(85/100);
    }

    public String nombre() {
        return nombre;
    }

}
