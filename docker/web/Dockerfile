FROM nginx:stable-alpine

## Copy our default nginx config
COPY nginx.conf /etc/nginx/conf.d/

## Start nginx
CMD nginx -g 'daemon off;'
