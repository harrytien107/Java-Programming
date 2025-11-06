#!/bin/bash

# ========================================
# Ubuntu Server Deployment Script
# Project: JAVA-010112213601
# ========================================

set -e  # Exit on error

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Print functions
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# ========================================
# Step 1: Get Ubuntu Server IP
# ========================================
print_info "Getting Ubuntu Server IP address..."
UBUNTU_IP=$(hostname -I | awk '{print $1}')
print_success "Ubuntu Server IP: $UBUNTU_IP"

# ========================================
# Step 2: Check Docker installation
# ========================================
print_info "Checking Docker installation..."
if ! command -v docker &> /dev/null; then
    print_warning "Docker not found. Installing Docker..."
    sudo apt update
    sudo apt install -y docker.io
    sudo systemctl start docker
    sudo systemctl enable docker
    sudo usermod -aG docker $USER
    print_success "Docker installed successfully"
    print_warning "Please logout and login again, then re-run this script"
    exit 0
else
    print_success "Docker is already installed: $(docker --version)"
fi

# ========================================
# Step 3: Check Docker Compose installation
# ========================================
print_info "Checking Docker Compose installation..."
if ! command -v docker-compose &> /dev/null; then
    print_warning "Docker Compose not found. Installing..."
    sudo apt install -y docker-compose
    print_success "Docker Compose installed successfully"
else
    print_success "Docker Compose is already installed: $(docker-compose --version)"
fi

# ========================================
# Step 4: Update docker-compose.yml
# ========================================
print_info "Updating docker-compose.yml with Ubuntu IP..."

if [ -f "docker-compose.yml" ]; then
    # Backup original file
    cp docker-compose.yml docker-compose.yml.backup
    print_success "Backup created: docker-compose.yml.backup"
    
    # Replace YOUR_UBUNTU_IP with actual IP
    sed -i "s/YOUR_UBUNTU_IP/$UBUNTU_IP/g" docker-compose.yml
    print_success "Updated REACT_APP_API_URL to http://$UBUNTU_IP:8080"
else
    print_error "docker-compose.yml not found!"
    exit 1
fi

# ========================================
# Step 5: Check and configure firewall
# ========================================
print_info "Checking firewall status..."
if command -v ufw &> /dev/null; then
    UFW_STATUS=$(sudo ufw status | grep -i "Status:" | awk '{print $2}')
    if [ "$UFW_STATUS" = "active" ]; then
        print_warning "Firewall is active. Opening required ports..."
        sudo ufw allow 3000/tcp comment 'Frontend React'
        sudo ufw allow 8080/tcp comment 'Backend Spring Boot'
        sudo ufw allow 3306/tcp comment 'MySQL Database'
        sudo ufw reload
        print_success "Firewall ports opened: 3000, 8080, 3306"
    else
        print_info "Firewall is inactive. No changes needed."
    fi
else
    print_info "UFW not found. Skipping firewall configuration."
fi

# ========================================
# Step 6: Stop existing containers
# ========================================
print_info "Stopping existing containers (if any)..."
if docker-compose ps | grep -q "Up"; then
    docker-compose down
    print_success "Existing containers stopped"
else
    print_info "No running containers found"
fi

# ========================================
# Step 7: Build and run Docker containers
# ========================================
print_info "Building and starting Docker containers..."
print_warning "This may take several minutes (5-10 minutes for first build)..."

docker-compose up -d --build

print_success "Docker containers started!"

# ========================================
# Step 8: Wait for services to be ready
# ========================================
print_info "Waiting for services to be ready..."
sleep 10

# ========================================
# Step 9: Check container status
# ========================================
print_info "Checking container status..."
docker-compose ps

# ========================================
# Step 10: Display access information
# ========================================
echo ""
echo "========================================"
echo "   🎉 DEPLOYMENT SUCCESSFUL! 🎉"
echo "========================================"
echo ""
echo "📱 Access URLs from Windows:"
echo "   Frontend:  http://$UBUNTU_IP:3000"
echo "   Backend:   http://$UBUNTU_IP:8080"
echo "   Swagger:   http://$UBUNTU_IP:8080/swagger-ui/index.html"
echo ""
echo "🔧 Useful commands:"
echo "   View logs:       docker-compose logs -f"
echo "   Stop services:   docker-compose down"
echo "   Restart:         docker-compose restart"
echo "   Rebuild:         docker-compose up -d --build"
echo ""
echo "📝 Notes:"
echo "   - Make sure VMware Network is in Bridged mode"
echo "   - Ensure Windows and Ubuntu are on the same LAN"
echo "   - Check firewall settings if cannot access"
echo ""
echo "========================================"

# ========================================
# Step 11: Show logs (optional)
# ========================================
read -p "Do you want to view logs now? (y/n): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    print_info "Showing logs (Press Ctrl+C to exit)..."
    docker-compose logs -f
fi


