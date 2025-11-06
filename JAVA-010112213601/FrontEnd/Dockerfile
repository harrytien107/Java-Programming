# Dockerfile (FrontEnd) - Optimized Version
FROM node:22-alpine AS build

# Install build dependencies
RUN apk add --no-cache python3 make g++

WORKDIR /app

# Copy package files first for better layer caching
COPY package*.json ./

# Use npm ci instead of npm install (2-3x faster)
# --prefer-offline: Use cache if available
# --no-audit: Skip audit for faster install
RUN npm ci --prefer-offline --no-audit --loglevel=error

# Copy source code
COPY . .

# Remove any existing .env files to prevent conflicts
RUN rm -f .env .env.local .env.development .env.production

# Set API URL from build args
ARG REACT_APP_API_URL
ENV REACT_APP_API_URL=$REACT_APP_API_URL

# Build with production mode
ENV NODE_ENV=production
RUN npm run build

# Production stage with Nginx
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]