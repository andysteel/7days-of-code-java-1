package com.gmail.andersoninfonet.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.gmail.andersoninfonet.responses.ImdbResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class JsonParserTest {
    
    private String json;

    @BeforeEach
    public void setup() {
        json = "{\"items\":[{\"id\":\"tt0111161\",\"rank\":\"1\",\"title\":\"The Shawshank Redemption\",\"fullTitle\":\"The Shawshank Redemption (1994)\",\"year\":\"1994\",\"image\":\"https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg\",\"crew\":\"Frank Darabont (dir.), Tim Robbins, Morgan Freeman\",\"imDbRating\":\"9.2\",\"imDbRatingCount\":\"2574700\"},{\"id\":\"tt1160419\",\"rank\":\"250\",\"title\":\"Dune\",\"fullTitle\":\"Dune (2021)\",\"year\":\"2021\",\"image\":\"https://m.media-amazon.com/images/M/MV5BN2FjNmEyNWMtYzM0ZS00NjIyLTg5YzYtYThlMGVjNzE1OGViXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_UX128_CR0,3,128,176_AL_.jpg\",\"crew\":\"Denis Villeneuve (dir.), Timothée Chalamet, Rebecca Ferguson\",\"imDbRating\":\"8.0\",\"imDbRatingCount\":\"546085\"},{\"id\":\"blablabl\"}]}";
    }
    @Test
    void deveExtrairJson() {
        String teste = JsonParser.extractJson(json);

        assertNotNull(teste);
        assertFalse(teste.isBlank());
        assertFalse(teste.contains("["));
        assertFalse(teste.contains("]"));
    }

    @Test
    void deveExtrairJsonMovie() {
        String testeJson = JsonParser.extractJson(json);
        String[] teste = JsonParser.extractJsonMovies(testeJson);

        assertNotNull(teste);
        assertTrue(() -> teste.length > 0);    
    }

    @Test
    void deveExtrairAtributos() {
    
        String testeJson = JsonParser.extractJson(json);
        String[] testeJsonMovies = JsonParser.extractJsonMovies(testeJson);
         
        String[] teste = JsonParser.extractJsonAttributes(testeJsonMovies);

        assertNotNull(teste);
        assertTrue(() -> teste.length > 0);
        assertEquals("id\":\"tt0111161", teste[0]);
    }

//id":"tt0111161","rank":"1","title":"The Shawshank Redemption","fullTitle":"The Shawshank Redemption (1994)","year":"1994","image":"https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg","crew":"Frank Darabont (dir.), Tim Robbins, Morgan Freeman","imDbRating":"9.2","imDbRatingCount":"2574700

    @Test
    void devePreFormartarUmArrayDeFilmes() {
        String testeJson = JsonParser.extractJson(json);
        String[] testeJsonMovies = JsonParser.extractJsonMovies(testeJson);

        String[] teste = JsonParser.preFormatMovieArrayToCreateMovie(testeJsonMovies);

        assertNotNull(teste);
        assertTrue(() -> teste.length > 0);
        assertEquals("id\":\"tt0111161\",\"rank\":\"1\",\"title\":\"The Shawshank Redemption\",\"fullTitle\":\"The Shawshank Redemption (1994)\",\"year\":\"1994\",\"image\":\"https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg\",\"crew\":\"Frank Darabont (dir.), Tim Robbins, Morgan Freeman\",\"imDbRating\":\"9.2\",\"imDbRatingCount\":\"2574700", teste[0]);
    }

    @Test
    void deveExtrairTitulos() {
        String testeJson = JsonParser.extractJson(json);
        String[] testeJsonMovies = JsonParser.extractJsonMovies(testeJson);
        String[] testeAJsonAttributes = JsonParser.extractJsonAttributes(testeJsonMovies);

        List<String> titulos = JsonParser.extractTiTles(testeAJsonAttributes);

        assertNotNull(titulos);
        assertFalse(titulos.isEmpty());
        assertEquals("The Shawshank Redemption", titulos.get(0));
    }

    @Test
    void deveExtrairUrlDaImagem() {
        String testeJson = JsonParser.extractJson(json);
        String[] testeJsonMovies = JsonParser.extractJsonMovies(testeJson);
        String[] testeAJsonAttributes = JsonParser.extractJsonAttributes(testeJsonMovies);

        List<String> imagesUrl = JsonParser.extractUrlImage(testeAJsonAttributes);

        assertNotNull(imagesUrl);
        assertFalse(imagesUrl.isEmpty());
        assertEquals("https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg", imagesUrl.get(0));
    }

    @Test
    void deveExtrairAtributosPorNome() {
        String testeJson = JsonParser.extractJson(json);
        String[] testeJsonMovies = JsonParser.extractJsonMovies(testeJson);
        String[] testeAJsonAttributes = JsonParser.extractJsonAttributes(testeJsonMovies);

        List<String> atributos = JsonParser.extractAttributeByName(testeAJsonAttributes, "year");

        assertNotNull(atributos);
        assertFalse(atributos.isEmpty());
        assertEquals("1994", atributos.get(0));
    }

    @Test
    void deveGerarUmaListaDeImdbListResponse() {

        List<ImdbResponse> listResponse = JsonParser.getImdbListResponse(json);

        assertNotNull(listResponse);
        assertFalse(listResponse.isEmpty());
        assertEquals("Frank Darabont (dir.), Tim Robbins, Morgan Freeman", listResponse.get(0).crew());
        assertEquals(1994, listResponse.get(0).year());
    }
}
