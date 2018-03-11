const axios = require("axios");
require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const urlEncoded = require("body-parser").urlencoded({ extended: false });
const app = express();

const User = require("./models/user-model");

const DietRoute = require('./routes/diet-route');

app.use('/app', DietRoute);

mongoose.connect(process.env.MONGO_URI, () => {
	console.log("Connected to DB");
});

const url = `https://api.edamam.com/api/food-database/parser?ingr=dosa&app_id=${process.env.APP_ID}&app_key=${process.env.APP_KEY}&page=0`;

app.post("/register", urlEncoded, (req, res) => {
	// res.json(req.body);
	let user = new User({
		fullName: req.body.name,
		userName: req.body.userName,
		password: req.body.password,
		age: req.body.age,
		gender: req.body.gender,
		height: req.body.height,
		weight: req.body.weight
	});
	user.save().then(data => {
		res.json({ data: data, message: "Registered" });
	});
});

app.post("/login", urlEncoded, (req, res) => {
	User.findOne({ userName: req.body.userName }).then(user => {
		console.log(user);
		if (!user) {
			res.send("user not found");
		} else {
			if (user.comparePassword(req.body.password)) {
				res.json({
					success: true
				});
			} else {
				res.json({
					success: false
				});
			}
		}
	});
});

app.get("/:id", (req, res) => {
	console.log(req.params.id);
	axios
		.get(url)
		.then(response => {
			let data = response.data.hints;
			let suggestions = data.filter(item =>
				item.food.label.toLowerCase().includes("dosa")
			);
			return suggestions;
		})
		.then(suggestion => {
			const uris = suggestion.map(item => item.food.uri);
			const nutrientUrl = `https://api.edamam.com/api/food-database/nutrients?app_id=${
				process.env.APP_ID
			}&app_key=${process.env.APP_KEY}`;
			console.log(nutrientUrl);
			const params = {
				yield: 1,
				ingredients: [
					{
						quantity: 1,
						measureURI:
							"http://www.edamam.com/ontologies/edamam.owl#Measure_unit",
						foodURI: uris[5]
					}
				]
			};
			axios.post(nutrientUrl, params).then(response => {
				// console.log(response.data);
				res.json(response.data);
			});
			// res.json(uris);
		});
});

app.post('/sendImage', urlEncoded, (req, res) => {
	console.log(req.body);
})

// app.listen(8000, () => {
// 	console.log("port 3000");
// });

module.exports = app;