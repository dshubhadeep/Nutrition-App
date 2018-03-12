const axios = require("axios");
require("dotenv").config();
const express = require("express");
const mongoose = require("mongoose");
const urlEncoded = require("body-parser").urlencoded({ extended: false });
const app = express();
const ejs = require("ejs");

const User = require("./models/user-model");

const DietRoute = require("./routes/diet-route");

app.use("/app", DietRoute);

app.set("view engine", "ejs");
app.use(express.static(__dirname + "/public"));

mongoose.connect(process.env.MONGO_URI, () => {
	console.log("Connected to DB");
});

app.post("/register", urlEncoded, (req, res) => {
	// res.json(req.body);
	let user = new User({
		fullName: req.body.name,
		userName: req.body.userName,
		password: req.body.password,
		age: req.body.age,
		gender: req.body.gender,
		height: req.body.height,
		weight: req.body.weight,
		location: req.body.location
	});
	user.save().then(data => {
		res.json({ data: data, message: "Registered" });
	});
});

app.post("/login", urlEncoded, (req, res) => {
	User.findOne({ userName: req.body.userName }).then(user => {
		console.log(user);
		if (!user) {
			res.json({
				success: false,
				message: "User not found"
			});
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

module.exports = app;
