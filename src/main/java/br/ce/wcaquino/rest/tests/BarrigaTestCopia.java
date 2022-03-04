package br.ce.wcaquino.rest.tests;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.utils.DataUtils;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

//Before é executado uma vez antes de cada metodo da classe.
//BeforeClass é executado somente uma vez para a classe inteira

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class BarrigaTestCopia extends BaseTest {

    Movimentacao mov = new Movimentacao();
    private String TOKEN;

    private static String nome_conta = "Conta " + System.nanoTime();
    private static Integer id_conta;
    private static Integer id_mov;

    @Before
    public void login() {
        //realizando login: email e senha de acesso
        Map<String, String> login = new HashMap<>();
        login.put("email", "randell@teste");
        login.put("senha", "123456");

                TOKEN = given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");


    }

    @Test
    public void t01_naoDeveAcessarAPISemToken() {
        given()

                .when()
                .get("/contas")
                .then()
                .statusCode(401)
        ;
    }

    @Test
    public void t02_deveIncluirContaComSucesso() {
        //criação da conta
      id_conta = given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \""+nome_conta+"\"}")
        .when()
                .post("/contas")
        .then()
                .statusCode(201)
                .extract().path("id");
        System.out.println("id da conta: " + id_conta);


    }

    @Test
    public void t03_deveAlterarContaComSucesso() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \""+nome_conta+" alterada\"}")
                .pathParam("id", id_conta)
        .when()
                .put("/contas/{id}")
        .then()
                .log().all()
                .statusCode(200)
                .body("nome", is(nome_conta + " alterada"))
        ;
    }

    @Test
    public void t04_naoDeveInserirContaExistente() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{\"nome\": \""+nome_conta+" alterada\"}")
        .when()
                .post("/contas")
        .then()
                .log().all()
                .statusCode(400)
                .body("error", is("Já existe uma conta com esse nome!"))
        ;
    }

    @Test
    public void t05_deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        //submetendo a requisicao do endpoint de movimentacao
         id_mov = given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
                .when()
                .post("/transacoes")
                .then()
                .statusCode(201)
                .extract().path("id")
        ;
    }

    @Test
    public void t06_deveValidarCamposObrigatoriosMovimentacao() {
        given()
                .header("Authorization", "JWT " + TOKEN)
                .body("{}")
                .when()
                .post("/transacoes")
                .then()
                .statusCode(400)
                .body("$", hasSize(8))
                .log().all()
                .body("msg", hasItems(
                        "Data da Movimentação é obrigatório",
                        "Data do pagamento é obrigatório",
                        "Descrição é obrigatório",
                        "Interessado é obrigatório",
                        "Valor é obrigatório",
                        "Valor deve ser um número"

                ))
        ;
    }

    @Test
    public void t07_naoDeveCadastrarMovimentacaoDataFutura() {
        mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

        given()
                .header("Authorization", "JWT " + TOKEN)
                .body(mov)
        .when()
                .post("/transacoes")
        .then()
                .statusCode(400)
                .body("$", hasSize(1))
                .body("msg", hasItems("Data da Movimentação deve ser menor ou igual à data atual"))
                .log().all()
        ;
    }

    @Test
    public void t08_naoDeveRemoverContaComMovimentacao(){


        given()
                .header("Authorization", "JWT " + TOKEN)
                .pathParam("id", id_conta)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)

                .body("constraint", is("transacoes_conta_id_foreign"))

        ;
    }

    @Test
    public void t09_deveCalcularSaldoContas(){
        given()
                .header("Authorization", "JWT " + TOKEN)

                .when()
                .get("/saldo")
                .then()
                .statusCode(200)
                .body("find{it.conta_id == "+id_conta+"}.saldo", is("100.00"))
                //.body("constraint", is("transacoes_conta_id_foreign"))

        ;
    }

    @Test
    public void t10_devoRemoverMovimentacao(){
        given()
                .header("Authorization", "JWT " + TOKEN)
                .pathParam("id", id_mov)
                .when()
                .delete("/transacoes/{id}")
                .then()
                .statusCode(204);

        //.body("constraint", is("transacoes_conta_id_foreign"))

        ;
    }



    private Movimentacao getMovimentacaoValida() {
        mov = new Movimentacao();
        //inserindo os dados para movimentacao
        mov.setConta_id(id_conta);
        //mov.setUsuario_id(usuario_id);
        mov.setDescricao("movimentacao teste a");
        mov.setEnvolvido("envolvidos na movimentacao");
        mov.setTipo("REC");
        mov.setData_transacao(DataUtils.getDataDiferencaDias(-1));
        mov.setData_pagamento(DataUtils.getDataDiferencaDias(5));
        mov.setValor(100f);
        mov.setStatus(true);
        return mov;
    }



}

