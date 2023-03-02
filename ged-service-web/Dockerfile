# Use a base image of Node.js 14
FROM node:18

# Set the working directory to /app
WORKDIR /app

# Copy package.json and package-lock.json to the working directory
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the Angular application files to the working directory
COPY . .

# Build the Angular application
RUN npm run build --prod

# Use a base image of nginx
FROM nginx

# Copy the nginx configuration file to the container
COPY nginx/nginx.conf /etc/nginx/conf.d/default.conf

# Copy the built Angular application files to the nginx web directory
COPY --from=0 /app/dist/ged-service-web /usr/share/nginx/html

# Expose port 80 for HTTP traffic
EXPOSE 80
