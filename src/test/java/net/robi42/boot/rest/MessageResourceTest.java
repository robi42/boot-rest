package net.robi42.boot.rest;

import net.robi42.boot.domain.Message;
import net.robi42.boot.search.SearchHitDto;
import net.robi42.boot.util.IntegrationTestBase;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.OK;
import static org.fest.assertions.Assertions.assertThat;

public class MessageResourceTest extends IntegrationTestBase {

    @Test
    public void respondsSuccessfully() throws Exception {
        final Response response = restApi.path(MessageResource.BASE_PATH).request().get();

        assertThat(response.getStatusInfo().getFamily())
                .isEqualTo(Response.Status.Family.SUCCESSFUL);
        assertThat(response.getStatus()).isEqualTo(OK.getStatusCode());
        response.close();
    }

    @Test
    public void respondsWithMessages() throws Exception {
        final List<Message> messages = restApi.path(MessageResource.BASE_PATH).request()
                .get(new GenericType<List<Message>>() {});
        assertThat(messages).isNotEmpty();
        assertThat(messages.size()).isEqualTo(3);

        final List<String> messageBodies = messages.stream()
                .map(Message::getBody).collect(toList());
        assertThat(messageBodies).contains("Foo");
    }

    @Test
    public void findsMessageViaSearch() throws Exception {
        final List<SearchHitDto> hits = restApi.path(MessageResource.BASE_PATH + "/search")
                .queryParam("q", "bar").request()
                .get(new GenericType<List<SearchHitDto>>() {});
        assertThat(hits.size()).isEqualTo(1);

        final SearchHitDto hit = hits.get(0);
        assertThat(hit.getScore()).isPositive();

        final Map<String, Object> source = hit.getSource();
        assertThat(source.get("id")).isEqualTo(hit.getId());
        assertThat(source.get("body")).isEqualTo("Bar");
    }
}
