package br.com.alura.forum.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import br.com.alura.forum.controller.dto.DetalhesDoTopicoDto;
import br.com.alura.forum.controller.dto.TopicoDto;
import br.com.alura.forum.controller.form.AtualizacaoTopicoForm;
import br.com.alura.forum.controller.form.TopicoForm;
import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.CursoRepository;
import br.com.alura.forum.repository.TopicoRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
public class TopicosController {

	@Autowired
	private TopicoRepository topicoRepository;
	
	@Autowired
	private CursoRepository cursoRepository;

	@GetMapping
	public List<TopicoDto> lista(String nomeCurso) {
		return TopicoDto.converter(
				Optional.ofNullable(nomeCurso)
				.map(topicoRepository::findByCursoNome)
				.orElseGet(topicoRepository::findAll));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<DetalhesDoTopicoDto> detalhar(@PathVariable Long id) {
		return topicoRepository
				.findById(id)
				.map(topico -> ResponseEntity.ok(new DetalhesDoTopicoDto(topico)))
				.orElse(ResponseEntity.notFound().build());		
	}
	
	@PostMapping
	public ResponseEntity<TopicoDto> cadastrar(@RequestBody @Valid TopicoForm form, UriComponentsBuilder uriBuilder) {
		Topico topico = form.converter(cursoRepository);	
		topicoRepository.save(topico);		
		URI uri = uriBuilder
				.path("/topicos/{id}")
				.buildAndExpand(topico.getId())
				.toUri(); 
		return ResponseEntity.created(uri).body(new TopicoDto(topico));
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TopicoDto> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoForm form) {
		return topicoRepository
				.findById(id)
				.map(topico -> form.atualizar(id, topicoRepository))
				.map(topicoRepository::save)
				.map(topico -> ResponseEntity.ok(new TopicoDto(topico)))
				.orElse(ResponseEntity.notFound().build());
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> remover(@PathVariable Long id) {
		return topicoRepository
				.findById(id)
				.map(topico -> {
					topicoRepository.deleteById(id);
					return ResponseEntity.ok().build();
				})
				.orElse(ResponseEntity.notFound().build());	
	}
}
