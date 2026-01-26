import Keycloak from "keycloak-js";

// Para desarrollo: http://localhost:8080
// Para producción (Docker): http://localhost:70/auth
const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "tingeso-realm",
  clientId: "frontend-app",
}); 

export default keycloak;