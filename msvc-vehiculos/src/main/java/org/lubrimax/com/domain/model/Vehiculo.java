package org.lubrimax.com.domain.model;

import lombok.*;
import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Vehiculo {
    private Long vehiculoId;
    private String placa;
    private String marca;
    private String modelo;
    private Integer anio;
    private String tipoCombustible;
    private Integer kilometrajeActual;
    private String estado;
    private FichaTecnica fichaTecnica;

    public void actualizarKilometraje(Integer kilometraje) {
        if (kilometraje == null || kilometraje < 0) {
            throw new IllegalArgumentException("El kilometrajeActual debe ser cero o mayor.");
        }
        this.kilometrajeActual = kilometraje;
    }

    public void actualizarFichaTecnica(FichaTecnica ficha) {
        if (ficha == null) {
            throw new IllegalArgumentException("La fichaTecnica es obligatoria.");
        }
        texto(ficha.getMarca(), "fichaTecnica.marca", 100);
        texto(ficha.getModelo(), "fichaTecnica.modelo", 100);
        texto(ficha.getTipoCombustible(), "fichaTecnica.tipoCombustible", 50);
        texto(ficha.getMotor(), "fichaTecnica.motor", 100);
        texto(ficha.getCilindrada(), "fichaTecnica.cilindrada", 50);
        if (ficha.getAnio() == null || ficha.getAnio() < 1 || ficha.getAnio() > 9999) {
            throw new IllegalArgumentException("El año debe estar entre 1 y 9999.");
        }

        this.fichaTecnica = new FichaTecnica(ficha.getMarca(), ficha.getModelo(), ficha.getAnio(),
                ficha.getMotor(), ficha.getCilindrada(), ficha.getTipoCombustible());
        this.marca = ficha.getMarca();
        this.modelo = ficha.getModelo();
        this.anio = ficha.getAnio();
        this.tipoCombustible = ficha.getTipoCombustible();
    }

    public void validarDatos() {
        texto(this.getPlaca(), "placa", 20);
        this.setPlaca(this.getPlaca().trim().toUpperCase(Locale.ROOT));
        texto(this.getEstado(), "estado", 50);
        FichaTecnica ficha = this.getFichaTecnica();
        if (ficha != null && (!Objects.equals(this.getMarca(), ficha.getMarca())
                || !Objects.equals(this.getModelo(), ficha.getModelo())
                || !Objects.equals(this.getAnio(), ficha.getAnio())
                || !Objects.equals(this.getTipoCombustible(), ficha.getTipoCombustible()))) {
            throw new IllegalArgumentException("Marca, modelo, año y combustible deben coincidir con la ficha técnica.");
        }
        this.actualizarKilometraje(this.getKilometrajeActual());
        this.actualizarFichaTecnica(ficha);
    }

    private void texto(String valor, String campo, int maximo) {
        if (valor == null || valor.isBlank() || valor.length() > maximo) {
            throw new IllegalArgumentException("El campo " + campo + " es obligatorio y admite hasta " + maximo + " caracteres.");
        }
    }
}
