variable "aws_region" {
  description = "Región de AWS donde se desplegarán los recursos"
  type        = string
  default     = "us-east-1"
}

variable "environment" {
  description = "Entorno de despliegue (dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "app_name" {
  description = "Nombre base de la aplicación"
  type        = string
  default     = "accenture-franchise-service"
}

variable "db_username" {
  description = "Usuario administrador de PostgreSQL RDS"
  type        = string
  default     = "franchise_admin"
}

variable "db_password" {
  description = "Contraseña de la base de datos PostgreSQL RDS"
  type        = string
  sensitive   = true
  default     = "AccentureSecurePwd2026!"
}

variable "db_name" {
  description = "Nombre de la base de datos PostgreSQL"
  type        = string
  default     = "franchise_db"
}

variable "container_image" {
  description = "URI de la imagen Docker en ECR o Docker Hub"
  type        = string
  default     = "accenture/franchise-service:latest"
}

variable "container_port" {
  description = "Puerto expuesto por el contenedor del backend"
  type        = number
  default     = 8080
}
