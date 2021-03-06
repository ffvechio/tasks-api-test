package br.com.felipebessa.tasks.apitest;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.hamcrest.CoreMatchers;
import org.junit.BeforeClass;
import org.junit.Test;

public class APITest {

    @BeforeClass
    public static void setup() {
        RestAssured.baseURI = "http://localhost:8001/tasks-backend";
    }

    @Test
    public void deveRetornarTarefas() {
        RestAssured
                .given()
                .when()
                .get("/todo")
                .then().statusCode(200)
                .log().all();
    }

    @Test
    public void deveAdicionarTarefaComSucesso() {
        RestAssured
                .given().contentType(ContentType.JSON)
                .body("{\"task\": \"Teste automation\", \"dueDate\": \"2020-12-30\"}")
                .when()
                .post("/todo")
                .then().statusCode(201)
                .log().all();
    }

    @Test
    public void naoDeveAdicionarTarefaInvalida() {
        RestAssured
                .given().contentType(ContentType.JSON)
                .body("{\"task\": \"Teste automation\", \"dueDate\": \"2010-12-30\"}")
                .when()
                .post("/todo")
                .then()
                .log().all()
                .statusCode(400)
                .body("message", CoreMatchers.is("Due date must not be in past"));
    }

    @Test
    public void deveRemoverTarefaComSucesso() {

        //inserir tarefa
        Integer id = RestAssured
                .given().contentType(ContentType.JSON)
                .body("{\"task\": \"Tarefa para remoção\", \"dueDate\": \"2020-12-30\"}")
                .when()
                .post("/todo")
                .then().statusCode(201)
                .extract().path("id");

        //remover tarefa
        RestAssured.given()
                .when()
                .delete("/todo/" + id)
                .then().statusCode(204);
    }
}

