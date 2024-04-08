package br.com.alura.screenmatch;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	// Esse Run abaixo é o método que irá atuar como MÉTODO MAIN, pois é ele que é chamado no main da aplicação:
	@Override
	public void run(String... args) throws Exception {

		// aqui nós vamos instanciar a classe de consumoapi
		var consumoApi = new ConsumoApi();

		// criando uma variável para receber o método de obter os dados, criado na classe de ConsumoApi
		var json = consumoApi.obterDados("https://www.omdbapi.com/?t=gilmore+girls&apikey=5d03b634");
		System.out.println(json);

		// Agora vamos usar o nosso conversor que criamos "Converte dados"
		ConverteDados conversor = new ConverteDados();

		// Vamos criar uma variável e passar para o conversor o JSON que obtivemos e queremos converter:
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

		// Vamos exibir essa variável dados criada, e ver se está representado na forma do toString daquele record:
		System.out.println(dados);


	}
}
