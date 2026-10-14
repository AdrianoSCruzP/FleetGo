package com.nttdata.ms_reservas.servicio;

import com.nttdata.ms_reservas.cliente.VehiculoClient;
import com.nttdata.ms_reservas.model.Reserva;
import com.nttdata.ms_reservas.model.ReservaRequest;
import com.nttdata.ms_reservas.model.Vehiculo;
import org.springframework.stereotype.Service;

import java.util.List;

/** Reglas de negocio de las reservas. */
@Service
public class ReservaService {

    private final static int DIAS_DESCUENTO = 7;
    private final static double PORCENTAJE_DESCUENTO = 0.10;

    private final VehiculoClient vehiculoClient;
    private final ReservaRepository repository;

    public ReservaService(VehiculoClient vehiculoClient, ReservaRepository repository) {
        this.vehiculoClient = vehiculoClient;
        this.repository = repository;
    }

    public Reserva crear(ReservaRequest req) {
        validar(req);

        Vehiculo vehiculo = vehiculoClient.buscar(req.vehiculoId());

        if (!vehiculo.disponible()){
            throw new VehiculoNoDisponibleException("VEHICULO NO DISPONIBLE");
        }

        if (repository.tieneReservaActiva(req.vehiculoId())){
            throw new VehiculoNoDisponibleException("YA RESERVADO");
        }

        double totalReserva = calcularTotal(vehiculo.tarifaDia(), req.dias());
        Reserva reserva = new Reserva(
                null,
                vehiculo.id(),
                vehiculo.placa(),
                req.cliente(),
                req.dias(),
                totalReserva,
                "CONFIRMADA"
        );

        return repository.guardar(reserva);
    }

    public List<Reserva> listar() {
        return repository.listar();
    }

    private double calcularTotal(double tarifaDia, int dias) {
        double total = tarifaDia * dias;

        if (dias >= DIAS_DESCUENTO){
            return total * (1 - PORCENTAJE_DESCUENTO);
        }
        return total;
    }

    private void validar(ReservaRequest req) {
        if (req.cliente() == null
                || req.cliente().isBlank()
                || req.dias() < 1
                || req.dias() > 30
        ){
            throw new IllegalArgumentException("DATOS INVALIDOS");
        }
    }
}
