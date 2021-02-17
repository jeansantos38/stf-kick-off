package rest.tests;

import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedHeader;
import com.github.jeansantos38.stf.dataclasses.web.http.HttpDetailedResponse;
import com.github.jeansantos38.stf.enums.http.HttpRequestMethod;
import com.github.jeansantos38.stf.enums.serialization.SerializationType;
import com.github.jeansantos38.stf.framework.io.InputOutputHelper;
import io.qameta.allure.Description;
import net.minidev.json.JSONArray;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import rest.base.HttpClientTestBase;
import rest.pojos.bookstore.Book;
import rest.pojos.bookstore.BooksStore;

import java.io.IOException;

public class ResponseParsingTests extends HttpClientTestBase {

    byte[] requestBody;
    byte[] responsePayload;
    String endpoint;
    String url;
    HttpDetailedHeader httpDetailedHeader = new HttpDetailedHeader("stf", "qa");

    @BeforeClass
    public void initializeMocks() throws IOException {
        requestBody = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("requestBody.json")).getBytes();
        responsePayload = InputOutputHelper.readContentFromFile(discoverAbsoluteFilePath("responseBooksStore.json")).getBytes();
        endpoint = "/deserialize/some/bookstore2";
        url = baseServerUrl + endpoint;
        configureStub(wireMockServer, responsePayload, endpoint);
    }

    @Test
    @Description("Deserialize response using fasterxml api.")
    public void deserializeToPojoTest1() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        BooksStore booksStore = httpDetailedResponse.deserializeResponseBodyToObject(BooksStore.class, SerializationType.JSON);
        Assert.assertEquals(booksStore.store.book[3].author, "J. R. R. Tolkien");
        Assert.assertEquals(booksStore.store.book[3].title, "The Lord of the Rings");
        Assert.assertEquals(booksStore.store.book[3].isbn, "0-395-19395-8");
        //or
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['isbn']", "0-395-19395-8");
    }

    @Test
    @Description("Deserialize response using jsonPath api.")
    public void deserializeToPojoTest2() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        Book book = httpDetailedResponse.deserializeJsonPathFromResponse(Book.class, "$.store.book[3]");
        Assert.assertEquals(book.author, "J. R. R. Tolkien");
        Assert.assertEquals(book.title, "The Lord of the Rings");
        Assert.assertEquals(book.isbn, "0-395-19395-8");
        //or
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['author']", "J. R. R. Tolkien");
    }

    @Test
    @Description("Deserialize response using jsonPath api.")
    public void readJsonPathFromResponseTest1() throws Exception {
        HttpDetailedResponse httpDetailedResponse = httpClient.performHttpRequest(HttpRequestMethod.POST, url, httpDetailedHeader, requestBody);
        httpDetailedResponse.assertExpectedStatusCode(200);
        JSONArray author = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['author']");
        JSONArray title = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['title']");
        JSONArray isbn = httpDetailedResponse.readJsonPathFromResponse("$['store']['book'][3]['isbn']");
        Assert.assertEquals(author.get(0), "J. R. R. Tolkien");
        Assert.assertEquals(title.get(0), "The Lord of the Rings");
        Assert.assertEquals(isbn.get(0), "0-395-19395-8");
        //or
        httpDetailedResponse.assertJsonPathFromResponse(HttpDetailedResponse.Operator.EQUAL, "$['store']['book'][3]['title']", "The Lord of the Rings");
    }
}