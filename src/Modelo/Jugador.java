package Modelo;
import Modelo.Casilleros.Propiedades;
import Modelo.Casilleros.Casillero;
import java.util.ArrayList;


public class Jugador {

    private int capital;
    private ArrayList<Propiedades> propiedades;
    private Casillero casilleroActual;
    private String nombre;
    private Estado estado;

    public Jugador(String nombreJugador) {
        this.capital = 100000;
        propiedades = new ArrayList<>();
        casilleroActual = null ;
        nombre = nombreJugador;
        estado = new Libre();
    }

    public void asignarCasillero(Casillero casillero){
        casilleroActual = casillero;
    }

    public int cantPropiedades(){
        return propiedades.size() ;
    }

    public void caeEn(Casillero casillero, int numDado, Tablero tablero){
        asignarCasillero(casillero);
        casilleroActual.accionAlCaer(this, numDado, tablero);
    }

    public Casillero actual(){ return casilleroActual; }

    public void cobrar(int monto){
        capital += monto;
    }

    public int capital(){
        return capital;
    }

    public boolean comprar(Propiedades propiedad, Jugador duenio){
        if (capital <= propiedad.valorMercado()) return false;
        propiedad.vender(this);
        return true;
    }

    public boolean venderAOtroJugador (Jugador otroJugador, Propiedades propiedad){
        if(! otroJugador.comprar(propiedad,this)) return false;
        propiedades.remove(propiedad);
        int costo = propiedad.valorMercado();
        this.cobrar(costo);
        return true;
    }

    public void venderAlBanco(Propiedades propiedad){
        propiedades.remove(propiedad);
        int costo = propiedad.valorMercado();
        this.cobrar(costo);
    }

    public Propiedades propiedadParaIntercambiar(){
        return  propiedades.remove(0);
    }

    public void intercambiarPropiedades(Jugador jugador){
        Propiedades propiedad1 = this.propiedadParaIntercambiar();
        Propiedades propiedad2 = jugador.propiedadParaIntercambiar();
        jugador.agregar_propiedad(propiedad1);
        this.agregar_propiedad(propiedad2);
        propiedad1.cambiarPropietario(jugador);
        propiedad2.cambiarPropietario(this);
    }

    public boolean solicitarDinero(double dineroSolicitado)    {
        if ( capital > dineroSolicitado){
            capital -= dineroSolicitado;
            return true;
        }
        boolean resultado = this.venderPropiedades((int)(dineroSolicitado), null);
        return resultado;
    }

    public boolean venderPropiedades(int dineroSolicitado, Jugador jugador) {
        for (int i = 0; i < propiedades.size(); i++) {
            int monto = propiedades.get(i).resetear();
            this.cobrar(monto);
            if (this.solicitarDinero(dineroSolicitado)) return true;
            }
        return false;
    }

    public void agregar_propiedad(Propiedades propiedad){
        propiedades.add(propiedad);
    }

    public String nombre(){
        return nombre;
    }

    public boolean posee(String propiedad){
        for ( int i=0 ; i < propiedades.size(); i++){
            if (propiedad == propiedades.get(i).nombre()){
                return true;
            }
        }
        return false;
    }

    public boolean mover(){
        return estado.mover(this);
    }

    public void actualizarEstado(Estado nuevo_estado){
        estado = nuevo_estado;
    }

    public boolean perdio() {return false;    }
}
