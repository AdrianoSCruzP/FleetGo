package com.nttdata.ms_reservas.servicio;

import com.nttdata.ms_reservas.model.Reserva;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/** Almacén en memoria de las reservas. */
@Repository
public class ReservaRepository {

    private final List<Reserva> reservas = new ArrayList<>();
    private final AtomicLong secuencia = new AtomicLong();

    public Reserva guardar(Reserva reserva) {
        Reserva reservaConId = new Reserva(
                secuencia.incrementAndGet(),
                reserva.vehiculoId(),
                reserva.placa(),
                reserva.cliente(),
                reserva.dias(),
                reserva.total(),
                reserva.estado()
        );

        reservas.add(reservaConId);
        return reservaConId;
    }

    public boolean tieneReservaActiva(Long vehiculoId) {
        return reservas.stream()
                .anyMatch(reserva -> reserva.vehiculoId().equals(vehiculoId));
    }

    public List<Reserva> listar() {
        return  reservas.stream().toList();
    }
}
