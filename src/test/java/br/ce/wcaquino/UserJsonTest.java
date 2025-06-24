package br.ce.wcaquino;

import static io.restassured.RestAssured.expect;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.collection.ArrayMatching.arrayContaining;
import static org.hamcrest.collection.IsArrayWithSize.arrayWithSize;

import io.restassured.RestAssured;
import io.restassured.http.Method;
import io.restassured.path.json.JsonPath;
import org.junit.Assert;
import org.junit.Test;

import io.restassured.response.Response;

import java.util.ArrayList;
import java.util.Arrays;

public class UserJsonTest {
  @Test
  public void deveVerificarPrimeiroNivel() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users/1")
            .then()
            .statusCode(200)
            .body("id", is(1))
            .body("name", containsString("Silva"))
            .body("age", greaterThan(18)) // maior que
            .body("age", equalTo(30));
    // .body("salary", equalTo(1234.5678));
  }

  @Test
  public void deveVerificarPrimeiroNivelOutrasFormas() {
    Response response = RestAssured.request(Method.GET, "https://restapi.wcaquino.me/users/1");

    // path

    // 1 - System.out.println("" + response.path("id"));
    // 2 - System.out.println((Integer) response.path("id"));
    // 3 - Assert.assertEquals(new Integer(1), response.path("id"));
    Assert.assertEquals(new Integer(1), response.path("%s", "id"));

    // json path
    JsonPath jpath = new JsonPath(response.asString());
    Assert.assertEquals(1, jpath.getInt("id"));

    // from método estático do JsonPath
    int id = JsonPath.from(response.asString()).getInt("id");
    Assert.assertEquals(1, id);
  }

  @Test
  public void deveVerificarSegundoNivel() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users/2")
            .then()
            .statusCode(200)
            .body("name", containsString("Joaquina"))
            .body("endereco.rua", is("Rua dos bobos"))
            .body("endereco.numero", is(0));

  }

  @Test
  public void deveVerificarLista() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users/3")
            .then()
            .statusCode(200)
            .body("name", containsString("Ana"))
            .body("filhos", hasSize(2))
            .body("filhos[0].name", is("Zezinho"))
            .body("filhos[1].name", is("Luizinho"))
            .body("filhos.name", hasItem("Zezinho"))
            .body("filhos.name", hasItems("Zezinho", "Luizinho"));
  }

  @Test
  public void deveRetornarErroUsuarioInexistente() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users/4")
            .then()
            .statusCode(404)
            .body("error", is("Usuário inexistente"));
  }

  @Test
  public void deveVerificarListaRaiz() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users")
            .then()
            .statusCode(200)
            .body("$", hasSize(3)) // $ indica que está na raiz
            .body("name", hasItems("João da Silva", "Maria Joaquina", "Ana Júlia"))
            .body("age[1]", is(25))
            .body("filhos.name", hasItem(Arrays.asList("Zezinho", "Luizinho"))) // hasItem: possui
            // um item
            .body("salary", contains(1234.5677f, 2500, null))// contains só aceita se mandar todos o
    // valores da lista e na ordem correta
    ;
  }

  @Test
  public void devoFazerVerificacoesAvancadas() {
    given()
            .when()
            .get("https://restapi.wcaquino.me/users")
            .then()
            .statusCode(200)
            .body("$", hasSize(3))
            .body("age.findAll{it <= 25}.size()", is(2))// busca em idade, chamando o método findAll
            // onde delimita os dados entre chaves, o it
            // é a instância atual da idade, e por
            // ultimo é solicitado o tamanho que é = 2
            .body("age.findAll{it <= 25 && it > 20}.size()", is(1)) // usuários com + de 25 anos e
            // até 25 / && = e
            .body("findAll{it.age <= 25 && it.age > 20}.name", hasItem("Maria Joaquina"))// findAll
            // = loop
            // retorna
            // uma
            // lista
            .body("findAll{it.age <= 25}[0].name", is("Maria Joaquina"))// transformar a lista em um
            // objeto
            .body("findAll{it.age = 20}[-1].name", is("Ana Júlia")) // findAll busca todos find
            // busca 1
            .body("find{it.age <= 30}.name", is("João da Silva"))
            .body("findAll{it.name.contains('n')}.name", hasItems("Maria Joaquina", "Ana Júlia"))
            .body("findAll{it.name.length() > 10}.name",
                    hasItems("Maria Joaquina", "João da Silva"))
            .body("name.collect{it.toUpperCase()}", hasItem("MARIA JOAQUINA"))
            .body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}",
                    hasItem("MARIA JOAQUINA")) // startsWith = começa com
            //.body("name.findAll{it.startsWith('Maria')}.collect{it.toUpperCase()}.toArray()",
                    //allOf(arrayContaining("MARIA JOAQUINA"), arrayWithSize(1)))
            // .body("age.collect{it * 2}", hasItems(60, 50, 40))
            .body("id.max()", is(3))
            .body("salary.min()", is(1234.5678f))
            .body("salary.findAll{it != null}.sum()", is(closeTo(3734.5678f, 0.001)))
            .body("salary.findAll{it != null}.sum()", allOf(greaterThan(3000d), lessThan(5000d))) // allOf:
    // Tudo de

    ;
  }

  @Test
  public void devoUnirJsonPathComJAVA() {
    ArrayList<String> names = given()
            .when()
            .get("https://restapi.wcaquino.me/users")
            .then()
            .statusCode(200)
            .extract().path("name.findAll{it.startsWith('Maria')}");
    Assert.assertEquals(1, names.size());
    Assert.assertTrue(names.get(0).equalsIgnoreCase("mArIa Joaquina"));
    Assert.assertEquals(names.get(0).toUpperCase(), "maria joaquina".toUpperCase());
  }
}
