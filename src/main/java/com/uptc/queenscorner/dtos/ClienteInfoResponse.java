package com.uptc.queenscorner.dtos;

public class ClienteInfoResponse {
    
    private Long id;
    private String nombre;
    private String documento;
    private String email;
    private String telefono;
    private String direccion;
    
    public ClienteInfoResponse() {}
    
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
