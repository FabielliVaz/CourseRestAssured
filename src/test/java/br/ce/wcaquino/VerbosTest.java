package br.ce.wcaquino;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class VerbosTest {

    @Test
    public void deveSalvarUsuario() {
        given()
            .log().all() // Loga todos os detalhes da requisição
            .contentType("application/json") // Define o tipo de conteúdo como JSON
            .body("{ \"name\": \"Jhordan\", \"age\": 31 }") // Corpo da requisição
        .when()
            .post("https://restapi.wcaquino.me/users") // URL do endpoint
        .then()
            .log().all() // Loga todos os detalhes da resposta
            .statusCode(201) // Verifica se o status code é 201 (Created)        
            .body("id", is(notNullValue())) // Verifica se o campo 'id' não é nulo
            .body("name", is("Jhordan")) // Verifica se o nome é "João da Silva"
            .body("age", is(31)) // Verifica se a idade é 31
            ;
    }

    @Test
    public void naoDeveSalvarUsuarioSemNome() {
        given()
            .log().all() // Loga todos os detalhes da requisição
            .contentType("application/json") // Define o tipo de conteúdo como JSON
            .body("{ \"age\": 31 }") // Corpo da requisição sem o campo 'name'
        .when()
            .post("https://restapi.wcaquino.me/users") // URL do endpoint
        .then()
            .log().all() // Loga todos os detalhes da resposta
            .statusCode(400) // Verifica se o status code é 400 (Bad Request)
            .body("id", is(nullValue())) // Verifica se o campo 'id' não é nulo
            .body("error", is("Name é um atributo obrigatório")) // Verifica se a mensagem de erro é a esperada
            ;
    }

    @Test
    public void deveSalvarUsuarioXML() {
        given()
            .log().all() 
            .contentType(ContentType.XML) 
            .body("<user><name>Junior</name><age>44</age></user>") 
        .when()
            .post("https://restapi.wcaquino.me/usersXML") 
        .then()
            .log().all() 
            .statusCode(201) 
            .body("user.@id", is(notNullValue())) 
            .body("user.name", is("Junior")) 
            .body("user.age", is("44")) 
            ;
    }

    @Test
    public void deveAlterarUsuario() {
        given()
            .log().all() 
            .contentType("application/json") 
            .body("{ \"name\": \"Carla\", \"age\": 89, \"salary\": 12 }") 
        .when()
            .put("https://restapi.wcaquino.me/users/1") 
        .then()
            .log().all() 
            .statusCode(200) 
        ;
    }
}