import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: "http://localhost:8080",
  realm: "loan-realm",
  clientId: "loan-spa",
}); 

export default keycloak;