# Consul + Spring Boot + Thymeleaf Microservices Demo

## Stack
- Java 17
- Spring Boot 3.5.3
- Spring Cloud 2025.0.0
- Thymeleaf UI: `http://localhost:8080`
- Spring Cloud Gateway: `http://localhost:8085`
- Consul UI: `http://localhost:8500`
- Order Service: internal port 8081
- Payment Service: internal port 8082

## Run with Docker Compose
From this directory:

```bash
docker compose up --build
```

Open `http://localhost:8080`, create an order, and check the order list. The Order Service calls `payment-service` by service name through Spring Cloud LoadBalancer, while Consul supplies discovery.

## Useful test endpoints
```bash
curl http://localhost:8085/api/orders
curl -X POST http://localhost:8085/api/orders \
  -H "Content-Type: application/json" \
  -d "{\"customerName\":\"Asha\",\"amount\":1250.50}"
curl http://localhost:8085/api/orders
curl -X POST http://localhost:8085/api/payments \
  -H "Content-Type: application/json" \
  -d "{\"customerName\":\"Asha\",\"amount\":1250.50}"
```

## Run from IDE
Build all modules from the root:
```bash
mvn clean package
```
Start Consul first (for example, in Docker), then run:
- `PaymentServiceApplication`
- `OrderServiceApplication`
- `ApiGatewayApplication`
- `WebUiApplication`

For IDE execution, default Consul host is `localhost`; UI gateway URL defaults to `http://localhost:8085`.

## Important limitations
- This is a learning/demo application. Orders and payment IDs are stored in memory and are lost when a service restarts.
- `APPROVED_DEMO` is a simulated payment response; it does not process real payments.
- Consul `-dev` mode is insecure and must never be used in production.
- This example provides service discovery and Gateway routing. It does NOT yet include Envoy sidecars, Consul Connect mTLS, service intentions, production ACLs, or rate limiting. Those require separate mesh/security configuration; Eureka replacement alone does not create a service mesh.
- Before production, add authentication/authorization, persistent storage, retries/timeouts, centralized configuration, secrets management, readiness checks, observability, and a secured Consul deployment.
- Check Spring Boot / Spring Cloud compatibility if changing versions.
