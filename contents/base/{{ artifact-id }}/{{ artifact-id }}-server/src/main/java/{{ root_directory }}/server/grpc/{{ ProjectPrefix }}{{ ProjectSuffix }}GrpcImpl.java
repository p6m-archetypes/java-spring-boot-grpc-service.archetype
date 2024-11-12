package {{ root_package }}.server.grpc;

import {{ group_id }}.platform.errorhandling.exceptions.grpc.GeneralGrpcExceptionHandler;
import io.grpc.stub.StreamObserver;
import org.lognet.springboot.grpc.GRpcService;
import {{ root_package }}.core.{{ ProjectPrefix }}{{ ProjectSuffix }}Core;
import {{ root_package }}.grpc.v1.*;
import {{ root_package }}.grpc.v1.{{ ProjectPrefix }}{{ ProjectSuffix }}Grpc.{{ ProjectPrefix }}{{ ProjectSuffix }}ImplBase;

@GRpcService(interceptors = {GeneralGrpcExceptionHandler.class})
public class {{ ProjectPrefix }}{{ ProjectSuffix }}GrpcImpl extends {{ ProjectPrefix }}{{ ProjectSuffix }}ImplBase {

    private final {{ ProjectPrefix }}{{ ProjectSuffix }}Core service;

    public {{ ProjectPrefix }}{{ ProjectSuffix }}GrpcImpl({{ ProjectPrefix }}{{ ProjectSuffix }}Core service) {
        this.service = service;
    }
{%- for entity_key in model.entities -%}
{%- set EntityName = entity_key | pascal_case -%}
{%- set entityName = entity_key | camel_case %}

    @Override
    public void create{{ EntityName }}({{ EntityName }}Dto request, StreamObserver<Create{{ EntityName }}Response> responseObserver) {
        Create{{ EntityName }}Response {{ entityName }}Response = service.create{{ EntityName }}(request);
        responseObserver.onNext({{ entityName }}Response);
        responseObserver.onCompleted();
    }

    @Override
    public void get{{ EntityName }}(Get{{ EntityName }}Request request, StreamObserver<Get{{ EntityName }}Response> responseObserver) {
        Get{{ EntityName }}Response response = service.get{{ EntityName }}(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void get{{ EntityName | pluralize }}(Get{{ EntityName | pluralize }}Request request, StreamObserver<Get{{ EntityName | pluralize }}Response> responseObserver) {
        Get{{ EntityName | pluralize }}Response response = service.get{{ EntityName | pluralize }}(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void update{{ EntityName }}({{ EntityName }}Dto request, StreamObserver<Update{{ EntityName }}Response> responseObserver) {
        Update{{ EntityName }}Response response = service.update{{ EntityName }}(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public void delete{{ EntityName }}(Delete{{ EntityName }}Request request, StreamObserver<Delete{{ EntityName }}Response> responseObserver) {
        Delete{{ EntityName }}Response response = service.delete{{ EntityName }}(request);
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
{% endfor %}
}
