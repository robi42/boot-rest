package net.robi42.boot.rest;

import lombok.val;
import net.robi42.boot.domain.Message;
import net.robi42.boot.search.ElasticsearchProvider.SearchHitDto;
import net.robi42.boot.util.IntegrationTestBase;
import org.junit.Test;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;

public class MessageResourceTest extends IntegrationTestBase {

    @Test
    public void respondsSuccessfully() throws Exception {
        val response = restApi.path(MessageResource.BASE_PATH).request().get();

        assertThat(response.getStatusInfo().getFamily()).isEqualTo(Response.Status.Family.SUCCESSFUL);
        response.close();
    }

    @Test
    public void respondsWithMessages() throws Exception {
        val messages = restApi.path(MessageResource.BASE_PATH).request().get(new GenericType<List<Message>>() {});

        assertThat(messages.size()).isEqualTo(3);
        assertThat(messages.stream().map(Message::getBody).collect(toList())).contains("Foo");
    }

    @Test
    public void findsMessageViaSearch() throws Exception {
        final List<SearchHitDto> hits = restApi.path(MessageResource.BASE_PATH + "/search")
                .queryParam("q", "bar").request()
                .get(new GenericType<List<SearchHitDto>>() {});
        assertThat(hits.size()).isEqualTo(1);

        val hit = hits.get(0);
        assertThat(hit.getScore()).isPositive();

        val source = hit.getSource();
        assertThat(source.get("id")).isEqualTo(hit.getId());
        assertThat(source.get("body")).isEqualTo("Bar");
    }
}
