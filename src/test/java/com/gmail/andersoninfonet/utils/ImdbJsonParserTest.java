package com.gmail.andersoninfonet.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import com.gmail.andersoninfonet.model.Movie;
import com.gmail.andersoninfonet.responses.ImdbResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImdbJsonParserTest {
    
    private String json;

    @BeforeEach
    public void setup() {
        json = "{\"items\":[{\"id\":\"tt0111161\",\"rank\":\"1\",\"title\":\"The Shawshank Redemption\",\"fullTitle\":\"The Shawshank Redemption (1994)\",\"year\":\"1994\",\"image\":\"https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg\",\"crew\":\"Frank Darabont (dir.), Tim Robbins, Morgan Freeman\",\"imDbRating\":\"9.2\",\"imDbRatingCount\":\"2574700\"},{\"id\":\"tt1160419\",\"rank\":\"250\",\"title\":\"Dune\",\"fullTitle\":\"Dune (2021)\",\"year\":\"2021\",\"image\":\"https://m.media-amazon.com/images/M/MV5BN2FjNmEyNWMtYzM0ZS00NjIyLTg5YzYtYThlMGVjNzE1OGViXkEyXkFqcGdeQXVyMTkxNjUyNQ@@._V1_UX128_CR0,3,128,176_AL_.jpg\",\"crew\":\"Denis Villeneuve (dir.), Timothée Chalamet, Rebecca Ferguson\",\"imDbRating\":\"8.0\",\"imDbRatingCount\":\"546085\"}]}";
    }

    @Test
    void deveRealizarParse() {
        List<Movie> movies = (List<Movie>)ParserImdb.getInstance().parse(json);

        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals("The Shawshank Redemption", movies.get(0).title());
    }

    @Test
    void deveExtrairTitulos() {

        List<String> titulos = ParserImdb.extractTiTles(json);

        assertNotNull(titulos);
        assertFalse(titulos.isEmpty());
        assertEquals("The Shawshank Redemption", titulos.get(0));
    }

    @Test
    void deveExtrairUrlDaImagem() {

        List<String> imagesUrl = ParserImdb.extractUrlImage(json);

        assertNotNull(imagesUrl);
        assertFalse(imagesUrl.isEmpty());
        assertEquals("https://m.media-amazon.com/images/M/MV5BMDFkYTc0MGEtZmNhMC00ZDIzLWFmNTEtODM1ZmRlYWMwMWFmXkEyXkFqcGdeQXVyMTMxODk2OTU@._V1_UX128_CR0,3,128,176_AL_.jpg", imagesUrl.get(0));
    }

    @Test
    void deveExtrairAtributosPorNome() {
        List<String> atributos = ParserImdb.extractAttributeByName(json, "year");

        assertNotNull(atributos);
        assertFalse(atributos.isEmpty());
        assertEquals("1994", atributos.get(0));
    }

    @Test
    void deveGerarUmaListaDeImdbListResponse() {

        List<ImdbResponse> listResponse = ParserImdb.getImdbListResponse(json);

        assertNotNull(listResponse);
        assertFalse(listResponse.isEmpty());
        assertEquals("Frank Darabont (dir.), Tim Robbins, Morgan Freeman", listResponse.get(0).crew());
        assertEquals(1994, listResponse.get(0).year());
    }
}
