package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Principal {

    // Criando um Scanner para fazer a leitura do dado que o Usuário irá inserir:
    private Scanner leitura = new Scanner(System.in);

    // Tornando o consumoapi um atributo dessa classe principal
    private ConsumoApi consumo = new ConsumoApi();

    // Como vamos usar o CONVERSOR várias vezes ele passa a ser um atributo da classe
    private ConverteDados conversor = new ConverteDados();

    // CRIANDO CONSTANTES DO ENDEREÇO QUE SERÁ IMUTÁVEL / usamos "final" e CAIXA ALTA na nomenclatura e Underline
    private final String ENDERECO = "https://www.omdbapi.com/?t=";

    private final String API_KEY = "&apikey=5d03b634";

    public void exibeMenu(){

        System.out.println("Digite o nome da série para busca: ");

        // O var irá receber a VARIÁVEL, e o método NEXTLINE() para pegarmos o nome da série
        var nomeSerie = leitura.nextLine();


        // Usando o método replace para substituir os espaços em brancos de um eventual nome de série
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

        // Temos agora um conversor sendo instanciado dentro do método
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);

        // Exibindo os dados:
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();

		//Criando um Iterador FOR para percorrer numero de temporadas na série:
		for (int i = 1; i <= dados.totalTemporadas(); i++) {

			// vamos iterar do início da temporada 1 e quebrar esse endereço de string e concatenar com o iterador do "i"
			json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);

			// Vamos criar uma INSTÂNCIA de DadosTemporada e usar o conversor genérico:
			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);

		}

		//Vamos imprimir esses iteráveis que a gente buscou - Com isso temos que usar o foreach:
		temporadas.forEach(System.out::println);
        
//        // Criando um Iterador For para percorrer os EPISÓDIOS dentro de CADA TEMPORADA - Coleções dentro de Coleções:
//        for (int i = 0; i < dados.totalTemporadas(); i++) {
//            List<DadosEpisodio> episodiosTemporada = temporadas.get(i).episodios();
//
//            // Criando outro iterador para a lista de episódios da temporada:
//            for (int j = 0; j < episodiosTemporada.size(); j++){
//                System.out.println(episodiosTemporada.get(j).titulo());
//            }
//        }

        //Usando o Recurso do ForEach - Lambda
        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));


        // Usando Streams + Método Collectors
        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream())
                .collect(Collectors.toList());


//        System.out.println("\nTop 10 episodios:");
//        dadosEpisodios.stream()
//                //aqui estamos ordenando - e como parâmetro estamos comparando a avaliação de todas entradas de DadosEpisodio
//                // O "reversed" significa que ele vai ordenar da forma contrária, do Melhor ao Pior:
//                // Filter - Criamos um filtro que o avaliação é diferente, ignorecase "N/A"
//                .filter(e -> !e.avaliacao().equalsIgnoreCase("N/A"))
//                // Criando um Peek para observar as operações encadeadas do JAVA
//                .peek(e -> System.out.println("Primeiro filtro (N/A) " + e))
//                .sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
//                .peek(e -> System.out.println("Ordenação " + e))
//                .limit(10)
//                .peek(e -> System.out.println("Limite " + e))
//                .map(e -> e.titulo().toUpperCase())
//                .peek(e -> System.out.println("Mapeamento " + e))
//                .forEach(System.out::println);

        // Criando uma nova lista, transformando dados da lista de temporadas, em uma lista de episódios
        List<Episodio> episodios = temporadas.stream()
                .flatMap(t -> t.episodios().stream()
                        .map(d -> new Episodio(t.numero(), d))
                ).collect(Collectors.toList());

        episodios.forEach(System.out::println);

//        System.out.println("Digite um trecho do título do episódio: ");
//        // Criamos uma Variável que o usuário irá digitar e vamos atribuir essa variável ao trecho titulo:
//        var trechoTitulo = leitura.nextLine();
//        Optional<Episodio> episodioBuscado = episodios.stream()
//                .filter(e -> e.getTitulo().toUpperCase().contains(trechoTitulo.toUpperCase()))
//                .findFirst();
//        if(episodioBuscado.isPresent()){
//            System.out.println("Episódio encontrado");
//            System.out.println("Temporada: " + episodioBuscado.get().getTemporada());
//        } else {
//            System.out.println("Episódio não encontrado!");
//        }


//        // Criando uma pergunta - Sobre um filtro de data:
//        System.out.println("A partir de que ano você deseja ver os episódios? ");
//        // Vamos criar uma variável que irá receber um Scanner de leitura
//        var ano = leitura.nextInt();
//        leitura.nextLine();
//
//        // Criando uma variável do Tipo Local Date, para armazenarmos a data de busca:
//        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
//
//
//        // Criando um formatador prévio - para converter o formato de data para o padrão Brasileiro:
//        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//
//        // Utilizando Streams combinada com método .isAfter() criando um filtro de data
//        episodios.stream()
//                .filter(e -> e.getDataDeLancamento() != null && e.getDataDeLancamento().isAfter(dataBusca))
//                // forEach -> Imprimindo tempora, episódios e data de lançamento - Concatenando informações
//                .forEach(e -> System.out.println(
//                        " Temporada: " + e.getTemporada() +
//                                " Episódio: " + e.getTitulo() +
//                                " Data de Lançamento: " + e.getDataDeLancamento().format(formatador)
//                ));

        // Criando um Mapa - Que irá percorrer os episódios de cada temproada e a avaliação de cada episódio da temporada
        Map<Integer, Double> avaliacoesPorTemporada = episodios.stream()
                // Criando um filtro que pega a avaliação e compara se é maior que 0.0
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.groupingBy(Episodio::getTemporada,
                        Collectors.averagingDouble(Episodio::getAvaliacao)));
        System.out.println(avaliacoesPorTemporada);


        // Utilizando Classe Double Summary Para exibir estatística da nossa API
        DoubleSummaryStatistics est = episodios.stream()
                .filter(e -> e.getAvaliacao() > 0.0)
                .collect(Collectors.summarizingDouble(Episodio::getAvaliacao));
        // Usando o método associado a variável para imprimir a média:
        System.out.println("Media: " + est.getAverage());
        // Usando o método associado a variável para imprimir o minimo e o máximo, quantidade total
        System.out.println("Melhor Episódio: " + est.getMax());
        System.out.println("Pior Episodio: " + est.getMin());
        System.out.println("Quantidade: " + est.getCount());


    }
}
