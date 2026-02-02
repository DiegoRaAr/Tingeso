import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap/dist/js/bootstrap.bundle.min.js';
import 'bootstrap-icons/font/bootstrap-icons.css';
import App from './App.jsx'
import keycloak from './services/keycloak.js'
import { ReactKeycloakProvider } from '@react-keycloak/web'
import ReactDOM from 'react-dom/client'

const initOptions = {
  onLoad: 'login-required',
  checkLoginIframe: false,
  pkceMethod: 'S256'
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <ReactKeycloakProvider authClient={keycloak} initOptions={initOptions}>
    <App />
  </ReactKeycloakProvider>,
)
