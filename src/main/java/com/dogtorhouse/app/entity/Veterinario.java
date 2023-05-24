package com.dogtorhouse.app.entity;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;


@Entity
@Table(name = "veterinarios")
public class Veterinario {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
	
	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 30)
    private String nombres;
	@NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 30)
    private String apellidos;
	private String identificacion;
	@DateTimeFormat(pattern = "yyyy-MM-dd") // Especifica el patrón de formato de fecha deseado
	private LocalDate fechaNacimiento;
	private String direccion;
	@Email
    @NotEmpty(message = "no puede estar vacio")
	@Size(min = 1, max = 30)
    @Column(unique = true)
    private String email;
	
	@NotEmpty(message = "no puede estar vacio")
	@Column(nullable = false)
	@Size(min = 8,message = "debe tener entre 8 y 32 caracteres")
    private String password;
	@Size(min = 1, max=10,message = "debe tener entre 8 y 32 caracteres")
    private String telefono;
	
	@Column(nullable = false)
    private Boolean activo;
	@Lob
	@Column(length = 4194304)
	private byte[] foto;
    private Date fechaBaja;
    
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "veterinarios_roles", joinColumns = @JoinColumn(name = "veterinario_id"), inverseJoinColumns = @JoinColumn(name = "rol_id"))
    private Set<Rol> roles = new HashSet<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombres() {
		return nombres;
	}

	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	public String getApellidos() {
		return apellidos;
	}

	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}


	public void setRoles(Set<Rol> roles) {
		this.roles = roles;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getPassword() {
		return password;
	}

	public Boolean getActivo() {
		return activo;
	}

	//@Transactional
	public Set<Rol> getRoles() {
		return roles;
	}

	public void setActivo(Boolean activo) {
		this.activo = activo;
	}
    
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return getNombres() + " " + getApellidos();
	}

	public Date getFechaBaja() {
		return fechaBaja;
	}

	public void setFechaBaja(Date fechaBaja) {
		this.fechaBaja = fechaBaja;
	}

	public byte[] getFoto() {
		return foto;
	}

	public void setFoto(byte[] foto) {
		this.foto = foto;
	}

	public String getIdentificacion() {
		return identificacion;
	}

	public void setIdentificacion(String identificacion) {
		this.identificacion = identificacion;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}
	
	
    
}
