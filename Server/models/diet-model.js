const mongoose = require('mongoose')
const Schema = mongoose.Schema

const DietSchema = new Schema({
    nutrients: {
        protein: Number,
        carbs: Number,
        fat: Number,
    },
    date: Date.now()
})