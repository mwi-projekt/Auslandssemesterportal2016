const express = require('express');
const path = require('path');
const bodyParser = require('body-parser');

const app = express();
const multer = require('multer');
app.use(bodyParser.urlencoded({ extended: true }));
app.use(bodyParser.json());
app.use(bodyParser.raw());
const port = process.env.PORT || 8080;

// sendFile will go here
app.get('/', function(req, res) {
  res.sendFile(path.join(__dirname, '/index.html'));
});

app.post('/international-office/go-out-auslandssemester.html', multer().none(), function(req, res) {
  console.log(req);
  res.send(JSON.stringify(req.body)).status(200);
});

app.listen(port);
console.log('Server started at http://localhost:' + port);