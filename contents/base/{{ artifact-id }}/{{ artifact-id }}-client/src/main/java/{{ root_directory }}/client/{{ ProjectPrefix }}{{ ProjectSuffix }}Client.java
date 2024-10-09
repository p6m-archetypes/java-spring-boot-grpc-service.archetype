package {{ root_package }}.client;

import io.grpc.ManagedChannelBuilder;
import {{ root_package }}.api.v1.{{ ProjectPrefix }}{{ ProjectSuffix }};
import {{ root_package }}.grpc.v1.*;

public class {{ ProjectPrefix }}{{ ProjectSuffix }}Client implements {{ ProjectPrefix }}{{ ProjectSuffix }} {
    {{ ProjectPrefix }}{{ ProjectSuffix }}Grpc.{{ ProjectPrefix }}{{ ProjectSuffix }}BlockingStub stub;

    public static {{ ProjectPrefix }}{{ ProjectSuffix }}Client of(String host, int port) {
        return new {{ ProjectPrefix }}{{ ProjectSuffix }}Client(ManagedChannelBuilder.forAddress(host, port).usePlaintext());
    }

    private {{ ProjectPrefix }}{{ ProjectSuffix }}Client(ManagedChannelBuilder<?> channelBuilder) {
        this.stub = {{ ProjectPrefix }}{{ ProjectSuffix }}Grpc.newBlockingStub(channelBuilder.build());
    }

{%- for entity_key in model.entities -%}
{%- set EntityName = entity_key | pascal_case -%}
{%- set entityName = entity_key | camel_case %}

    @Override
    public Create{{ EntityName }}Response create{{ EntityName }}({{ EntityName }}Dto {{ entityName }}) {
        return stub.create{{ EntityName }}({{ entityName }});
    }

    @Override
    public Get{{ EntityName | pluralize }}Response get{{ EntityName | pluralize }}(Get{{ EntityName | pluralize }}Request request) {
        return stub.get{{ EntityName | pluralize }}(request);
    }

    @Override
    public Get{{ EntityName }}Response get{{ EntityName }}(Get{{ EntityName }}Request request) {
        return stub.get{{ EntityName }}(request);
    }

    @Override
    public Update{{ EntityName }}Response update{{ EntityName }}({{ EntityName }}Dto {{ entityName }}) {
        return stub.update{{ EntityName }}({{ entityName }});
    }
{% endfor %}
}
