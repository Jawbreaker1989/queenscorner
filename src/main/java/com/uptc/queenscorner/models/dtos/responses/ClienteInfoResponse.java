package com.uptc.queenscorner.models.dtos.responses;

/**
 * DTO que retorna información resumida de un cliente.
 * 
 * Se utiliza en respuestas embebidas (ej: cuando se devuelve una factura,
 * se incluye información del cliente sin necesidad de todos los datos completos).
 * 
 * Contiene solo los campos más relevantes del cliente:
 * ID, nombre, documento, email, teléfono y dirección.
 */
public class ClienteInfoResponse {
    
    /** ID único del cliente */
    private Long id;
    /** Nombre o razón social del cliente */
    private String nombre;
    /** Número de documento (cédula, RUT, etc.) */
    private String documento;
    /** Correo electrónico */
    private String email;
    /** Teléfono de contacto */
    private String telefono;
    /** Dirección física */
    private String direccion;
    
    public ClienteInfoResponse() {}
    
    /**
     * Constructor con parámetros para crear instancia completa
     */
    public ClienteInfoResponse(Long id, String nombre, String documento, String email, 
                               String telefono, String direccion) {
        this.id = id;
        this.nombre = nombre;
        this.documento = documento;
        this.email = email;
        this.telefono = telefono;
        this.direccion = direccion;
    }
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    
    public String getDocumento() { return documento; }
    public void setDocumento(String documento) { this.documento = documento; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
}  
