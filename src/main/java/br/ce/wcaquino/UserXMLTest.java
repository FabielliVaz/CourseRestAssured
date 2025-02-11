package br.ce.wcaquino;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;

public class UserXMLTest<NodeImpl> {

  public static RequestSpecification reqSpec;
  public static ResponseSpecification resSpec;

  @BeforeClass
  public static void setup() {
    RestAssured.baseURI = "http://restapi.wcaquino.me";
    //RestAssured.port = 443;
    //RestAssured.basePath = "/v2";

    RequestSpecBuilder reqBuilder = new RequestSpecBuilder();
    reqBuilder.log(LogDetail.ALL);
    reqSpec = reqBuilder.build();

    ResponseSpecBuilder resBuilder = new ResponseSpecBuilder();
    resBuilder.expectStatusCode(200);
    resSpec = resBuilder.build();

    RestAssured.requestSpecification = reqSpec;
    RestAssured.responseSpecification = resSpec;
  }


  @Test
  public void devoTrabalharComXML() { //para XML todos os valores são string
    given().when().get("/usersXML/3").then()
            //.statusCode(200)
            .rootPath("user")// define o caminho raiz
            .body("name", is("Ana Julia")).body("@id", is("3"))//para referenciar um atributo usa @
            .rootPath("user.filhos").body("name.size()", is(2))
            .detachRootPath("filhos") //retirar do rootPath um valor - separar
            .body("filhos.name[0]", is("Zezinho"))
            .body("filhos.name[1]", is("Luizinho"))
            .appendRootPath("filhos")//acrescentar
            .body("name", hasItem("Zezinho"))
            .body("name", hasItems("Luizinho", "Zezinho"))
            ;
  }

  @Test
  public void devoFazerPesquisasAvancadasComXML() { //para XML todos os valores são string
      given()
      .when()
            .get("/usersXML")
      .then()
            .body("users.user.size()", is(3))
            .body("users.user.findAll{it.age.toInteger() <= 25}.size()", is(2))
            .body("users.user.@id", hasItems("1", "2", "3")).body("users.user.find{it.age == 25}.name", is("Maria Joaquina"))
            .body("users.user.findAll{it.name.toString().contains('n')}.name", hasItems("Maria Joaquina", "Ana Julia"))
            .body("users.user.salary.find{it != null}.toDouble()", is(1234.5678d))
            .body("users.user.age.collect{it.toInteger() * 2}", hasItems(40, 50, 60))
            .body("users.user.name.findAll{it.toString().startsWith('Maria')}.collect{it.toString().toUpperCase()}", is("MARIA JOAQUINA"));

  }

  @Test
  public void devoFazerPesquisasAvancadasComXMLeJava() {
    ArrayList<NodeImpl> nomes = given()//extração
            .when().get("/usersXML").then().statusCode(200).extract().path("users.user.name.findAll{it.toString().contains('n')}");
    Assert.assertEquals(2, nomes.size());
    Assert.assertEquals("Maria Joaquina".toUpperCase(), nomes.get(0).toString().toUpperCase());
    Assert.assertTrue("ANA JULIA".equalsIgnoreCase(nomes.get(1).toString())); // ignorar maiúsculas e minúsculas
  }

  @Test
  public void devoFazerPesquisasAvancadasComXPath() {
    given().when().get("/usersXML").then().statusCode(200)
            .body(hasXPath("count(/users/user)", is("3")))
            .body(hasXPath("/users/user[@id= '1' ]")).body(hasXPath("//user[@id= '2' ]")) // neste caso o barra vai descendo nos níveis do XML
            .body(hasXPath("//name[text() = 'Luizinho']/../../name", is("Ana Julia"))) //Com /.. vai subindo, navegando entre os atributos
            .body(hasXPath("//name[text() = 'Ana Julia']/following-sibling::filhos", allOf(containsString("Zezinho"), containsString("Luizinho"))))//navega entre os irmãos do campo: https://www.red-gate.com/simple-talk/wp-content/uploads/imported/1269-Locators_table_1_0_2.pdf?file=4937
            .body(hasXPath("/users/user/name", is("João da Silva")))
            .body(hasXPath("//name", is("João da Silva")))
            .body(hasXPath("/users/user[2]/name", is("Maria Joaquina")))
            .body(hasXPath("/users/user[last()]/name", is("Ana Julia"))) //último registro que existe na lista
            .body(hasXPath("count(/users/user/name[contains(., 'n')])", is("2"))) //o ponto serve para procurar em qualquer valor do registro um 'n'
            .body(hasXPath("//user[age < 24]/name", is("Ana Julia")))
            .body(hasXPath("//user[age > 24]/name", is("João da Silva")))
            .body(hasXPath("//user[age > 20 and age < 30]/name", is("Maria Joaquina"))) //também é possível separara as asserções com [] para cada ação
    ;
  }
}


