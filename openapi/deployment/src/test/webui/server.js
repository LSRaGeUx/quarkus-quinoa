// Stands in for a dev server like Vite: serves what build.js generated from the stored OpenAPI schema
const fs = require('fs');
const http = require('http');

http.createServer((req, res) => {
    fs.readFile(__dirname + '/build/openapi.yaml', (err, data) => {
        if (err) {
            res.writeHead(404);
            res.end(JSON.stringify(err));
            return;
        }
        res.writeHead(200);
        res.end(data);
    });
}).listen(3000);
