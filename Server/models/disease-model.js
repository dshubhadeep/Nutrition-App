const mongoose = require("mongoose");
const Schema = mongoose.Schema;

const DiseaseSchema = new Schema({
	nutrient: String,
	defiency: [
		{
			type: String
		}
	],
	excess: [
		{
			type: String
		}
	],
    value: Number,
    intake: {
        male: Number,
        female: Number
    }
});

module.exports = mongoose.model("disease", DiseaseSchema);
