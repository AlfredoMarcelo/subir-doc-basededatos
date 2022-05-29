package cl.aiep.java.archivos.controlador;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cl.aiep.java.archivos.modelo.Archivo;
import cl.aiep.java.archivos.repositorio.ArchivoRepository;

@Controller
public class AppController {

	@Autowired
	ArchivoRepository repository;
	
	
	@GetMapping("/")
	public String formulario() {
		return "formulario";
	}
	
	
	//cargar imagen a la base de datos
	@PostMapping("/")
	public String procesarFormulario(@RequestParam("archivo")MultipartFile archivo) {
		try {
			
			String nombreArchivo = archivo.getOriginalFilename();
			String tipoArchivo = archivo.getContentType();
			byte[] contenidoArchivo = archivo.getBytes();
			Archivo archivoBd = Archivo.builder()
				.datos(contenidoArchivo)
				.filename(nombreArchivo)
				.tipo(tipoArchivo)
				.build()
				;
			repository.saveAndFlush( archivoBd);
			return "redirect:/listar";
		} catch(Exception e) {
			return "formulario";
		}
	}
	
	//http://localhost:8081/archivo/a/1 para mostrar (descargar)
	//http://localhost:8081/archivo/i/1 => para mostrar (inline)
	@GetMapping("/archivo/{disposicion}/{id}")
	public ResponseEntity<byte[]> mostrarDescargarArchivo(
			@PathVariable("disposicion") String disposicion,
			@PathVariable("id") Archivo archivo
			){
		String disposition = null;
		if("a".equalsIgnoreCase(disposicion)) {
			disposition = "attachment";
		}else {
			disposition = "inline";

		}
		//Con esto se genera un respuesta http(cabecera)
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, disposition)
				.contentType( MediaType.valueOf(archivo.getTipo()))
				.body(archivo.getDatos())
				;
	}
	
	@GetMapping("/listar")
	public String listarImagen(Model modelo) {
		List <Archivo> archivos = repository.findAll();
		modelo.addAttribute("archivos", archivos);
		return "listar";
	}
	
}
