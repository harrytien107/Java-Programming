#!/bin/bash

# ========================================
# Rebuild Frontend Container
# Fix: Frontend login issue - API URL not updated
# ========================================

set -e

echo "🔄 Rebuilding Frontend Container..."
echo ""

# Check if we're in the right directory
if [ ! -f "docker-compose.yml" ]; then
    echo "❌ Error: docker-compose.yml not found!"
    echo "Please run this script from the project root directory"
    exit 1
fi

# Step 1: Stop frontend container
echo "1️⃣  Stopping frontend container..."
docker compose stop frontend
echo "✅ Frontend stopped"
echo ""

# Step 2: Remove frontend container
echo "2️⃣  Removing frontend container..."
docker compose rm -f frontend
echo "✅ Frontend container removed"
echo ""

# Step 3: Remove frontend image
echo "3️⃣  Removing frontend image..."
docker rmi java-010112213601-frontend || echo "⚠️  Image not found, continuing..."
echo "✅ Frontend image removed"
echo ""

# Step 4: Rebuild frontend
echo "4️⃣  Rebuilding frontend with new API URL..."
echo "📦 This will take 2-3 minutes..."
docker compose up -d --build frontend
echo "✅ Frontend rebuilt and started"
echo ""

# Step 5: Wait for container to be ready
echo "5️⃣  Waiting for frontend to be ready..."
sleep 10
echo ""

# Step 6: Check status
echo "6️⃣  Checking frontend status..."
docker ps | grep frontend
echo ""

# Step 7: Verify API URL in build
echo "7️⃣  Verifying API URL in frontend build..."
UBUNTU_IP=$(hostname -I | awk '{print $1}')
echo "Ubuntu IP: $UBUNTU_IP"
echo "Expected API URL: http://$UBUNTU_IP:8080"
echo ""

API_CHECK=$(docker exec frontend sh -c "grep -r '$UBUNTU_IP:8080' /usr/share/nginx/html/static/js/ 2>/dev/null | head -1" || echo "")
if [ -n "$API_CHECK" ]; then
    echo "✅ API URL found in frontend build!"
    echo "   $API_CHECK"
else
    echo "⚠️  Could not verify API URL in build (might be minified)"
    echo "   Try logging in to verify"
fi
echo ""

# Success message
echo "========================================"
echo "   🎉 Frontend Rebuild Complete!"
echo "========================================"
echo ""
echo "📱 Access URLs:"
echo "   Frontend:  http://$UBUNTU_IP:3000"
echo "   Login:     http://$UBUNTU_IP:3000/login"
echo "   Backend:   http://$UBUNTU_IP:8080"
echo ""
echo "🔐 Login credentials:"
echo "   Username: admin"
echo "   Password: 1234"
echo ""
echo "📝 Next steps:"
echo "   1. Open browser on Windows"
echo "   2. Go to: http://$UBUNTU_IP:3000/login"
echo "   3. Login with admin/1234"
echo "   4. Should redirect to /admin/dashboard"
echo ""
echo "🔍 If login still fails:"
echo "   - Check browser DevTools (F12) → Network tab"
echo "   - Look for request to: http://$UBUNTU_IP:8080/auth/login"
echo "   - Check status should be 200 OK"
echo ""
echo "📋 View logs:"
echo "   docker logs -f frontend"
echo "   docker logs -f backend"
echo ""
echo "========================================"


