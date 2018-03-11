const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const DietSchema = new Schema({
	nutrients: {
		PROTEIN: Number,
		CARBS: Number,
		FAT: Number,
		FIBER: Number
	},
	date: Date.now()
});
