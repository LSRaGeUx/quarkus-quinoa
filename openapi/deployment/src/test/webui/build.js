// Stands in for a codegen tool like Orval: the build output is made from the stored OpenAPI schema
const fs = require('fs');

fs.mkdirSync('build', { recursive: true });
fs.copyFileSync('openapi.yaml', 'build/openapi.yaml');
fs.writeFileSync('build/index.html', 'test');
