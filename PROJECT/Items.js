const mongoose = require('mongoose');

const ItemSchema = new mongoose.Schema({
  type: { type: String, required: true }, // lost or found
  itemName: { type: String, required: true },
  description: String,
  location: String,
  dateReported: { type: Date, default: Date.now },
  contactInfo: String,
});

module.exports = mongoose.model('Item', ItemSchema);
