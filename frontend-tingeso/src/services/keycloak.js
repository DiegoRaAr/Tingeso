import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:70/auth",
  realm: "tingeso-realm",
  clientId: "frontend-app",
}); 

export default keycloak;