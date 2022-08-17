# building
FROM node:14 as building

WORKDIR /reactapp
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

# serving
FROM nginx:alpine
COPY nginx.conf /etc/nginx/conf.d/default.conf
COPY --from=building /reactapp/build /usr/share/nginx/html

CMD nginx -g 'daemon off;'
