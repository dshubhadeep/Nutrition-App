const router = require("express").Router();
const axios = require("axios");
const urlEncoded = require("body-parser").urlencoded({ extended: false });

router.get("/search/:id", (req, res) => {
	let query = req.params.id;
	query = query.replace(/ /g, "%20");
	console.log(query);
	const url = `https://api.edamam.com/api/food-database/parser?ingr=${query}&app_id=${
		process.env.APP_ID
	}&app_key=${process.env.APP_KEY}&page=0`;
	console.log(url);
	axios.get(url).then(response => {
		let hints = response.data.hints;
        let data = hints.filter(item => {
            const pattern = /Food_[EWG|INR]/
            const tester = pattern.test(item.food.uri)
			return item.food.label.toLowerCase().includes(query) && !tester;
		});
		let suggestions = data.map(item => {
			return item.food;
		});
		res.json(suggestions);
	});
});

router.post("/nutrient/", urlEncoded, (req, res) => {
	const uri = req.body.uri;
	console.log(uri);
	const url = `https://api.edamam.com/api/food-database/nutrients?app_id=${
		process.env.APP_ID
	}&app_key=${process.env.APP_KEY}`;
	const params = {
		yield: 1,
		ingredients: [
			{
				quantity: 1,
				measureURI: "http://www.edamam.com/ontologies/edamam.owl#Measure_unit",
				foodURI: uri
			}
		]
	};
	axios.post(url, params).then(response => {
		// console.log(response.data);
		const data = response.data;
		let nutrients;
		const totalCalories = data.calories;
		console.log(Object.keys(data.totalNutrients).length);

		if (Object.keys(data.totalNutrients).length !== 0) {
			nutrients = {
				FAT: (
					data.totalNutrients["FAT"].quantity /
					data.totalWeight *
					100
				).toFixed(1),
				CARBS: (
					data.totalNutrients["CHOCDF"].quantity /
					data.totalWeight *
					100
				).toFixed(1),
				FIBER: (
					data.totalNutrients["FIBTG"].quantity /
					data.totalWeight *
					100
				).toFixed(1),
				PROTEIN: (
					data.totalNutrients["PROCNT"].quantity /
					data.totalWeight *
					100
				).toFixed(1),
				message: `Nutrients found`
			};
		} else {
			console.log("Else");
			nutrients = { message: "Not found" };
		}

		res.json({
			data: {
				food: data.ingredients[0].parsed[0].food,
				cal: totalCalories,
				nutrients: nutrients
			}
		});
	});
});

module.exports = router;
