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
  onLoad: 'check-sso',
  checkLoginIframe: false
}

const eventLogger = (event, error) => {
  console.log('Keycloak event:', event, error)
}

ReactDOM.createRoot(document.getElementById('root')).render(
  <ReactKeycloakProvider 
    authClient={keycloak} 
    initOptions={initOptions}
    onEvent={eventLogger}
  >
    <App />
  </ReactKeycloakProvider>,
)
