package com.upc.cwa.carwash.Entities;

import java.util.Date;

public class Reserva {
    public long id;
    public long idvehiculo;
    public long idservicio;
    public String fecha;
    public String hora;
    public String estado;
    public String comentario;
    public Double calificacion;
    public Vehiculo vehiculo;
    public Servicio servicio;
}
