package br.com.alura.screenmatch.service;

public interface IConverteDados {
    // Esse é o Type Generics: Não sabemos o tipo que ele irá nos devolver
    // O IConverteDados irá receber um Json e Receber uma Classe, no Conversor de dados vou tentar transformá-lo na classe que foi indicada
    <T> T obterDados(String json, Class<T> classe);
}
