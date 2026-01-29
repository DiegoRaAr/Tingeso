import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://54.94.174.49:70/auth",
  realm: "tingeso-realm",
  clientId: "frontend-app",
}); 

export default keycloak;