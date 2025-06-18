const express = require('express');
const mongoose = require('mongoose');
const bodyParser = require('body-parser');
const cors = require('cors');

const app = express();
app.use(cors());
app.use(bodyParser.json());

// ✅ Serve static files from 'public'
app.use(express.static('public'));

// ✅ Mongoose schema
const ItemSchema = new mongoose.Schema({
  type: { type: String, required: true },
  name: { type: String, required: true },
  description: String,
  date: { type: Date, default: Date.now },
  location: String,
  contact: String
});

const Item = mongoose.model('Item', ItemSchema);

// ✅ MongoDB connection
const mongoURI = 'mongodb://localhost:27017/lostfounddb';

mongoose.connect(mongoURI, {
  useNewUrlParser: true,
  useUnifiedTopology: true,
})
.then(() => console.log('MongoDB connected'))
.catch(err => console.log(err));

// ❌ REMOVE this route to allow index.html to load at `/`
// app.get('/', (req, res) => {
//   res.send('Lost & Found API is running...');
// });

// ✅ Start the server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
