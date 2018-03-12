const router = require("express").Router();
const axios = require("axios");
const urlEncoded = require("body-parser").urlencoded({ extended: false });
const watson = require("watson-developer-cloud");
const fs = require("fs");
const formidable = require("formidable");
const vcapServices = require("vcap_services");
const credentials = vcapServices.getCredentials("watson_vision_combined");

const User = require("../models/user-model");
const Diet = require("../models/diet-model");
const Day = require("../models/day-model");

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
			const pattern = /Food_[EWG|INR]/;
			const tester = pattern.test(item.food.uri);
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

router.post("/sendImage", urlEncoded, (req, result) => {
	const visual_recognition = watson.visual_recognition({
		api_key: process.env.WATSON_API_KEY,
		version: "v3",
		version_date: "2016-05-20"
	});
	console.log("Body", req.body);
	const form = new formidable.IncomingForm();
	form.keepExtensions = true;
	form.parse(req, function(err, fields, files) {
		if (err) {
			console.log(err);
		} else {
			console.log("Fields - ", files.Image);
			result.send("Hello");
		}
	});
});

router.post("/addMeal", urlEncoded, (req, res) => {
	const data = JSON.parse(req.body.intake);

	User.findOne({ userName: data.usr })
		.populate("diet", "nutrients")
		.then(response => {
			const totalCal = parseInt(data.cals);
			const nutrients = {
				PROTEIN: parseFloat(data.proteins.toFixed(1)),
				CARBS: parseFloat(data.carbs.toFixed(1)),
				FAT: parseFloat(data.fats.toFixed(1)),
				FIBER: parseFloat(data.fiber.toFixed(1))
			};
			let diet = new Diet({
				nutrients: nutrients
			});
			diet.save().then(saved => {
				const id = saved._id;

				User.findOneAndUpdate(
					{ userName: data.usr },
					{ $push: { diet: id } }
				).then(() => {
					console.log("done");
				});
			});
			return response.diet;
		})
		.then(totalMeals => {
			// Check day
			if (totalMeals.length % 3 === 0 && totalMeals.length != 0) {
				const dayNumber = totalMeals.length / 3;
				const endIndex = dayNumber * 3;
				const startIndex = endIndex - 3;
				console.log("Start", startIndex, "End", endIndex);
				const mealsForOneDay = totalMeals.slice(startIndex, endIndex);
				// res.send(mealsForOneDay);
				nutrients = {
					PROTEIN: 0,
					CARBS: 0,
					FAT: 0,
					FIBER: 0
				};
				console.log(mealsForOneDay);
				mealsForOneDay.forEach(meal => {
					nutrients["FAT"] += meal.nutrients["FAT"];
					nutrients["CARBS"] += meal.nutrients["CARBS"];
					nutrients["PROTEIN"] += meal.nutrients["PROTEIN"];
					nutrients["FIBER"] += meal.nutrients["FIBER"];
				});

				let day = new Day({
					nutrients: nutrients,
					dayNumber: dayNumber
				});

				day.save().then(saved => {
					console.log("Day Saved");
					const id = saved._id;
					res.send(saved);
					User.findOneAndUpdate(
						{ userName: data.usr },
						{ $push: { day: id } }
					).then(() => {
						console.log("done");
					});
				});
			} else {
				res.json({ msg: "Hello" });
			}
		});
});

router.get("/plot", (req, res) => {
	User.findOne({ userName: "smith123" })
		.populate("diet", "nutrients")
		.exec()
		.then(result => {
			res.json(result.diet);
			console.log(result.diet.length);
		});
});

module.exports = router;
