provider "kubernetes" {
  config_path = "~/.kube/config"
}

resource "kubernetes_manifest" "all_resources" {
  for_each = { for file in fileset("${path.module}", "*.yaml") : file => file if file != "kind-config-three-nodes.yaml" }
  manifest = yamldecode(file(each.value))
}