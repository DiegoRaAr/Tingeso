import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://15.228.56.166:70/auth",
  realm: "tingeso-realm",
  clientId: "frontend-app",
}); 

export default keycloak;