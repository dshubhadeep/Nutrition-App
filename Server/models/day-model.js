const mongoose = require('mongoose');
const Schema = mongoose.Schema;

const DaySchema = new Schema({
    nutrients: {
        PROTEIN: Number,
        CARBS: Number,
        FAT: Number,
        FIBER: Number
    },
    excess: {
        PROTEIN: Number,
        CARBS: Number,
        FAT: Number,
        FIBER: Number
    },
    dayNumber: Number
});

module.exports = mongoose.model('Day', DaySchema);