package br.com.alura.forum.controller.form;

import org.hibernate.validator.constraints.Length;

import br.com.alura.forum.modelo.Topico;
import br.com.alura.forum.repository.TopicoRepository;
import jakarta.validation.constraints.NotEmpty;

public class AtualizacaoTopicoForm {

	@NotEmpty
	@Length(min = 5)
	private String titulo;
	@NotEmpty
	@Length(min = 10)
	private String mensagem;

	public String getTitulo() {
		return titulo;
	}

	public String getMensagem() {
		return mensagem;
	}

	public Topico atualizar(Long id, TopicoRepository topicoRepository) {
		Topico topico = topicoRepository.getReferenceById(id);
		topico.setTitulo(this.titulo);
		topico.setMensagem(this.mensagem);
		return topico;
	}
}
