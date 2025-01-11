# Kubernetes Provider Configuration
provider "kubernetes" {
  config_path = "~/.kube/config"  # Adjust the path if your kubeconfig is elsewhere
}

# Kubernetes Deployment for Authentication App
resource "kubernetes_deployment" "authenticationapp" {
  metadata {
    name = "authenticationapp"
  }

  spec {
    replicas = 1

    selector {
      match_labels = {
        app = "authenticationapp"
      }
    }

    template {
      metadata {
        labels = {
          app = "authenticationapp"
        }
      }

      spec {
        container {
          name  = "authenticationapp"
          image = "artemizadospinescu/authentication_image"  # Replace with your image
          ports {
            container_port = 8082
            protocol       = "TCP"
          }
        }

        hostname      = "authenticationapp"
        restart_policy = "Always"
      }
    }
  }
}

# Kubernetes Service for Authentication App (NodePort)
resource "kubernetes_service" "authenticationapp" {
  metadata {
    name = "authenticationapp"
    labels = {
      app = "authenticationapp"
    }
  }

  spec {
    selector = {
      app = "authenticationapp"
    }

    ports {
      port        = 8082
      target_port = 8082
      node_port   = 32000
    }

    type = "NodePort"
  }
}
