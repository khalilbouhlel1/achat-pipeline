terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Use AWS Academy's pre-existing LabRole
data "aws_iam_role" "lab_role" {
  name = "LabRole"
}

# Use the Default VPC and its subnets (excluding us-east-1e, which EKS does not support)
data "aws_vpc" "default" {
  default = true
}

data "aws_subnets" "eks_subnets" {
  filter {
    name   = "vpc-id"
    values = [data.aws_vpc.default.id]
  }
  filter {
    name   = "availability-zone"
    values = ["us-east-1a", "us-east-1b", "us-east-1c", "us-east-1d", "us-east-1f"]
  }
}

# 1. Create the EKS Cluster
resource "aws_eks_cluster" "achat_cluster" {
  name     = "achat-cluster"
  role_arn = data.aws_iam_role.lab_role.arn

  vpc_config {
    subnet_ids = data.aws_subnets.eks_subnets.ids
  }
}

# 2. Create the Worker Nodes
resource "aws_eks_node_group" "achat_nodes" {
  cluster_name    = aws_eks_cluster.achat_cluster.name
  node_group_name = "achat-nodes"
  node_role_arn   = data.aws_iam_role.lab_role.arn
  subnet_ids      = data.aws_subnets.eks_subnets.ids

  scaling_config {
    desired_size = 2
    max_size     = 2
    min_size     = 1
  }

  instance_types = ["t3.medium"]

  depends_on = [aws_eks_cluster.achat_cluster]
}