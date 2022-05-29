package cl.aiep.java.archivos.modelo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Archivo {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String filename;
	private String tipo;
	@Lob //indica que vamos a guardar muchos datos, esto crea un valor especial en la base de datos
	private byte[] datos;
	
}
