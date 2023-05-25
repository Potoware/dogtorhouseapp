INSERT INTO dogtorhouse.roles (nombre) VALUES
	 ('VETERINARIO'),
	 ('CLIENTE');
	
INSERT INTO veterinarios (activo,apellidos,email,fecha_baja,nombres,password,telefono) VALUES
 (1,'Petro Urrego','veterinario@dogtorhouse.com',NULL,'Juan Roman','$2a$10$P9sHIL2sOQqYHObarlu0HemQ1CWTP9fpttUXh03W7atOzWtTc91yy',NULL);

INSERT INTO 
veterinarios_roles (veterinario_id,rol_id) VALUES
	 (1,1);
