package {{ root_package }}.integration.tests;

import org.junit.jupiter.api.Test;
import {{ root_package }}.grpc.v1.*;{% if persistence != 'None' %}
{%- for entity_key in model.entities -%}
{%- set EntityName = entity_key | pascal_case %}
import {{ root_package }}.persistence.repositories.{{ EntityName }}Repository;{% endfor %}{% endif %}

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class {{ ProjectPrefix }}{{ ProjectSuffix }}GrpcTest extends {{ ProjectPrefix }}{{ ProjectSuffix }}BaseIT {
{%- for entity_key in model.entities -%}
{%- set EntityName = entity_key | pascal_case -%}
{%- set entityName = entity_key | camel_case %}

    @Test
    void test_create{{ EntityName }}() {
        Create{{ EntityName }}Response response = client.create{{ EntityName }}({{ EntityName }}Dto.newBuilder().setName("{{ EntityName }}").build());
    }

    @Test
    void test_getCreateAndGet{{ EntityName }}() {
        Create{{ EntityName }}Response createResponse = client.create{{ EntityName }}({{ EntityName }}Dto.newBuilder().setName("{{ EntityName }}").build());

        Get{{ EntityName }}Response getResponse = client.get{{ EntityName }}(
                Get{{ EntityName }}Request.newBuilder().setId(createResponse.get{{ EntityName }}().getId().getValue()).build()
        );

        assertThat(createResponse.get{{ EntityName }}()).isEqualTo(getResponse.get{{ EntityName }}());
    }

    @Test
    void test_get{{ EntityName | pluralize }}() {{'{'}}{% if persistence != 'None' %}
        long existingRecords = server.getContext().getBean({{ EntityName }}Repository.class).count();

        int pageSize = 10;
        int recordsToAdd = 20;
        for (int i = 0; i < recordsToAdd; i++) {
            client.create{{ EntityName }}({{ EntityName }}Dto.newBuilder().setName("{{ EntityName }} " + i).build());
        }

        Get{{ EntityName | pluralize }}Response firstPageResponse = client.get{{ EntityName | pluralize }}(Get{{ EntityName | pluralize }}Request.newBuilder()
                .setPageSize(pageSize)
                .setStartPage(0)
                .build()
        );

        assertThat(firstPageResponse.getTotalElements()).isEqualTo(existingRecords + recordsToAdd);
        assertThat(firstPageResponse.getHasNext()).isTrue();
        assertThat(firstPageResponse.getHasPrevious()).isFalse();

        Get{{ EntityName | pluralize }}Response lastPageResponse = client.get{{ EntityName | pluralize }}(Get{{ EntityName | pluralize }}Request.newBuilder()
                .setPageSize(pageSize)
                .setStartPage(firstPageResponse.getTotalPages() - 1)
                .build()
        );

        assertThat(lastPageResponse.getHasNext()).isFalse();
        assertThat(lastPageResponse.getHasPrevious()).isTrue();{% else %}
        Get{{ EntityName | pluralize }}Response response = client.get{{ EntityName | pluralize }}(Get{{ EntityName | pluralize }}Request.getDefaultInstance());
        assertThat(response).isNotNull();{% endif %}
    }

    @Test
    void test_update{{ EntityName }}() {
        Create{{ EntityName }}Response createResponse = client.create{{ EntityName }}({{ EntityName }}Dto.newBuilder()
                                                                           .setName("{{ EntityName }}")
                                                                           .build());

        {{ EntityName }}Dto expected = {{ EntityName }}Dto.newBuilder()
                                    .setId(createResponse.get{{ EntityName }}()
                                                         .getId())
                                    .setName("{{ EntityName }}")
                                    .build();
        Update{{ EntityName }}Response response = client.update{{ EntityName }}(expected);

        assertThat(response.get{{ EntityName }}()).isEqualTo(expected);
    }

    @Test
    void test_delete{{ EntityName }}() {
        Create{{ EntityName }}Response createResponse = client.create{{ EntityName }}({{ EntityName }}Dto.newBuilder().setName("{{ EntityName }}").build());

        Delete{{ EntityName }}Response response = client.delete{{ EntityName }}(
            Delete{{ EntityName }}Request.newBuilder().setId(createResponse.get{{ EntityName }}().getId().getValue()).build()
        );

        assertThat(response.getMessage()).isEqualTo("Success");
    }
{% endfor %}
}
