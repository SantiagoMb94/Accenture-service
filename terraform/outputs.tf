output "alb_dns_name" {
  description = "Dirección DNS pública del Application Load Balancer"
  value       = aws_lb.main.dns_name
}

output "rds_endpoint" {
  description = "Endpoint de conexión a la base de datos PostgreSQL administrada"
  value       = aws_db_instance.postgres.endpoint
}

output "ecs_cluster_name" {
  description = "Nombre del cluster ECS Fargate"
  value       = aws_ecs_cluster.main.name
}

output "ecs_service_name" {
  description = "Nombre del servicio ECS"
  value       = aws_ecs_service.main.name
}
