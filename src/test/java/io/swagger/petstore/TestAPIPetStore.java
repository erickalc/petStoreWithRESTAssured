package io.swagger.petstore;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.io.IOException;

import org.apache.http.HttpStatus;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import io.swagger.utils.ExcelUtils;

public class TestAPIPetStore{
	
	private static ResponseSpecification responseSpec;
	private static RequestSpecification requestSpec;
	
	@BeforeClass
	public static void createResponseSpecification() {
		responseSpec = new ResponseSpecBuilder().
				expectStatusCode(HttpStatus.SC_OK).
				expectContentType(ContentType.JSON).
				build();
	}
	
	@BeforeClass
	public static void createRequestSpecification() {
		requestSpec = new RequestSpecBuilder().
				setBaseUri("https://petstore.swagger.io/v2/").
				setContentType(ContentType.JSON).
				setAccept(ContentType.JSON).
				build();
	}
	
	@Test (priority = 0)
	public void postUser() throws IOException {
		ExcelUtils dataExcel = ExcelUtils.lerArquivoExcelUsers();
		
		JSONObject myuser = new JSONObject();
		JSONArray myuserarray = new JSONArray();
		
		for (int i = 1; i < dataExcel.getRowCount(); i++) {
			myuser.put("id", dataExcel.getCellData(i, 0));
			myuser.put("username", dataExcel.getCellData(i, 1));
			myuser.put("firstName", dataExcel.getCellData(i, 2));
			myuser.put("lastName", dataExcel.getCellData(i, 3));
			myuser.put("email", dataExcel.getCellData(i, 4));
			myuser.put("password", dataExcel.getCellData(i, 5));
			myuser.put("phone", dataExcel.getCellData(i, 6));
			myuser.put("userStatus", dataExcel.getCellData(i, 7));
			
			myuserarray.add(myuser);
			
			given().
				spec(requestSpec).
				body(myuserarray.toJSONString()).
			when().
				post("/user/createWithList").
			then().
				spec(responseSpec).
			and().
				assertThat().
				body("code", equalTo(200)).
				body("message", equalTo("ok")).
				log().all();
		}
		
	}

	@Test (priority = 1)
	public void postPet() throws IOException {
		ExcelUtils dataPetsExcel = ExcelUtils.lerArquivoExcelPets();

		JSONObject mypet = new JSONObject();
		
		for (int i = 1; i < dataPetsExcel.getRowCount(); i++) {
			mypet.put("id", dataPetsExcel.getCellData(i, 0));

			JSONObject itens_catg = new JSONObject();

			itens_catg.put("id", dataPetsExcel.getCellData(i, 1));
			itens_catg.put("name", dataPetsExcel.getCellData(i, 2));
			
			mypet.put("category", itens_catg);
			mypet.put("name", dataPetsExcel.getCellData(i, 3));
			
			JSONArray photo = new JSONArray();
			photo.add(dataPetsExcel.getCellData(i, 4));

			mypet.put("photoUrls", photo);

			JSONObject itens_tag = new JSONObject();
			itens_tag.put("id", dataPetsExcel.getCellData(i, 5));
			itens_tag.put("name", dataPetsExcel.getCellData(i, 6));

			JSONArray tag = new JSONArray();
			tag.add(itens_tag);

			mypet.put("tags", tag);
			
			mypet.put("status", dataPetsExcel.getCellData(i, 7));
			
			given().
				spec(requestSpec).
				body(mypet.toJSONString()).
			when().
				post("/pet").
			then().
				spec(responseSpec).
			and().
				assertThat().
				body("name", equalTo(dataPetsExcel.getCellData(i, 3))).
				log().all();
		}
//		String myPetString = "{ \"id\": 5624, \"category\": { \"id\": 1, \"name\": \"Gato\" }, \"name\": \"James\", \"photoUrls\": [ \"james.png\" ], \"tags\": [ { \"id\": 34, \"name\": \"meugato\" } ], \"status\": \"available\" }";
//
//		JsonObject petToJson = (JsonObject) JsonParser.parseString(myPetString);
		
		//System.out.println(petToJson);
	}

	@Test (priority = 3)
	public void placeOrder() throws IOException {
		
		ExcelUtils dataOrders = ExcelUtils.lerArquivoExcelOrders();
		
		JSONObject myorder = new JSONObject();
		for (int i = 1; i < dataOrders.getRowCount(); i++) {
			myorder.put("id", dataOrders.getCellData(i, 0));
			myorder.put("petId", dataOrders.getCellData(i, 1));
			myorder.put("quantity", dataOrders.getCellData(i, 2));
			myorder.put("shipDate", dataOrders.getCellData(i, 3));
			myorder.put("status", dataOrders.getCellData(i, 4));
			myorder.put("complete", "true");
			
			given().
				spec(requestSpec).
				body(myorder.toJSONString()).
			when().
				post("store/order").
			then().
				spec(responseSpec).
			and().
				assertThat().
				body("status", equalTo("placed")).
				log().all();
		}
	}
	
	@Test (priority = 4)
	public void replaceOrder() throws IOException {
		ExcelUtils dataOrders = ExcelUtils.lerArquivoExcelOrders();
		
		JSONObject myorder = new JSONObject();
		for (int i = 1; i < dataOrders.getRowCount(); i++) {
			myorder.put("id", dataOrders.getCellData(i, 0));
			myorder.put("petId", dataOrders.getCellData(i, 1));
			myorder.put("quantity", dataOrders.getCellData(i, 2));
			myorder.put("shipDate", dataOrders.getCellData(i, 3));
			myorder.put("complete", "true");
			if (dataOrders.getCellData(i, 1).equals("202010") | dataOrders.getCellData(i, 1).equals("202011") |
					dataOrders.getCellData(i, 1).equals("202012") | dataOrders.getCellData(i, 1).equals("202013") |
					dataOrders.getCellData(i, 1).equals("202014")) {
				myorder.put("status", "delivered");
				given().
					spec(requestSpec).
					body(myorder.toJSONString()).
				when().
					post("store/order").
				then().
					spec(responseSpec).
				and().
					assertThat().
					body("status", equalTo("delivered")).
					log().all();
			}else {
				myorder.put("status", "approved");
				given().
					spec(requestSpec).
					body(myorder.toJSONString()).
				when().
					post("store/order").
				then().
					spec(responseSpec).
				and().
					assertThat().
					body("status", equalTo("approved")).
					log().all();
			}
		
		}
	}
	
	@Test (priority = 5)
	public void getUser() {
		baseURI = "https://petstore.swagger.io/v2/";

		given().
			get("/user/erickazevedo").
		then().
			spec(responseSpec).
		and().
			log().all();
	}
	
	@Test (priority = 5)
	public void getPet() {
		baseURI = "https://petstore.swagger.io/v2/";

		given().
			get("pet/202010").
		then().
			spec(responseSpec).
		and().
			log().all();
	}
}