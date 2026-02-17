import Keycloak from "keycloak-js";

const keycloak = new Keycloak({
  url: import.meta.env.VITE_KEYCLOAK_URL || "http://localhost:70/auth",
  realm: import.meta.env.VITE_KEYCLOAK_REALM || "tingeso-realm",
  clientId: import.meta.env.VITE_KEYCLOAK_CLIENT || "frontend-app",
}); 

export default keycloak;