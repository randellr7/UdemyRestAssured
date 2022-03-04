package br.ce.wcaquino.rest.tests.refac;

import br.ce.wcaquino.rest.core.BaseTest;
import br.ce.wcaquino.rest.tests.Movimentacao;
import br.ce.wcaquino.rest.utils.DataUtils;
import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class MovimentacaoTest extends BaseTest {

    Movimentacao mov = new Movimentacao();

    @BeforeClass
    public static void login() {
        //realizando login: email e senha de acesso
        Map<String, String> login = new HashMap<>();
        login.put("email", "randell@teste");
        login.put("senha", "123456");

        String TOKEN =
                given()
                        .body(login)
                        .when()
                        .post("/signin")
                        .then()
                        .statusCode(200)
                        .extract().path("token");

        RestAssured.requestSpecification.header("Authorization", "JWT " + TOKEN);

        RestAssured.get("/reset").then().statusCode(200);

    }

    public Integer getIdContaPeloNome(String nome){
        return RestAssured.get("/contas?nome="+nome).then().extract().path("id[0]");
    }

    @Test
    public void deveInserirMovimentacaoComSucesso() {
        Movimentacao mov = getMovimentacaoValida();

        //submetendo a requisicao do endpoint de movimentacao
        given()
                .body(mov)
        .when()
                .post("/transacoes")
                .then()
        .statusCode(201)
        ;
    }

    @Test
    public void deveValidarCamposObrigatoriosMovimentacao() {
        given()
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
    public void naoDeveCadastrarMovimentacaoDataFutura() {
        mov = getMovimentacaoValida();
        mov.setData_transacao(DataUtils.getDataDiferencaDias(2));

        given()
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
    public void naoDeveRemoverContaComMovimentacao(){
        // Conta com movimentacao
        Integer CONTA_ID = getIdContaPeloNome("Conta com movimentacao");

        given()
                .pathParam("id", CONTA_ID)
                .when()
                .delete("/contas/{id}")
                .then()
                .statusCode(500)

                .body("constraint", is("transacoes_conta_id_foreign"))
        ;
    }

    @Test
    public void devoRemoverMovimentacao(){
        Integer MOV_ID = getIdMovPelaDescricao("Movimentacao para exclusao");

        given()
                .pathParam("id", MOV_ID)
            .when()
                .delete("/transacoes/{id}")
            .then()
                .statusCode(204);

        //.body("constraint", is("transacoes_conta_id_foreign"))

        ;
    }

    private Integer getIdMovPelaDescricao(String movimentacao_para_exclusao) {
        return RestAssured.get("/transacoes?descricao="+movimentacao_para_exclusao).then().extract().path("id[0]");
    }

    private Movimentacao getMovimentacaoValida() {
        mov = new Movimentacao();
        //inserindo os dados para movimentacao
        mov.setConta_id(getIdContaPeloNome("Conta para movimentacoes"));
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
