package com.dogtorhouse.app.util;

public enum TipoRol {
	ADMINISTRADOR("ADMINISTRADOR"), VETERINARIO("VETERINARIO"), CLIENTE("CLIENTE");

	private String roleName;

	TipoRol(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleName() {
		return roleName;
	}
}
